package net.cgps.wgsa.genotyphi.core;

import net.cgps.wgsa.genotyphi.GenotyphiResult;

import java.util.function.Function;

public class ResolveGenotyphi implements Function<GenotyphiResult.AggregatedAssignments, String> {


  @Override
  public String apply(final GenotyphiResult.AggregatedAssignments aggregatedAssignments) {

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
