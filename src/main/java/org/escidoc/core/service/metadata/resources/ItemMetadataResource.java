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
import java.util.Date;

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
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
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

  private static final Logger LOG = LoggerFactory.getLogger(ItemMetadataResource.class);
  private static final String XSLT_FILE = "md-to-html-form.xsl";
  private static final String BASIC_REALM = "eSciDoc Resources";
  private static final String AUTHORIZATION = "authorization";
  private static final String BASIC = "Basic";

  @Context
  private HttpServletRequest sr;

  @Context
  private Request r;

  @Inject
  private ItemRepository ir;

  // TODO we should use the browser cookie instead of eSciDocUserHandle query
  // parameter
  @GET
  @Produces(MediaType.APPLICATION_XML)
  public Response getAsXml(@PathParam("item-id") final String itemId,
      @PathParam("metadata-name") final String metadataName, @QueryParam("eu") final String escidocUri,
      @QueryParam("eSciDocUserHandle") final String encodedHandle) {

    checkPreconditions(itemId, metadataName, escidocUri, sr);
    final String msg = "HTTP GET request for item with the id: " + itemId + ", metadata name: " + metadataName
        + ", server uri: " + escidocUri;
    LOG.debug(msg);

    try {
      final Item item = findItem(itemId, escidocUri, encodedHandle);

      final MetadataRecord mr = findMetadataByName(metadataName, item);
      if (mr.getContent() == null) {
        return Response.status(Status.NO_CONTENT).build();
      }
      final ResponseBuilder b = r.evaluatePreconditions(getLastModificationDate(item), getEntityTag(mr));
      if (b != null) {
        return b.build();
      }

      // @formatter:off
      return Response
          .ok(new DOMSource(mr.getContent()))
          .lastModified(getLastModificationDate(item))
          .tag(getEntityTag(mr))
          .build();
	   // @formatter:on

    } catch (final AuthenticationException e) {
      LOG.debug("Auth. credentials is not valid while accessing protected source. ");
      return response401();
    } catch (final AuthorizationException e) {
      LOG.debug("Auth. credentials is not valid while accessing protected source. ");
      return response401();
    } catch (final InternalClientException e) {
      if (e.getCause() instanceof org.jibx.runtime.JiBXException) {
        // We assume here, the ijc can not unmarshall the HTML Login Form.
        LOG.debug("No auth token is provided or not valid while accessing protected source. ");
        return response401();
      }
      LOG.error("Can not fetch metadata with the name, " + metadataName + ", from item, " + itemId + ", reason: "
          + e.getMessage());
      throw new WebApplicationException(e, Status.INTERNAL_SERVER_ERROR);
    }
  }

  @GET
  @Produces(MediaType.TEXT_HTML)
  public Response getAsHtml(@PathParam("item-id") final String itemId,
      @PathParam("metadata-name") final String metadataName, @QueryParam("eu") final String escidocUri,
      @QueryParam("eSciDocUserHandle") final String encodedHandle) {
    checkPreconditions(itemId, metadataName, escidocUri, sr);
    final String msg = "HTTP GET request for item with the id: " + itemId + ", metadata name: " + metadataName
        + ", server uri: " + escidocUri;
    LOG.debug(msg);

    try {
      final Item item = findItem(itemId, escidocUri, encodedHandle);
      final MetadataRecord metadata = findMetadataByName(metadataName, item);
      if (metadata.getContent() == null) {
        return Response.status(Status.NO_CONTENT).build();
      }

      final String transformed = transformToHtml(metadata);
      final ResponseBuilder b = r.evaluatePreconditions(getLastModificationDate(item), getEntityTag(transformed));
      if (b != null) {
        return b.build();
      }

      // @formatter:off
      return Response
          .ok(transformed,MediaType.TEXT_HTML)
          .lastModified(getLastModificationDate(item))
          .tag(getEntityTag(metadata))
          .build();
    //@formatter:on
    } catch (final AuthenticationException e) {
      return response401();
    } catch (final AuthorizationException e) {
      return response401();
    } catch (final InternalClientException e) {
      // We assume here, the ijc can not unmarshall the HTML Login Form.
      if (e.getCause() instanceof org.jibx.runtime.JiBXException) {
        return response401();
      }
      LOG.error("Can not fetch metadata with the name, " + metadataName + ", from item, " + itemId + ", reason: "
          + e.getMessage());
      throw new WebApplicationException(e, Status.INTERNAL_SERVER_ERROR);
    }
  }

  @PUT
  @Consumes("application/xml")
  @Produces("application/xml")
  public Response update(@PathParam("item-id") final String itemId,
      @PathParam("metadata-name") final String metadataName, @QueryParam("eu") final String escidocUri,
      final DOMSource s, @QueryParam("eSciDocUserHandle") final String encodedHandle) {
    checkPreconditions(itemId, metadataName, escidocUri, sr);
    final String msg = "HTTP PUT request for item with the id: " + itemId + ", metadata name: " + metadataName
        + ", server uri: " + escidocUri;
    LOG.debug(msg);

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
    }
  }

  private static EntityTag getEntityTag(final String mr) {
    return new EntityTag(computeDigest(mr.getBytes()));
  }

  private static EntityTag getEntityTag(final MetadataRecord mr) {
    return getEntityTag(mr.toString());
  }

  private static Date getLastModificationDate(final Item item) {
    return item.getLastModificationDate().toDate();
  }

  /**
   * Runtime Exceptions:
   * 
   * @throws AuthenticationException
   * @throws AuthorizationException
   */
  private Item findItem(final String itemId, final String escidocUri, final String encodedHandle)
      throws AuthenticationException, AuthorizationException, InternalClientException {
    try {
      final String decodedHandle = getHandleIfAny(escidocUri, encodedHandle);
      final Item item = ir.find(itemId, new URI(escidocUri), decodedHandle);
      if (item == null) {
        throw new NotFoundException("Item," + itemId + ", not found");
      }
      return item;
    } catch (final AuthenticationException e) {
      throw new AuthenticationException(e.getMessage(), e);
    } catch (final ItemNotFoundException e) {
      throw new NotFoundException("Item," + itemId + ", not found");
    } catch (final EscidocException e) {
      throw new WebApplicationException(e, Status.INTERNAL_SERVER_ERROR);
    } catch (final TransportException e) {
      throw new WebApplicationException(e, Status.INTERNAL_SERVER_ERROR);
    } catch (final MalformedURLException e) {
      throw new WebApplicationException(e, Status.INTERNAL_SERVER_ERROR);
    } catch (final URISyntaxException e) {
      throw new WebApplicationException(e, Status.INTERNAL_SERVER_ERROR);
    }
  }

  private String getHandleIfAny(final String escidocUri, final String encodedHandle) throws AuthenticationException,
      TransportException, MalformedURLException {
    if (has(encodedHandle)) {
      return decodeHandle(encodedHandle);
    } else if (hasAuthHeader()) {
      final String[] creds = sr.getHeader(AUTHORIZATION).split(" ");
      if (useHttpBasicAuth(creds) && notEmpty(creds)) {
        return loginToEscidoc(escidocUri, creds);
      }
    }
    return "";
  }

  private static String transformToHtml(final MetadataRecord mr) {
    try {
      final StringWriter s = new StringWriter();
      TransformerFactory.newInstance().newTransformer(new StreamSource(readXsl())).transform(
          new DOMSource(mr.getContent()), new StreamResult(s));
      return s.toString();
    } catch (final TransformerConfigurationException e) {
      throw new WebApplicationException(e, Status.INTERNAL_SERVER_ERROR);
    } catch (final TransformerException e) {
      throw new WebApplicationException(e, Status.INTERNAL_SERVER_ERROR);
    } catch (final TransformerFactoryConfigurationError e) {
      throw new WebApplicationException(e, Status.INTERNAL_SERVER_ERROR);
    }
  }

  private static String loginToEscidoc(final String escidocUri, final String[] creds) throws AuthenticationException,
      TransportException, MalformedURLException {
    return new Authentication(new URL(escidocUri), getUserName(creds), getPassword(creds)).getHandle();
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
    return sr.getHeader(AUTHORIZATION) != null;
  }

  private static boolean has(final String encodedHandle) {
    return encodedHandle != null;
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
    // @formatter:off
    return Response
        .status(Status.UNAUTHORIZED)
        .header("WWW-Authenticate", "Basic realm=\"" + BASIC_REALM + "\"")
        .type("text/plain")
        .entity("Authentification credentials are required")
        .build();
	   // @formatter:on
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