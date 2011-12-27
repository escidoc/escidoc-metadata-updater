package org.escidoc.core.service.metadata.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("helloworld")
public class HelloResource {

  @GET
  @Produces({"text/plain", "text/html"})
  public String getAsTextORHtml() {
    return "Ok";
  }

  @PUT
  @Consumes("application/xml")
  public String update() {
    return "<html>Created</html>";
  }

}
