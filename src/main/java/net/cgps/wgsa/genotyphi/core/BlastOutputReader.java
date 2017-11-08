package net.cgps.wgsa.genotyphi.core;

import net.cgps.wgsa.genotyphi.lib.ncbi.BlastOutput;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Source;
import javax.xml.transform.sax.SAXSource;
import java.io.BufferedReader;
import java.util.function.Function;

public class BlastOutputReader implements Function<BufferedReader, BlastOutput> {
  @Override
  public BlastOutput apply(final BufferedReader inputStream) {

    final String packageName = BlastOutput.class.getPackage().getName();

    try {
      final JAXBContext jc = JAXBContext.newInstance(packageName);
      final Unmarshaller unmarshaller = jc.createUnmarshaller();

      final XMLReader xmlreader = XMLReaderFactory.createXMLReader();
      xmlreader.setFeature("http://xml.org/sax/features/namespaces", true);
      xmlreader.setFeature("http://xml.org/sax/features/namespace-prefixes", true);
      xmlreader.setEntityResolver((publicId, systemId) -> {

            String file = null;
            if (systemId.contains("NCBI_BlastOutput.dtd")) {
              file = "/NCBI_BlastOutput.dtd";
            }
            if (systemId.contains("NCBI_Entity.mod.dtd")) {
              file = "/NCBI_Entity.mod.dtd";
            }
            if (systemId.contains("NCBI_BlastOutput.mod.dtd")) {
              file = "/NCBI_BlastOutput.mod.dtd";
            }
            return new InputSource(BlastOutput.class.getResourceAsStream(file));
          }
      );
      final InputSource input = new InputSource(inputStream);
      final Source source = new SAXSource(xmlreader, input);
      return (BlastOutput) unmarshaller.unmarshal(source);
    } catch (final JAXBException | SAXException e) {
      throw new RuntimeException(e);
    }
  }
}
