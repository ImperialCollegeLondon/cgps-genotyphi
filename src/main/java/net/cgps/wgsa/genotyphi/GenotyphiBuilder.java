package net.cgps.wgsa.genotyphi;

import net.cgps.wgsa.genotyphi.core.GenotyphiSchema;
import net.cgps.wgsa.genotyphi.lib.MakeBlastDB;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class GenotyphiBuilder {

  private final Logger logger = LoggerFactory.getLogger(GenotyphiBuilder.class);

  public void run(final Path inputPath, final Path databasePath) {

    // Builds blast database & genotyphi schema doc.
    final GenotyphiSchema genotyphiSchema = new SchemaBuilder().apply(Paths.get(inputPath.toString(), "data.csv"));

    try {
      Files.write(Paths.get(databasePath.toString(), "schema.jsn"), genotyphiSchema.toJson().getBytes(), StandardOpenOption.CREATE);
    } catch (final IOException e) {
      this.logger.error("Failed to write genotyphi DB. {}", e);
      throw new RuntimeException(e);
    }

    new MakeBlastDB(databasePath).accept("genotyphi", Paths.get(inputPath.toString(), "genes.fa"));
  }
}
