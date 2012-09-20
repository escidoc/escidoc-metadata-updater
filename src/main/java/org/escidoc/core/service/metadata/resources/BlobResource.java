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

import com.google.inject.Inject;

import org.escidoc.core.service.metadata.AppConstant;
import org.escidoc.core.service.metadata.AuthentificationUtils;
import org.escidoc.core.service.metadata.Utils;
import org.escidoc.core.service.metadata.repository.ItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import de.escidoc.core.client.ItemHandlerClient;
import de.escidoc.core.client.StagingHandlerClient;
import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;
import de.escidoc.core.client.exceptions.application.security.AuthorizationException;
import de.escidoc.core.resources.om.item.Item;
import de.escidoc.core.resources.om.item.StorageType;
import de.escidoc.core.resources.om.item.component.Component;

@Path("items/{item-id}/files/{component-id}/blob")
public class BlobResource {

    private final static Logger LOG = LoggerFactory.getLogger(BlobResource.class);

    @Context
    private HttpServletRequest servletRequest;

    @Context
    private Request request;

    @Inject
    private ItemRepository itemRepo;

    @PUT
    @Consumes(MediaType.WILDCARD)
    public Response update(
//@formatter:off
        @PathParam("item-id") final String itemId, 
        @PathParam("component-id") final String componentId, 
        @QueryParam(AppConstant.EU) final String escidocUri,
        final InputStream is) {
        //@formatter:on
        // TODO check for preconditions
        try {
            final StagingHandlerClient stagingClient = new StagingHandlerClient(new URL(escidocUri));
            stagingClient.setHandle(AuthentificationUtils.getHandleIfAny(servletRequest, escidocUri, ""));
            final URL upload = stagingClient.upload(is);
            LOG.debug("Succesfully upload file to " + upload.toString());

            final ItemHandlerClient itemClient = new ItemHandlerClient(new URL(escidocUri));
            itemClient.setHandle(AuthentificationUtils.getHandleIfAny(servletRequest, escidocUri, ""));

            // TODO create an issue in escidoc-ijc fix for 1.4.3
            // final Component component = c.retrieveComponent(itemId, componentId);

            final Item item = itemClient.retrieve(itemId);
            final Component component = item.getComponents().get(componentId);

            component.getContent().setXLinkHref(upload.toString());
            component.getContent().setStorageType(StorageType.INTERNAL_MANAGED);

            // TODO create an issue in escidoc-ijc fix for 1.4.3
            itemClient.updateComponent(itemId, component);
            final Item updatedItem = itemClient.update(item);
            // TODO 200 or 204?
            return Response.noContent().build();
        }
        catch (final EscidocException e) {
            if (e instanceof AuthorizationException) {
                return Utils.response401();
            }
            return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
        catch (final InternalClientException e) {
            return Response.serverError().entity("Something strange happens in our server: " + e.getMessage()).build();
        }
        catch (final TransportException e) {
            return Response.serverError().entity("Something strange happens in our server: " + e.getMessage()).build();
        }
        catch (final MalformedURLException e) {
            return Response
                .status(Status.BAD_REQUEST).entity(escidocUri + " is not a valid URL, details: " + e.getMessage())
                .type(MediaType.TEXT_PLAIN_TYPE).build();
        }
    }
}