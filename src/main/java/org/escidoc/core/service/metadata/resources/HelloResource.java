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

  private final static Logger LOG = LoggerFactory.getLogger(HelloResource.class);

  @GET
  @Produces({"text/plain", "text/html"})
  public Response getAsTextOrHtmlOrXml() {
    final InputStream xsl = Thread.currentThread().getContextClassLoader().getResourceAsStream("e.xsl");
    final InputStream xml = Thread.currentThread().getContextClassLoader().getResourceAsStream("e.xml");
    try {
      final StringWriter s = new StringWriter();
      TransformerFactory.newInstance().newTransformer(new StreamSource(xsl)).transform(new StreamSource(xml),
          new StreamResult(s)
      // new StreamResult(new FileOutputStream("e.html"))
      );
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