package org.escidoc.core.service.metadata.internal;

import com.sun.jersey.api.client.ClientResponse;

import static org.junit.Assert.assertEquals;

import org.escidoc.core.service.metadata.ItemMetadataUpdateServiceSpec;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.MediaType;

public class FunctionalTest extends Base implements ItemMetadataUpdateServiceSpec {

  private final static Logger LOG = LoggerFactory.getLogger(FunctionalTest.class);

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

  @Override
  public void shouldReturn303WhenNoValidCookie() throws Exception {
    throw new UnsupportedOperationException("not-yet-implemented.");
  }

  @Override
  public void shouldReturn200WhenTryingToFetchUnreleasedItemGivenAValidCookie() throws Exception {
    throw new UnsupportedOperationException("not-yet-implemented.");
  }

}
