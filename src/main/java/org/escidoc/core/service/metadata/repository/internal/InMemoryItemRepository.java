package org.escidoc.core.service.metadata.repository.internal;


import org.escidoc.core.service.metadata.repository.ItemRepository;

import java.util.HashMap;
import java.util.Map;

import de.escidoc.core.resources.om.item.Item;

public class InMemoryItemRepository implements ItemRepository {

  private final Map<String, Item> map;

  public InMemoryItemRepository() {
    map = new HashMap<String, Item>();
    map.put("escidoc:test", new Item());
  }

  @Override
  public Item find(final String itemId) {
    return map.get(itemId);
  }
}
