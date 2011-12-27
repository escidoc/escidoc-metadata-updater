package org.escidoc.core.service.metadata.repository.internal;

import org.escidoc.core.service.metadata.repository.ItemRepository;

import java.net.MalformedURLException;
import java.net.URI;

import de.escidoc.core.client.ItemHandlerClient;
import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;
import de.escidoc.core.client.interfaces.ItemHandlerClientInterface;
import de.escidoc.core.resources.om.item.Item;

public class ItemRepositoryImpl implements ItemRepository {
  ItemHandlerClientInterface c;

  public ItemRepositoryImpl(final URI serviceUri) throws MalformedURLException {
    c = new ItemHandlerClient(serviceUri.toURL());
  }

  @Override
  public Item find(final String itemId) throws EscidocException, InternalClientException,
      TransportException {
    return c.retrieve(itemId);
  }
}
