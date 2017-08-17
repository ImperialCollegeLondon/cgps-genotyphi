package net.cgps.wgsa.genotyphi.core;

import java.util.Collection;
import java.util.Collections;

public class GenotyphiGene {

  private final String sequenceId;
  private final Collection<GenotyphiMutation> variants;

  @SuppressWarnings("unused")
  private GenotyphiGene() {

    this("", Collections.emptyList());
  }

  public GenotyphiGene(final String sequenceId, final Collection<GenotyphiMutation> variants) {

    this.sequenceId = sequenceId;
    this.variants = variants;
  }

  public String getSequenceId() {

    return this.sequenceId;
  }

  public Collection<GenotyphiMutation> getVariants() {

    return this.variants;
  }
}
