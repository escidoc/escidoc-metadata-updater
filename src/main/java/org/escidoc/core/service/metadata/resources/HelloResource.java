package org.escidoc.core.service.metadata.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("helloworld")
public class HelloResource {

  @GET
  @Produces({"text/plain", "text/html"})
  public Response getAsTextOrHtmlOrXml() {
    // @formatter:off
    return Response
        .ok("OK")
        .build();
    //@formatter:on
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
