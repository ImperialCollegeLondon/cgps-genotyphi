package net.cgps.wgsa.genotyphi.core;

import net.cgps.wgsa.genotyphi.lib.AbstractJsonnable;

public class GenotyphiMutation extends AbstractJsonnable {

  private final String variant;
  private final GenotyphiSchema.GenotyphiGroup genotyphiGroup;
  private final int location;

  @SuppressWarnings("unused")
  private GenotyphiMutation() {

    this("", null, 0);
  }

  public GenotyphiMutation(final String variant, final GenotyphiSchema.GenotyphiGroup genotyphiGroup, final int location) {
    super();
    this.variant = variant;
    this.genotyphiGroup = genotyphiGroup;
    this.location = location;
  }

  public String getVariant() {

    return this.variant;
  }

  public int getLocation() {

    return this.location;
  }

  public GenotyphiSchema.GenotyphiGroup getGenotyphiGroup() {

    return this.genotyphiGroup;
  }
}