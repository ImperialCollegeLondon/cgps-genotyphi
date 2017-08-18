package net.cgps.wgsa.genotyphi;

import net.cgps.wgsa.genotyphi.core.GenotyphiGene;
import net.cgps.wgsa.genotyphi.core.GenotyphiMutation;
import net.cgps.wgsa.genotyphi.core.GenotyphiSchema;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class SchemaBuilder implements Function<Path, GenotyphiSchema> {

  private final Logger logger = LoggerFactory.getLogger(SchemaBuilder.class);

  @Override
  public GenotyphiSchema apply(final Path dataCsv) {
    final Map<String, GenotyphiGene> geneMap = new HashMap<>(70);

    try (final CSVParser csvParser = CSVParser.parse(dataCsv.toAbsolutePath().toFile(), Charset.defaultCharset(), CSVFormat.RFC4180)) {

      for (final CSVRecord record : csvParser.getRecords()) {

        if ("Group".equals(record.get(0))) {
          continue;
        }

        final String geneName = record.get(2);
        if (!geneMap.containsKey(geneName)) {
          // Add new gene record keyed by name.
          geneMap.put(geneName, new GenotyphiGene(geneName, new ArrayList<>(2)));
        }

        // Add the variant to the gene.
        geneMap.get(geneName).getVariants().add(new GenotyphiMutation(record.get(1), GenotyphiSchema.GenotyphiGroup.build(record.get(0)), Integer.valueOf(record.get(4))));
      }
    } catch (final IOException e) {
      this.logger.error("Failure parsing genotyphi input file:", e);
      throw new RuntimeException(e);
    }

    return new GenotyphiSchema(geneMap.values());
  }
}
