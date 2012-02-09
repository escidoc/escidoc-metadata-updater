package org.escidoc.core.service.metadata.internal;

import com.google.inject.servlet.GuiceFilter;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.escidoc.core.service.metadata.guice.AppServletConfig;
import org.junit.Before;

public class Base {

  protected static final String SERVICE_URL = "http://esfedrep1.fiz-karlsruhe.de:8080/";

  protected static final String SYSADMIN = "sysadmin";

  protected static final String SYSADMIN_PASSWORD = "eSciDoc";

  protected static final String ITEM_ID = "escidoc:139";

  protected static final String NON_EXISTING_ITEM_ID = "not-exists";

  protected static final String NON_EXISTING_METADATA_NAME = "not-exists";

  protected static final String EXISTING_ITEM_ID = "escidoc:test";

  public static final String PROTECTED_ITEM_ID = "protected";

  protected static final String EXISTING_METADATA_NAME = "escidoc";

  protected static final String EMPTY_METADATA = "empty";

  protected Client client;

  protected WebResource resource;

  protected Server server;

  @Before
  public void setup() throws Exception {
    server = new Server(8089);
    final ServletContextHandler sch = new ServletContextHandler(server, "/");
    sch.addEventListener(new AppServletConfig());
    sch.addFilter(GuiceFilter.class, "/*", null);
    sch.addServlet(DefaultServlet.class, "/");
    server.start();
    client = Client.create();
    // @formatter:off
    resource = client
        .resource("http://localhost:8089")
        .path("v0.9");
	   // @formatter:on
  }
}