package org.escidoc.core.service.metadata.repository;

import java.net.MalformedURLException;
import java.net.URI;

import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;
import de.escidoc.core.client.exceptions.application.security.AuthenticationException;
import de.escidoc.core.client.exceptions.application.security.AuthorizationException;
import de.escidoc.core.resources.GenericResource;
import de.escidoc.core.resources.oum.OrganizationalUnit;

public interface OrgUnitRepository {

  OrganizationalUnit find(String id, URI serviceUri, String token) throws AuthenticationException,
      AuthorizationException, EscidocException, InternalClientException, TransportException, MalformedURLException;

  GenericResource update(OrganizationalUnit ou) throws AuthenticationException, AuthorizationException,
      EscidocException, InternalClientException, TransportException;

}
