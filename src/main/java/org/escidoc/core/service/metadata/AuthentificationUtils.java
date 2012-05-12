/**
 * CDDL HEADER START
 *
 * The contents of this file are subject to the terms of the
 * Common Development and Distribution License, Version 1.0 only
 * (the "License"). You may not use this file except in compliance
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
 * All rights reserved. Use is subject to license terms.
 */
package org.escidoc.core.service.metadata;

import com.google.common.base.Preconditions;

import com.sun.jersey.core.util.Base64;

import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;

import de.escidoc.core.client.Authentication;
import de.escidoc.core.client.exceptions.TransportException;
import de.escidoc.core.client.exceptions.application.security.AuthenticationException;

public class AuthentificationUtils {
    private AuthentificationUtils() {
        // Utility class
    }

    // FIXME what happens, if escidoc handle is expired? We haven't handle that case yet.
    // IMO, we should redirect the client to eSciDoc Login page.
    public static final String getHandleIfAny(
        final HttpServletRequest sr, final String escidocUri, final String encodedHandle)
        throws AuthenticationException, TransportException, MalformedURLException {
        Preconditions.checkNotNull(sr, "sr is null: %s", sr);
        Preconditions.checkNotNull(escidocUri, "escidocUri is null: %s", escidocUri);
        if (hasAuthHeader(sr)) {
            final String[] creds = sr.getHeader(AppConstant.AUTHORIZATION).split(" ");
            if (useHttpBasicAuth(creds) && notEmpty(creds)) {
                return loginToEscidoc(escidocUri, creds);
            }
        }
        else if (has(encodedHandle)) {
            return encodedHandle;
        }
        return "";
    }

    private static final boolean has(final String encodedHandle) {
        return encodedHandle != null;
    }

    private static final String decodeHandle(final String handle) {
        if (has(handle)) {
            return Base64.base64Decode(handle);
        }
        return "";
    }

    public static final boolean hasAuthHeader(final HttpServletRequest sr) {
        return sr.getHeader(AppConstant.AUTHORIZATION) != null;
    }

    public static final String loginToEscidoc(final String escidocUri, final String[] creds)
        throws AuthenticationException, TransportException, MalformedURLException {
        Preconditions.checkNotNull(escidocUri, "escidocUri is null: %s", escidocUri);
        Preconditions.checkArgument(creds.length > 0);
        return new Authentication(new URL(escidocUri), getUserName(creds), getPassword(creds)).getHandle();
    }

    private static final String getPassword(final String[] creds) {
        final String decoded = decodeHandle(creds[1]);
        final String[] arrays = decoded.split(":");
        if (arrays.length == 1) {
            return "";
        }
        return arrays[1];
    }

    private static final String getUserName(final String[] creds) {
        return decodeHandle(creds[1]).split(":")[0];
    }

    private static final boolean notEmpty(final String[] creds) {
        return decodeHandle(creds[1]).split(":").length > 0;
    }

    private static final boolean useHttpBasicAuth(final String[] creds) {
        return creds[0].contains(AppConstant.BASIC);
    }
}
