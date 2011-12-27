package org.escidoc.core.service.metada;

public interface ItemMetadataUpdateServiceSpec {

  void shouldReturn404ForNonExistingItem();

  void shouldReturn404ForNonExistingMetadata();

  void shouldGetEscidocMetadata();

  void shouldUpdateEscidocMetadata();

  void shouldNotUpdateMetadataIfInConfict();
}