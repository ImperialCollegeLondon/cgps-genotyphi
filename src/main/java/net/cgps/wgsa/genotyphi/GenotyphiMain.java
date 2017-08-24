package net.cgps.wgsa.genotyphi;

import ch.qos.logback.classic.Level;
import net.cgps.wgsa.genotyphi.core.GenotyphiSchema;
import net.cgps.wgsa.genotyphi.core.SchemaReader;
import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GenotyphiMain {

  private final Logger logger = LoggerFactory.getLogger(GenotyphiMain.class);

  public static void main(final String[] args) {

    // Initialise the options parser
    final Options options = GenotyphiMain.myOptions();
    final CommandLineParser parser = new DefaultParser();

    if (args.length == 0) {
      final HelpFormatter formatter = new HelpFormatter();
      formatter.setWidth(200);
      formatter.printHelp("java -jar paarsnp.jar <options>", options);
      System.exit(1);
    }

    try {
      final CommandLine commandLine = parser.parse(options, args);

      final ch.qos.logback.classic.Logger root = (ch.qos.logback.classic.Logger) org.slf4j.LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
      root.setLevel(Level.valueOf(commandLine.getOptionValue('l', "INFO")));

      String databasePath = commandLine.getOptionValue('d', "resources");

      if (!Files.exists(Paths.get(databasePath))) {
        databasePath = "/genotyphi/" + databasePath;
      }

      if (commandLine.hasOption('b')) {

        // Run build mode
        new GenotyphiBuilder().run(Paths.get(commandLine.getOptionValue('i', "resources/")), Paths.get(databasePath));

      } else {

        // Run normal mode.
        // Resolve the file path.
        final Path input = Paths.get(commandLine.getOptionValue('i'));

        final Collection<Path> fastas;
        final Path workingDirectory;

        if (Files.exists(input, LinkOption.NOFOLLOW_LINKS)) {

          if (Files.isRegularFile(input)) {
            workingDirectory = input.toAbsolutePath().getParent();
            fastas = Collections.singletonList(input);
            root.debug("Processing one file", input);
          } else {
            fastas = new ArrayList<>(10000);
            try (final DirectoryStream<Path> stream = Files.newDirectoryStream(
                input
                , entry -> {
                  root.debug(entry.toString());
                  return entry.toString().endsWith(".fna") || entry.toString().endsWith(".fa") || entry.toString().endsWith(".fasta");
                })
            ) {
              stream.forEach(fastas::add);
            }
            root.debug("Processing {} files from \"{}\".", fastas.size(), input.toAbsolutePath().toString());
            workingDirectory = input;
          }
        } else {
          throw new RuntimeException("Can't find input file or directory " + input.toAbsolutePath().toString());
        }

        new GenotyphiMain().run(fastas, workingDirectory, commandLine.hasOption('o'), Paths.get(databasePath), Format.valueOf(commandLine.getOptionValue('f', "text").toUpperCase()));
      }
    } catch (final Exception e) {
      LoggerFactory.getLogger(GenotyphiMain.class).error("Failed to run due to: ", e);
      System.exit(1);
    }
  }

  private static Options myOptions() {

    final Option assemblyListOption = Option.builder("i").longOpt("input").hasArg().argName("Assembly file(s)").desc("If a directory is provided then all FASTAs (.fna, .fa, .fasta) are searched.").build();

    final Option buildModeOption = Option.builder("b").longOpt("buildMode").argName("Build Mode").desc("If provided then the genotyphi build mode will be executed. Build mode generates the blast databases (requireds makeblastdb to be on $PATH). The -i option can be used to set the input directory containing data.csv & genes.fa").build();

    final Option resourceDirectoryOption = Option.builder("d").longOpt("database-directory").hasArg().argName("Database directory").desc("Location of the BLAST databases and resources for .").build();

    final Option logLevel = Option.builder("l").longOpt("log-level").hasArg().argName("Logging level").desc("INFO, DEBUG etc").build();

    final Option outputOption = Option.builder("o").longOpt("outfile").argName("Create output file").desc("Use this flag if you want the result written to STDOUT rather than file.").build();

    final Option formatOption = Option.builder("f").longOpt("format").hasArg().argName("Select the output format from " + Format.getFormats()).build();

    final Options options = new Options();
    options.addOption(assemblyListOption)
        .addOption(resourceDirectoryOption)
        .addOption(outputOption)
        .addOption(buildModeOption)
        .addOption(formatOption)
        .addOption(logLevel);

    return options;
  }

  private void run(final Collection<Path> fastas, final Path workingDirectory, final boolean toStdout, final Path databasePath, final Format format) {

    final GenotyphiSchema schema = new SchemaReader().apply(databasePath);

    final GenotyphiRunner runner = new GenotyphiRunner(schema, databasePath);

    final Consumer<GenotyphiResult> writer = this.getConsumer(format, toStdout, workingDirectory);

    this.logger.info("Writing format {}", format.name().toLowerCase());

    fastas
        .stream()
        .map(runner)
        .forEach(writer);
  }

  private Consumer<GenotyphiResult> getConsumer(final Format format, final boolean isToStdout, final Path workingDirectory) {

    final Function<GenotyphiResult, String> formatter;

    switch (format) {
      case JSON:
        formatter = genotyphiResult -> genotyphiResult.toJson();
        break;
      case PRETTY_JSON:
        formatter = genotyphiResult -> genotyphiResult.toPrettyJson();
        break;
      case SIMPLE_JSON:
        formatter = genotyphiResult -> GenotyphiResultSimple.fromFullResult(genotyphiResult).toPrettyJson();
        break;
      case TEXT:
      default:
        formatter = new TextFormatter();
    }

    if (isToStdout) {
      return new StdoutWriter(formatter);
    } else {
      return new FileWriter(formatter, workingDirectory);
    }
  }

  public enum Format {
    TEXT, JSON, PRETTY_JSON, SIMPLE_JSON;

    public static String getFormats() {
      return Stream.of(Format.values()).map(Format::name).map(String::toLowerCase).collect(Collectors.joining(", "));
    }
  }
}

