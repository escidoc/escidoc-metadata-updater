package org.escidoc.core.service.metadata.resources;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;

import com.sun.jersey.api.NotFoundException;

import static org.escidoc.core.service.metadata.Utils.checkPreconditions;
import static org.escidoc.core.service.metadata.Utils.getEntityTag;
import static org.escidoc.core.service.metadata.Utils.getHandleIfAny;
import static org.escidoc.core.service.metadata.Utils.getLastModificationDate;
import static org.escidoc.core.service.metadata.Utils.response401;

import org.escidoc.core.service.metadata.AppConstant;
import org.escidoc.core.service.metadata.Utils;
import org.escidoc.core.service.metadata.repository.ContextRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;

import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;

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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;
import javax.xml.transform.dom.DOMSource;

import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;
import de.escidoc.core.client.exceptions.application.security.AuthenticationException;
import de.escidoc.core.client.exceptions.application.security.AuthorizationException;
import de.escidoc.core.resources.GenericResource;
import de.escidoc.core.resources.om.context.AdminDescriptor;
import de.escidoc.core.resources.om.context.AdminDescriptors;

@Path("contexts/{id}/metadata/{metadata-name}")
public class ContextMetadataResource {

  private static final String CONTEXT = "context";

  private final static Logger LOG = LoggerFactory.getLogger(ContextMetadataResource.class);

  @Context
  private HttpServletRequest sr;

  @Context
  private Request r;

  @Inject
  private ContextRepository repo;

  // TODO Idea refactor *MetadataResource:
  // 1. using generic
  // 2. using composition
  // 3. using inheritance
  @GET
  @Produces(MediaType.APPLICATION_XML)
  public Response getAsXml(@PathParam(AppConstant.ID) final String id,
      @PathParam("metadata-name") final String metadataName, @QueryParam(AppConstant.EU) final String escidocUri,
      @QueryParam("eSciDocUserHandle") final String encodedHandle) {

    checkPreconditions(id, metadataName, escidocUri, sr);
    debug(id, metadataName, escidocUri);

    try {
      final de.escidoc.core.resources.om.context.Context resource = find(id, escidocUri, encodedHandle);
      final AdminDescriptor metadata = findMetadataByName(metadataName, resource);
      if (metadata.getContent() == null) {
        return Response.status(Status.NO_CONTENT).build();
      }
      final ResponseBuilder b = r.evaluatePreconditions(getLastModificationDate(resource), getEntityTag(metadata));
      if (b != null) {
        return b.build();
      }

      // @formatter:off
      return Response
          .ok(new DOMSource(metadata.getContent()))
          .lastModified(getLastModificationDate(resource))
          .tag(getEntityTag(metadata))
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
      LOG.error("Can not fetch metadata with the name, " + metadataName + ", from " + CONTEXT + " id + ", "reason: "
          + e.getMessage());
      throw new WebApplicationException(e, Status.INTERNAL_SERVER_ERROR);
    }
  }

  @PUT
  @Consumes(MediaType.APPLICATION_XML)
  public Response update(@PathParam(AppConstant.ID) final String id,
      @PathParam("metadata-name") final String metadataName, @QueryParam(AppConstant.EU) final String escidocUri,
      final DOMSource s, @QueryParam("eSciDocUserHandle") final String encodedHandle) {

    checkPreconditions(id, metadataName, escidocUri, sr);
    debugPut(id, metadataName, escidocUri);

    try {

      final de.escidoc.core.resources.om.context.Context resource = find(id, escidocUri, encodedHandle);
      final AdminDescriptor metadata = findMetadataByName(metadataName, resource);
      if (metadata.getContent() == null) {
        return Response.status(Status.NO_CONTENT).build();
      }

      metadata.setContent((Element) s.getNode().getFirstChild());
      final GenericResource updated = repo.update(resource);
      Preconditions.checkNotNull(updated, "updated is null: %s", updated);
      // @formatter:off
      return Response
          .ok()
          .build();
       // @formatter:on
    } catch (final AuthenticationException e) {
      return response401();
    } catch (final AuthorizationException e) {
      return response401();
    } catch (final InternalClientException e) {
      return response401();
    } catch (final EscidocException e) {
      LOG.error("Can not update metadata with the name, " + metadataName + ", from " + CONTEXT + ", " + id
          + ", reason: " + e.getMessage());
      throw new WebApplicationException(e, Status.INTERNAL_SERVER_ERROR);
    } catch (final TransportException e) {
      LOG.error("Can not update metadata with the name, " + metadataName + ", from organizational unit, " + id
          + ", reason: " + e.getMessage());
      throw new WebApplicationException(e, Status.INTERNAL_SERVER_ERROR);
    }
  }

