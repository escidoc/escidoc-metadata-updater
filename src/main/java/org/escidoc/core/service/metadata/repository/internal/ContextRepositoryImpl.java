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