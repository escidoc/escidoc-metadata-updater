package org.escidoc.core.service.metadata.guice;

import com.google.inject.servlet.GuiceFilter;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;

public class AppGuiceLauncher {

  public static void main(final String[] args) throws Exception {
    final Server server = new Server(8088);
    final ServletContextHandler sch = new ServletContextHandler(server, "/");
    sch.addEventListener(new AppServletConfig());
    sch.addFilter(GuiceFilter.class, "/*", null);
    sch.addServlet(DefaultServlet.class, "/");
    server.start();
    server.join();
  }

}
