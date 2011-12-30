package org.escidoc.core.service.metadata.resources;

import com.google.common.base.Preconditions;

import com.sun.jersey.api.NotFoundException;

import org.escidoc.core.service.metadata.repository.ItemRepository;
import org.escidoc.core.service.metadata.repository.internal.InMemoryItemRepository;
import org.escidoc.core.service.metadata.repository.internal.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.ws.rs.Consumes;
import javax.ws.rs.CookieParam;
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
      @PathParam("metadata-name") final String metadataName, @QueryParam("eu") final String escidocUri) {

    checkPreconditions(itemId, metadataName, escidocUri);
    checkQueryParameter(escidocUri);
    final MetadataRecord mr = findMetadataByName(metadataName, findItem(itemId, escidocUri));

    if (Utils.asString(mr).isEmpty()) {
      return Response.status(Status.NO_CONTENT).build();
    }
    return Response.ok(Utils.asString(mr)).build();
  }

  private static void checkPreconditions(final String itemId, final String metadataName, final String escidocUri) {
    Preconditions.checkNotNull(itemId, "itemId is null: %s", itemId);
    Preconditions.checkNotNull(metadataName, "m is null: %s", metadataName);

    final String msg = "Get a request for item with the id: " + itemId + ", metadata name: " + metadataName
        + ", server uri: " + escidocUri;
    LOG.debug(msg);
  }

  private static MetadataRecord findMetadataByName(final String metadataName, final Item item) {
    final MetadataRecords mrList = item.getMetadataRecords();
    if (mrList == null || mrList.isEmpty()) {
      throw new NotFoundException("Metadata, " + metadataName + ", is not found");
    }

    final MetadataRecord mr = mrList.get(metadataName);
    if (mr == null) {
      throw new NotFoundException("Metadata, " + metadataName + ", is not found");
    }
    return mr;
  }

  private Item findItem(final String itemId, final String escidocUri) {
    final Item item = fetchItem(itemId, escidocUri);
    if (item == null) {
      throw new NotFoundException("Item, " + itemId + ", is not found.");
    }
    return item;
  }

  private static void checkQueryParameter(final String escidocUri) {
    if (escidocUri == null || escidocUri.isEmpty()) {
      throw new WebApplicationException(400);
    }
  }

  @GET
  @Produces(MediaType.APPLICATION_XML)
  public Response getAsXml(@PathParam("item-id") final String itemId,
      @PathParam("metadata-name") final String metadataName, @QueryParam("eu") final String escidocUri,
      @CookieParam("escidocCookie") final String escidocCookie) {
    checkPreconditions(itemId, metadataName, escidocUri);
    checkQueryParameter(escidocUri);

    MetadataRecord mr = null;
    if (escidocCookie == null) {
      mr = findMetadataByName(metadataName, findItem(itemId, escidocUri));
    } else {
      mr = findMetadataByName(metadataName, findItem(itemId, escidocUri, escidocCookie));
    }

    Preconditions.checkNotNull(mr, "mr is null: %s", mr);
    if (mr.getContent() == null) {
      return Response.status(Status.NO_CONTENT).build();
    }

    return Response.ok(new DOMSource(mr.getContent())).build();
  }

  private Item findItem(final String itemId, final String escidocUri, final String cookie) {
    final Item item = fetchItem(itemId, escidocUri, cookie);
    throw new UnsupportedOperationException("not-yet-implemented.");
  }

  private Item fetchItem(final String itemId, final String escidocUri, final String cookie) {
    //@formatter:off
    return ir.
        find(itemId, new URI(escidocUri))
        .withToken(cookie)
        .execute();
    //@formatter:on
    throw new UnsupportedOperationException("not-yet-implemented.");
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
  @Consumes("application/xml")
  @Produces("application/xml")
  public Response update(@PathParam("item-id") final String itemId,
      @PathParam("metadata-name") final String metadataName, @QueryParam("eu") final String escidocUri,
      final DOMSource s) {
    checkPreconditions(itemId, metadataName, escidocUri);

    LOG.debug("Metadata should be updated to: " + s);
    final Item item = findItem(itemId, escidocUri);

    // @formatter:off
    return Response
        .status(405)
        .entity("PUT is not supported yet")
        .build();
	   // @formatter:on
  }
}