/**
 * CDDL HEADER START
 *
 * The contents of this file are subject to the terms of the
 * Common Development and Distribution License, Version 1.0 only
 * (the "License"). You may not use this file except in compliance
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
 * All rights reserved. Use is subject to license terms.
 */
package org.escidoc.core.service.metadata.resources;

import com.google.common.base.Preconditions;

import org.escidoc.core.service.metadata.AppConstant;
import org.escidoc.core.service.metadata.AuthentificationUtils;
import org.escidoc.core.service.metadata.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.CookieParam;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import de.escidoc.core.client.ItemHandlerClient;
import de.escidoc.core.client.StagingHandlerClient;
import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;
import de.escidoc.core.client.exceptions.application.notfound.ItemNotFoundException;
import de.escidoc.core.client.exceptions.application.security.AuthorizationException;
import de.escidoc.core.resources.om.item.Item;
import de.escidoc.core.resources.om.item.StorageType;
import de.escidoc.core.resources.om.item.component.Component;

@Path("items/{item-id}/files/{component-id}/blob")
public class BlobResource {

    private final static Logger LOG = LoggerFactory.getLogger(BlobResource.class);

    @Context
    private HttpServletRequest servletRequest;

    @PUT
    @Consumes(MediaType.WILDCARD)
    public Response update(
//@formatter:off
        @PathParam("item-id") final String itemId, 
        @PathParam("component-id") final String componentId, 
        @QueryParam(AppConstant.EU) final String escidocUri,
        @CookieParam("escidocCookie") final String escidocCookie,
        final InputStream is) {
        //@formatter:on
        checkPreconditions(itemId, componentId, servletRequest);
        checkQueryParameter(escidocUri);

        try {
            // TODO move clients to an extra class
            final ItemHandlerClient itemClient = new ItemHandlerClient(new URL(escidocUri));
            final String token = AuthentificationUtils.getHandleIfAny(servletRequest, escidocUri, escidocCookie);
            itemClient.setHandle(token);

            final Item item = itemClient.retrieve(itemId);
            final Component component = item.getComponents().get(componentId);
            if (component == null) {
                return Response
                    .status(Status.NOT_FOUND).entity("Can not find a component with the id " + componentId).build();
            }

            final StagingHandlerClient stagingClient = new StagingHandlerClient(new URL(escidocUri));
            stagingClient.setHandle(token);

            final URL upload = stagingClient.upload(is);
            LOG.debug("Succesfully upload file to " + upload.toString());

            component.getContent().setXLinkHref(upload.toString());
            component.getContent().setStorageType(StorageType.INTERNAL_MANAGED);

            // TODO last modification date of component is null, we should get it from the item.
            // why we need the last modification date from the item?
            component.setLastModificationDate(item.getLastModificationDate());
            final Component updated = itemClient.updateComponent(itemId, component);
            LOG.info("Succesfully update component with the id: " + updated.getObjid());
            // TODO 200 or 204?
            return Response.noContent().build();
        }
        catch (final EscidocException e) {
            if (e instanceof AuthorizationException) {
                return Utils.response401();
            }
            else if (e instanceof ItemNotFoundException) {
                return Response.status(Status.NOT_FOUND).entity("Can not find an item with the id " + itemId).build();
            }
            return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
        catch (final InternalClientException e) {
            return Response.serverError().entity("Something strange happens in our server: " + e.getMessage()).build();
        }
        catch (final TransportException e) {
            return Response.serverError().entity("eSciDoc Server is unreachable, details: " + e.getMessage()).build();
        }
        catch (final MalformedURLException e) {
            return Response
                .status(Status.BAD_REQUEST).entity(escidocUri + " is not a valid URL, details: " + e.getMessage())
                .type(MediaType.TEXT_PLAIN_TYPE).build();
        }
    }

    private static final void checkPreconditions(
        final String itemId, final String componentId, final HttpServletRequest request) {
        Preconditions.checkNotNull(itemId, "itemId is null: %s", itemId);
        Preconditions.checkNotNull(componentId, "componentId is null: %s", componentId);
        Preconditions.checkNotNull(request, "request is null: %s", request);
    }

    private static final void checkQueryParameter(final String escidocUri) {
        if (escidocUri == null || escidocUri.isEmpty()) {
            throw new WebApplicationException(Status.BAD_REQUEST);
        }
    }
}