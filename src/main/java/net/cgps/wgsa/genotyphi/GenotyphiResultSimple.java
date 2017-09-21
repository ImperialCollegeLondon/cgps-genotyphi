package net.cgps.wgsa.genotyphi;

import net.cgps.wgsa.genotyphi.core.GenotyphiMutation;
import net.cgps.wgsa.genotyphi.lib.Jsonnable;

import java.util.Collection;
import java.util.Map;

public class GenotyphiResultSimple extends Jsonnable {

  private final String assemblyId;
  private final String genotype;
  private final Collection<Map.Entry<String, Collection<GenotyphiMutation>>> genotyphiMutations;
  private final byte foundLoci;

  public GenotyphiResultSimple(final String assemblyId, final String genotype, final Collection<Map.Entry<String, Collection<GenotyphiMutation>>> genotyphiMutations, final byte foundLoci) {
    this.assemblyId = assemblyId;
    this.genotype = genotype;
    this.genotyphiMutations = genotyphiMutations;
    this.foundLoci = foundLoci;
  }

  public static GenotyphiResultSimple fromFullResult(final GenotyphiResult fullResult) {
    return new GenotyphiResultSimple(fullResult.getAssemblyId(), fullResult.getGenotype(), fullResult.getGenotyphiMutations(), fullResult.getFoundLoci());
  }

  public String getAssemblyId() {
    return this.assemblyId;
  }

  public String getGenotype() {
    return this.genotype;
  }

  public Collection<Map.Entry<String, Collection<GenotyphiMutation>>> getGenotyphiMutations() {
    return this.genotyphiMutations;
  }

  public byte getFoundLoci() {
    return this.foundLoci;
  }
}
