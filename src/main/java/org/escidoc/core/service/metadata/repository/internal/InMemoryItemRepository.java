package org.escidoc.core.service.metadata.repository.internal;

import org.escidoc.core.service.metadata.repository.ItemRepository;

import java.util.HashMap;
import java.util.Map;

import de.escidoc.core.resources.common.MetadataRecord;
import de.escidoc.core.resources.common.MetadataRecords;
import de.escidoc.core.resources.om.item.Item;

public class InMemoryItemRepository implements ItemRepository {

  private final Map<String, Item> map;

  public InMemoryItemRepository() {
    map = new HashMap<String, Item>();
    final Item i = new Item();

    final MetadataRecords mrs = new MetadataRecords();
    final MetadataRecord mr = new MetadataRecord("escidoc");
    mrs.add(mr);
    i.setMetadataRecords(mrs);
    map.put("escidoc:test", i);
  }

  @Override
  public Item find(final String itemId) {
    return map.get(itemId);
  }
}
