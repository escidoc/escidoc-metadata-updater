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
package org.escidoc.core.service.metadata.internal;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource.Builder;

import static org.junit.Assert.assertEquals;

import org.escidoc.core.service.metadata.AppConstant;
import org.escidoc.core.service.metadata.OrgUnitMetadataUpdateServiceSpec;
import org.junit.After;
import org.junit.Test;

import javax.ws.rs.core.MediaType;

public class OrgUnitMetadataFunctionalTest extends Base implements OrgUnitMetadataUpdateServiceSpec {

    private static final String METADATA = "metadata";

    private static final String ORGANIZATIONS = "organizations";

    private static final String ORG_UNIT_ID = "escidoc:1507";

    @After
    public void stop() throws Exception {
        server.stop();
    }

    @Test
    @Override
    public void shouldReturn200ForExistingOrgUnitAndMetadata() throws Exception {
        // @formatter:off
    final Builder builder = resource
        .path(ORGANIZATIONS)
        .path(ORG_UNIT_ID)
        .path(METADATA)
        .path(EXISTING_METADATA_NAME)
        .queryParam(AppConstant.EU, SERVICE_URL)
        .accept(MediaType.APPLICATION_XML);
    assertEquals("response is not equals", 200, builder.get(ClientResponse.class).getStatus());
  }
}