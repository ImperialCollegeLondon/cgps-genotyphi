package net.cgps.wgsa.genotyphi;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.function.Consumer;
import java.util.function.Function;

public class StdoutWriter implements Consumer<GenotyphiResult> {

  private final Function<GenotyphiResult, String> formatter;

  public StdoutWriter(final Function<GenotyphiResult, String> formatter) {
    this.formatter = formatter;
  }

  @Override
  public void accept(final GenotyphiResult genotyphiResult) {
    try (final BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(System.out))) {
      bufferedWriter.append(formatter.apply(genotyphiResult));
      bufferedWriter.newLine();
    } catch (final IOException e) {
      throw new RuntimeException(e);
    }
  }
}
