package org.escidoc.core.service.metadata;

import com.google.inject.servlet.GuiceFilter;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.escidoc.core.service.metadata.servlet.AppServletConfig;

public class Main {

  private static final int PORT_NUMBER = 8088;

  public static void main(final String[] args) throws Exception {
    final Server server = new Server(PORT_NUMBER);
    final ServletContextHandler sch = new ServletContextHandler(server, "/");
    sch.addEventListener(new AppServletConfig());
    sch.addFilter(GuiceFilter.class, "/*", null);
    sch.addServlet(DefaultServlet.class, "/");
    server.start();
    server.join();
  }
}