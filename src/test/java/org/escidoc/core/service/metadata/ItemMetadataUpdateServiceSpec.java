package org.escidoc.core.service.metadata;

public interface ItemMetadataUpdateServiceSpec {

  void shouldReturn404ForNonExistingItem() throws Exception;

  void shouldReturn404ForNonExistingMetadata() throws Exception;

  void shouldReturn200ForExistingItemAndMetadata() throws Exception;

  void shouldUpdateEscidocMetadata() throws Exception;

  void shouldNotUpdateMetadataIfInConfict() throws Exception;

  void shouldReturn204ForMetadataWithNoContent() throws Exception;

  void shouldReturn400ForMissingServerParameter() throws Exception;

  void shouldReturn200forHelloWorld() throws Exception;

  void shouldReturnXmlForMetadata() throws Exception;
}