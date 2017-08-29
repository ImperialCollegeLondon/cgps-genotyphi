package net.cgps.wgsa.genotyphi.formats;

import net.cgps.wgsa.genotyphi.GenotyphiResult;

import java.util.stream.Collectors;

public class CsvFormatter implements Formatter {

  @Override
  public String apply(final GenotyphiResult genotyphiResult) {

    final StringBuilder report = new StringBuilder(200)
        .append(genotyphiResult.getAssemblyId())
        .append(",")
        .append(genotyphiResult.getGenotype())
        .append(",")
        .append("\"");

    return report.append(genotyphiResult
        .getGenotyphiMutations()
        .stream()
        .flatMap(gene -> gene
            .getValue()
            .stream()
            .map(mutation -> gene.getKey() + "_" + mutation.getLocation() + mutation.getVariant() + "_(" + mutation.getGenotyphiGroup().toCode() + ")")
        )
        .collect(Collectors.joining("; ")))
        .append("\"")
        .toString();
  }

  @Override
  public String getFileExtension() {
    return ".csv";
  }
}
