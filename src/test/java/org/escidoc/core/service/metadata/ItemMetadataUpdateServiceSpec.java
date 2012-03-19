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

public interface ItemMetadataUpdateServiceSpec {

  void shouldReturn200forHelloWorld() throws Exception;

  void shouldReturn404ForNonExistingItem() throws Exception;

  void shouldReturn404ForNonExistingMetadata() throws Exception;

  void shouldReturn200ForExistingItemAndMetadata() throws Exception;

  void shouldReturn204ForMetadataWithNoContent() throws Exception;

  void shouldReturn400ForMissingServerParameter() throws Exception;

  void shouldReturnXmlForMetadata() throws Exception;

  void shouldUpdateEscidocMetadata() throws Exception;

  void shouldNotUpdateMetadataIfInConfict() throws Exception;

  void shouldReturn200WhenTryingToFetchUnreleasedItemGivenAValidToken() throws Exception;

  void shouldReturn200WhenTryingToUpdateMetadataGivenValidToken() throws Exception;

  void shouldReturn200WhenTryingToUpdateMetadataGivenValidHandleInUriParam() throws Exception;

  void shouldReturn200forHelloWorldXml() throws Exception;

  void shouldReturn401WhenNoValidCookie() throws Exception;

  void shouldReturn200WhenTryingToAccessProctedResourceGivenBasicAuth() throws Exception;
}