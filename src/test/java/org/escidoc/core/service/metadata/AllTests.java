package org.escidoc.core.service.metadata;

import org.escidoc.core.service.metadata.internal.ItemMetadataFunctionalTest;
import org.escidoc.core.service.metadata.internal.InMemory;
import org.escidoc.core.service.metadata.internal.OrgUnitMetadataFunctionalTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
// @formatter:off
@Suite.SuiteClasses({InMemory.class, 
                     ItemMetadataFunctionalTest.class,
                     OrgUnitMetadataFunctionalTest.class})
 // @formatter:on
public class AllTests {
  // running all tests
}
