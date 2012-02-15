package org.escidoc.core.service.metadata.internal;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource.Builder;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.core.util.Base64;

import static org.junit.Assert.assertEquals;

import org.escidoc.core.service.metadata.AppConstant;
import org.escidoc.core.service.metadata.ItemMetadataUpdateServiceSpec;
import org.junit.After;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;

import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.MediaType;
import javax.xml.transform.dom.DOMSource;

import de.escidoc.core.client.Authentication;

public class ItemMetadataFunctionalTest extends Base implements ItemMetadataUpdateServiceSpec {

  private final static Logger LOG = LoggerFactory.getLogger(ItemMetadataFunctionalTest.class);

  @After
  public void stop() throws Exception {
    server.stop();
  }

  @Test
  public void shouldRechallangeIfUsernameAndPasswordIsEmpty() {
    client.addFilter(new HTTPBasicAuthFilter("", ""));

    // @formatter:off
    final Builder builder = resource
        .path("items")
        .path("escidoc:93")
        .path("metadata")
        .path(EXISTING_METADATA_NAME)
        .queryParam(AppConstant.EU, SERVICE_URL)
        .accept(MediaType.APPLICATION_XML);
    
    final ClientResponse r = builder.get(ClientResponse.class);
    assertEquals("response is not equals", 401, r.getStatus());
  }
  @Test
  @Override
  public void shouldReturn404ForNonExistingItem() {
    // @formatter:off
    final ClientResponse r = resource
        .path("items")
        .path(NON_EXISTING_ITEM_ID)
        .path("metadata")
        .path(EXISTING_METADATA_NAME)
        .queryParam(AppConstant.EU, SERVICE_URL)
        .accept(MediaType.APPLICATION_XML)
        .get(ClientResponse.class);
     // @formatter:on
    assertEquals("response is not equals", 404, r.getStatus());
  }

  @Test
  @Override
  public void shouldReturn404ForNonExistingMetadata() {
    // @formatter:off
    final ClientResponse r = resource
        .path("items")
        .path(ITEM_ID)
        .path("metadata")
        .path(NON_EXISTING_METADATA_NAME)
        .queryParam(AppConstant.EU, SERVICE_URL)
        .accept(MediaType.APPLICATION_XML)
        .get(ClientResponse.class);
     // @formatter:on

    assertEquals("response is not equals", 404, r.getStatus());
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
    // @formatter:off
    final ClientResponse r = resource
        .path("items")
        .path(EXISTING_ITEM_ID)
        .path("metadata")
        .path(EXISTING_METADATA_NAME)
        .accept(MediaType.APPLICATION_XML)
        .get(ClientResponse.class);
     // @formatter:on
    assertEquals("response is not equals", 400, r.getStatus());
  }

  @Test
  @Override
  public void shouldReturn200forHelloWorld() throws Exception {
    // @formatter:off
    final ClientResponse response = resource
        .path("helloworld")
        .accept(MediaType.TEXT_PLAIN)
        .get(ClientResponse.class);
    
     // @formatter:on
    assertEquals("response is not equals", 200, response.getStatus());
    final String entity = response.getEntity(String.class);
    LOG.debug("Got: " + entity);
    assertEquals("Entity is not equals. ", "OK", entity);
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
        .queryParam(AppConstant.EU, SERVICE_URL)
        .accept(MediaType.APPLICATION_XML);
    
    final ClientResponse r = builder.get(ClientResponse.class);
   // @formatter:on
    assertEquals("response is not equals", 401, r.getStatus());
  }

  @Test
  @Override
  public void shouldReturnXmlForMetadata() {
    // @formatter:off
    final ClientResponse r = resource
        .path("items")
        .path(ITEM_ID)
        .path("metadata")
        .path(EXISTING_METADATA_NAME)
        .queryParam(AppConstant.EU, SERVICE_URL)
        .accept(MediaType.APPLICATION_XML)
        .get(ClientResponse.class);
   // @formatter:on
    assertEquals("response is not equals", 200, r.getStatus());

    LOG.debug("Get metadata as XML : " + r.getEntity(String.class));
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
        .queryParam(AppConstant.EU, SERVICE_URL)
        .accept(MediaType.APPLICATION_XML)
        .get(ClientResponse.class);
	   // @formatter:on
    assertEquals("response is not equals", 200, r.getStatus());

    LOG.debug("Get metadata as XML : " + r.getEntity(String.class));
  }

  @Test
  @Override
  public void shouldReturn200WhenTryingToFetchUnreleasedItemGivenAValidToken() throws Exception {
    final String token = new Authentication(new URL(SERVICE_URL), SYSADMIN, SYSADMIN_PASSWORD).getHandle();
    // @formatter:off
    final ClientResponse r = resource
        .path("items")
        .path("escidoc:93")
        .path("metadata")
        .path(EXISTING_METADATA_NAME)
        .queryParam(AppConstant.EU, SERVICE_URL)
        .accept(MediaType.APPLICATION_XML)
        .cookie(new Cookie("escidocCookie",token)) 
        .get(ClientResponse.class);
   // @formatter:on

    LOG.debug("Entity: " + r.getEntity(String.class));
    assertEquals("response is not equals", 200, r.getStatus());
  }

  @Test
  @Override
  public void shouldReturn200WhenTryingToUpdateMetadataGivenValidToken() throws Exception {

    final String token = new Authentication(new URL(SERVICE_URL), SYSADMIN, SYSADMIN_PASSWORD).getHandle();
    // @formatter:off
    final Builder builder = resource
        .path("items")
        .path("escidoc:93")
        .path("metadata")
        .path(EXISTING_METADATA_NAME)
        .queryParam(AppConstant.EU, SERVICE_URL)
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
  public void shouldReturn200forHelloWorldXml() throws Exception {
    throw new UnsupportedOperationException("not-yet-implemented.");
  }

  @Test
  @Override
  public void shouldReturn200WhenTryingToAccessProctedResourceGivenBasicAuth() throws Exception {
    client.addFilter(new HTTPBasicAuthFilter("sysadmin", "eSciDoc"));

    // @formatter:off
    final Builder builder = resource
        .path("items")
        .path("escidoc:93")
        .path("metadata")
        .path(EXISTING_METADATA_NAME)
        .queryParam(AppConstant.EU, SERVICE_URL)
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
  public void shouldReturnNotModifiedIfTheLastModificationDateAndEtagSentAreEquals() throws Exception {
    // @formatter:off
    final EntityTag et = resource
        .path("items")
        .path(ITEM_ID)
        .path("metadata")
        .path(EXISTING_METADATA_NAME)
        .queryParam(AppConstant.EU, SERVICE_URL)
        .accept(MediaType.APPLICATION_XML)
        .get(ClientResponse.class).getEntityTag();
    
    final ClientResponse r = resource
        .path("items")
        .path(ITEM_ID)
        .path("metadata")
        .path(EXISTING_METADATA_NAME)
        .queryParam(AppConstant.EU, SERVICE_URL)
        .accept(MediaType.APPLICATION_XML)
        .header("If-None-Match", et)
        .get(ClientResponse.class);
	   // @formatter:on
    assertEquals("response is not equals", 304, r.getStatus());
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
        .queryParam(AppConstant.EU, SERVICE_URL)
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

}