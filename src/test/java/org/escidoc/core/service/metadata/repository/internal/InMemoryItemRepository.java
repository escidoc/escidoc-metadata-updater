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

import org.escidoc.core.service.metadata.Utils;
import org.escidoc.core.service.metadata.internal.Base;
import org.escidoc.core.service.metadata.repository.ItemRepository;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;
import de.escidoc.core.client.exceptions.application.security.AuthenticationException;
import de.escidoc.core.client.exceptions.application.security.AuthorizationException;
import de.escidoc.core.resources.common.MetadataRecord;
import de.escidoc.core.resources.common.MetadataRecords;
import de.escidoc.core.resources.om.item.Item;

public class InMemoryItemRepository implements ItemRepository {

    private final static Logger LOG = LoggerFactory.getLogger(InMemoryItemRepository.class);

    private final MetadataRecords mrs = new MetadataRecords();

    private final Item i = new Item();

    private final Map<String, Item> map = new HashMap<String, Item>();

    public InMemoryItemRepository() {

        addEmptyMetadata();
        addDefaultMetadata();
        i.setLastModificationDate(new DateTime());
        i.setMetadataRecords(mrs);

        map.put("escidoc:test", i);

        addProtectedItem();
    }

    private void addProtectedItem() {
        final Item i = new Item();
        i.setLastModificationDate(new DateTime());
        map.put(Base.PROTECTED_ITEM_ID, i);
        i.setMetadataRecords(mrs);

    }

    private void addDefaultMetadata() {
        final MetadataRecord mr = new MetadataRecord("escidoc");
        try {
            mr.setContent(Utils.buildSimpleMetadata());
        }
        catch (final ParserConfigurationException e) {
            LOG.error("can not add metadata content:  " + e.getMessage(), e);
        }
        mrs.add(mr);
    }

    private void addEmptyMetadata() {
        mrs.add(new MetadataRecord("empty"));
    }

    @Override
    public Item find(final String itemId, final URI serviceUri, final String token) throws EscidocException,
        InternalClientException, TransportException, MalformedURLException {
        if (itemId.equals(Base.PROTECTED_ITEM_ID) && notEscidoc(token)) {
            throw new AuthenticationException("Item, " + itemId + ", is a protected resource", new Exception());
        }
        return map.get(itemId);
    }

    private static boolean notEscidoc(final String token) {
        return token == null || !token.startsWith("ESCIDOC");
    }

    @Override
    public Item update(final Item item, final URI serviceUri, final String token) throws AuthenticationException,
        AuthorizationException, EscidocException, InternalClientException, TransportException, MalformedURLException {
        throw new UnsupportedOperationException("not-yet-implemented.");
    }
}
