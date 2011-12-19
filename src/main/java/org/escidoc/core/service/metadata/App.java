package org.escidoc.core.service.metadata;

import com.sun.jersey.api.container.grizzly2.GrizzlyServerFactory;
import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.api.core.ResourceConfig;

import org.glassfish.grizzly.http.server.HttpServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;

import javax.ws.rs.core.UriBuilder;

public class App {

  private final static Logger LOG = LoggerFactory.getLogger(App.class);
  public static final String RESOURCE_PACKAGE = "org.escidoc.core.service.metadata.resources";
  public static final String HOST = "http://localhost/";
  public static final int PORT = 9998;

  private static URI getBaseUri() {
    return UriBuilder.fromUri(HOST).port(PORT).build();
  }

  private static final URI BASE_URI = getBaseUri();

  public static void main(final String[] args) throws IOException {
    final HttpServer server = startServer();
    LOG.info(String.format("Jersey app started with WADL available at "
        + "application.wadl\nTry out helloworld\nHit enter to stop it..."));
    System.in.read();
    server.stop();
  }

  private static HttpServer startServer() throws IllegalArgumentException, NullPointerException,
      IOException {
    LOG.info("Starting grizzly");
    final ResourceConfig config = new PackagesResourceConfig(RESOURCE_PACKAGE);
    return GrizzlyServerFactory.createHttpServer(BASE_URI, config);
  }
}