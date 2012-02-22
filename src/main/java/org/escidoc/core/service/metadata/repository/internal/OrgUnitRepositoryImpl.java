package org.escidoc.core.service.metadata.repository.internal;

import com.google.common.base.Preconditions;

import org.escidoc.core.service.metadata.repository.OrgUnitRepository;

import java.net.MalformedURLException;
import java.net.URI;

import de.escidoc.core.client.OrganizationalUnitHandlerClient;
import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;
import de.escidoc.core.client.exceptions.application.security.AuthenticationException;
import de.escidoc.core.client.exceptions.application.security.AuthorizationException;
import de.escidoc.core.client.interfaces.OrganizationalUnitHandlerClientInterface;
import de.escidoc.core.resources.GenericResource;
import de.escidoc.core.resources.common.MetadataRecord;
import de.escidoc.core.resources.oum.OrganizationalUnit;

public class OrgUnitRepositoryImpl implements OrgUnitRepository {

  private OrganizationalUnitHandlerClientInterface c;

  @Override
  public OrganizationalUnit find(final String id, final URI serviceUri, final String token) throws EscidocException,
      InternalClientException, TransportException, MalformedURLException, AuthenticationException,
      AuthorizationException {
    Preconditions.checkNotNull(id, "id is null: %s", id);
    Preconditions.checkNotNull(serviceUri, "serviceUri is null: %s", serviceUri);

    c = new OrganizationalUnitHandlerClient(serviceUri.toURL());
    c.setHandle(token);
    return c.retrieve(id);
  }

  @Override
  public GenericResource update(final OrganizationalUnit ou) throws AuthenticationException, AuthorizationException,
      EscidocException, InternalClientException, TransportException {
    return c.update(ou);
  }

  @Override
  public MetadataRecord findMetadataByName(final String objid, final String metadataName)
      throws InternalClientException, TransportException, EscidocException {
    return c.retrieveMdRecord(objid, metadataName);
  }
}