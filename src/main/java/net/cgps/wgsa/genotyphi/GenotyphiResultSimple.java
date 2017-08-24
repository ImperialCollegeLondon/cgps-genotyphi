package net.cgps.wgsa.genotyphi;

import net.cgps.wgsa.genotyphi.core.GenotyphiMutation;
import net.cgps.wgsa.genotyphi.lib.Jsonnable;

import java.util.Collection;
import java.util.Map;

public class GenotyphiResultSimple extends Jsonnable {

  private final String assemblyId;
  private final String genotype;
  private final Map<String, Collection<GenotyphiMutation>> genotyphiMutations;

  public GenotyphiResultSimple(final String assemblyId, final String genotype, final Map<String, Collection<GenotyphiMutation>> genotyphiMutations) {
    this.assemblyId = assemblyId;
    this.genotype = genotype;
    this.genotyphiMutations = genotyphiMutations;
  }

  public static GenotyphiResultSimple fromFullResult(final GenotyphiResult fullResult) {
    return new GenotyphiResultSimple(fullResult.getAssemblyId(), fullResult.getGenotype(), fullResult.getGenotyphiMutations());
  }

  public String getAssemblyId() {
    return assemblyId;
  }

  public String getGenotype() {
    return genotype;
  }

  public Map<String, Collection<GenotyphiMutation>> getGenotyphiMutations() {
    return genotyphiMutations;
  }
}
