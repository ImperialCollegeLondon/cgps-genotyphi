package net.cgps.wgsa.genotyphi.formats;

import net.cgps.wgsa.genotyphi.GenotyphiResult;

import java.util.stream.Collectors;

public class TextFormatter implements Formatter {

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

  @Override
  public String getFileExtension() {
    return ".txt";
  }
}
