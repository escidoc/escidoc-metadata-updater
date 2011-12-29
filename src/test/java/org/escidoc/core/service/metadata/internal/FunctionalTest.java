package org.escidoc.core.service.metadata.internal;

import org.escidoc.core.service.metadata.ItemMetadataUpdateServiceSpec;

public class FunctionalTest implements ItemMetadataUpdateServiceSpec {

  @Override
  public void shouldReturn404ForNonExistingItem() {
    throw new UnsupportedOperationException("not-yet-implemented.");
  }

  @Override
  public void shouldReturn404ForNonExistingMetadata() {
    throw new UnsupportedOperationException("not-yet-implemented.");
  }

  @Override
  public void shouldReturn200ForExistingItemAndMetadata() {
    throw new UnsupportedOperationException("not-yet-implemented.");
  }

  @Override
  public void shouldUpdateEscidocMetadata() {
    throw new UnsupportedOperationException("not-yet-implemented.");
  }

  @Override
  public void shouldNotUpdateMetadataIfInConfict() {
    throw new UnsupportedOperationException("not-yet-implemented.");
  }

  @Override
  public void shouldGetEscidocMetadata() {
    throw new UnsupportedOperationException("not-yet-implemented.");
  }

  @Override
  public void shouldReturn204ForMetadataWithNoContent() {
    throw new UnsupportedOperationException("not-yet-implemented.");
  }

  @Override
  public void shouldReturn400ForMissingServerParameter() {
    throw new UnsupportedOperationException("not-yet-implemented.");
  }

  @Override
  public void shouldReturn200forHelloWorld() throws Exception {
    throw new UnsupportedOperationException("not-yet-implemented.");
  }

  @Override
  public void shouldReturnXmlForMEtadata() {
    throw new UnsupportedOperationException("not-yet-implemented.");
  }

}
