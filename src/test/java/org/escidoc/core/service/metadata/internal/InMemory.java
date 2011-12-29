package org.escidoc.core.service.metadata.internal;

import com.sun.jersey.api.client.ClientResponse;

import static org.junit.Assert.assertEquals;

import org.escidoc.core.service.metadata.ItemMetadataUpdateServiceSpec;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.MediaType;

public class InMemory extends Base implements ItemMetadataUpdateServiceSpec {

  private final static Logger LOG = LoggerFactory.getLogger(InMemory.class);

  @Test
  @Override
  public void shouldReturn200forHelloWorld() throws Exception {
    final ClientResponse response = resource.path("helloworld").accept("text/plain").get(
        ClientResponse.class);
    assertEquals("response is not equals", 200, response.getStatus());
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
  public void shouldReturn404ForNonExistingItem() {
    final ClientResponse r = resource.path("items").path(NON_EXISTING_ITEM_ID).path("metadata").path(
        EXISTING_METADATA_NAME).queryParam("eu", SERVICE_URL).accept(MediaType.TEXT_PLAIN).get(
        ClientResponse.class);
    assertEquals("response is not equals", 404, r.getStatus());
  }

  @Test
  @Override
  public void shouldReturn404ForNonExistingMetadata() {

    final ClientResponse r = resource.path("items").path(EXISTING_ITEM_ID).path("metadata").path(
        NON_EXISTING_METADATA_NAME).queryParam("eu", SERVICE_URL).get(ClientResponse.class);
    assertEquals("response is not equals", 404, r.getStatus());
  }

  @Test
  @Override
  public void shouldReturn204ForMetadataWithNoContent() {
    final ClientResponse r = resource.path("items").path(EXISTING_ITEM_ID).path("metadata").path(
        EMPTY_METADATA).queryParam("eu", SERVICE_URL).get(ClientResponse.class);
    assertEquals("response is not equals", 204, r.getStatus());
  }

  @Test
  @Override
  public void shouldReturn400ForMissingServerParameter() {
    final ClientResponse r = resource.path("items").path(EXISTING_ITEM_ID).path("metadata").path(
        EXISTING_METADATA_NAME).accept(MediaType.TEXT_PLAIN).get(ClientResponse.class);
    final String e = r.getEntity(String.class);
    LOG.debug(e);

    assertEquals("response is not equals", 400, r.getStatus());
  }

  @Test
  @Override
  public void shouldReturn200ForExistingItemAndMetadata() {
    final ClientResponse r = resource.path("items").path(EXISTING_ITEM_ID).path("metadata").path(
        EXISTING_METADATA_NAME).queryParam("eu", SERVICE_URL).get(ClientResponse.class);

    assertEquals("response is not equals", 200, r.getStatus());
  }

  @Test
  @Override
  public void shouldReturnXmlForMetadata() {
    final ClientResponse r = resource.path("items").path(EXISTING_ITEM_ID).path("metadata").path(
        EXISTING_METADATA_NAME).queryParam("eu", SERVICE_URL).accept(MediaType.APPLICATION_XML).get(
        ClientResponse.class);
    throw new UnsupportedOperationException("not-yet-implemented.");
  }
}