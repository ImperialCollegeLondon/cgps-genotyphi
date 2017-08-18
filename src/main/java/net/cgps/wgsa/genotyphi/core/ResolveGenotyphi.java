package net.cgps.wgsa.genotyphi.core;

import net.cgps.wgsa.genotyphi.GenotyphiResult;

import java.util.Collection;
import java.util.function.BiFunction;

public class ResolveGenotyphi implements BiFunction<GenotyphiResult.AggregatedAssignments, Collection<GenotyphiMutation>, String> {


  @Override
  public String apply(final GenotyphiResult.AggregatedAssignments aggregatedAssignments, final Collection<GenotyphiMutation> genotyphiMutations) {

    final String bestResult;

    if (!aggregatedAssignments.getSubcladeGroups().isEmpty()) {
      bestResult = aggregatedAssignments.toSubcladeString();
    } else if (!aggregatedAssignments.getCladeGroups().isEmpty()) {
      bestResult = aggregatedAssignments.toCladeString();
    } else if (!aggregatedAssignments.getPrimaryGroups().isEmpty()) {
      bestResult = aggregatedAssignments.toPrimaryString();
    } else {
      bestResult = "";
    }

    return bestResult;
  }

}
