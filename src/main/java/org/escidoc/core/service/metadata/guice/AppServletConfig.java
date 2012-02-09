package org.escidoc.core.service.metadata.guice;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;

import com.sun.jersey.guice.JerseyServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

import org.escidoc.core.service.metadata.repository.ItemRepository;
import org.escidoc.core.service.metadata.repository.OrgUnitRepository;
import org.escidoc.core.service.metadata.repository.internal.ItemRepositoryImpl;
import org.escidoc.core.service.metadata.repository.internal.OrgUnitRepositoryImpl;
import org.escidoc.core.service.metadata.resources.HelloResource;
import org.escidoc.core.service.metadata.resources.ItemMetadataResource;
import org.escidoc.core.service.metadata.resources.OrgUnitMetadataResource;

public class AppServletConfig extends GuiceServletContextListener {

  @Override
  protected Injector getInjector() {
    return Guice.createInjector(new JerseyServletModule() {
      @Override
      protected void configureServlets() {
        super.configureServlets();
        bind(ItemMetadataResource.class);
        bind(OrgUnitMetadataResource.class);
        bind(HelloResource.class);
        bind(ItemRepository.class).to(ItemRepositoryImpl.class);
        bind(OrgUnitRepository.class).to(OrgUnitRepositoryImpl.class);
        serve("/v0.9/*").with(GuiceContainer.class);
      }
    });
  }
}
