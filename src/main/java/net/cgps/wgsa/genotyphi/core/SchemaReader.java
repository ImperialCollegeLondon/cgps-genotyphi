package net.cgps.wgsa.genotyphi.core;

import net.cgps.wgsa.genotyphi.lib.AbstractJsonnable;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Function;

public class SchemaReader implements Function<Path, GenotyphiSchema> {
  @Override
  public GenotyphiSchema apply(final Path path) {

    return AbstractJsonnable.fromJson(Paths.get(path.toString(), "schema.jsn").toFile(), GenotyphiSchema.class);
  }
}
