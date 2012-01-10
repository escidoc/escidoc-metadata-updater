package org.escidoc.core.service.metadata.internal;

import com.google.inject.servlet.GuiceFilter;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.WebResource.Builder;
import com.sun.jersey.core.util.Base64;

import static org.junit.Assert.assertEquals;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.escidoc.core.service.metadata.ItemMetadataUpdateServiceSpec;
import org.escidoc.core.service.metadata.guice.AppServletConfig;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;

import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;
import javax.xml.transform.dom.DOMSource;

import de.escidoc.core.client.Authentication;

public class FunctionalTest extends Base implements ItemMetadataUpdateServiceSpec {

  private final static Logger LOG = LoggerFactory.getLogger(FunctionalTest.class);

  private WebResource resource;

  private Server server;

  @Before
  public void setup() throws Exception {
    server = new Server(8089);
    final ServletContextHandler sch = new ServletContextHandler(server, "/");
    sch.addEventListener(new AppServletConfig());
    sch.addFilter(GuiceFilter.class, "/*", null);
    sch.addServlet(DefaultServlet.class, "/");
    server.start();
    final Client client = Client.create();
    resource = client.resource("http://localhost:8089").path("rest");
  }

  @After
  public void stop() throws Exception {
    server.stop();
  }

  @Test
  @Override
  public void shouldReturn404ForNonExistingItem() {
    throw new UnsupportedOperationException("not-yet-implemented.");
  }

  @Test
  @Override
  public void shouldReturn404ForNonExistingMetadata() {
    throw new UnsupportedOperationException("not-yet-implemented.");
  }

  @Test
  @Override
  public void shouldUpdateEscidocMetadata() {
    throw new UnsupportedOperationException("not-yet-implemented.");
  }

  @Test
  @Override
  public void shouldNotUpdateMetadataIfInConfict() {
    throw new UnsupportedOperationException("not-yet-implemented.");
  }

  @Test
  @Override
  public void shouldReturn204ForMetadataWithNoContent() {
    throw new UnsupportedOperationException("not-yet-implemented.");
  }

  @Test
  @Override
  public void shouldReturn400ForMissingServerParameter() {
    throw new UnsupportedOperationException("not-yet-implemented.");
  }

  @Test
  @Override
  public void shouldReturn200forHelloWorld() throws Exception {
    throw new UnsupportedOperationException("not-yet-implemented.");
  }

  @Test
  @Override
  public void shouldReturnXmlForMetadata() {
    throw new UnsupportedOperationException("not-yet-implemented.");
  }

  @Test
  @Override
  public void shouldReturn200ForExistingItemAndMetadata() {
    // @formatter:off
    final ClientResponse r = resource
        .path("items")
        .path(ITEM_ID)
        .path("metadata")
        .path(EXISTING_METADATA_NAME)
        .queryParam("eu", SERVICE_URL)
        .accept(MediaType.APPLICATION_XML)
        .get(ClientResponse.class);
	   // @formatter:on
    assertEquals("response is not equals", 200, r.getStatus());

    LOG.debug("Get metadata as XML : " + r.getEntity(String.class));
  }

  @Test
  @Override
  public void shouldReturn200WhenTryingToFetchUnreleasedItemGivenAValidCookie() throws Exception {
    final String token = new Authentication(new URL(SERVICE_URL), SYSADMIN, SYSADMIN_PASSWORD).getHandle();
    // @formatter:off
    final ClientResponse r = resource
        .path("items")
        .path("escidoc:93")
        .path("metadata")
        .path(EXISTING_METADATA_NAME)
        .queryParam("eu", SERVICE_URL)
        .accept(MediaType.APPLICATION_XML)
        .cookie(new Cookie("escidocCookie",token)) 
        .get(ClientResponse.class);
   // @formatter:on

    LOG.debug("Entity: " + r.getEntity(String.class));
    assertEquals("response is not equals", 200, r.getStatus());
  }

  @Test
  @Override
  public void shouldReturn200WhenTryingToUpdateMetadataGivenValidCookie() throws Exception {

    final String token = new Authentication(new URL(SERVICE_URL), SYSADMIN, SYSADMIN_PASSWORD).getHandle();
    // @formatter:off
    final Builder builder = resource
        .path("items")
        .path("escidoc:93")
        .path("metadata")
        .path(EXISTING_METADATA_NAME)
        .queryParam("eu", SERVICE_URL)
        .accept(MediaType.APPLICATION_XML)
        .cookie(new Cookie("escidocCookie",token));
    
    final DOMSource e = builder
        .get(ClientResponse.class)
        .getEntity(DOMSource.class);
    
    final ClientResponse r = builder.put(ClientResponse.class,e);
   // @formatter:on

    LOG.debug("Entity: " + r.getEntity(String.class));
    assertEquals("response is not equals", 200, r.getStatus());
  }

  @Test
  @Override
  public void shouldReturn200WhenTryingToUpdateMetadataGivenValidHandleInUriParam() throws Exception {

    final String token = new Authentication(new URL(SERVICE_URL), SYSADMIN, SYSADMIN_PASSWORD).getHandle();
    // @formatter:off
    final Builder builder = resource
        .path("items")
        .path("escidoc:93")
        .path("metadata")
        .path(EXISTING_METADATA_NAME)
        .queryParam("eu", SERVICE_URL)
        .queryParam("eSciDocUserHandle", new String(Base64.encode(token)))
        .accept(MediaType.APPLICATION_XML);
    
    final DOMSource e = builder
        .get(ClientResponse.class)
        .getEntity(DOMSource.class);
    
    final ClientResponse r = builder.put(ClientResponse.class,e);
   // @formatter:on

    LOG.debug("Entity: " + r.getEntity(String.class));
    assertEquals("response is not equals", 200, r.getStatus());
  }

  @Test
  @Override
  public void shouldReturn200forHelloWorldXml() throws Exception {
    throw new UnsupportedOperationException("not-yet-implemented.");
  }

  @Test
  @Override
  public void shouldReturn401WhenNoValidCookie() throws Exception {
    // @formatter:off
    final Builder builder = resource
        .path("items")
        .path("escidoc:93")
        .path("metadata")
        .path(EXISTING_METADATA_NAME)
        .queryParam("eu", SERVICE_URL)
        .accept(MediaType.APPLICATION_XML);
    
    final DOMSource e = builder
        .get(ClientResponse.class)
        .getEntity(DOMSource.class);
    
    final ClientResponse r = builder.put(ClientResponse.class,e);
   // @formatter:on

    LOG.debug("Entity: " + r.getEntity(String.class));
    assertEquals("response is not equals", 401, r.getStatus());
  }

  @Test
  @Override
  public void shouldReturn200WhenTryingToAccessProctedResourceGivenBasicAuth() throws Exception {
    throw new UnsupportedOperationException("not-yet-implemented.");
  }
}