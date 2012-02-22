package org.escidoc.core.service.metadata.repository;

import java.net.MalformedURLException;
import java.net.URI;

import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;
import de.escidoc.core.client.exceptions.application.security.AuthenticationException;
import de.escidoc.core.client.exceptions.application.security.AuthorizationException;
import de.escidoc.core.resources.common.MetadataRecord;
import de.escidoc.core.resources.om.item.Item;

public interface ItemRepository {

  Item find(String itemId, URI serviceUri, String token) throws AuthenticationException, AuthorizationException,
      EscidocException, InternalClientException, TransportException, MalformedURLException;

  Item update(final Item item) throws AuthenticationException, AuthorizationException, EscidocException,
      InternalClientException, TransportException;

  MetadataRecord findMetadataByName(final String itemId, final String metadataName) throws EscidocException,
      InternalClientException, TransportException;
}