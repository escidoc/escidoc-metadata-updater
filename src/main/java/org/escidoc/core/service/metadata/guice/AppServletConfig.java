package org.escidoc.core.service.metadata.guice;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;

import com.sun.jersey.guice.JerseyServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

import org.escidoc.core.service.metadata.repository.ItemRepository;
import org.escidoc.core.service.metadata.repository.internal.InMemoryItemRepository;

public class AppServletConfig extends GuiceServletContextListener {

  @Override
  protected Injector getInjector() {
    return Guice.createInjector(new JerseyServletModule() {
      @Override
      protected void configureServlets() {
        super.configureServlets();
        bind(ItemRepository.class).to(InMemoryItemRepository.class);
        serve("/*").with(GuiceContainer.class);
      }
    });
  }
}
