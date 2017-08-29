package net.cgps.wgsa.genotyphi.core;

import net.cgps.wgsa.genotyphi.GenotyphiResult;
import net.cgps.wgsa.genotyphi.lib.Mutation;
import net.cgps.wgsa.genotyphi.lib.MutationSearchResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Converts the lib matches from genotyphi BLAST to the result document.
 */
public class GenotyphiBlastReader implements Function<Stream<MutationSearchResult>, GenotyphiBlastReader.GenotyphiResultData> {

  private final Logger logger = LoggerFactory.getLogger(GenotyphiBlastReader.class);
  private final Map<String, GenotyphiGene> genotyphiSchema;
  private final Comparator<MutationSearchResult> BLAST_SORTER = Comparator.comparingDouble(a -> a.getBlastSearchStatistics().getPercentIdentity());

  public GenotyphiBlastReader(final GenotyphiSchema genotyphiSchema) {

    this.genotyphiSchema = genotyphiSchema.asEntries().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
  }

  @Override
  public GenotyphiResultData apply(final Stream<MutationSearchResult> searchResults) {

    final Counter counter = new Counter();

    // filter to best match only.
    final Collection<MutationSearchResult> results = searchResults
        .collect(
            Collectors.groupingBy(
                mutationSearchResult -> mutationSearchResult
                    .getBlastSearchStatistics()
                    .getLibrarySequenceId(), Collectors.mapping(Function.identity(), Collectors.toList())))
        .values()
        .stream()
        .flatMap(
            familyMatches -> familyMatches
                .stream()
                .filter(result -> {
                  // Check the match region includes the typing mutation.
                  // There's one sequence with two variants.
                  final Collection<GenotyphiMutation> variants = this.genotyphiSchema.get(result.getBlastSearchStatistics().getLibrarySequenceId()).getVariants();

                  // Count the number of found variants.
                  final Collection<GenotyphiMutation> foundVariants = variants
                      .stream()
                      .filter(variant ->
                          (result.getBlastSearchStatistics().getLibrarySequenceStart() < variant.getLocation()) && (variant.getLocation() < result.getBlastSearchStatistics().getLibrarySequenceStop()))
                      .peek(variant -> counter.increment()).collect(Collectors.toList());

                  // Keep where >0 variants found.
                  return (!foundVariants.isEmpty());
                })
                .sorted(this.BLAST_SORTER)
        )
        .collect(Collectors.toList());

    // Extract the typing SNP matches
    // For each match check if the genotyphi variants are present, and gather those that are
    final Collection<Map.Entry<String, Collection<GenotyphiMutation>>> foundVariants = results
        .stream()
        .filter(result -> !result.getMutations().isEmpty())
        .map(result -> {

          final GenotyphiGene gene = this.genotyphiSchema.get(result.getBlastSearchStatistics().getLibrarySequenceId());

          return new AbstractMap.SimpleImmutableEntry<String, Collection<GenotyphiMutation>>(gene.getSequenceId(), gene.getVariants()
              .stream()
              .filter(variant -> result.getMutations()
                  .stream()
                  .filter(mutation -> Mutation.MutationType.S == mutation.getMutationType())
                  .filter(mutation -> mutation.getReferenceLocation() == variant.getLocation())
                  .anyMatch(mutation -> mutation.getMutationSequence().equals(variant.getVariant())))
              .collect(Collectors.toList()));
        })
        .filter(gene -> !gene.getValue().isEmpty())
        .distinct()
        .collect(Collectors.toList());

    this.logger.debug("{} genotyphi markers found", foundVariants.stream().map(Map.Entry::getValue).mapToInt(Collection::size).sum());

    final Set<GenotyphiSchema.GenotyphiGroup> collectedGroups = foundVariants
        .stream()
        .map(Map.Entry::getValue)
        .flatMap(Collection::stream)
        .peek(variant -> this.logger.debug("Variant={}", variant.toString()))
        .map(GenotyphiMutation::getGenotyphiGroup)
        .peek(group -> this.logger.debug("Group: {}", group.toString()))
        .collect(Collectors.toSet());

    final GenotyphiResult.AggregatedAssignments potentialGroups = FixNesting.buildDefault().apply(collectedGroups);

    return new GenotyphiResultData(
        new ResolveGenotyphi().apply(potentialGroups),
        potentialGroups,
        foundVariants,
        results,
        counter.currentCount());
  }

  static class Counter {

    int count;

    public void increment() {

      this.count++;
    }

    public int currentCount() {

      return this.count;
    }
  }

  public static class GenotyphiResultData {

    private final String type;
    private final GenotyphiResult.AggregatedAssignments aggregatedAssignments;
    private final Collection<Map.Entry<String, Collection<GenotyphiMutation>>> genotyphiMutations;
    private final Collection<MutationSearchResult> blastResults;
    private final float foundLoci;

    public GenotyphiResultData(final String type, final GenotyphiResult.AggregatedAssignments aggregatedAssignments, final Collection<Map.Entry<String, Collection<GenotyphiMutation>>> genotyphiMutations, final Collection<MutationSearchResult> results, final float foundLoci) {

      this.type = type;
      this.aggregatedAssignments = aggregatedAssignments;
      this.genotyphiMutations = genotyphiMutations;
      this.blastResults = results;
      this.foundLoci = foundLoci;
    }

    public GenotyphiResult.AggregatedAssignments getAggregatedAssignments() {

      return this.aggregatedAssignments;
    }

    public String getType() {

      return this.type;
    }

    public Collection<Map.Entry<String, Collection<GenotyphiMutation>>> getGenotyphiMutations() {

      return this.genotyphiMutations;
    }

    @Override
    public String toString() {

      return "GenotyphiResultData{" +
          "type='" + this.type + '\'' +
          ", genotyphiSet=" + this.aggregatedAssignments +
          '}';
    }


    public Collection<MutationSearchResult> getBlastResults() {

      return this.blastResults;
    }

    public float getFoundLoci() {

      return this.foundLoci;
    }
  }

}
