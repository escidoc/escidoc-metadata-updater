package org.escidoc.core.service.metadata.resources;

import com.google.common.base.Preconditions;

import com.sun.jersey.api.NotFoundException;
import com.sun.jersey.core.util.Base64;

import org.escidoc.core.service.metadata.repository.ItemRepository;
import org.escidoc.core.service.metadata.repository.internal.ItemRepositoryImpl;
import org.escidoc.core.service.metadata.repository.internal.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;

import java.io.InputStream;
import java.io.StringWriter;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.MessageDigest;

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
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;
import de.escidoc.core.client.exceptions.application.notfound.ResourceNotFoundException;
import de.escidoc.core.client.exceptions.application.security.AuthenticationException;
import de.escidoc.core.resources.common.MetadataRecord;
import de.escidoc.core.resources.common.MetadataRecords;
import de.escidoc.core.resources.om.item.Item;

@Path("items/{item-id}/metadata/{metadata-name}")
public class ItemMetadataResource {
  @Context
  Request request;

  private final static Logger LOG = LoggerFactory.getLogger(ItemMetadataResource.class);
  private static final String XSLT_FILE = "md-to-html-form.xsl";
  // FIXME make it configurable
  // private final ItemRepository ir = new InMemoryItemRepository();

  private final ItemRepository ir = new ItemRepositoryImpl();

  // TODO we should use the browser cookie instead of eSciDocUserHandle query
  // parameter
  @GET
  @Produces(MediaType.APPLICATION_XML)
  public Response getAsXml(@Context final UriInfo ui, @PathParam("item-id") final String itemId,
      @PathParam("metadata-name") final String metadataName, @QueryParam("eu") final String escidocUri,
      @CookieParam("escidocCookie") final String escidocCookie, @QueryParam("eSciDocUserHandle") final String handle) {

    checkPreconditions(itemId, metadataName, escidocUri);
    checkQueryParameter(escidocUri);
    Preconditions.checkNotNull(request, "request is null: %s", request);

    try {
      final MetadataRecord mr = tryFindMetadataByName(ui, itemId, metadataName, escidocUri, decodeHandle(handle));
      Preconditions.checkNotNull(mr, "mr is null: %s", mr);
      if (mr.getContent() == null) {
        return Response.status(Status.NO_CONTENT).build();
      }
      // @formatter:off
      return Response
          .ok(new DOMSource(mr.getContent()))
          .tag(new EntityTag(computeDigest(mr.getContent().toString().getBytes())))
          .build();
	   // @formatter:on
    } catch (final InternalClientException e) {
      LOG.debug("Cookie is not provided or not valid while accessing protected source. ");
      return redirect(ui, escidocUri);
    } catch (final AuthenticationException e) {
      LOG.debug("Cookie is not valid while accessing protected source. ");
      return redirect(ui, escidocUri);
    }
  }

  @GET
  @Produces(MediaType.TEXT_HTML)
  public Response getAsHtml(@Context final UriInfo ui, @PathParam("item-id") final String itemId,
      @PathParam("metadata-name") final String metadataName, @QueryParam("eu") final String escidocUri,
      @CookieParam("escidocCookie") final String escidocCookie, @QueryParam("eSciDocUserHandle") final String handle) {

    checkPreconditions(itemId, metadataName, escidocUri);
    checkQueryParameter(escidocUri);
    Preconditions.checkNotNull(request, "request is null: %s", request);

    try {
      final MetadataRecord mr = tryFindMetadataByName(ui, itemId, metadataName, escidocUri, decodeHandle(handle));
      Preconditions.checkNotNull(mr, "mr is null: %s", mr);
      if (mr.getContent() == null) {
        return Response.status(Status.NO_CONTENT).build();
      }

      final StringWriter s = new StringWriter();
      TransformerFactory.newInstance().newTransformer(new StreamSource(readXsl())).transform(new DOMSource(mr.getContent()), new StreamResult(s));
      LOG.info("result: " + s);
      // @formatter:off
        return Response
            .ok(s.toString(),MediaType.TEXT_HTML)
            .tag(new EntityTag(computeDigest(s.toString().getBytes())))
            .build();
      //@formatter:on
    } catch (final AuthenticationException e) {
      LOG.debug("Cookie is not valid while accessing protected source. ");
      return redirect(ui, escidocUri);
    } catch (final InternalClientException e) {
      LOG.debug("Cookie is not provided or not valid while accessing protected source. ");
      return redirect(ui, escidocUri);
    } catch (final TransformerConfigurationException e) {
      LOG.error("Error: " + e.getMessage(), e);
      throw new WebApplicationException(e, Status.INTERNAL_SERVER_ERROR);
    } catch (final TransformerException e) {
      LOG.error("Error: " + e.getMessage(), e);
      throw new WebApplicationException(e, Status.INTERNAL_SERVER_ERROR);
    }
  }

