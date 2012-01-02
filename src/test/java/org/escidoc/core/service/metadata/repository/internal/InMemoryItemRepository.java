package org.escidoc.core.service.metadata.repository.internal;

import org.escidoc.core.service.metadata.internal.Base;
import org.escidoc.core.service.metadata.repository.ItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;
import de.escidoc.core.client.exceptions.application.security.AuthorizationException;
import de.escidoc.core.resources.common.MetadataRecord;
import de.escidoc.core.resources.common.MetadataRecords;
import de.escidoc.core.resources.om.item.Item;

public class InMemoryItemRepository implements ItemRepository {

  private final static Logger LOG = LoggerFactory.getLogger(InMemoryItemRepository.class);

  private final MetadataRecords mrs = new MetadataRecords();
  private final Item i = new Item();
  private final Map<String, Item> map = new HashMap<String, Item>();

  public InMemoryItemRepository() {

    addEmptyMetadata();
    addDefaultMetadata();
    i.setMetadataRecords(mrs);

    map.put("escidoc:test", i);

    addProtectedItem();
  }

  private void addProtectedItem() {
    map.put(Base.PROTECTED_ITEM_ID, new Item());
  }

  private void addDefaultMetadata() {
    final MetadataRecord mr = new MetadataRecord("escidoc");
    try {
      mr.setContent(Utils.buildSimpleMetadata());
    } catch (final ParserConfigurationException e) {
      LOG.error("can not add metadata content:  " + e.getMessage(), e);
    }
    mrs.add(mr);
  }

  private void addEmptyMetadata() {
    mrs.add(new MetadataRecord("empty"));
  }

  @Override
  public Item find(final String itemId, final URI serviceUri, final String token) throws EscidocException,
      InternalClientException, TransportException, MalformedURLException {
    if (itemId.equals(Base.PROTECTED_ITEM_ID)) {
      throw new AuthorizationException();
    }
    return map.get(itemId);
  }

  @Override
  public Item find(final String itemId, final URI serviceUri) throws EscidocException, InternalClientException,
      TransportException, MalformedURLException {
    throw new UnsupportedOperationException("not-yet-implemented.");
  }

  @Override
  public Item update(final Item item) throws EscidocException, InternalClientException, TransportException {
    throw new UnsupportedOperationException("not-yet-implemented.");
  }
}