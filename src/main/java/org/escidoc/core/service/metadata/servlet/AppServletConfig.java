/**
 * CDDL HEADER START
 *
 * The contents of this file are subject to the terms of the
 * Common Development and Distribution License, Version 1.0 only
 * (the "License").  You may not use this file except in compliance
 * with the License.
 *
 * You can obtain a copy of the license at license/ESCIDOC.LICENSE
 * or https://www.escidoc.org/license/ESCIDOC.LICENSE .
 * See the License for the specific language governing permissions
 * and limitations under the License.
 *
 * When distributing Covered Code, include this CDDL HEADER in each
 * file and include the License file at license/ESCIDOC.LICENSE.
 * If applicable, add the following below this CDDL HEADER, with the
 * fields enclosed by brackets "[]" replaced with your own identifying
 * information: Portions Copyright [yyyy] [name of copyright owner]
 *
 * CDDL HEADER END
 *
 *
 *
 * Copyright 2012 Fachinformationszentrum Karlsruhe Gesellschaft
 * fuer wissenschaftlich-technische Information mbH and Max-Planck-
 * Gesellschaft zur Foerderung der Wissenschaft e.V.
 * All rights reserved.  Use is subject to license terms.
 */
package org.escidoc.core.service.metadata.servlet;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;

import com.sun.jersey.guice.JerseyServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

import org.escidoc.core.service.metadata.repository.ContainerRepository;
import org.escidoc.core.service.metadata.repository.ContextRepository;
import org.escidoc.core.service.metadata.repository.ItemRepository;
import org.escidoc.core.service.metadata.repository.OrgUnitRepository;
import org.escidoc.core.service.metadata.repository.internal.ContainerRepositoryImpl;
import org.escidoc.core.service.metadata.repository.internal.ContextRepositoryImpl;
import org.escidoc.core.service.metadata.repository.internal.ItemRepositoryImpl;
import org.escidoc.core.service.metadata.repository.internal.OrgUnitRepositoryImpl;
import org.escidoc.core.service.metadata.resources.BlobResource;
import org.escidoc.core.service.metadata.resources.ContainerMetadataResource;
import org.escidoc.core.service.metadata.resources.ContextMetadataResource;
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

                bind(HelloResource.class);

                bind(ItemMetadataResource.class);
                bind(OrgUnitMetadataResource.class);
                bind(ContextMetadataResource.class);
                bind(ContainerMetadataResource.class);
                bind(BlobResource.class);

                bind(ItemRepository.class).to(ItemRepositoryImpl.class);
                bind(OrgUnitRepository.class).to(OrgUnitRepositoryImpl.class);
                bind(ContextRepository.class).to(ContextRepositoryImpl.class);
                bind(ContainerRepository.class).to(ContainerRepositoryImpl.class);

                serve("/v0.9/*").with(GuiceContainer.class);
            }
        });
    }
}
