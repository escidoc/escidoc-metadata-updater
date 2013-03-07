/**
 * CDDL HEADER START
 *
 * The contents of this file are subject to the terms of the
 * Common Development and Distribution License, Version 1.0 only
 * (the "License").  You may not use this file except in compliance
 * with the License.
 *
 * You can obtain a copy of the license at license/ESCIDOC.LICENSE
 * or https://www.escidoc.org/license/ESCIDOC.LICENSE .
 * See the License for the specific language governing permissions
 * and limitations under the License.
 *
 * When distributing Covered Code, include this CDDL HEADER in each
 * file and include the License file at license/ESCIDOC.LICENSE.
 * If applicable, add the following below this CDDL HEADER, with the
 * fields enclosed by brackets "[]" replaced with your own identifying
 * information: Portions Copyright [yyyy] [name of copyright owner]
 *
 * CDDL HEADER END
 *
 *
 *
 * Copyright 2012 Fachinformationszentrum Karlsruhe Gesellschaft
 * fuer wissenschaftlich-technische Information mbH and Max-Planck-
 * Gesellschaft zur Foerderung der Wissenschaft e.V.
 * All rights reserved.  Use is subject to license terms.
 */
package org.escidoc.core.service.metadata.resources;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;

import com.sun.jersey.api.NotFoundException;

import static org.escidoc.core.service.metadata.Utils.checkPreconditions;
import static org.escidoc.core.service.metadata.Utils.getEntityTag;
import static org.escidoc.core.service.metadata.Utils.getLastModificationDate;
import static org.escidoc.core.service.metadata.Utils.response401;

import org.escidoc.core.service.metadata.AppConstant;
import org.escidoc.core.service.metadata.AuthentificationUtils;
import org.escidoc.core.service.metadata.Utils;
import org.escidoc.core.service.metadata.repository.ItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.servlet.http.HttpServletRequest;
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
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;
import javax.xml.transform.dom.DOMSource;

import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;
import de.escidoc.core.client.exceptions.application.notfound.ItemNotFoundException;
import de.escidoc.core.client.exceptions.application.security.AuthenticationException;
import de.escidoc.core.client.exceptions.application.security.AuthorizationException;
import de.escidoc.core.resources.common.MetadataRecord;
import de.escidoc.core.resources.common.MetadataRecords;
import de.escidoc.core.resources.om.item.Item;

@Path("items/{id}/metadata/{metadata-name}")
public class ItemMetadataResource {
    private static final Logger LOG = LoggerFactory.getLogger(ItemMetadataResource.class);

    @Context
    private HttpServletRequest servletRequest;

    @Context
    private Request request;

    @Inject
    private ItemRepository repository;

