package org.escidoc.core.service.metadata.resources;

import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("metadata")
public class ItemMetadataResource {

  @GET
  @Produces("text/plain")
  public String getAsText() {
    return "Ok";
  }

  @GET
  @Produces("application/xml")
  public String getAsXml() {
    return "Ok";
  }

  @PUT
  @Produces("application/xml")
  public String update() {
    return "Ok";
  }

}
