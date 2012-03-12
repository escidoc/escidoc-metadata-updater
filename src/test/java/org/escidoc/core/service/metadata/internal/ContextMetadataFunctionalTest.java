package org.escidoc.core.service.metadata.internal;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource.Builder;

import static org.junit.Assert.assertEquals;

import org.escidoc.core.service.metadata.AppConstant;
import org.escidoc.core.service.metadata.ContextMetadataUpdateServiceSpec;
import org.junit.After;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;

import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;
import javax.xml.transform.dom.DOMSource;

import de.escidoc.core.client.Authentication;

public class ContextMetadataFunctionalTest extends Base implements ContextMetadataUpdateServiceSpec {

  private final static Logger LOG = LoggerFactory.getLogger(ContextMetadataFunctionalTest.class);

  private static final String CONTEXT_NAME = "descriptor_for_book";

  private static final String METADATA = "metadata";

  private static final String CONTEXT_ID = "escidoc:3829";

  @After
  public void stop() throws Exception {
    server.stop();
  }

  @Test
  @Override
  public void shouldReturn200ForExistingContextAndMetadata() throws Exception {
    // @formatter:off
    final Builder builder = resource
        .path(AppConstant.CONTEXTS)
        .path(CONTEXT_ID)
        .path(METADATA)
        .path(CONTEXT_NAME)
        .queryParam(AppConstant.EU, SERVICE_URL)
        .accept(MediaType.APPLICATION_XML);
    assertEquals("response is not equals", 200, builder.get(ClientResponse.class).getStatus());
    final String responseMessage=  builder.get(ClientResponse.class).getEntity(String.class);
    LOG.debug("Entity as String: "+responseMessage);

  }

  @Test
  @Override
  public void shouldReturn200WhenTryingToUpdateMetadataGivenValidToken() throws Exception  {

    final String token = new Authentication(new URL(SERVICE_URL), SYSADMIN, SYSADMIN_PASSWORD).getHandle();
    // @formatter:off
    final Builder builder = resource
        .path(AppConstant.CONTEXTS)
        .path(CONTEXT_ID)
        .path(METADATA)
        .path(CONTEXT_NAME)
        .queryParam(AppConstant.EU, SERVICE_URL)
        .accept(MediaType.APPLICATION_XML)
        .cookie(new Cookie("escidocCookie",token));
    
    final DOMSource e = builder
        .get(ClientResponse.class)
        .getEntity(DOMSource.class);
    
    final ClientResponse r = builder.put(ClientResponse.class,e);
   // @formatter:on

    LOG.debug("Entity: " + r.getEntity(String.class));
    assertEquals("response is not equals", 200, r.getStatus());
  }
}