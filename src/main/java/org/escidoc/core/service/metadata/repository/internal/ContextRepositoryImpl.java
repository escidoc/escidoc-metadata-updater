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
package org.escidoc.core.service.metadata.repository.internal;

import com.google.common.base.Preconditions;

import org.escidoc.core.service.metadata.repository.ContextRepository;

import java.net.MalformedURLException;
import java.net.URI;

import de.escidoc.core.client.ContextHandlerClient;
import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;
import de.escidoc.core.client.interfaces.ContextHandlerClientInterface;
import de.escidoc.core.resources.GenericResource;
import de.escidoc.core.resources.om.context.Context;

public class ContextRepositoryImpl implements ContextRepository {

    private ContextHandlerClientInterface c;

    @Override
    public de.escidoc.core.resources.om.context.Context find(final String id, final URI serviceUri, final String token)
        throws MalformedURLException, InternalClientException, EscidocException, TransportException {

        Preconditions.checkNotNull(id, "id is null: %s", id);
        Preconditions.checkNotNull(serviceUri, "serviceUri is null: %s", serviceUri);

        c = new ContextHandlerClient(serviceUri.toURL());
        c.setHandle(token);
        return c.retrieve(id);
    }

    @Override
    public GenericResource update(final Context resource) throws EscidocException, InternalClientException,
        TransportException {
        return c.update(resource);
    }
}