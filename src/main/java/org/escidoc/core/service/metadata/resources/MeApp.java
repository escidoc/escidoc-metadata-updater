package org.escidoc.core.service.metadata.resources;

import com.sun.jersey.api.core.PackagesResourceConfig;

import org.escidoc.core.service.metadata.App;

import javax.ws.rs.ApplicationPath;

@ApplicationPath("/")
public class MeApp extends PackagesResourceConfig {

  public MeApp() {
    super(App.RESOURCE_PACKAGE);
  }
}