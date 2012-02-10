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