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
  private ItemHandlerClientInterface c;

  @Override
  public Item find(final String itemId) throws EscidocException, InternalClientException,
      TransportException {
    throw new UnsupportedOperationException("Remove this method");
  }

  @Override
  public Item find(final String itemId, final URI serviceUri) throws EscidocException,
      InternalClientException, TransportException, MalformedURLException {

    c = new ItemHandlerClient(serviceUri.toURL());
    return c.retrieve(itemId);
  }
}
