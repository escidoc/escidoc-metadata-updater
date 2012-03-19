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
package org.escidoc.core.service.metadata.internal;

import com.google.inject.servlet.GuiceFilter;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.escidoc.core.service.metadata.servlet.AppServletConfig;
import org.junit.Before;

public class Base {

    protected static final String SERVICE_URL = "http://esfedrep1.fiz-karlsruhe.de:8080/";

    protected static final String SYSADMIN = "sysadmin";

    protected static final String SYSADMIN_PASSWORD = "eSciDoc";

    protected static final String ITEM_ID = "escidoc:139";

    protected static final String NON_EXISTING_ITEM_ID = "not-exists";

    protected static final String NON_EXISTING_METADATA_NAME = "not-exists";

    protected static final String EXISTING_ITEM_ID = "escidoc:test";

    public static final String PROTECTED_ITEM_ID = "protected";

    protected static final String EXISTING_METADATA_NAME = "escidoc";

    protected static final String EMPTY_METADATA = "empty";

    protected Client client;

    protected WebResource resource;

    protected Server server;

    @Before
    public void setup() throws Exception {
        server = new Server(8089);
        final ServletContextHandler sch = new ServletContextHandler(server, "/");
        sch.addEventListener(new AppServletConfig());
        sch.addFilter(GuiceFilter.class, "/*", null);
        sch.addServlet(DefaultServlet.class, "/");
        server.start();
        client = Client.create();
        // @formatter:off
    resource = client
        .resource("http://localhost:8089")
        .path("v0.9");
	   // @formatter:on
    }
}