  @GET
  @Produces(MediaType.TEXT_HTML)
  public Response getAsHtml(@PathParam(AppConstant.ID) final String id,
      @PathParam("metadata-name") final String metadataName, @QueryParam(AppConstant.EU) final String escidocUri,
      @QueryParam("eSciDocUserHandle") final String encodedHandle) {

    checkPreconditions(id, metadataName, escidocUri, sr);
    final String msg = "HTTP GET request for with the id: " + id + ", metadata name: " + metadataName
        + ", server uri: " + escidocUri;
    LOG.debug(msg);

    try {
      final de.escidoc.core.resources.om.context.Context resource = find(id, escidocUri, encodedHandle);
      final AdminDescriptor metadata = findMetadataByName(metadataName, resource);
      if (metadata.getContent() == null) {
        return Response.status(Status.NO_CONTENT).build();
      }

      final ResponseBuilder b = r.evaluatePreconditions(getLastModificationDate(resource), getEntityTag(metadata));
      if (b != null) {
        return b.build();
      }

      final StringWriter s = new StringWriter();
      Utils.transformXml(metadata, s);

      // @formatter:off
     return Response
         .ok(s.toString(),MediaType.TEXT_HTML)
//         .lastModified(getLastModificationDate(org))
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
      LOG.error("Can not fetch metadata with the name, " + metadataName + ", from organizational unit, " + id
          + ", reason: " + e.getMessage());
      throw new WebApplicationException(e, Status.INTERNAL_SERVER_ERROR);
    }
  }

  private static void debugPut(final String id, final String metadataName, final String escidocUri) {
    final StringBuilder builder = new StringBuilder();
    builder.append("HTTP PUT request for ");
    builder.append("organizational unit");
    builder.append(" with the id: ");
    builder.append(id);
    builder.append(", metadata name: ");
    builder.append(metadataName);
    builder.append(", server uri: ");
    builder.append(escidocUri);
    LOG.debug(builder.toString());
  }

  /**
   * Runtime Exceptions:
   * 
   * @throws AuthenticationException
   * @throws AuthorizationException
   */
  private de.escidoc.core.resources.om.context.Context find(final String id, final String escidocUri,
      final String encodedHandle) throws AuthenticationException, AuthorizationException, InternalClientException {
    try {
      final String decodedHandle = getHandleIfAny(sr, escidocUri, encodedHandle);
      final de.escidoc.core.resources.om.context.Context resource = repo.find(id, new URI(escidocUri), decodedHandle);
      if (resource == null) {
        throw new NotFoundException("Context," + id + ", not found");
      }
      return resource;
    } catch (final AuthenticationException e) {
      throw new AuthenticationException(e.getMessage(), e);
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

  private static AdminDescriptor findMetadataByName(final String metadataName,
      final de.escidoc.core.resources.om.context.Context resource) {
    final AdminDescriptors list = resource.getAdminDescriptors();
    if (list == null || list.isEmpty()) {
      throw new NotFoundException("Metadata, " + metadataName + ", is not found");
    }

    final AdminDescriptor metadata = list.get(metadataName);
    if (metadata == null) {
      throw new NotFoundException("Metadata, " + metadataName + ", is not found");
    }
    return metadata;
  }

  private static void debug(final String id, final String metadataName, final String escidocUri) {
    final StringBuilder builder = new StringBuilder();
    builder.append("GET request for ");
    builder.append(CONTEXT);
    builder.append(" with the id: ");
    builder.append(id);
    builder.append(", metadata name: ");
    builder.append(metadataName);
    builder.append(", server uri: ");
    builder.append(escidocUri);
    LOG.debug(builder.toString());
  }
}