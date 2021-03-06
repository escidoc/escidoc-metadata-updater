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

import org.escidoc.core.service.metadata.repository.ItemRepository;

import java.net.MalformedURLException;
import java.net.URI;

import de.escidoc.core.client.ItemHandlerClient;
import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;
import de.escidoc.core.client.exceptions.application.security.AuthenticationException;
import de.escidoc.core.client.exceptions.application.security.AuthorizationException;
import de.escidoc.core.client.interfaces.ItemHandlerClientInterface;
import de.escidoc.core.resources.om.item.Item;

public class ItemRepositoryImpl implements ItemRepository {
    private ItemHandlerClientInterface c;

    // TODO NOTE: it can throw Authentification or AuthorizationException
    @Override
    public Item find(final String itemId, final URI serviceUri, final String token) throws EscidocException,
        InternalClientException, TransportException, MalformedURLException, AuthenticationException,
        AuthorizationException {
        Preconditions.checkNotNull(itemId, "itemId is null: %s", itemId);
        Preconditions.checkNotNull(serviceUri, "serviceUri is null: %s", serviceUri);

        c = new ItemHandlerClient(serviceUri.toURL());
        c.setHandle(token);
        return c.retrieve(itemId);
    }

    @Override
    public Item update(final Item item) throws AuthenticationException, AuthorizationException, EscidocException,
        InternalClientException, TransportException {
        Preconditions.checkNotNull(item, "item is null: %s", item);
        return c.update(item);
    }
}