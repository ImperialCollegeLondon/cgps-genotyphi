package net.cgps.wgsa.genotyphi;

import net.cgps.wgsa.genotyphi.core.GenotyphiMutation;
import net.cgps.wgsa.genotyphi.core.GenotyphiSchema;
import net.cgps.wgsa.genotyphi.lib.Jsonnable;
import net.cgps.wgsa.genotyphi.lib.MutationSearchResult;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GenotyphiResult extends Jsonnable {

  private final String assemblyId;
  private final String genotype;
  private final Map<String, Collection<GenotyphiMutation>> genotyphiMutations;
  private final AggregatedAssignments data;
  private final Collection<MutationSearchResult> blastResults;
  private final float foundLoci;


  @SuppressWarnings("unused")
  private GenotyphiResult() {

    this("", "", Collections.emptyMap(), null, Collections.emptyList(), 0f);
  }

  public GenotyphiResult(final String assemblyId, final String genotype, final Map<String, Collection<GenotyphiMutation>> genotyphiMutations, final AggregatedAssignments data, final Collection<MutationSearchResult> blastResults, final float foundLoci) {
    this.assemblyId = assemblyId;

    this.genotype = genotype;
    this.genotyphiMutations = genotyphiMutations;
    this.data = data;
    this.blastResults = blastResults;
    this.foundLoci = foundLoci;
  }

  public String getGenotype() {

    return this.genotype;
  }

  public AggregatedAssignments getAggregatedAssignments() {

    return this.data;
  }

  public Map<String, Collection<GenotyphiMutation>> getGenotyphiMutations() {

    return this.genotyphiMutations;
  }

  public Collection<MutationSearchResult> getBlastResults() {

    return this.blastResults;
  }

  public float getFoundLoci() {

    return this.foundLoci;
  }

  public String getAssemblyId() {
    return this.assemblyId;
  }

  public static class AggregatedAssignments {

    private final List<GenotyphiSchema.GenotyphiGroup> primaryGroups;
    private final Collection<GenotyphiSchema.GenotyphiGroup> cladeGroups;
    private final Collection<GenotyphiSchema.GenotyphiGroup> subcladeGroups;

    @SuppressWarnings("unused")
    private AggregatedAssignments() {

      this(Collections.emptyList(), Collections.emptyList(), Collections.emptyList());
    }

    public AggregatedAssignments(final List<GenotyphiSchema.GenotyphiGroup> primaryGroups, final Collection<GenotyphiSchema.GenotyphiGroup> cladeGroups, final Collection<GenotyphiSchema.GenotyphiGroup> subcladeGroups) {

      this.primaryGroups = primaryGroups;
      this.cladeGroups = cladeGroups;
      this.subcladeGroups = subcladeGroups;
    }


    public String toString() {

      return "GenotyphiSet{" +
          "primaryGroups=" + this.primaryGroups +
          ", cladeGroups=" + this.cladeGroups +
          ", subcladeGroups=" + this.subcladeGroups +
          '}';
    }

    public List<GenotyphiSchema.GenotyphiGroup> getPrimaryGroups() {

      return this.primaryGroups;
    }

    public String toPrimaryString() {

      return this.primaryGroups.get(0).toCode();
    }

    public Collection<GenotyphiSchema.GenotyphiGroup> getCladeGroups() {

      return this.cladeGroups;
    }

    public String toCladeString() {

      return this.cladeGroups.stream().map(GenotyphiSchema.GenotyphiGroup::toCode).collect(Collectors.joining(","));
    }

    public Collection<GenotyphiSchema.GenotyphiGroup> getSubcladeGroups() {

      return this.subcladeGroups;
    }

    public String toSubcladeString() {

      return this.subcladeGroups.stream().map(GenotyphiSchema.GenotyphiGroup::toCode).collect(Collectors.joining(","));
    }
  }

}
