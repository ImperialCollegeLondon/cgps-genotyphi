FROM openjdk:8-jre

RUN apt-get update && apt-get install -y --no-install-recommends \
		curl \
		python \
	&& rm -rf /var/lib/apt/lists/* \
	&& mkdir /opt/blast \
      && curl ftp://ftp.ncbi.nlm.nih.gov/blast/executables/blast+/2.2.30/ncbi-blast-2.2.30+-x64-linux.tar.gz \
      | tar -zxC /opt/blast --strip-components=1 \
      && cd /opt/blast \
      && find . ! -name 'blastn' -type f -exec rm -f {} + \
      && rm -rf doc

ENV PATH /opt/blast/bin:$PATH

COPY build/genotyphi.jar /

RUN mkdir /data && mkdir /resources

WORKDIR /data

CMD "bash"
#ENTRYPOINT ["java","-jar","/paarsnp.jar"]
