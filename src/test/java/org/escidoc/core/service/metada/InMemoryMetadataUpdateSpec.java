package org.escidoc.core.service.metada;

public class InMemoryMetadataUpdateSpec implements ItemMetadataUpdateServiceSpec {

  @Override
  public void shouldReturn404ForNonExistingItem() {
    throw new UnsupportedOperationException("not-yet-implemented.");
  }

  @Override
  public void shouldReturn404ForNonExistingMetadata() {
    throw new UnsupportedOperationException("not-yet-implemented.");
  }

  @Override
  public void shouldReturn200ForExistingItem() {
    throw new UnsupportedOperationException("not-yet-implemented.");
  }

  @Override
  public void shouldGetEscidocMetadata() {
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

}
