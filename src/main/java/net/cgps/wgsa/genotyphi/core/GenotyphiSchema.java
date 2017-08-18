package net.cgps.wgsa.genotyphi.core;

import net.cgps.wgsa.genotyphi.lib.AbstractJsonnable;

import java.util.*;
import java.util.stream.Stream;

public class GenotyphiSchema extends AbstractJsonnable {

  private final Collection<GenotyphiGene> genes;

  @SuppressWarnings("unused")
  private GenotyphiSchema() {

    this(Collections.emptyList());
  }

  public GenotyphiSchema(final Collection<GenotyphiGene> genes) {

    this.genes = genes;
  }

  public Collection<GenotyphiGene> getGenes() {

    return this.genes;
  }

  public Stream<Map.Entry<String, GenotyphiGene>> asEntries() {

    return this.genes.stream().map(gene -> new AbstractMap.SimpleImmutableEntry<>(gene.getSequenceId(), gene));
  }

  public enum Depth {
    PRIMARY(0), CLADE(1), SUBCLADE(2);

    private final int index;

    Depth(final int index) {

      this.index = index;
    }

    public static Depth maxDepth() {

      return Stream.of(Depth.values()).sorted(Comparator.comparingInt(Depth::getIndex).reversed()).findFirst().get();
    }

    public static Optional<Depth> toDepth(final int levels) {

      return Stream.of(Depth.values()).filter(depth -> (depth.getIndex() + 1) == levels).findFirst();
    }

    public int getIndex() {

      return this.index;
    }
  }

  public static class GenotyphiGroup {

    // Deepest level of the genotyphi codes is 3.
    private static final int maxDepth = Depth.maxDepth().getIndex() + 1;
    private final Depth depth; // 1 - 3
    private final List<String> code;

    @SuppressWarnings("unused")
    private GenotyphiGroup() {

      this(Depth.PRIMARY, Collections.emptyList());
    }

    private GenotyphiGroup(final Depth depth, final List<String> code) {

      this.depth = depth;
      this.code = code;
    }

    public static GenotyphiGroup build(final String code) {

      final String[] codeArr = code.split("\\.");
      if (maxDepth < codeArr.length) {
        throw new RuntimeException("Invalid code for Genotyphi (too many levels " + codeArr.length + "): " + code);
      }

      final Depth depth = Depth.toDepth(codeArr.length).orElseThrow(() -> new RuntimeException("Not a recognised number of levels: " + codeArr.length));
      return new GenotyphiGroup(depth, Arrays.asList(codeArr));
    }

    public List<String> getCode() {

      return this.code;
    }

    public Depth getDepth() {

      return this.depth;
    }

    @Override
    public int hashCode() {

      int result = this.depth.hashCode();
      result = (31 * result) + this.code.hashCode();
      return result;
    }

    @Override
    public boolean equals(final Object o) {

      if (this == o) {
        return true;
      }
      if ((null == o) || (this.getClass() != o.getClass())) {
        return false;
      }

      final GenotyphiGroup that = (GenotyphiGroup) o;

      if (this.depth != that.depth) {
        return false;
      }
      // Probably incorrect - comparing Object[] arrays with Arrays.equals
      return this.code.equals(that.code);
    }

    @Override
    public String toString() {

      return this.toCode() + " (Depth=" + this.depth + ")";
    }

    public String toCode() {

      return String.join(".", this.code);
    }
  }

}
