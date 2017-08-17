# cgps-genotyphi
CGPS implementation of (Genotyphi by Kat Holt)[https://github.com/katholt/genotyphi] for assembled genomes. Genotyphi is a typing scheme for _Salmonella_ Typhi that uses a set a curated mutations to assign strains to a particular labelled clade.

For a full description of Genotyphi please visit the above link, and read the (original paper)[https://www.nature.com/articles/ncomms12827/]. 

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
docker build -t genotyphi-builder -f builder.Dockerfile .
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

NB If you used the recommended docker build process, substitute `genotyphi` for `registry.gitlab.com/cgps/cgps-genotyphi/genotyphi`.

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

### Output Format

Currently only a JSON output is supported.

#### JSON

A complete example of the JSON format can be found in [here](/examples/output.jsn)

The key field of interest to most will be the `resistanceProfile` field. For each antibiotic, the resistance state (e.g. `RESISTANT`) and resistance groups that have been found are listed.

The individual results and detailed BLAST data can be found in the `paarResult` & `snparResult` fields.

```
{
      "resistanceState": "RESISTANT",
      "resistanceSets": [
        {
          "effect": "RESISTANT",
          "resistanceSetName": "TEM-1",
          "agents": [
            "AMP"
          ],
          "elementIds": [
            "TEM-1"
          ],
          "modifiers": {}
        }
      ],
      "agent": {
        "name": "AMP",
        "fullName": "Ampicillin",
        "type": "Beta-lactam"
      }
    },
```

#### Naming Docker Builds

Container tags are automatically generated during the build phase by Maven using [jgitver](https://github.com/jgitver/jgitver).

To create a "release tag" (i.e. not appended with "-SNAPSHOT") and push the resulting container to a remote Docker repository:
```
git tag -a -m "My message" v1.0.0-rc4
docker run -it --rm --name genotyphi -v /var/run/docker.sock:/var/run/docker.sock -v "$(pwd)":/usr/src/mymaven -v maven-repo:/root/.m2 -v ~/.docker:/root/.docker -w /usr/src/mymaven genotyphi-builder mvn install
```

The Docker repository can be changed from the CGPS default by editing the `<genotyphi.docker-repository>` property in the top level `pom.xml`.
