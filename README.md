# cgps-genotyphi
CGPS implementation of [Genotyphi by Kat Holt _et al_](https://github.com/katholt/genotyphi) for assembled genomes. Genotyphi is a typing scheme for _Salmonella_ Typhi that uses a set of curated mutations to assign strains to a particular labelled clade.

For a full description of Genotyphi please visit the above link, and read the [original paper](https://www.nature.com/articles/ncomms12827/).

[Getting Started](#getting-started)

[With Docker](#running-with-docker)

[Without Docker](#running-directly)

[Output Format](#output-format)

## Getting Started

CGPS-Genotyphi can be run as a JAVA programme (Linux/MacOS) or using Docker (all platforms). We will provide a public Docker container soon.

### Docker-based Build (recommended)

Requires:
* Docker (Optional: Git for building from master with version tags)
* Runs on any OS supported by Docker.

1. Download the code as a zip bundle, e.g. for the latest code use the example below. Alternatively, pick a specific release from ("Releases")[/releases]. Alternatively, you can `git clone  https://github.com/ImperialCollegeLondon/cgps-genotyphi.git`.

```
wget https://github.com/ImperialCollegeLondon/cgps-genotyphi/archive/master.zip
unzip code-genotyphi-master.zip
```
2. Installation
```
cd genotyphi
docker build -t genotyphi-builder -f Dockerfile .
# The next command actually builds genotyphi as a JAR and as a container
docker run -it --rm --name genotyphi -v /var/run/docker.sock:/var/run/docker.sock -v "$(pwd)":/usr/src/mymaven -v ~/.docker:/root/.docker -w /usr/src/mymaven genotyphi-builder mvn package
```
Or, for faster future builds, create a docker volume (2nd command) and use it for future builds (third command):
```
docker build -t genotyphi-builder -f Dockerfile .
docker volume create --name maven-repo
# Use this command for faster future builds.
docker run -it --rm --name genotyphi -v /var/run/docker.sock:/var/run/docker.sock -v "$(pwd)":/usr/src/mymaven -v maven-repo:/root/.m2 -v ~/.docker:/root/.docker -w /usr/src/mymaven genotyphi-builder mvn package
```

At this point you can use [Docker](#running-with-docker) or run it directly from the [terminal](#running-directly) (requires JAVA 8 & blastn to be installed as well).

### Maven Build

Requires:
* git, maven, java 8, makeblastdb (on $PATH)

Optional:
* blastn on $PATH (for running the unit tests)

```
git clone https://github.com/ImperialCollegeLondon/cgps-genotyphi.git
cd cgps-genotyphi
mvn -Dmaven.test.skip=true install
# (or leave out -Dmaven.test.skip=true if blastn is available)
```

This will configure the BLAST databases and resources that PAARSNP needs.

At this point you can use [Docker](#running-with-docker) or run it directly from the [terminal](#running-directly).

To create the Paarsnp runner container, run:

1. cd build
1. docker build -t genotyphi -f DockerFile .

### Running with Docker

Usage
-----

To run genotyphi on a single Salmonella _Typhi_ FASTA file in the local directory using the container. An output file `{assembly}_genotyphi.jsn` is created.

NB If you used the recommended docker build process, substitute `genotyphi` for `registry.gitlab.com/cgps/cgps-genotyphi`.

`docker run --rm -v $PWD:/data genotyphi -i assembly.fa`

To run genotyphi on all FASTA files in the local directory, with an output file for each one:

`docker run --rm -v $PWD:/data genotyphi -i .`

If the FASTA files are in a different directory use 

`docker run --rm -v /full/path/to/FASTAS/:/data registry.gitlab.com/cgps/cgps-genotyphi -i .`
 
NB "/data" is a protected folder for genotyphi, and is normally used to mount the local drive.

To get the results to STDOUT rather than file:

`docker run --rm -v $PWD:/data genotyphi -i assembly.fa -o`

NB not pretty printed, one record per line

### Running Directly

* The JAR file is `build/genotyphi.jar` and can be moved anywhere. It assumes the database directory is in the same directory, but this can be specified with the `-d` command line option.
* Get options and help: `java -jar genotyphi.jar`
* e.g. a single assembly `java -jar genotyphi.jar -i salty_assembly.fa`

## Output Format

The output format can be selected using the `-f`/`-format` option. It defaults to `Text`.
1. [Text](#text-format)
1. [CSV](#csv-format)
1. [JSON](#json-format)
1. [Pretty JSON](#pretty-json-format)
1. [Simple JSON](#simple-json-format)

### Text Format

The text format contains three lines:
 
1. The assembly ID
1. The genotype
1. The determining mutations: {geneName}_{location}{variant}_({associated genotype}) 

```
Name: 007898
Genotype: 4.3.1
Mutations: STY2513_1047T_(4.3.1), STY2867_515C_(2), STY3196_989A_(3)
```

### CSV Format

The CSV format contains the same fields as the [text format](#text-format), but in columns instead. In default mode one file per assembly is written. If you want a single CSV file for all assemblies use the `-o` option and write the STDOUT to file, e.g:

`docker run --rm -v $PWD:/data registry.gitlab.com/cgps/cgps-genotyphi -i . -o -f csv > genotyphi.csv`

```
10071_8#7.contigs_velvet,3.5.4,"STY0176_969T_(3.5.4); STY2867_515C_(2); STY3196_989A_(3); STY4063_411T_(3.5)"
13566_1#53.contigs_velvet,3.1.1,"STY3203_9C_(3.1); STY2863_154T_(3.1.1); STY2867_515C_(2); STY3196_989A_(3)"
9870_8#7.contigs_velvet,4.3.1,"STY2513_1047T_(4.3.1); STY2867_515C_(2); STY3196_989A_(3)"
ERR1079262_paired.contigs_spades,3.2.2,"STY4741_444T_(3.2.2); STY3196_989A_(3)"
```

### JSON Format

A complete example of the JSON format can be found in [here](/examples/output.jsn). The example below is "pretty" formatted. By default it is printed on a single line with no spaces.

```
{
  "assemblyId" : "my_assembly",
  "genotype" : "4.3.1",
  "foundLoci" : 68.0,
  "aggregatedAssignments" : {
    "primaryGroups" : [ {
      "depth" : "PRIMARY",
      "code" : [ "3" ]
    } ],
    "cladeGroups" : [ ],
    "subcladeGroups" : [ {
      "depth" : "SUBCLADE",
      "code" : [ "4", "3", "1" ]
    } ]
  },
  "genotyphiMutations" : {
    "STY2513" : [ {
      "variant" : "T",
      "genotyphiGroup" : {
        "depth" : "SUBCLADE",
        "code" : [ "4", "3", "1" ]
      },
      "location" : 1047
    } ],
    "STY2867" : [ {
      "variant" : "C",
      "genotyphiGroup" : {
        "depth" : "PRIMARY",
        "code" : [ "2" ]
      },
      "location" : 515
    } ],
    "STY3196" : [ {
      "variant" : "A",
      "genotyphiGroup" : {
        "depth" : "PRIMARY",
        "code" : [ "3" ]
      },
      "location" : 989
    } ]
  },
  "blastResults" : [ {
    "blastSearchStatistics" : {
      "librarySequenceId" : "STY3940",
      "librarySequenceStart" : 1,
      "querySequenceId" : ".12045_3_90.22",
      "querySequenceStart" : 55709,
      "percentIdentity" : 100.0,
      "evalue" : 0.0,
      "reversed" : false,
      "librarySequenceStop" : 1401,
      "querySequenceStop" : 57109,
      "librarySequenceLength" : 1401
    },
    "mutations" : [ ],
    "queryMatchSequence" : "GTGTCA...",
    "referenceMatchSequence" : "GTGTCA..."
  },
  ...
  ]
}
```

### Pretty JSON Format

This formats the JSON nicely as in the example given above.

### Simple JSON Format

The same as the above JSON format, but without the BLAST results or aggregation result details.

## Naming Docker Builds

Container tags are automatically generated during the build phase by Maven using [jgitver](https://github.com/jgitver/jgitver).

To create a "release tag" (i.e. not appended with "-SNAPSHOT") and push the resulting container to a remote Docker repository:
```
git tag -a -m "My message" v1.0.0-rc4
docker run -it --rm --name genotyphi -v /var/run/docker.sock:/var/run/docker.sock -v "$(pwd)":/usr/src/mymaven -v maven-repo:/root/.m2 -v ~/.docker:/root/.docker -w /usr/src/mymaven genotyphi-builder mvn install
```

The Docker repository can be changed from the CGPS default by editing the `<genotyphi.docker-repository>` property in the top level `pom.xml`.
