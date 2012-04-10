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
package org.escidoc.core.service.metadata.internal;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource.Builder;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;

import static org.junit.Assert.assertEquals;

import org.escidoc.core.service.metadata.AppConstant;
import org.escidoc.core.service.metadata.ContainerMetadataUpadateServiceSpec;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.MediaType;
import javax.xml.transform.dom.DOMSource;

public class ContainerMetadataFunctionalTest extends Base implements ContainerMetadataUpadateServiceSpec {

    private final static Logger LOG = LoggerFactory.getLogger(ContainerMetadataFunctionalTest.class);

    // TODO move to a property file, to make it configurable
    private static final String PROTECTED_CONTAINER_ID = "escidoc:19";

    private static final String RELEASED_CONTAINER_ID = "escidoc:73";

    @Test
    @Override
    public void shouldReturn200ForExistingResourceAndMetadata() throws Exception {
        // @formatter:off
        final Builder builder = resource
            .path(AppConstant.CONTAINERS)
            .path(RELEASED_CONTAINER_ID)
            .path(AppConstant.METADATA)
            .path(AppConstant.ESCIDOC_METADATA)
            .queryParam(AppConstant.EU, SERVICE_URL)
            .accept(MediaType.APPLICATION_XML);
        assertEquals("response is not equals", 200, builder
            .get(ClientResponse.class)
            .getStatus());
        final String responseMessage = builder.get(ClientResponse.class).getEntity(String.class);
        LOG.debug("Entity as String: " + responseMessage);
    }

 
    /*
     * (non-Javadoc)
     * 
     * @see org.escidoc.core.service.metadata.ContainerMetadataUpadateServiceSpec#shouldReturn401WhenNotAuthorized()
     */
    @Test
    @Override
    public void shouldReturn401WhenNotAuthorized() throws Exception {
        // @formatter:off
        final Builder builder = resource
            .path(AppConstant.CONTAINERS)
            .path(PROTECTED_CONTAINER_ID)
            .path(AppConstant.METADATA)
            .path(AppConstant.ESCIDOC_METADATA)
            .queryParam(AppConstant.EU, SERVICE_URL)
            .accept(MediaType.APPLICATION_XML);
        assertEquals("response is not equals", 401, builder
            .get(ClientResponse.class)
            .getStatus());
        
        // @formatter:on
        final String responseMessage = builder.get(ClientResponse.class).getEntity(String.class);
        LOG.debug("Entity as String: " + responseMessage);
    }

    @Test
    @Override
    public void shouldReturn200WhenTryingToUpdateMetadataGivenCredentials() throws Exception {
        client.addFilter(new HTTPBasicAuthFilter("sysadmin", "eSciDoc"));

        // @formatter:off
        final Builder builder = resource
            .path(AppConstant.CONTAINERS)
            .path(RELEASED_CONTAINER_ID)
            .path(AppConstant.METADATA)
            .path(AppConstant.ESCIDOC_METADATA)
            .queryParam(AppConstant.EU, SERVICE_URL)
            .accept(MediaType.APPLICATION_XML);
        
        final DOMSource e = builder.get(ClientResponse.class).getEntity(DOMSource.class);
        final ClientResponse r = builder.put(ClientResponse.class, e);
        // @formatter:on

        LOG.debug("Entity: " + r.getEntity(String.class));
        assertEquals("response is not equals", 200, r.getStatus());
    }

    @Test
    @Override
    public void shouldReturn401henTryingToUpdateMetadataButNotAuthorized() throws Exception {
        // @formatter:off
        final Builder builder = resource
            .path(AppConstant.CONTAINERS)
            .path(RELEASED_CONTAINER_ID)
            .path(AppConstant.METADATA)
            .path(AppConstant.ESCIDOC_METADATA)
            .queryParam(AppConstant.EU, SERVICE_URL)
            .accept(MediaType.APPLICATION_XML);
        
        final DOMSource e = builder.get(ClientResponse.class).getEntity(DOMSource.class);
        final ClientResponse r = builder.put(ClientResponse.class, e);
        // @formatter:on

        LOG.debug("Entity: " + r.getEntity(String.class));
        assertEquals("response is not equals", 401, r.getStatus());
    }

    @Test
    @Override
    public void shouldReturn200ForExistingResourceAndMetadataInHtml() throws Exception {
        // @formatter:off
        final Builder builder = resource
            .path(AppConstant.CONTAINERS)
            .path(RELEASED_CONTAINER_ID)
            .path(AppConstant.METADATA)
            .path(AppConstant.ESCIDOC_METADATA)
            .queryParam(AppConstant.EU, SERVICE_URL)
            .accept(MediaType.TEXT_HTML);
        assertEquals("response is not equals", 200, builder
            .get(ClientResponse.class)
            .getStatus());
        final String responseMessage = builder.get(ClientResponse.class).getEntity(String.class);
        LOG.debug("Entity as String: " + responseMessage);
    }
}