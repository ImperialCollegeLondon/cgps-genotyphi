package net.cgps.wgsa.genotyphi;

import java.util.function.Consumer;
import java.util.function.Function;

public class StdoutWriter implements Consumer<GenotyphiResult> {

  private final Function<GenotyphiResult, String> formatter;

  public StdoutWriter(final Function<GenotyphiResult, String> formatter) {
    this.formatter = formatter;
  }

  @Override
  public void accept(final GenotyphiResult genotyphiResult) {
    System.out.println(formatter.apply(genotyphiResult));
  }
}
