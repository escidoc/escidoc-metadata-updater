package org.escidoc.core.service.metadata.internal;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import static org.junit.Assert.assertEquals;

import org.escidoc.core.service.metadata.ItemMetadataUpdateServiceSpec;
import org.escidoc.core.service.metadata.Server;
import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;

import javax.ws.rs.core.MediaType;

import de.escidoc.core.client.Authentication;

public class ItemMetadataUpdateServiceSpecImpl implements ItemMetadataUpdateServiceSpec {

  private final static Logger LOG = LoggerFactory.getLogger(ItemMetadataUpdateServiceSpecImpl.class);

  private static final String SERVICE_URL = "http://esfedrep1.fiz-karlsruhe.de:8080/";

  private static final String SYSADMIN = "sysadmin";

  private static final String SYSADMIN_PASSWORD = "eSciDoc";

  private static final String ITEM_ID = "escidoc:139";

  private static final String METADATA_NAME = null;

  private static final String NON_EXISTING_ITEM_ID = null;

  private static final String NON_EXISTING_METADATA_NAME = null;

  private static final String EXISTING_ITEM_ID = "escidoc:test";

  private static final String EXISTING_METADATA_NAME = "escidoc";

  private String userHandle;

  private Client client;

  private WebResource resource;

  private static HttpServer server;

  @BeforeClass
  public static void start() throws Exception {
    server = Server.start();
  }

  @AfterClass
  public static void shutdown() throws Exception {
    server.stop();
  }

  @Before
  public void setup() throws Exception {
    client = Client.create();
    resource = client.resource(Server.BASE_URI);

    final Authentication authentication = new Authentication(new URL(SERVICE_URL), SYSADMIN,
        SYSADMIN_PASSWORD);
    userHandle = authentication.getHandle();
  }

  @Test
  public void shouldReturn200forHelloWorld() throws Exception {
    final ClientResponse response = resource.path("helloworld").accept("text/plain").get(
        ClientResponse.class);
    assertEquals("response is not equals", 200, response.getStatus());
  }

  @Test
  @Override
  public void shouldGetEscidocMetadata() {
    final ClientResponse r = resource.path("items/" + ITEM_ID + "/metadata/" + METADATA_NAME).accept(
        MediaType.TEXT_PLAIN).get(ClientResponse.class);
    assertEquals("response is not equals", 200, r.getStatus());
  }

  @Test
  @Override
  public void shouldUpdateEscidocMetadata() {
    throw new UnsupportedOperationException("not-yet-implemented.");
  }

  @Test
  @Override
  public void shouldNotUpdateMetadataIfInConfict() {
    throw new UnsupportedOperationException("not-yet-implemented.");
  }

  @Test
  @Override
  public void shouldReturn404ForNonExistingItem() {
    final ClientResponse r = resource.path(
        "items/" + NON_EXISTING_ITEM_ID + "/metadata/" + METADATA_NAME).accept(MediaType.TEXT_PLAIN).get(
        ClientResponse.class);
    assertEquals("response is not equals", 404, r.getStatus());
  }

  @Test
  @Override
  public void shouldReturn404ForNonExistingMetadata() {
    final ClientResponse r = resource.path(
        "items/" + EXISTING_ITEM_ID + "/metadata/" + NON_EXISTING_METADATA_NAME).accept(
        MediaType.TEXT_PLAIN).get(ClientResponse.class);
    assertEquals("response is not equals", 404, r.getStatus());
  }

  @Test
  @Override
  public void shouldReturn200ForExistingItemAndMetadata() {
    final ClientResponse r = resource.path(
        "items/" + EXISTING_ITEM_ID + "/metadata/" + EXISTING_METADATA_NAME).accept(
        MediaType.TEXT_PLAIN).get(ClientResponse.class);
    final String e = r.getEntity(String.class);
    LOG.debug(e);

    assertEquals("response is not equals", 200, r.getStatus());
  }
}