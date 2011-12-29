package org.escidoc.core.service.metadata.repository.internal;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.StringWriter;

import javax.ws.rs.WebApplicationException;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import de.escidoc.core.resources.common.MetadataRecord;

public class Utils {

  public static final String DC_URI = "http://purl.org/dc/elements/1.1/";

  public static Element buildSimpleMetadata() throws ParserConfigurationException {
    final Document doc = createNewDocument();
    final Element e = doc.createElementNS(DC_URI, "dc");
    final Element t = doc.createElementNS(DC_URI, "title");
    t.setPrefix("dc");
    t.setTextContent("test title");
    e.appendChild(t);
    return e;
  }

  private static Document createNewDocument() throws ParserConfigurationException {
    return DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
  }

  public static String asString(final MetadataRecord mr) {
    final Element node = mr.getContent();
    try {
      final Transformer transformer = TransformerFactory.newInstance().newTransformer();
      final StringWriter sw = new StringWriter();
      transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
      transformer.transform(new DOMSource(node), new StreamResult(sw));
      return sw.toString();
    } catch (final TransformerConfigurationException e) {
      throw new WebApplicationException(500);
    } catch (final TransformerException e) {
      throw new WebApplicationException(500);
    }
  }
}
