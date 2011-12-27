package org.escidoc.core.service.metadata.resources;

import com.google.common.base.Preconditions;

import org.escidoc.core.service.metadata.repository.ItemRepository;
import org.escidoc.core.service.metadata.repository.internal.InMemoryItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;

import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;
import de.escidoc.core.resources.common.MetadataRecords;
import de.escidoc.core.resources.om.item.Item;

@Path("items/{item-id}/metadata/{metadata-name}")
public class ItemMetadataResource {

  private final static Logger LOG = LoggerFactory.getLogger(ItemMetadataResource.class);
  private final ItemRepository ir = new InMemoryItemRepository();

  @GET
  @Produces("text/plain")
  public String getAsText(@PathParam("item-id") final String itemId,
      @PathParam("metadata-name") final String m) {
    Preconditions.checkNotNull(itemId, "itemId is null: %s", itemId);
    Preconditions.checkNotNull(m, "m is null: %s", m);

    final String msg = "Get a request for item with the id: " + itemId + " metadata name: " + m;
    LOG.debug(msg);

    final Item i = fetchItem(itemId);
    if (i == null) {
      throw new WebApplicationException(404);
    }
    final MetadataRecords mr = i.getMetadataRecords();

    return msg;
  }

  private Item fetchItem(final String itemId) {
    try {
      return ir.find(itemId);
    } catch (final EscidocException e) {
      throw new WebApplicationException(404);
    } catch (final InternalClientException e) {
      throw new WebApplicationException(404);
    } catch (final TransportException e) {
      throw new WebApplicationException(404);
    }
  }

  @PUT
  @Produces("application/xml")
  public String update() {
    throw new WebApplicationException(500);
  }
}