package net.cgps.wgsa.genotyphi;

import java.util.function.Function;
import java.util.stream.Collectors;

public class TextFormatter implements Function<GenotyphiResult, String> {
  @Override
  public String apply(final GenotyphiResult genotyphiResult) {
    final StringBuilder report = new StringBuilder(200)
        .append("Name: ")
        .append(genotyphiResult.getAssemblyId())
        .append("\n")
        .append("Genotype: ")
        .append(genotyphiResult.getGenotype())
        .append("\n")
        .append("Mutations: ");

    return report.append(genotyphiResult
        .getGenotyphiMutations()
        .entrySet()
        .stream()
        .flatMap(gene -> gene
            .getValue()
            .stream()
            .map(mutation -> gene.getKey() + "_" + mutation.getLocation() + mutation.getVariant() + "_(" + mutation.getGenotyphiGroup().toCode() + ")")
        )
        .collect(Collectors.joining(", "))).toString();
  }
}
