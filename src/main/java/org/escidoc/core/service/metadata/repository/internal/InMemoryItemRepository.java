package org.escidoc.core.service.metadata.repository.internal;

import org.escidoc.core.service.metadata.repository.ItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import de.escidoc.core.resources.common.MetadataRecord;
import de.escidoc.core.resources.common.MetadataRecords;
import de.escidoc.core.resources.om.item.Item;

public class InMemoryItemRepository implements ItemRepository {

  private final static Logger LOG = LoggerFactory.getLogger(InMemoryItemRepository.class);

  private final MetadataRecords mrs = new MetadataRecords();
  private final Item i = new Item();
  private final Map<String, Item> map;

  public InMemoryItemRepository() {
    map = new HashMap<String, Item>();

    addEmptyMetadata(mrs);
    addDefaultMetadata(mrs);

    i.setMetadataRecords(mrs);
    map.put("escidoc:test", i);
  }

  private static void addDefaultMetadata(final MetadataRecords mrs) {
    final MetadataRecord mr = new MetadataRecord("escidoc");
    try {
      mr.setContent(Utils.buildSimpleMetadata());
    } catch (final ParserConfigurationException e) {
      LOG.error("can not add metadata content:  " + e.getMessage(), e);
    }
    mrs.add(mr);
  }

  private static void addEmptyMetadata(final MetadataRecords mrs) {
    mrs.add(new MetadataRecord("empty"));
  }

  @Override
  public Item find(final String itemId) {
    return map.get(itemId);
  }
}
