package org.escidoc.core.service.metadata.resources;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;

import com.sun.jersey.api.NotFoundException;
import com.sun.jersey.core.util.Base64;

import org.escidoc.core.service.metadata.repository.ItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;

import java.io.InputStream;
import java.io.StringWriter;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.MessageDigest;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
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
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import de.escidoc.core.client.Authentication;
import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;
import de.escidoc.core.client.exceptions.application.notfound.ItemNotFoundException;
import de.escidoc.core.client.exceptions.application.security.AuthenticationException;
import de.escidoc.core.client.exceptions.application.security.AuthorizationException;
import de.escidoc.core.resources.common.MetadataRecord;
import de.escidoc.core.resources.common.MetadataRecords;
import de.escidoc.core.resources.om.item.Item;

@Path("items/{item-id}/metadata/{metadata-name}")
public class ItemMetadataResource {

  private final static Logger LOG = LoggerFactory.getLogger(ItemMetadataResource.class);
  private static final String XSLT_FILE = "md-to-html-form.xsl";
  private static final String BASIC_REALM = "www.escidoc.org";
  private static final String AUTHORIZATION = "authorization";
  private static final String BASIC = "Basic";

  @Context
  private HttpServletRequest request;

  @Inject
  private ItemRepository ir;

  // TODO we should use the browser cookie instead of eSciDocUserHandle query
  // parameter
  @GET
  @Produces(MediaType.APPLICATION_XML)
  public Response getAsXml(@Context final UriInfo ui, @PathParam("item-id") final String itemId,
      @PathParam("metadata-name") final String metadataName, @QueryParam("eu") final String escidocUri,
      @QueryParam("eSciDocUserHandle") final String encodedHandle) {
    checkPreconditions(itemId, metadataName, escidocUri, request);

    try {
      final Item item = findItem(itemId, escidocUri, encodedHandle);
      final MetadataRecord mr = findMetadataByName(metadataName, item);
      if (mr.getContent() == null) {
        return Response.status(Status.NO_CONTENT).build();
      }

      // @formatter:off
      return Response
          .ok(new DOMSource(mr.getContent()))
          .tag(new EntityTag(computeDigest(mr.getContent().toString().getBytes())))
          .build();
	   // @formatter:on
    } catch (final AuthenticationException e) {
      LOG.debug("Cookie is not valid while accessing protected source. ");
      return response401();
    } catch (final AuthorizationException e) {
      LOG.debug("Cookie is not valid while accessing protected source. ");
      return response401();
    } catch (final ItemNotFoundException e) {
      throw new NotFoundException("Item," + itemId + ", not found");
    } catch (final EscidocException e) {
      LOG.error("Can not fetch metadata with the name, " + metadataName + ", from item, " + itemId + ", reason: "
          + e.getMessage());
      throw new WebApplicationException(e, Status.INTERNAL_SERVER_ERROR);
    } catch (final InternalClientException e) {
      if (e.getCause() instanceof org.jibx.runtime.JiBXException) {
        // We assume here, the ijc can not unmarshall the HTML Login Form.
        LOG.debug("No auth token is provided or not valid while accessing protected source. ");
        return response401();
      }
      LOG.error("Can not fetch metadata with the name, " + metadataName + ", from item, " + itemId + ", reason: "
          + e.getMessage());
      throw new WebApplicationException(e, Status.INTERNAL_SERVER_ERROR);
    } catch (final TransportException e) {
      LOG.error("Can not fetch metadata with the name, " + metadataName + ", from item, " + itemId + ", reason: "
          + e.getMessage());
      throw new WebApplicationException(e, Status.INTERNAL_SERVER_ERROR);
    } catch (final MalformedURLException e) {
      LOG.error("Can not fetch metadata with the name, " + metadataName + ", from item, " + itemId + ", reason: "
          + e.getMessage());
      throw new WebApplicationException(e, Status.INTERNAL_SERVER_ERROR);
    } catch (final URISyntaxException e) {
      LOG.error("Can not fetch metadata with the name, " + metadataName + ", from item, " + itemId + ", reason: "
          + e.getMessage());
      throw new WebApplicationException(e, Status.INTERNAL_SERVER_ERROR);
    }
  }

