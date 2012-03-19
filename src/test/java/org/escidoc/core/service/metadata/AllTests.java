/**
 * CDDL HEADER START
 *
 * The contents of this file are subject to the terms of the
 * Common Development and Distribution License, Version 1.0 only
 * (the "License").  You may not use this file except in compliance
 * with the License.
 *
 * You can obtain a copy of the license at license/ESCIDOC.LICENSE
 * or https://www.escidoc.org/license/ESCIDOC.LICENSE .
 * See the License for the specific language governing permissions
 * and limitations under the License.
 *
 * When distributing Covered Code, include this CDDL HEADER in each
 * file and include the License file at license/ESCIDOC.LICENSE.
 * If applicable, add the following below this CDDL HEADER, with the
 * fields enclosed by brackets "[]" replaced with your own identifying
 * information: Portions Copyright [yyyy] [name of copyright owner]
 *
 * CDDL HEADER END
 *
 *
 *
 * Copyright 2012 Fachinformationszentrum Karlsruhe Gesellschaft
 * fuer wissenschaftlich-technische Information mbH and Max-Planck-
 * Gesellschaft zur Foerderung der Wissenschaft e.V.
 * All rights reserved.  Use is subject to license terms.
 */
package org.escidoc.core.service.metadata;

import org.escidoc.core.service.metadata.internal.ContextMetadataFunctionalTest;
import org.escidoc.core.service.metadata.internal.ItemInMemoryTest;
import org.escidoc.core.service.metadata.internal.ItemMetadataFunctionalTest;
import org.escidoc.core.service.metadata.internal.OrgUnitMetadataFunctionalTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
// @formatter:off
@Suite.SuiteClasses({ItemInMemoryTest.class, 
                     ItemMetadataFunctionalTest.class,
                     OrgUnitMetadataFunctionalTest.class,
                     ContextMetadataFunctionalTest.class
                     })
 // @formatter:on
public class AllTests {
  // running all tests
}
