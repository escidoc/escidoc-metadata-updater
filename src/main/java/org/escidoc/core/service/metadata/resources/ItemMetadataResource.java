package org.escidoc.core.service.metadata.resources;

import com.google.common.base.Preconditions;

import com.sun.jersey.api.NotFoundException;

import org.escidoc.core.service.metadata.repository.ItemRepository;
import org.escidoc.core.service.metadata.repository.internal.InMemoryItemRepository;
import org.escidoc.core.service.metadata.repository.internal.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.xml.transform.dom.DOMSource;

import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;
import de.escidoc.core.resources.common.MetadataRecord;
import de.escidoc.core.resources.common.MetadataRecords;
import de.escidoc.core.resources.om.item.Item;

@Path("items/{item-id}/metadata/{metadata-name}")
public class ItemMetadataResource {

  private final static Logger LOG = LoggerFactory.getLogger(ItemMetadataResource.class);

  // FIXME configure this using Guice
  private final ItemRepository ir = new InMemoryItemRepository();

  // private final ItemRepository ir = new ItemRepositoryImpl();

  @GET
  @Produces(MediaType.TEXT_PLAIN)
  public Response getAsText(@PathParam("item-id") final String itemId,
      @PathParam("metadata-name") final String metadataName,
      @QueryParam("eu") final String escidocUri) {
    Preconditions.checkNotNull(itemId, "itemId is null: %s", itemId);
    Preconditions.checkNotNull(metadataName, "m is null: %s", metadataName);

    final String msg = "Get a request for item with the id: " + itemId + " metadata name: "
        + metadataName + ", server uri: " + escidocUri;
    LOG.debug(msg);

    if (escidocUri == null || escidocUri.isEmpty()) {
      throw new WebApplicationException(400);
    }

    final Item item = fetchItem(itemId, escidocUri);
    if (item == null) {
      throw new NotFoundException("Item, " + itemId + ", is not found.");
    }

    final MetadataRecords mrList = item.getMetadataRecords();
    if (mrList == null || mrList.isEmpty()) {
      throw new NotFoundException("Metadata, " + metadataName + ", is not found");
    }

    final MetadataRecord mr = mrList.get(metadataName);
    if (mr == null) {
      throw new NotFoundException("Metadata, " + metadataName + ", is not found");
    }

    final String asString = Utils.asString(mr);
    if (asString.isEmpty()) {
      return Response.status(Status.NO_CONTENT).build();
    }
    return Response.ok(asString).build();
  }

  @GET
  @Produces(MediaType.APPLICATION_XML)
  public Response getAsXml(@PathParam("item-id") final String itemId,
      @PathParam("metadata-name") final String metadataName,
      @QueryParam("eu") final String escidocUri) {
    Preconditions.checkNotNull(itemId, "itemId is null: %s", itemId);
    Preconditions.checkNotNull(metadataName, "m is null: %s", metadataName);

    final String msg = "Get a request for item with the id: " + itemId + " metadata name: "
        + metadataName + ", server uri: " + escidocUri;
    LOG.debug(msg);

    if (escidocUri == null || escidocUri.isEmpty()) {
      throw new WebApplicationException(400);
    }

    final Item item = fetchItem(itemId, escidocUri);
    if (item == null) {
      throw new NotFoundException("Item, " + itemId + ", is not found.");
    }

    final MetadataRecords mrList = item.getMetadataRecords();
    if (mrList == null || mrList.isEmpty()) {
      throw new NotFoundException("Metadata, " + metadataName + ", is not found");
    }

    final MetadataRecord mr = mrList.get(metadataName);
    if (mr == null) {
      throw new NotFoundException("Metadata, " + metadataName + ", is not found");
    }

    final Element content = mr.getContent();
    if (content == null) {
      return Response.status(Status.NO_CONTENT).build();
    }

    return Response.ok(new DOMSource(mr.getContent())).build();
  }

  private Item fetchItem(final String itemId, final String s) {
    try {
      return ir.find(itemId, new URI(s));
    } catch (final EscidocException e) {
      // FIXME map the true exception.
      LOG.error("Can not fetch item " + itemId + " cause: " + e.getMessage(), e);
      throw new WebApplicationException(e, Status.INTERNAL_SERVER_ERROR);
    } catch (final InternalClientException e) {
      LOG.error("Can not fetch item " + itemId + " cause: " + e.getMessage(), e);
      throw new WebApplicationException(e, Status.INTERNAL_SERVER_ERROR);
    } catch (final TransportException e) {
      LOG.error("Can not fetch item " + itemId + " cause: " + e.getMessage(), e);
      throw new WebApplicationException(e, Status.INTERNAL_SERVER_ERROR);
    } catch (final MalformedURLException e) {
      LOG.error("Can not fetch item " + itemId + " cause: " + e.getMessage(), e);
      throw new WebApplicationException(e, Status.INTERNAL_SERVER_ERROR);
    } catch (final URISyntaxException e) {
      LOG.error("Can not fetch item " + itemId + " cause: " + e.getMessage(), e);
      throw new WebApplicationException(e, Status.INTERNAL_SERVER_ERROR);
    }
  }

  @PUT
  @Produces("application/xml")
  public String update() {
    throw new WebApplicationException(500);
  }
}