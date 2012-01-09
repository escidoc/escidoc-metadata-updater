package org.escidoc.core.service.metadata.resources;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.io.StringWriter;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

@Path("helloworld")
public class HelloResource {

  private static final String XSLT_FILE = "md-to-html-form.xsl";
  private static final String XML_FILE = "org-unit-metadata.xml";

  private final static Logger LOG = LoggerFactory.getLogger(HelloResource.class);

  @GET
  @Produces({"text/plain", "text/html"})
  public Response getAsTextOrHtmlOrXml() {
    try {
      final StringWriter s = new StringWriter();
      TransformerFactory.newInstance().newTransformer(new StreamSource(readXsl())).transform(
          new StreamSource(readXml()), new StreamResult(s));
      LOG.info("result: " + s);
      // @formatter:off
      return Response
          .ok(s.toString())
          .build();
    //@formatter:on
    } catch (final TransformerConfigurationException e) {
      LOG.error("Error: " + e.getMessage(), e);
      throw new WebApplicationException(e, Status.INTERNAL_SERVER_ERROR);
    } catch (final TransformerException e) {
      LOG.error("Error: " + e.getMessage(), e);
      throw new WebApplicationException(e, Status.INTERNAL_SERVER_ERROR);
    }
  }

  private static InputStream readXml() {
    return Thread.currentThread().getContextClassLoader().getResourceAsStream(XML_FILE);
  }

  private static InputStream readXsl() {
    return Thread.currentThread().getContextClassLoader().getResourceAsStream(XSLT_FILE);
  }

  @GET
  @Produces("application/xml")
  public Response getAsXml() {
    // @formatter:off
    return Response
        .ok("<OK/>")
        .build();
    //@formatter:on
  }

  @PUT
  @Consumes("application/xml")
  public String update() {
    return "<html>Created</html>";
  }
}