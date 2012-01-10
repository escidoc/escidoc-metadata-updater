package org.escidoc.core.service.metadata;

import org.escidoc.core.service.metadata.internal.FunctionalTest;
import org.escidoc.core.service.metadata.internal.InMemory;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({InMemory.class, FunctionalTest.class})
public class AllTests {
  // running all tests
}
