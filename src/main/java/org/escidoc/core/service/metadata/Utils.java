/**
 * CDDL HEADER START
 * 
 * The contents of this file are subject to the terms of the Common Development
 * and Distribution License, Version 1.0 only (the "License"). You may not use
 * this file except in compliance with the License.
 * 
 * You can obtain a copy of the license at license/ESCIDOC.LICENSE or
 * https://www.escidoc.org/license/ESCIDOC.LICENSE . See the License for the
 * specific language governing permissions and limitations under the License.
 * 
 * When distributing Covered Code, include this CDDL HEADER in each file and
 * include the License file at license/ESCIDOC.LICENSE. If applicable, add the
 * following below this CDDL HEADER, with the fields enclosed by brackets "[]"
 * replaced with your own identifying information: Portions Copyright [yyyy]
 * [name of copyright owner]
 * 
 * CDDL HEADER END
 * 
 * 
 * 
 * Copyright 2012 Fachinformationszentrum Karlsruhe Gesellschaft fuer
 * wissenschaftlich-technische Information mbH and Max-Planck- Gesellschaft zur
 * Foerderung der Wissenschaft e.V. All rights reserved. Use is subject to
 * license terms.
 */
package org.escidoc.core.service.metadata;

import com.google.common.base.Preconditions;

import com.sun.jersey.core.util.Base64;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import de.escidoc.core.client.Authentication;
import de.escidoc.core.client.exceptions.TransportException;
import de.escidoc.core.client.exceptions.application.security.AuthenticationException;
import de.escidoc.core.resources.GenericResource;
import de.escidoc.core.resources.common.MetadataRecord;
import de.escidoc.core.resources.om.context.AdminDescriptor;

public final class Utils {

    private final static Logger LOG = LoggerFactory.getLogger(Utils.class);

    private Utils() {
        // Utility classes
    }

    public static Element buildSimpleMetadata() throws ParserConfigurationException {
        final Document doc = createNewDocument();
        final Element dc = doc.createElementNS(AppConstant.DC_URI, "dc");
        final Element titleElement = doc.createElementNS(AppConstant.DC_URI, "title");
        titleElement.setPrefix("dc");
        titleElement.setTextContent("test title");
        dc.appendChild(titleElement);
        return dc;
    }

    public static Document createNewDocument() throws ParserConfigurationException {
        return DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
    }

    public static String asString(final MetadataRecord metadata) {
        final Element node = metadata.getContent();
        try {
            final Transformer transformer = TransformerFactory.newInstance().newTransformer();
            final StringWriter sw = new StringWriter();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            transformer.transform(new DOMSource(node), new StreamResult(sw));
            return sw.toString();
        }
        catch (final TransformerConfigurationException e) {
            throw new WebApplicationException(500);
        }
        catch (final TransformerException e) {
            throw new WebApplicationException(500);
        }
    }

    // }
    public static String loginToEscidoc(final String escidocUri, final String[] creds) throws AuthenticationException,
        TransportException, MalformedURLException {
        Preconditions.checkNotNull(escidocUri, "escidocUri is null: %s", escidocUri);
        Preconditions.checkArgument(creds.length > 0);
        return new Authentication(new URL(escidocUri), getUserName(creds), getPassword(creds)).getHandle();
    }

    private static String getPassword(final String[] creds) {
        final String decoded = decodeHandle(creds[1]);
        final String[] arrays = decoded.split(":");
        if (arrays.length == 1) {
            return "";
        }
        return arrays[1];
    }

    private static String getUserName(final String[] creds) {
        return decodeHandle(creds[1]).split(":")[0];
    }

    private static boolean notEmpty(final String[] creds) {
        return decodeHandle(creds[1]).split(":").length > 0;
    }

    private static boolean useHttpBasicAuth(final String[] creds) {
        return creds[0].contains(AppConstant.BASIC);
    }

    private static boolean has(final String encodedHandle) {
        return encodedHandle != null;
    }

    public static String computeDigest(final byte[] content) {
        try {
            final MessageDigest md = MessageDigest.getInstance("SHA");
            final byte[] digest = md.digest(content);
            return new BigInteger(digest).toString(16);
        }
        catch (final Exception e) {
            return "";
        }
    }

    private static InputStream readXsl(final String xsltFile) {
        return Thread.currentThread().getContextClassLoader().getResourceAsStream(xsltFile);
    }

    public static Response response401() {
        // @formatter:off
        return Response
            .status(Status.UNAUTHORIZED)
            .header("WWW-Authenticate",
            "Basic realm=\"" + AppConstant.BASIC_REALM + "\"")
            .type("text/plain")
            .entity("Authentification credentials are required")
            .build();
        // @formatter:on
    }

    private static String decodeHandle(final String handle) {
        if (has(handle)) {
            return Base64.base64Decode(handle);
        }
        return "";
    }

    public static void checkPreconditions(
        final String id, final String metadataName, final String escidocUri, final HttpServletRequest request) {
        Preconditions.checkNotNull(id, "id is null: %s", id);
        Preconditions.checkNotNull(metadataName, "m is null: %s", metadataName);
        Preconditions.checkNotNull(request, "request is null: %s", request);
        checkQueryParameter(escidocUri);
    }

