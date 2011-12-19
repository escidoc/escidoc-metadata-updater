package org.escidoc.core.service.metadata.resources;

import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("metadata")
public class Metadata {

  @PUT
  @Produces("application/xml")
  public String update() {
    return "Ok";

  }

}
