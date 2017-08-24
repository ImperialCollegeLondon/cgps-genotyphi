package net.cgps.wgsa.genotyphi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Consumer;
import java.util.function.Function;

public class FileWriter implements Consumer<GenotyphiResult> {

  private final Logger logger = LoggerFactory.getLogger(FileWriter.class);
  private final Function<GenotyphiResult, String> formatter;
  private final Path workingDirectory;

  public FileWriter(final Function<GenotyphiResult, String> formatter, final Path workingDirectory) {
    this.formatter = formatter;
    this.workingDirectory = workingDirectory;
  }

  @Override
  public void accept(final GenotyphiResult genotyphiResult) {

    final Path outFile = Paths.get(workingDirectory.toString(), genotyphiResult.getAssemblyId() + "_genotyphi.jsn");

    this.logger.debug("Writing {}", outFile.toAbsolutePath().toString());

    try (final BufferedWriter writer = Files.newBufferedWriter(outFile)) {
      writer.write(formatter.apply(genotyphiResult));
    } catch (final IOException e) {
      this.logger.error("Failed to write output for {}", genotyphiResult.getAssemblyId());
      throw new RuntimeException(e);
    }
  }
}