    public static void checkQueryParameter(final String escidocUri) {
        if (escidocUri == null || escidocUri.isEmpty()) {
            throw new WebApplicationException(Status.BAD_REQUEST);
        }
    }

    public static EntityTag getEntityTag(final String mr) {
        return new EntityTag(computeDigest(mr.getBytes()));
    }

    public static EntityTag getEntityTag(final MetadataRecord mr) {
        return getEntityTag(mr.toString());
    }

    public static EntityTag getEntityTag(final AdminDescriptor mr) {
        return getEntityTag(mr.toString());
    }

    public static Date getLastModificationDate(final GenericResource r) {
        return r.getLastModificationDate().toDate();
    }

    public static String getHandleIfAny(final HttpServletRequest sr, final String escidocUri, final String encodedHandle)
        throws AuthenticationException, TransportException, MalformedURLException {
        if (has(encodedHandle)) {
            return decodeHandle(encodedHandle);
        }
        else if (hasAuthHeader(sr)) {
            final String[] creds = sr.getHeader(AppConstant.AUTHORIZATION).split(" ");
            if (useHttpBasicAuth(creds) && notEmpty(creds)) {
                return loginToEscidoc(escidocUri, creds);
            }
        }
        return "";
    }

    public static boolean hasAuthHeader(final HttpServletRequest sr) {
        return sr.getHeader(AppConstant.AUTHORIZATION) != null;
    }

    public static void transformXml(final MetadataRecord mr, final String xsltFile, final StringWriter writer) {
        try {
            final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            dbf.setValidating(false);
            final DocumentBuilder docBuilder = dbf.newDocumentBuilder();

            DEBUG(xsltFile);

            final InputStream stream = Utils.readXsl(xsltFile);
            Document xsltDoc = docBuilder.parse(stream);

            final Transformer transformer = TransformerFactory.newInstance().newTransformer(new DOMSource(xsltDoc));
            LOG.debug("transform class is: " + transformer.getClass().getCanonicalName());

            transformer.transform(new DOMSource(mr.getContent()), new StreamResult(writer));
        }
        catch (final TransformerConfigurationException e) {
            throw new WebApplicationException(e, Status.INTERNAL_SERVER_ERROR);
        }
        catch (final TransformerException e) {
            throw new WebApplicationException(e, Status.INTERNAL_SERVER_ERROR);
        }
        catch (final TransformerFactoryConfigurationError e) {
            throw new WebApplicationException(e, Status.INTERNAL_SERVER_ERROR);
        }
        catch (final IOException e) {
            throw new WebApplicationException(e, Status.INTERNAL_SERVER_ERROR);
        }
        catch (final ParserConfigurationException e) {
            throw new WebApplicationException(e, Status.INTERNAL_SERVER_ERROR);
        }
        catch (SAXException e) {
            throw new WebApplicationException(e, Status.INTERNAL_SERVER_ERROR);
        }
    }

    private static void DEBUG(final String xsltFile) throws IOException {
        LOG.debug("Loading XSLT file: " + xsltFile);
        final String value = readAsString(xsltFile);
        LOG.debug("as Stream is: " + value);
    }

    private static String readAsString(final String xsltFile) throws IOException {
        final InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(xsltFile);
        final char[] buffer = new char[0x10000];
        final StringBuilder out = new StringBuilder();
        final Reader in = new InputStreamReader(is, "UTF-8");
        int read;
        do {
            read = in.read(buffer, 0, buffer.length);
            if (read > 0) {
                out.append(buffer, 0, read);
            }
        }
        while (read >= 0);
        final String result = out.toString();
        return result;

    }

    public static void transformXml(final AdminDescriptor mr, final String xsltFile, final StringWriter s) {
        try {
            TransformerFactory
                .newInstance().newTransformer(new StreamSource(Utils.readXsl(xsltFile)))
                .transform(new DOMSource(mr.getContent()), new StreamResult(s));
        }
        catch (final TransformerConfigurationException e) {
            throw new WebApplicationException(e, Status.INTERNAL_SERVER_ERROR);
        }
        catch (final TransformerException e) {
            throw new WebApplicationException(e, Status.INTERNAL_SERVER_ERROR);
        }
        catch (final TransformerFactoryConfigurationError e) {
            throw new WebApplicationException(e, Status.INTERNAL_SERVER_ERROR);
        }
    }

    public static void buildRawXmlEditor(final MetadataRecord md, final StringWriter writer) {
        transformXml(md, AppConstant.XML_TO_RAW_EDITOR_XSLT, writer);
    }

    public static void buildRawXmlEditor(final AdminDescriptor md, final StringWriter writer) {
        transformXml(md, AppConstant.XML_TO_RAW_EDITOR_XSLT, writer);
    }

    public static void buildPubmanOrganizationEditor(final MetadataRecord md, final StringWriter writer) {
        transformXml(md, AppConstant.XML_TO_PUBMAN_ORGANIZATION_EDITOR_XSLT, writer);
    }

    public static void buildPubmanContextMetadataEditor(final AdminDescriptor metadata, final StringWriter writer) {
        // FIXME replace with the real xslt
        transformXml(metadata, AppConstant.XML_TO_RAW_EDITOR_XSLT, writer);
    }
}