  @GET
  @Produces(MediaType.TEXT_PLAIN)
  public Response getAsText(@Context final UriInfo ui, @PathParam("item-id") final String itemId,
      @PathParam("metadata-name") final String metadataName, @QueryParam("eu") final String escidocUri,
      @CookieParam("escidocCookie") final String escidocCookie, @QueryParam("eSciDocUserHandle") final String handle) {

    checkPreconditions(itemId, metadataName, escidocUri);
    checkQueryParameter(escidocUri);
    Preconditions.checkNotNull(request, "request is null: %s", request);

    try {
      final MetadataRecord mr = tryFindMetadataByName(ui, itemId, metadataName, escidocUri, decodeHandle(handle));
      if (Utils.asString(mr).isEmpty()) {
        return Response.status(Status.NO_CONTENT).build();
      }
      // @formatter:off
      return Response
          .ok(Utils.asString(mr))
          .tag(new EntityTag(computeDigest(Utils.asString(mr).getBytes())))
          .build();
	   // @formatter:on 
    } catch (final InternalClientException e) {
      LOG.debug("Cookie is not provided or not valid while accessing protected source. ");
      return redirect(ui, escidocUri);
    } catch (final AuthenticationException e) {
      LOG.debug("Cookie is not valid while accessing protected source. ");
      return redirect(ui, escidocUri);
    }
  }

  @PUT
  @Consumes("application/xml")
  @Produces("application/xml")
  public Response update(@Context final UriInfo ui, @PathParam("item-id") final String itemId,
      @PathParam("metadata-name") final String metadataName, @QueryParam("eu") final String escidocUri,
      final DOMSource s, @CookieParam("escidocCookie") final String escidocCookie,
      @QueryParam("eSciDocUserHandle") final String handle) {

    checkPreconditions(itemId, metadataName, escidocUri);
    Preconditions.checkNotNull(request, "request is null: %s", request);

    final Item item = tryFindItemById(itemId, escidocUri, Base64.base64Decode(handle));
    final MetadataRecord mr = findMetadataByName(metadataName, item);
    mr.setContent((Element) s.getNode().getFirstChild());

    try {
      final Item updated = ir.update(item);
      Preconditions.checkNotNull(updated, "updated is null: %s", updated);
      // @formatter:off
      return Response
          .ok()
          .build();
  	   // @formatter:on
    } catch (final EscidocException e) {
      LOG.error("Can not update metadata with the name, " + metadataName + ", from item, " + itemId + ", reason: "
          + e.getMessage());
      throw new WebApplicationException(e, Status.INTERNAL_SERVER_ERROR);
    } catch (final InternalClientException e) {
      return redirect(ui, escidocUri);
    } catch (final TransportException e) {
      LOG.error("Can not update metadata with the name, " + metadataName + ", from item, " + itemId + ", reason: "
          + e.getMessage());
      throw new WebApplicationException(e, Status.INTERNAL_SERVER_ERROR);
    }
  }

  private static String computeDigest(final byte[] content) {
    try {
      final MessageDigest md = MessageDigest.getInstance("SHA");
      final byte[] digest = md.digest(content);
      final BigInteger bi = new BigInteger(digest);
      return bi.toString(16);
    } catch (final Exception e) {
      return "";
    }
  }

  private static InputStream readXsl() {
    return Thread.currentThread().getContextClassLoader().getResourceAsStream(XSLT_FILE);
  }

  private static Response redirect(final UriInfo ui, final String escidocUri) {
    // @formatter:off
    final URI u = UriBuilder.
        fromUri(escidocUri)
        .path("aa")
        .path("login")
        .queryParam("target",ui.getRequestUri())
        .build();
	   // @formatter:on
    return Response.seeOther(u).build();
  }

  private MetadataRecord tryFindMetadataByName(final UriInfo ui, final String itemId, final String metadataName,
      final String escidocUri, final String escidocCookie) throws InternalClientException, AuthenticationException {
    MetadataRecord mr = null;
    try {
      mr = findMetadataByName(metadataName, findItem(itemId, escidocUri, escidocCookie));
    } catch (final AuthenticationException e) {
      throw new AuthenticationException("", e);
    } catch (final ResourceNotFoundException e) {
      LOG.error("Can not fetch item " + itemId + " cause: " + e.getMessage(), e);
      // FIXME add not found URI
      throw new NotFoundException(e.getMessage());
    } catch (final EscidocException e) {
      // FIXME map the true exception.
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
    return mr;
  }

  private static String decodeHandle(final String handle) {
    if (handle != null) {
      return Base64.base64Decode(handle);
    }
    return "";
  }

  private Item tryFindItemById(final String itemId, final String escidocUri, final String escidocCookie) {
    try {
      final Item item = findItem(itemId, escidocUri, escidocCookie);
      Preconditions.checkNotNull(item, "item is null: %s", item);
      return item;
    } catch (final EscidocException e) {
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
      throw new WebApplicationException(Status.BAD_REQUEST);
    }
  }

  private Item findItem(final String itemId, final String escidocUri, final String cookie) throws EscidocException,
      InternalClientException, TransportException, MalformedURLException, URISyntaxException, AuthenticationException {
    return ir.find(itemId, new URI(escidocUri), cookie);
  }
}