  private Item findItem(final String itemId, final String escidocUri, final String encodedHandle)
      throws AuthenticationException, TransportException, MalformedURLException, EscidocException,
      InternalClientException, URISyntaxException {
    final String decodedHandle = getHandleIfAny(escidocUri, encodedHandle);
    final Item item = ir.find(itemId, new URI(escidocUri), decodedHandle);
    if (item == null) {
      throw new NotFoundException("Item," + itemId + ", not found");
    }
    return item;
  }

  private String getHandleIfAny(final String escidocUri, final String encodedHandle) throws AuthenticationException,
      TransportException, MalformedURLException {
    if (has(encodedHandle)) {
      return decodeHandle(encodedHandle);
    } else if (hasAuthHeader()) {
      final String[] creds = request.getHeader(AUTHORIZATION).split(" ");
      if (useHttpBasicAuth(creds) && notEmpty(creds)) {
        return loginToEscidoc(escidocUri, creds);
      }
    }
    return "";
  }

  private static String loginToEscidoc(final String escidocUri, final String[] creds) throws AuthenticationException,
      TransportException, MalformedURLException {
    String decodedHandle;
    decodedHandle = new Authentication(new URL(escidocUri), getUserName(creds), getPassword(creds)).getHandle();
    return decodedHandle;
  }

  private static String getPassword(final String[] creds) {
    return decodeHandle(creds[1]).split(":")[1];
  }

  private static String getUserName(final String[] creds) {
    return decodeHandle(creds[1]).split(":")[0];
  }

  private static boolean notEmpty(final String[] creds) {
    return decodeHandle(creds[1]).split(":").length > 0;
  }

  private static boolean useHttpBasicAuth(final String[] creds) {
    return creds[0].contains(BASIC);
  }

  private boolean hasAuthHeader() {
    return request.getHeader(AUTHORIZATION) != null;
  }

  private static boolean has(final String encodedHandle) {
    return encodedHandle != null;
  }

  @GET
  @Produces(MediaType.TEXT_HTML)
  public Response getAsHtml(@Context final UriInfo ui, @PathParam("item-id") final String itemId,
      @PathParam("metadata-name") final String metadataName, @QueryParam("eu") final String escidocUri,
      @QueryParam("eSciDocUserHandle") final String encodedHandle) {

    checkPreconditions(itemId, metadataName, escidocUri, request);

    try {
      final Item item = findItem(itemId, escidocUri, encodedHandle);
      final MetadataRecord mr = findMetadataByName(metadataName, item);
      if (mr.getContent() == null) {
        return Response.status(Status.NO_CONTENT).build();
      }

      final StringWriter s = new StringWriter();
      TransformerFactory.newInstance().newTransformer(new StreamSource(readXsl())).transform(
          new DOMSource(mr.getContent()), new StreamResult(s));
      // @formatter:off
      return Response
          .ok(s.toString(),MediaType.TEXT_HTML)
          .tag(new EntityTag(computeDigest(s.toString().getBytes())))
          .build();
    //@formatter:on
    } catch (final AuthenticationException e) {
      LOG.debug("Cookie is not valid while accessing protected source. ");
      return response401();
    } catch (final AuthorizationException e) {
      LOG.debug("Cookie is not valid while accessing protected source. ");
      return response401();
    }

    catch (final ItemNotFoundException e) {
      throw new NotFoundException("Item," + itemId + ", not found");
    }

    catch (final EscidocException e) {
      LOG.error("Can not fetch metadata with the name, " + metadataName + ", from item, " + itemId + ", reason: "
          + e.getMessage());
      throw new WebApplicationException(e, Status.INTERNAL_SERVER_ERROR);
    } catch (final InternalClientException e) {
      if (e.getCause() instanceof org.jibx.runtime.JiBXException) {
        // We assume here, the ijc can not unmarshall the HTML Login Form.
        LOG.debug("No auth token is provided or not valid while accessing protected source. ");
        return response401();
      }
      LOG.error("Can not fetch metadata with the name, " + metadataName + ", from item, " + itemId + ", reason: "
          + e.getMessage());
      throw new WebApplicationException(e, Status.INTERNAL_SERVER_ERROR);
    } catch (final TransportException e) {
      LOG.error("Can not fetch metadata with the name, " + metadataName + ", from item, " + itemId + ", reason: "
          + e.getMessage());
      throw new WebApplicationException(e, Status.INTERNAL_SERVER_ERROR);
    } catch (final MalformedURLException e) {
      LOG.error("Can not fetch metadata with the name, " + metadataName + ", from item, " + itemId + ", reason: "
          + e.getMessage());
      throw new WebApplicationException(e, Status.INTERNAL_SERVER_ERROR);
    } catch (final URISyntaxException e) {
      LOG.error("Can not fetch metadata with the name, " + metadataName + ", from item, " + itemId + ", reason: "
          + e.getMessage());
      throw new WebApplicationException(e, Status.INTERNAL_SERVER_ERROR);
    } catch (final TransformerConfigurationException e) {
      LOG.error("Can not fetch metadata with the name, " + metadataName + ", from item, " + itemId + ", reason: "
          + e.getMessage());
      throw new WebApplicationException(e, Status.INTERNAL_SERVER_ERROR);
    } catch (final TransformerException e) {
      LOG.error("Can not fetch metadata with the name, " + metadataName + ", from item, " + itemId + ", reason: "
          + e.getMessage());
      throw new WebApplicationException(e, Status.INTERNAL_SERVER_ERROR);
    }
  }