    @GET
    @Produces(MediaType.APPLICATION_XML)
    public Response getAsXml(
        // @formatter:off
        @PathParam(AppConstant.ID) final String id, 
        @PathParam("metadata-name") final String metadataName,
        @QueryParam(AppConstant.EU) final String escidocUri, 
        @CookieParam("escidocCookie") final String escidocCookie) {
	   // @formatter:on
        checkPreconditions(id, metadataName, escidocUri, servletRequest);
        debug(id, metadataName, escidocUri);

        try {
            final Item resource = find(id, escidocUri, escidocCookie);
            final MetadataRecord mr = findMetadataByName(metadataName, resource);
            if (mr.getContent() == null) {
                return Response.status(Status.NO_CONTENT).build();
            }

            final ResponseBuilder builder =
                request.evaluatePreconditions(getLastModificationDate(resource), getEntityTag(mr));
            if (builder != null) {
                return builder.build();
            }

            // @formatter:off
            return Response
                .ok(new DOMSource(mr.getContent()))
                .lastModified(getLastModificationDate(resource))
                .tag(getEntityTag(mr))
                .build();
            // @formatter:on
        }
        catch (final AuthenticationException e) {
            LOG.debug("Auth. credentials is not valid while accessing protected source. ");
            return response401();
        }
        catch (final AuthorizationException e) {
            LOG.debug("Auth. credentials is not valid while accessing protected source. ");
            return response401();
        }
        catch (final InternalClientException e) {
            if (e.getCause() instanceof org.jibx.runtime.JiBXException) {
                // We assume here, the ijc can not unmarshall the HTML Login Form.
                LOG.debug("No auth token is provided or not valid while accessing protected source. ");
                return response401();
            }
            LOG.error("Can not fetch metadata with the name, " + metadataName + ", from item, " + id + ", reason: "
                + e.getMessage());
            throw new WebApplicationException(e, Status.INTERNAL_SERVER_ERROR);
        }
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    // @formatter:off
    public Response getAsHtml(
        @PathParam(AppConstant.ID) final String id, 
        @PathParam("metadata-name") final String metadataName,
        @QueryParam(AppConstant.EU) final String escidocUri,
        @CookieParam("escidocCookie") final String escidocCookie) {
   // @formatter:on

        checkPreconditions(id, metadataName, escidocUri, servletRequest);
        debug(id, metadataName, escidocUri);
        checkCookie(escidocCookie);

        try {
            final Item resource = find(id, escidocUri, escidocCookie);
            final MetadataRecord metadata = findMetadataByName(metadataName, resource);
            if (metadata.getContent() == null) {
                return Response.status(Status.NO_CONTENT).build();
            }

            final String result = Utils.transformToHtml(metadata);
            final ResponseBuilder b =
                request.evaluatePreconditions(getLastModificationDate(resource), getEntityTag(result));
            if (b != null) {
                return b.build();
            }

            // @formatter:off
            return Response
                .ok(result, MediaType.TEXT_HTML)
                .lastModified(getLastModificationDate(resource))
                .tag(getEntityTag(result)).build();
            //@formatter:on
        }
        catch (final AuthenticationException e) {
            return response401();
        }
        catch (final AuthorizationException e) {
            return response401();
        }
        catch (final InternalClientException e) {
            // We assume here, the ijc can not unmarshall the HTML Login Form.
            if (e.getCause() instanceof org.jibx.runtime.JiBXException) {
                return response401();
            }
            LOG.error("Can not fetch metadata with the name, " + metadataName + ", from " + AppConstant.ITEM + ", "
                + id + ", reason: " + e.getMessage());
            throw new WebApplicationException(e, Status.INTERNAL_SERVER_ERROR);
        }
    }

    private static void debug(final String id, final String metadataName, final String escidocUri) {
        final String msg =
            "HTTP GET request for item with the id: " + id + ", metadata name: " + metadataName + ", server uri: "
                + escidocUri;
        LOG.debug(msg);
    }

    private static void checkCookie(final String escidocCookie) {
        if (escidocCookie != null) {
            LOG.debug("found escidoc cookie: " + escidocCookie);
        }
        else {
            LOG.debug("no cookie");
        }
    }

    @PUT
    @Consumes("application/xml")
    public Response update(
        // @formatter:off
        @PathParam(AppConstant.ID) final String id,
        @PathParam("metadata-name") final String metadataName,
        @QueryParam(AppConstant.EU) final String escidocUri, 
        final DOMSource domSource,
        @CookieParam("escidocCookie") final String escidocCookie) {
	   // @formatter:on
        checkPreconditions(id, metadataName, escidocUri, servletRequest);

        try {
            final Item resource = find(id, escidocUri, escidocCookie);
            final MetadataRecord metadata = findMetadataByName(metadataName, resource);
            if (metadata.getContent() == null) {
                return Response.status(Status.NO_CONTENT).build();
            }

            metadata.setContent((Element) domSource.getNode().getFirstChild());
            final Item updated = update(resource, escidocUri, escidocCookie);
            Preconditions.checkNotNull(updated, "updated is null: %s", updated);
            // @formatter:off
            return Response.ok().build();
            // @formatter:on
        }
        catch (final AuthorizationException e) {
            return response401();
        }
        catch (final AuthenticationException e) {
            return response401();
        }
        catch (final InternalClientException e) {
            return response401();
        }
    }

    private Item update(final Item resource, final String escidocUri, final String escidocCookie)
        throws AuthenticationException, AuthorizationException, InternalClientException {
        try {
            final Item updated =
                repository.update(resource, new URI(escidocUri),
                    AuthentificationUtils.getHandleIfAny(servletRequest, escidocUri, escidocCookie));
            if (updated == null) {
                throw new NotFoundException("Item," + resource.getObjid() + ", not found");
            }
            return updated;
        }
        catch (final AuthenticationException e) {
            throw new AuthenticationException(e.getMessage(), e);
        }
        catch (final AuthorizationException e) {
            LOG.debug("Auth. credentials is not valid while accessing protected source. ");
            throw new AuthorizationException(e.getMessage(), e);
        }
        catch (final ItemNotFoundException e) {
            throw new NotFoundException("Item," + resource.getObjid() + ", not found");
        }
        catch (final EscidocException e) {
            throw new WebApplicationException(e, Status.INTERNAL_SERVER_ERROR);
        }
        catch (final TransportException e) {
            throw new WebApplicationException(e, Status.INTERNAL_SERVER_ERROR);
        }
        catch (final MalformedURLException e) {
            throw new WebApplicationException(e, Status.INTERNAL_SERVER_ERROR);
        }
        catch (final URISyntaxException e) {
            throw new WebApplicationException(e, Status.INTERNAL_SERVER_ERROR);
        }

    }

    /**
     * Runtime Exceptions
     * 
     * @throws {@link AuthenticationException} is a RuntimeException
     * @throws {@link AuthorizationException} is a RuntimeException
     */
    private Item find(final String id, final String escidocUri, final String escidocCookie)
        throws AuthenticationException, AuthorizationException, InternalClientException {
        try {
            final Item resource =
                repository.find(id, new URI(escidocUri),
                    AuthentificationUtils.getHandleIfAny(servletRequest, escidocUri, escidocCookie));
            if (resource == null) {
                throw new NotFoundException("Item," + id + ", not found");
            }
            return resource;
        }
        catch (final AuthenticationException e) {
            throw new AuthenticationException(e.getMessage(), e);
        }
        catch (final AuthorizationException e) {
            LOG.debug("Auth. credentials is not valid while accessing protected source. ");
            throw new AuthorizationException(e.getMessage(), e);
        }
        catch (final ItemNotFoundException e) {
            throw new NotFoundException("Item," + id + ", not found");
        }
        catch (final EscidocException e) {
            throw new WebApplicationException(e, Status.INTERNAL_SERVER_ERROR);
        }
        catch (final TransportException e) {
            throw new WebApplicationException(e, Status.INTERNAL_SERVER_ERROR);
        }
        catch (final MalformedURLException e) {
            throw new WebApplicationException(e, Status.INTERNAL_SERVER_ERROR);
        }
        catch (final URISyntaxException e) {
            throw new WebApplicationException(e, Status.INTERNAL_SERVER_ERROR);
        }
    }

    private static MetadataRecord findMetadataByName(final String metadataName, final Item item) {
        final MetadataRecords mrList = item.getMetadataRecords();
        if (mrList == null || mrList.isEmpty()) {
            throw new NotFoundException("Metadata, " + metadataName + ", is not found");
        }

        final MetadataRecord metadata = mrList.get(metadataName);
        if (metadata == null) {
            throw new NotFoundException("Metadata, " + metadataName + ", is not found");
        }
        return metadata;
    }
}