package org.escidoc.core.service.metadata;

import com.sun.jersey.api.core.PackagesResourceConfig;


import javax.ws.rs.ApplicationPath;

@ApplicationPath("/")
public class MyApp extends PackagesResourceConfig {

  public MyApp() {
    super(App.RESOURCE_PACKAGE);
  }
}