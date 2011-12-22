package org.escidoc.core.service.metada;

public interface ItemMetadataUpdateServiceSpec {

  void shouldGetEscidocMetadata();

  void shouldUpdateEscidocMetadata();

  void shouldNotUpdateMetadataIfInConfict();

}