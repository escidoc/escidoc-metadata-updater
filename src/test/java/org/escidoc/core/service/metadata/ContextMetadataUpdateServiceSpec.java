package org.escidoc.core.service.metadata;

public interface ContextMetadataUpdateServiceSpec {

  void shouldReturn200ForExistingContextAndMetadata() throws Exception;

  void shouldReturn200WhenTryingToUpdateMetadataGivenValidToken() throws Exception;

}