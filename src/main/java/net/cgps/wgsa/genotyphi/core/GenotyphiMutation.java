package net.cgps.wgsa.genotyphi.core;

import net.cgps.wgsa.genotyphi.lib.Jsonnable;

public class GenotyphiMutation extends Jsonnable {

  private final String variant;
  private final GenotyphiSchema.GenotyphiGroup genotyphiGroup;
  private final int location;

  @SuppressWarnings("unused")
  private GenotyphiMutation() {

    this("", null, 0);
  }

  public GenotyphiMutation(final String variant, final GenotyphiSchema.GenotyphiGroup genotyphiGroup, final int location) {
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

  @Override
  public boolean equals(final Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    final GenotyphiMutation that = (GenotyphiMutation) o;

    if (location != that.location) return false;
    if (variant != null ? !variant.equals(that.variant) : that.variant != null) return false;
    return genotyphiGroup != null ? genotyphiGroup.equals(that.genotyphiGroup) : that.genotyphiGroup == null;
  }

  @Override
  public int hashCode() {
    int result = variant != null ? variant.hashCode() : 0;
    result = 31 * result + (genotyphiGroup != null ? genotyphiGroup.hashCode() : 0);
    result = 31 * result + location;
    return result;
  }
}
