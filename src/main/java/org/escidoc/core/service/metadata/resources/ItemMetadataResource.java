package org.escidoc.core.service.metadata.resources;

import com.google.common.base.Preconditions;

import com.sun.jersey.api.NotFoundException;

import org.escidoc.core.service.metadata.repository.ItemRepository;
import org.escidoc.core.service.metadata.repository.internal.ItemRepositoryImpl;
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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import javax.xml.transform.dom.DOMSource;

import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;
import de.escidoc.core.client.exceptions.application.notfound.ResourceNotFoundException;
import de.escidoc.core.client.exceptions.application.security.AuthorizationException;
import de.escidoc.core.resources.common.MetadataRecord;
import de.escidoc.core.resources.common.MetadataRecords;
import de.escidoc.core.resources.om.item.Item;

@Path("items/{item-id}/metadata/{metadata-name}")
public class ItemMetadataResource {

  private final static Logger LOG = LoggerFactory.getLogger(ItemMetadataResource.class);

  // FIXME configure this using Guice
  // private final ItemRepository ir = new InMemoryItemRepository();

  private final ItemRepository ir = new ItemRepositoryImpl();

  @GET
  @Produces(MediaType.TEXT_PLAIN)
  public Response getAsText(@PathParam("item-id") final String itemId,
      @PathParam("metadata-name") final String metadataName, @QueryParam("eu") final String escidocUri,
      @CookieParam("escidocCookie") final String escidocCookie) {

    checkPreconditions(itemId, metadataName, escidocUri);
    checkQueryParameter(escidocUri);

    MetadataRecord mr = null;
    try {
      mr = findMetadataByName(metadataName, findItem(itemId, escidocUri, escidocCookie));
    } catch (final AuthorizationException e) {
      LOG.error("Can not fetch item " + itemId + " cause: " + e.getMessage(), e);
      return Response.seeOther(null).build();
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
    if (Utils.asString(mr).isEmpty()) {
      return Response.status(Status.NO_CONTENT).build();
    }
    return Response.ok(Utils.asString(mr)).build();
  }

  @GET
  @Produces(MediaType.APPLICATION_XML)
  public Response getAsXml(@Context final UriInfo ui, @PathParam("item-id") final String itemId,
      @PathParam("metadata-name") final String metadataName, @QueryParam("eu") final String escidocUri,
      @CookieParam("escidocCookie") final String escidocCookie) {
    checkPreconditions(itemId, metadataName, escidocUri);
    checkQueryParameter(escidocUri);

    MetadataRecord mr = null;
    try {
      mr = findMetadataByName(metadataName, findItem(itemId, escidocUri, escidocCookie));
    } catch (final AuthorizationException e) {
      LOG.error("Can not fetch item " + itemId + " cause: " + e.getMessage(), e);
      return Response.seeOther(null).build();
    } catch (final ResourceNotFoundException e) {
      LOG.error("Can not fetch item " + itemId + " cause: " + e.getMessage(), e);
      // FIXME add not found URI
      throw new NotFoundException(e.getMessage());
    } catch (final EscidocException e) {
      // FIXME map the true exception.
      LOG.error("Can not fetch item " + itemId + " cause: " + e.getMessage(), e);
      throw new WebApplicationException(e, Status.INTERNAL_SERVER_ERROR);
    } catch (final InternalClientException e) {
      LOG.error("Can not fetch item " + itemId + " cause: " + e.getMessage(), e);
      // FIXME this is bad, if the request is not auth-ed the server response
      // with 303 and have HTML form as entity.
      // TODO try with: using REST client and de/serialize the xml manually.

      final URI u = UriBuilder.fromUri(escidocUri).path("aa").path("login").queryParam("target", ui.getRequestUri()).build();
      LOG.debug("absolute path: " + u);
      return Response.temporaryRedirect(
      // seeOther(
          UriBuilder.fromUri(escidocUri).path("aa").path("login").queryParam("target", ui.getRequestUri()).build()).build();
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

    Preconditions.checkNotNull(mr, "mr is null: %s", mr);
    if (mr.getContent() == null) {
      return Response.status(Status.NO_CONTENT).build();
    }

    return Response.ok(new DOMSource(mr.getContent())).build();
  }

  @PUT
  @Consumes("application/xml")
  @Produces("application/xml")
  public Response update(@PathParam("item-id") final String itemId,
      @PathParam("metadata-name") final String metadataName, @QueryParam("eu") final String escidocUri,
      final DOMSource s, @CookieParam("escidocCookie") final String escidocCookie) {
    checkPreconditions(itemId, metadataName, escidocUri);

    LOG.debug("Metadata should be updated to: " + s);
    try {
      final Item item = findItem(itemId, escidocUri, escidocCookie);

    } catch (final AuthorizationException e) {
      LOG.error("Can not fetch item " + itemId + " cause: " + e.getMessage(), e);
      return Response.seeOther(null).build();
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
    // @formatter:off
    return Response
        .status(405)
        .entity("PUT is not supported yet")
        .build();
	   // @formatter:on
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

  private static void checkQueryParameter(final String escidocUri) {
    if (escidocUri == null || escidocUri.isEmpty()) {
      throw new WebApplicationException(400);
    }
  }

  private Item findItem(final String itemId, final String escidocUri, final String cookie) throws EscidocException,
      InternalClientException, TransportException, MalformedURLException, URISyntaxException {
    // try {
    return ir.find(itemId, new URI(escidocUri), cookie);

  }

}