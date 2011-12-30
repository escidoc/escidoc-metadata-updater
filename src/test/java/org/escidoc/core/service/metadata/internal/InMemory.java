package org.escidoc.core.service.metadata.internal;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource.Builder;

import static org.junit.Assert.assertEquals;

import org.escidoc.core.service.metadata.ItemMetadataUpdateServiceSpec;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.MediaType;
import javax.xml.transform.dom.DOMSource;

public class InMemory extends Base implements ItemMetadataUpdateServiceSpec {

  private final static Logger LOG = LoggerFactory.getLogger(InMemory.class);

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
  public void shouldReturn200forHelloWorldXml() throws Exception {
    //@formatter:off
    final ClientResponse response = resource
        .path("helloworld")
        .accept(MediaType.APPLICATION_XML)
        .get(ClientResponse.class);
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
    final ClientResponse r = resource
        .path("items")
        .path(NON_EXISTING_ITEM_ID)
        .path("metadata")
        .path(EXISTING_METADATA_NAME)
        .queryParam("eu", SERVICE_URL)
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
        .path(EXISTING_ITEM_ID)
        .path("metadata")
        .path(NON_EXISTING_METADATA_NAME)
        .queryParam("eu", SERVICE_URL)
        .accept(MediaType.APPLICATION_XML)
        .get(ClientResponse.class);
	   // @formatter:on

    assertEquals("response is not equals", 404, r.getStatus());
  }

  @Test
  @Override
  public void shouldReturn204ForMetadataWithNoContent() {
    // @formatter:off
    final ClientResponse r = resource
        .path("items")
        .path(EXISTING_ITEM_ID)
        .path("metadata")
        .path(EMPTY_METADATA)
        .queryParam("eu", SERVICE_URL)
        .accept(MediaType.APPLICATION_XML)
        .get(ClientResponse.class);
	   // @formatter:on
    assertEquals("response is not equals", 204, r.getStatus());
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
  public void shouldReturn200ForExistingItemAndMetadata() {
    // @formatter:off
    final ClientResponse r = resource
        .path("items")
        .path(EXISTING_ITEM_ID)
        .path("metadata")
        .path(EXISTING_METADATA_NAME)
        .queryParam("eu", SERVICE_URL)
        .accept(MediaType.APPLICATION_XML)
        .get(ClientResponse.class);
	   // @formatter:on

    assertEquals("response is not equals", 200, r.getStatus());
  }

  @Test
  @Override
  public void shouldReturnXmlForMetadata() {
    // @formatter:off
	    final ClientResponse r = resource
	        .path("items")
	        .path(EXISTING_ITEM_ID)
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
  public void shouldUpdateEscidocMetadata() {
    // @formatter:off
	   final Builder builder = resource
	        .path("items")
	        .path(EXISTING_ITEM_ID)
	        .path("metadata")
	        .path(EXISTING_METADATA_NAME)
          .queryParam("eu", SERVICE_URL)
          .accept(MediaType.APPLICATION_XML);
	   
    final DOMSource e = builder
        .get(ClientResponse.class)
        .getEntity(DOMSource.class);
    
    final ClientResponse r = builder.put(ClientResponse.class,e);
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
  public void shouldReturn403WhenNoValidCookie() throws Exception {
    // @formatter:off
	    final ClientResponse r = resource
	        .path("items")
	        .path(PROTECTED_ITEM_ID)
	        .path("metadata")
	        .path(EXISTING_METADATA_NAME)
          .queryParam("eu", SERVICE_URL)
          .accept(MediaType.APPLICATION_XML)
          .get(ClientResponse.class);
	   // @formatter:on

    assertEquals("response is not equals", 403, r.getStatus());
    LOG.debug("Error message: " + r.getEntity(String.class));
  }

  @Test
  @Override
  public void shouldReturn200WhenTryingToFetchUnreleasedItemGivenAValidCookie() throws Exception {
    throw new UnsupportedOperationException("not-yet-implemented.");
  }
}