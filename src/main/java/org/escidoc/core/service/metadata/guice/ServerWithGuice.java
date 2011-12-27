package org.escidoc.core.service.metadata.guice;

import com.google.inject.servlet.GuiceFilter;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;

public class ServerWithGuice {
  public static final String RESOURCE_PACKAGE = "org.escidoc.core.service.metadata.resources";
  public static final String HOST = "http://localhost/";
  public static final int PORT = 9997;
  private final Server server;

  public ServerWithGuice() {
    server = new Server(PORT);
    final ServletContextHandler sch = new ServletContextHandler(server, "/");
    sch.addEventListener(new AppServletConfig());
    sch.addFilter(GuiceFilter.class, "/*", null);
    sch.addServlet(DefaultServlet.class, "/");
  }

  public void start() throws Exception {
    server.start();
    server.join();
  }

  public void stop() throws Exception {
    server.stop();
  }
}
