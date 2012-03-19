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
package org.escidoc.core.service.metadata.internal;

import com.google.inject.servlet.GuiceFilter;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource.Builder;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;

import static org.junit.Assert.assertEquals;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.escidoc.core.service.metadata.AppConstant;
import org.escidoc.core.service.metadata.ItemMetadataUpdateServiceSpec;
import org.escidoc.core.service.metadata.servlet.InMemoryServletConfig;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.MediaType;
import javax.xml.transform.dom.DOMSource;

public class ItemInMemoryTest extends Base implements ItemMetadataUpdateServiceSpec {

    private final static Logger LOG = LoggerFactory.getLogger(ItemInMemoryTest.class);

    private Server server;

    private Client client;

    @Override
    @Before
    public void setup() throws Exception {
        server = new Server(8089);
        final ServletContextHandler sch = new ServletContextHandler(server, "/");
        sch.addEventListener(new InMemoryServletConfig());
        sch.addFilter(GuiceFilter.class, "/*", null);
        sch.addServlet(DefaultServlet.class, "/");
        server.start();
        client = Client.create();
        resource = client.resource("http://localhost:8089").path("rest");
    }

    @After
    public void stop() throws Exception {
        server.stop();
    }

    @Test
    @Override
    public void shouldReturn200forHelloWorld() throws Exception {
        // @formatter:off
        final ClientResponse response =
            resource.path("helloworld").accept(MediaType.TEXT_PLAIN).get(ClientResponse.class);

        // @formatter:on
        assertEquals("response is not equals", 200, response.getStatus());
        final String entity = response.getEntity(String.class);
        LOG.debug("Got: " + entity);
        assertEquals("Entity is not equals. ", "OK", entity);
    }

    @Test
    @Override
    public void shouldReturn200forHelloWorldXml() throws Exception {
        //@formatter:off
        final ClientResponse response =
            resource.path("helloworld").accept(MediaType.APPLICATION_XML).get(ClientResponse.class);
        //@formatter:on
        assertEquals("response is not equals", 200, response.getStatus());
        final String entity = response.getEntity(String.class);
        LOG.debug("Got: " + entity);
        assertEquals("Entity is not equals. ", "<OK/>", entity);
    }

    @Test
    @Override
    public void shouldReturn404ForNonExistingItem() {
        // @formatter:off
        final ClientResponse r =
            resource.path("items").path(NON_EXISTING_ITEM_ID).path("metadata").path(EXISTING_METADATA_NAME).queryParam(
                AppConstant.EU, SERVICE_URL).accept(MediaType.APPLICATION_XML).get(ClientResponse.class);
        // @formatter:on
        assertEquals("response is not equals", 404, r.getStatus());
    }

    @Test
    @Override
    public void shouldReturn404ForNonExistingMetadata() {
        // @formatter:off
        final ClientResponse r =
            resource.path("items").path(EXISTING_ITEM_ID).path("metadata").path(NON_EXISTING_METADATA_NAME).queryParam(
                AppConstant.EU, SERVICE_URL).accept(MediaType.APPLICATION_XML).get(ClientResponse.class);
        // @formatter:on

        assertEquals("response is not equals", 404, r.getStatus());
    }

    @Test
    @Override
    public void shouldReturn204ForMetadataWithNoContent() {
        // @formatter:off
        final ClientResponse r =
            resource.path("items").path(EXISTING_ITEM_ID).path("metadata").path(EMPTY_METADATA).queryParam(
                AppConstant.EU, SERVICE_URL).accept(MediaType.APPLICATION_XML).get(ClientResponse.class);
        // @formatter:on
        assertEquals("response is not equals", 204, r.getStatus());
    }

    @Test
    @Override
    public void shouldReturn400ForMissingServerParameter() {
        // @formatter:off
        final ClientResponse r =
            resource.path("items").path(EXISTING_ITEM_ID).path("metadata").path(EXISTING_METADATA_NAME).accept(
                MediaType.APPLICATION_XML).get(ClientResponse.class);
        // @formatter:on
        assertEquals("response is not equals", 400, r.getStatus());
    }

