package org.escidoc.core.service.metadata;

public interface ItemMetadataUpdateServiceSpec {

  void shouldReturn404ForNonExistingItem();

  void shouldReturn404ForNonExistingMetadata();

  void shouldReturn200ForExistingItemAndMetadata();

  void shouldUpdateEscidocMetadata();

  void shouldNotUpdateMetadataIfInConfict();

  void shouldGetEscidocMetadata();
}