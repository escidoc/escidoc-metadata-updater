package org.escidoc.core.service.metadata.internal;

import com.sun.jersey.api.client.ClientResponse;

import static org.junit.Assert.assertEquals;

import org.escidoc.core.service.metadata.ItemMetadataUpdateServiceSpec;
import org.junit.Test;

import javax.ws.rs.core.MediaType;

public class FunctionalTest extends Base implements ItemMetadataUpdateServiceSpec {

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
    final ClientResponse r = resource.path(
        "items/" + ITEM_ID + "/metadata/" + EXISTING_METADATA_NAME).accept(MediaType.TEXT_PLAIN).get(
        ClientResponse.class);
    assertEquals("response is not equals", 200, r.getStatus());
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

}
