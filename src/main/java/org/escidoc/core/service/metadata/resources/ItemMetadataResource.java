package org.escidoc.core.service.metadata.resources;

import com.google.common.base.Preconditions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;

@Path("items/{item-id}/metadata/{metadata-name}")
public class ItemMetadataResource {

  private final static Logger LOG = LoggerFactory.getLogger(ItemMetadataResource.class);

  @GET
  @Produces("text/plain")
  public String getAsText(@PathParam("item-id") final String itemId,
      @PathParam("metadata-name") final String m) {
    Preconditions.checkNotNull(itemId, "itemId is null: %s", itemId);

    if (itemId == null || itemId.isEmpty()) {
      throw new WebApplicationException(400);
    }

    if (m == null || m.isEmpty()) {
      throw new WebApplicationException(400);
    }

    final String msg = "Get a request for item with the id: " + itemId + " metadata name: " + m;
    LOG.debug(msg);

    // TODO either we use client lib for fetching the metadata or use the
    // escidoc rest interface

    return msg;
  }

  @PUT
  @Produces("application/xml")
  public String update() {
    throw new WebApplicationException(500);
  }
}