    @Test
    @Override
    public void shouldReturn200ForExistingItemAndMetadata() {
        // @formatter:off
        final ClientResponse r =
            resource.path("items").path(EXISTING_ITEM_ID).path("metadata").path(EXISTING_METADATA_NAME).queryParam(
                AppConstant.EU, SERVICE_URL).accept(MediaType.APPLICATION_XML).get(ClientResponse.class);
        // @formatter:on

        assertEquals("response is not equals", 200, r.getStatus());
    }

    @Test
    @Override
    public void shouldReturnXmlForMetadata() {
        // @formatter:off
        final ClientResponse r =
            resource.path("items").path(EXISTING_ITEM_ID).path("metadata").path(EXISTING_METADATA_NAME).queryParam(
                AppConstant.EU, SERVICE_URL).accept(MediaType.APPLICATION_XML).get(ClientResponse.class);
        // @formatter:on
        assertEquals("response is not equals", 200, r.getStatus());

        LOG.debug("Get metadata as XML : " + r.getEntity(String.class));
    }

    @Test
    @Override
    public void shouldUpdateEscidocMetadata() {
        // @formatter:off
        final Builder builder =
            resource.path("items").path(EXISTING_ITEM_ID).path("metadata").path(EXISTING_METADATA_NAME).queryParam(
                AppConstant.EU, SERVICE_URL).accept(MediaType.APPLICATION_XML);

        final DOMSource e = builder.get(ClientResponse.class).getEntity(DOMSource.class);

        final ClientResponse r = builder.put(ClientResponse.class, e);
        // @formatter:on

        assertEquals("response is not equals", 200, r.getStatus());
    }

    @Test
    @Override
    public void shouldNotUpdateMetadataIfInConfict() {
        throw new UnsupportedOperationException("not-yet-implemented.");
    }

    @Test
    @Override
    public void shouldReturn200WhenTryingToFetchUnreleasedItemGivenAValidToken() throws Exception {
        throw new UnsupportedOperationException("not-yet-implemented.");
    }

    @Test
    @Override
    public void shouldReturn200WhenTryingToUpdateMetadataGivenValidToken() throws Exception {
        throw new UnsupportedOperationException("not-yet-implemented.");
    }

    @Test
    @Override
    public void shouldReturn200WhenTryingToUpdateMetadataGivenValidHandleInUriParam() throws Exception {
        throw new UnsupportedOperationException("not-yet-implemented.");
    }

    @Test
    @Override
    public void shouldReturn401WhenNoValidCookie() throws Exception {

        // @formatter:off
        final ClientResponse r =
            resource.path("items").path(PROTECTED_ITEM_ID).path("metadata").path(EXISTING_METADATA_NAME).queryParam(
                AppConstant.EU, SERVICE_URL).accept(MediaType.APPLICATION_XML).get(ClientResponse.class);
        // @formatter:on

        LOG.debug("Error message: " + r.getEntity(String.class));
        assertEquals("response is not equals", 401, r.getStatus());
    }

    @Test
    @Override
    public void shouldReturn200WhenTryingToAccessProctedResourceGivenBasicAuth() throws Exception {
        client.addFilter(new HTTPBasicAuthFilter("sysadmin", "eSciDoc"));

        // @formatter:off
        final ClientResponse r =
            resource.path("items").path(PROTECTED_ITEM_ID).path("metadata").path(EXISTING_METADATA_NAME).queryParam(
                AppConstant.EU, SERVICE_URL).accept(MediaType.APPLICATION_XML).get(ClientResponse.class);
        // @formatter:on

        LOG.debug("Error message: " + r.getEntity(String.class));
        assertEquals("response is not equals", 200, r.getStatus());
    }
}