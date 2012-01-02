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
  public Item find(final String itemId, final URI serviceUri) throws EscidocException,
      InternalClientException, TransportException, MalformedURLException {

    c = new ItemHandlerClient(serviceUri.toURL());
    return c.retrieve(itemId);
  }

  // TODO NOTE: it can throw Authentification or AuthorizationException
  @Override
  public Item find(final String itemId, final URI serviceUri, final String token)
      throws EscidocException, InternalClientException, TransportException, MalformedURLException {

    c = new ItemHandlerClient(serviceUri.toURL());
    c.setHandle(token);
    return c.retrieve(itemId);
  }

  @Override
  public Item update(final Item item) throws EscidocException, InternalClientException,
      TransportException {
    return c.update(item);
  }

}