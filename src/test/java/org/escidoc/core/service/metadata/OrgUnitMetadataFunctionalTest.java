package org.escidoc.core.service.metadata;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource.Builder;

import static org.junit.Assert.assertEquals;

import org.escidoc.core.service.metadata.internal.Base;
import org.junit.After;
import org.junit.Test;

import javax.ws.rs.core.MediaType;

public class OrgUnitMetadataFunctionalTest extends Base implements OrgUnitMetadataUpdateServiceSpec {

  private static final String EU_PARAM = "eu";

  private static final String METADATA = "metadata";

  private static final String ORGANIZATIONS = "organizations";

  private static final String ORG_UNIT_ID = "escidoc:1507";

  @After
  public void stop() throws Exception {
    server.stop();
  }

  @Test
  @Override
  public void shouldReturn200ForExistingOrgUnitAndMetadata() throws Exception {
    // @formatter:off
    final Builder builder = resource
        .path(ORGANIZATIONS)
        .path(ORG_UNIT_ID)
        .path(METADATA)
        .path(EXISTING_METADATA_NAME)
        .queryParam(EU_PARAM, SERVICE_URL)
        .accept(MediaType.APPLICATION_XML);
    assertEquals("response is not equals", 200, builder.get(ClientResponse.class).getStatus());
  }
}