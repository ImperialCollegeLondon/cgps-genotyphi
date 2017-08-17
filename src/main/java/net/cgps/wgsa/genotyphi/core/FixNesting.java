package net.cgps.wgsa.genotyphi.core;

import net.cgps.wgsa.genotyphi.GenotyphiResult;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class FixNesting implements Function<Set<GenotyphiSchema.GenotyphiGroup>, GenotyphiResult.AggregatedAssignments> {

  private final Collection<Pair<GenotyphiSchema.GenotyphiGroup, GenotyphiSchema.GenotyphiGroup>> subcladeNestPairs;

  public FixNesting(final Collection<Pair<GenotyphiSchema.GenotyphiGroup, GenotyphiSchema.GenotyphiGroup>> subcladeNestPairs) {

    this.subcladeNestPairs = new ArrayList<>(subcladeNestPairs);
  }

  /**
   * Creates the default set of "fixes" to be applied as according to the original script.
   *
   * @return
   */
  public static FixNesting buildDefault() {

    final Collection<Pair<GenotyphiSchema.GenotyphiGroup, GenotyphiSchema.GenotyphiGroup>> pairs = new ArrayList<>(10);

    //  	# fix 2.3, 2.2 nesting
//	if ('2.2' in clades) and ('2.3' in clades):
//      clades.remove('2.2')
//
//      # fix 3.5.3, 3.5.4 nesting
//	if ('3.5.3' in subclades) and ('3.5.4' in subclades):
//      subclades.remove('3.5.3')
//
//      # fix 2.3.1, 2.3.3 nesting
//	if ('2.3.1' in subclades) and ('2.3.2' in subclades):
//      subclades.remove('2.3.2')
//
//      # fix 2.3.1, 2.3.3 nesting
//	if ('2.3.5' in subclades) and ('2.3.3' in subclades):
//      subclades.remove('2.3.3')
//
    pairs.add(new ImmutablePair<>(GenotyphiSchema.GenotyphiGroup.build("2.2"), GenotyphiSchema.GenotyphiGroup.build("2.3")));
    pairs.add(new ImmutablePair<>(GenotyphiSchema.GenotyphiGroup.build("3.5.4"), GenotyphiSchema.GenotyphiGroup.build("3.5.3")));
    pairs.add(new ImmutablePair<>(GenotyphiSchema.GenotyphiGroup.build("2.3.2"), GenotyphiSchema.GenotyphiGroup.build("2.3.1")));
    pairs.add(new ImmutablePair<>(GenotyphiSchema.GenotyphiGroup.build("2.3.3"), GenotyphiSchema.GenotyphiGroup.build("2.3.5")));
    pairs.add(new ImmutablePair<>(GenotyphiSchema.GenotyphiGroup.build("2"), GenotyphiSchema.GenotyphiGroup.build("3")));

    return new FixNesting(pairs);
  }

  // Original source
//      # fix primary clades relative to CT18 = 3.2.1, ie has clade1, clade2, clade3 SNPs
//	if len(primary) == 1:
//      if '3' in primary:
//         primary = ['2']	 # clade 2 differs from CT18 by the clade3-defining SNP
//	                     		# note other option is clade 4 snp, which defines clade 4 relative to CT18
//  elif len(primary) == 2:
//      if ('2' in primary) and ('3' in primary):
//         primary = ['1']	 # clade 2 differs from CT18 by the clade3-defining SNP
//  elif len(primary) == 0:
//      primary = ['3']
//  elif len(primary) == 3:
//      if ('1' in primary) and ('2' in primary) and ('3' in primary):
//         primary = ['0']
//
//  # fix clade relative to CT18:
//  if '3.2' in clades:
//      clades.remove('3.2')  # anything NOT in 3.2 will have this SNP
//	else:
//      if len(clades) == 0:
//      clades.append('3.2')  # anything with no clade, and 3.2 SNP not called, belongs in 3.2 with CT18
//
//	# fix 3.5.3, 3.5.4 nesting
//	if ('3.5.3' in clades) and ('3.5.4' in clades):
//      clades.remove('3.5.3')
//
//      # fix subclades relative to CT18:
//  if '3.2.1' in subclades:
//      subclades.remove('3.2.1')  # anything NOT in 3.2.1 will have this SNP
//	else:
//      if len(subclades) == 0:
//      subclades.append('3.2.1')  # anything with no subclade, and 3.2.1 SNP NOT called, belongs in 3.2.1 with CT18

  @Override
  public GenotyphiResult.AggregatedAssignments apply(final Set<GenotyphiSchema.GenotyphiGroup> genotyphiGroups) {


    // Do the simple filters.
    this.subcladeNestPairs.forEach(pair -> this.dependentRemoveGroup(genotyphiGroups).accept(pair.getLeft(), pair.getRight()));

    // CT18 fixes.
    final List<GenotyphiSchema.GenotyphiGroup> primaryGroups = genotyphiGroups
        .stream()
        .filter(group -> GenotyphiSchema.Depth.PRIMARY == group.getDepth())
        .collect(Collectors.toList());

    final GenotyphiSchema.GenotyphiGroup groupZero = GenotyphiSchema.GenotyphiGroup.build("0");
    final GenotyphiSchema.GenotyphiGroup groupOne = GenotyphiSchema.GenotyphiGroup.build("1");
    final GenotyphiSchema.GenotyphiGroup groupTwo = GenotyphiSchema.GenotyphiGroup.build("2");

    // Group 0 is indicated by SNPs from group 1 & 2
    if (primaryGroups.size() == 2) {
      if (primaryGroups.contains(groupOne) && primaryGroups.contains(groupTwo)) {
        primaryGroups.clear();
        primaryGroups.add(groupZero);
      }
    }

    final Set<GenotyphiSchema.GenotyphiGroup> clades = genotyphiGroups.stream().filter(group -> GenotyphiSchema.Depth.CLADE == group.getDepth()).collect(Collectors.toSet());

    final Set<GenotyphiSchema.GenotyphiGroup> subclades = genotyphiGroups.stream().filter(group -> GenotyphiSchema.Depth.SUBCLADE == group.getDepth()).collect(Collectors.toSet());

    return new GenotyphiResult.AggregatedAssignments(primaryGroups, clades, subclades);
  }

  /**
   * Returns a function where if group (b) is present in the Set of groups then (a) is removed.
   *
   * @param genotyphiGroups
   * @return
   */
  private BiConsumer<GenotyphiSchema.GenotyphiGroup, GenotyphiSchema.GenotyphiGroup> dependentRemoveGroup(final Set<GenotyphiSchema.GenotyphiGroup> genotyphiGroups) {

    return (a, b) -> {

      if (genotyphiGroups.stream().anyMatch(b::equals)) {
        final Collection<GenotyphiSchema.GenotyphiGroup> overlappedGroups = genotyphiGroups.stream().filter(a::equals).collect(Collectors.toList());
        overlappedGroups.forEach(genotyphiGroups::remove);
      }
    };
  }

}