  @PUT
  @Consumes("application/xml")
  @Produces("application/xml")
  public Response update(@Context final UriInfo ui, @PathParam("item-id") final String itemId,
      @PathParam("metadata-name") final String metadataName, @QueryParam("eu") final String escidocUri,
      final DOMSource s, @QueryParam("eSciDocUserHandle") final String encodedHandle) {
    checkPreconditions(itemId, metadataName, escidocUri, request);

    try {

      final Item item = findItem(itemId, escidocUri, encodedHandle);
      final MetadataRecord mr = findMetadataByName(metadataName, item);
      if (mr.getContent() == null) {
        return Response.status(Status.NO_CONTENT).build();
      }

      mr.setContent((Element) s.getNode().getFirstChild());
      final Item updated = ir.update(item);
      Preconditions.checkNotNull(updated, "updated is null: %s", updated);
      // @formatter:off
      return Response
          .ok()
          .build();
  	   // @formatter:on
    } catch (final AuthorizationException e) {
      return response401();
    } catch (final AuthenticationException e) {
      return response401();
    } catch (final EscidocException e) {
      LOG.error("Can not update metadata with the name, " + metadataName + ", from item, " + itemId + ", reason: "
          + e.getMessage());
      throw new WebApplicationException(e, Status.INTERNAL_SERVER_ERROR);
    } catch (final InternalClientException e) {
      return response401();
    } catch (final TransportException e) {
      LOG.error("Can not update metadata with the name, " + metadataName + ", from item, " + itemId + ", reason: "
          + e.getMessage());
      throw new WebApplicationException(e, Status.INTERNAL_SERVER_ERROR);
    } catch (final MalformedURLException e) {
      throw new WebApplicationException(e, Status.INTERNAL_SERVER_ERROR);
    } catch (final URISyntaxException e) {
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

  private static Response response401() {
    return Response.status(Status.UNAUTHORIZED).header("WWW-Authenticate", "Basic realm=\"" + BASIC_REALM + "\"").type(
        "text/plain").entity("Authentification credentials are required").build();
  }

  private static String decodeHandle(final String handle) {
    if (has(handle)) {
      return Base64.base64Decode(handle);
    }
    return "";
  }

  private static void checkPreconditions(final String itemId, final String metadataName, final String escidocUri,
      final HttpServletRequest request) {
    Preconditions.checkNotNull(itemId, "itemId is null: %s", itemId);
    Preconditions.checkNotNull(metadataName, "m is null: %s", metadataName);
    Preconditions.checkNotNull(request, "request is null: %s", request);
    checkQueryParameter(escidocUri);

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
}