package org.escidoc.core.service.metadata.repository;

import java.net.MalformedURLException;
import java.net.URI;

import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;
import de.escidoc.core.client.exceptions.application.security.AuthenticationException;
import de.escidoc.core.client.exceptions.application.security.AuthorizationException;
import de.escidoc.core.resources.GenericResource;

public interface ContextRepository {

  de.escidoc.core.resources.om.context.Context find(String id, URI uri, String decodedHandle)
      throws AuthenticationException, AuthorizationException, EscidocException, InternalClientException,
      TransportException, MalformedURLException;

  GenericResource update(de.escidoc.core.resources.om.context.Context resource) throws EscidocException,
      InternalClientException, TransportException;
}