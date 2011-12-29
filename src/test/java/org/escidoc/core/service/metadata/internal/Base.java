package org.escidoc.core.service.metadata.internal;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

import org.escidoc.core.service.metadata.Server;
import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

public class Base {

  protected static final String SERVICE_URL = "http://esfedrep1.fiz-karlsruhe.de:8080/";

  protected static final String SYSADMIN = "sysadmin";

  protected static final String SYSADMIN_PASSWORD = "eSciDoc";

  protected static final String ITEM_ID = "escidoc:139";

  protected static final String NON_EXISTING_ITEM_ID = "not-exists";

  protected static final String NON_EXISTING_METADATA_NAME = "not-exists";

  protected static final String EXISTING_ITEM_ID = "escidoc:test";

  protected static final String EXISTING_METADATA_NAME = "escidoc";

  protected static final String EMPTY_METADATA = "empty";

  protected Client client;

  protected WebResource resource;

  protected static HttpServer server;

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

    // final Authentication authentication = new Authentication(new
    // URL(SERVICE_URL), SYSADMIN,
    // SYSADMIN_PASSWORD);
    // final String userHandle = authentication.getHandle();
  }

  public void shouldReturn200forHelloWorldXml() throws Exception {
    throw new UnsupportedOperationException("not-yet-implemented.");
  }
}
