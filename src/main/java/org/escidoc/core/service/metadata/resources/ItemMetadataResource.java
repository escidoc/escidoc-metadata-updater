package org.escidoc.core.service.metadata.resources;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

@Path("items/{item-id}/metadata/{metadata-name}")
public class ItemMetadataResource {

  private final static Logger LOG = LoggerFactory.getLogger(ItemMetadataResource.class);

  @GET
  @Produces("text/plain")
  public String getAsText(@PathParam("item-id") final String itemId,
      @PathParam("metadata-name") final String m) {
    final String msg = "Get a request for item with the id: " + itemId + " metadata name: " + m;
    LOG.debug(msg);
    return msg;
  }

  @PUT
  @Produces("application/xml")
  public String update() {
    return "Ok";
  }

}
