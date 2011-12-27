package org.escidoc.core.service.metadata.repository;

import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;
import de.escidoc.core.resources.om.item.Item;

public interface ItemRepository {

  Item find(String itemId) throws EscidocException, InternalClientException, TransportException;

}
