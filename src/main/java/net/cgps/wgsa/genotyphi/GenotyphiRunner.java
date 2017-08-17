package net.cgps.wgsa.genotyphi;

import net.cgps.wgsa.genotyphi.core.GenotyphiBlastReader;
import net.cgps.wgsa.genotyphi.core.GenotyphiSchema;
import net.cgps.wgsa.genotyphi.core.ResolveGenotyphi;
import net.cgps.wgsa.genotyphi.lib.BlastRunner;
import net.cgps.wgsa.genotyphi.lib.MutationReader;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Function;

public class GenotyphiRunner implements Function<Path, GenotyphiResult> {

  private final GenotyphiSchema schema;
  private final Path resourceDirectory;

  GenotyphiRunner(final GenotyphiSchema schema, final Path databaseDirectory) {
    this.schema = schema;
    this.resourceDirectory = databaseDirectory;
  }

  @Override
  public GenotyphiResult apply(final Path assemblyFile) {

    final String name = assemblyFile.getFileName().toString();
    final String assemblyId = name.substring(0, name.lastIndexOf('.'));

    final String[] command = new String[]{
        "blastn",
        "-task", "blastn",
        "-outfmt", "5",
        "-query", assemblyFile.toAbsolutePath().toString(),
        "-db", Paths.get(this.resourceDirectory.toString(), "genotyphi").toAbsolutePath().toString(),
        "-perc_identity", "80",
        "-evalue", "1e-60",
        "-num_alignments", "200",
    };

    final GenotyphiBlastReader.GenotyphiResultData resultData = new GenotyphiBlastReader(this.schema, new ResolveGenotyphi()).apply(new BlastRunner<>(new MutationReader()).apply(command));

    return new GenotyphiResult(assemblyId, resultData.getType(), resultData.getGenotyphiMutations(), resultData.getAggregatedAssignments(), resultData.getBlastResults(), resultData.getFoundLoci());
  }
}
