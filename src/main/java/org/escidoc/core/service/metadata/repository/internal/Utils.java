package org.escidoc.core.service.metadata.repository.internal;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

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

}
