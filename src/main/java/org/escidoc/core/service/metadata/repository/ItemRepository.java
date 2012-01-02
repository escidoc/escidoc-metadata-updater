package org.escidoc.core.service.metadata.repository;

import java.net.MalformedURLException;
import java.net.URI;

import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;
import de.escidoc.core.resources.om.item.Item;

public interface ItemRepository {

  Item find(String itemId, URI serviceUri) throws EscidocException, InternalClientException,
      TransportException, MalformedURLException;

  Item find(String itemId, URI serviceUri, String token) throws EscidocException,
      InternalClientException, TransportException, MalformedURLException;

  Item update(Item item) throws EscidocException, InternalClientException, TransportException;
}
