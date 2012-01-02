package org.escidoc.core.service.metadata;

public interface ItemMetadataUpdateServiceSpec {

  void shouldReturn200forHelloWorld() throws Exception;

  void shouldReturn404ForNonExistingItem() throws Exception;

  void shouldReturn404ForNonExistingMetadata() throws Exception;

  void shouldReturn200ForExistingItemAndMetadata() throws Exception;

  void shouldReturn204ForMetadataWithNoContent() throws Exception;

  void shouldReturn400ForMissingServerParameter() throws Exception;

  void shouldReturnXmlForMetadata() throws Exception;

  void shouldUpdateEscidocMetadata() throws Exception;

  void shouldNotUpdateMetadataIfInConfict() throws Exception;

  void shouldReturn303WhenNoValidCookie() throws Exception;

  void shouldReturn200WhenTryingToFetchUnreleasedItemGivenAValidCookie() throws Exception;

  void shouldReturn200WhenTryingToUpdateMetadataGivenValidCookie() throws Exception;
}