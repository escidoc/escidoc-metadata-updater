package org.escidoc.core.service.metadata.guice;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;

import com.sun.jersey.guice.JerseyServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

import org.escidoc.core.service.metadata.repository.ItemRepository;
import org.escidoc.core.service.metadata.repository.internal.ItemRepositoryImpl;
import org.escidoc.core.service.metadata.resources.HelloResource;
import org.escidoc.core.service.metadata.resources.ItemMetadataResource;

public class AppServletConfig extends GuiceServletContextListener {

  @Override
  protected Injector getInjector() {
    return Guice.createInjector(new JerseyServletModule() {
      @Override
      protected void configureServlets() {
        super.configureServlets();
        bind(ItemMetadataResource.class);
        bind(HelloResource.class);
        bind(ItemRepository.class).to(ItemRepositoryImpl.class);
        serve("/rest/*").with(GuiceContainer.class);
      }
    });
  }
}
