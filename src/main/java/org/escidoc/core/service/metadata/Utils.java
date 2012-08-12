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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.math.BigInteger;
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

import de.escidoc.core.resources.GenericResource;
import de.escidoc.core.resources.common.MetadataRecord;
import de.escidoc.core.resources.om.context.AdminDescriptor;

//FIXME this is a class that collects all static final methods. Some of the methods
// does not belong here. We need to refactor this class.
public final class Utils {

    private static final Logger LOG = LoggerFactory.getLogger(Utils.class);

    private Utils() {
        // Utility classes
    }

    public static final Element buildSimpleMetadata() throws ParserConfigurationException {
        final Document doc = createNewDocument();
        final Element dc = doc.createElementNS(AppConstant.DC_URI, "dc");
        final Element titleElement = doc.createElementNS(AppConstant.DC_URI, "title");
        titleElement.setPrefix("dc");
        titleElement.setTextContent("test title");
        dc.appendChild(titleElement);
        return dc;
    }

    public static final Document createNewDocument() throws ParserConfigurationException {
        return DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
    }

    public static final String asString(final MetadataRecord metadata) {
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

    public static final String computeDigest(final byte[] content) {
        try {
            final MessageDigest md = MessageDigest.getInstance("SHA");
            final byte[] digest = md.digest(content);
            return new BigInteger(digest).toString(16);
        }
        catch (final Exception e) {
            return "";
        }
    }

    private static final InputStream readXsl(final String xsltFile) {
        return Thread.currentThread().getContextClassLoader().getResourceAsStream(xsltFile);
    }

    public static final Response response401() {
        // @formatter:off
        return Response
            .status(Status.UNAUTHORIZED)
            .header("WWW-Authenticate",
            "Basic realm=\"" + AppConstant.BASIC_REALM + "\"")
            .type("text/plain")
            .entity("Authentification credentials were missing or incorrect.")
            .build();
        // @formatter:on
    }

    public static final void checkPreconditions(
        final String id, final String metadataName, final String escidocUri, final HttpServletRequest request) {
        Preconditions.checkNotNull(id, "id is null: %s", id);
        Preconditions.checkNotNull(metadataName, "m is null: %s", metadataName);
        Preconditions.checkNotNull(request, "request is null: %s", request);
        checkQueryParameter(escidocUri);
    }

    public static final void checkQueryParameter(final String escidocUri) {
        if (escidocUri == null || escidocUri.isEmpty()) {
            throw new WebApplicationException(Status.BAD_REQUEST);
        }
    }

    public static final EntityTag getEntityTag(final String mr) {
        return new EntityTag(computeDigest(mr.getBytes()));
    }

    public static final EntityTag getEntityTag(final MetadataRecord mr) {
        return getEntityTag(mr.toString());
    }

    public static final EntityTag getEntityTag(final AdminDescriptor mr) {
        return getEntityTag(mr.toString());
    }

    public static final Date getLastModificationDate(final GenericResource r) {
        return r.getLastModificationDate().toDate();
    }

    public static final void transformXml(final MetadataRecord mr, final String xsltFile, final StringWriter writer) {
        try {
            final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            dbf.setValidating(false);
            final DocumentBuilder docBuilder = dbf.newDocumentBuilder();

            final InputStream stream = Utils.readXsl(xsltFile);
            final Document xsltDoc = docBuilder.parse(stream);

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
        catch (final SAXException e) {
            throw new WebApplicationException(e, Status.INTERNAL_SERVER_ERROR);
        }
    }

    public static final void transformXml(final AdminDescriptor mr, final String xsltFile, final StringWriter writer) {
        try {
            final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            dbf.setValidating(false);
            final DocumentBuilder docBuilder = dbf.newDocumentBuilder();

            final InputStream stream = Utils.readXsl(xsltFile);
            final Document xsltDoc = docBuilder.parse(stream);

            final Transformer transformer = TransformerFactory.newInstance().newTransformer(new DOMSource(xsltDoc));
            LOG.debug("transform class is: " + transformer.getClass().getCanonicalName());

            transformer.transform(new DOMSource(mr.getContent()), new StreamResult(writer));
        }
        catch (final TransformerConfigurationException e) {
            LOG.error("An error occurs while transforming the XML to HTML. Message: " + e.getMessage());
            throw new WebApplicationException(e, Status.INTERNAL_SERVER_ERROR);
        }
        catch (final TransformerException e) {
            LOG.error("An error occurs while transforming the XML to HTML. Message: " + e.getMessage());
            throw new WebApplicationException(e, Status.INTERNAL_SERVER_ERROR);
        }
        catch (final TransformerFactoryConfigurationError e) {
            LOG.error("An error occurs while transforming the XML to HTML. Message: " + e.getMessage());
            throw new WebApplicationException(e, Status.INTERNAL_SERVER_ERROR);
        }
        catch (final IOException e) {
            LOG.error("An error occurs while transforming the XML to HTML. Message: " + e.getMessage());
            throw new WebApplicationException(e, Status.INTERNAL_SERVER_ERROR);
        }
        catch (final ParserConfigurationException e) {
            LOG.error("An error occurs while transforming the XML to HTML. Message: " + e.getMessage());
            throw new WebApplicationException(e, Status.INTERNAL_SERVER_ERROR);
        }
        catch (final SAXException e) {
            LOG.error("An error occurs while transforming the XML to HTML. Message: " + e.getMessage());
            throw new WebApplicationException(e, Status.INTERNAL_SERVER_ERROR);
        }
    }

    public static final void buildRawXmlEditor(final MetadataRecord md, final StringWriter writer) {
        transformXml(md, AppConstant.XML_TO_RAW_EDITOR_XSLT, writer);
    }

    public static final void buildRawXmlEditor(final AdminDescriptor md, final StringWriter writer) {
        transformXml(md, AppConstant.XML_TO_RAW_EDITOR_XSLT, writer);
    }

    public static final void buildPubmanOrganizationEditor(final MetadataRecord md, final StringWriter writer) {
        transformXml(md, AppConstant.XML_TO_PUBMAN_ORGANIZATION_MD_EDITOR_XSLT, writer);
    }

    public static final void buildPubmanContextMetadataEditor(final AdminDescriptor metadata, final StringWriter writer) {
        transformXml(metadata, AppConstant.XML_TO_PUBMAN_CONTEXT_MD_EDITOR_XSLT, writer);
    }

    public static String transformToHtml(final MetadataRecord metadata) {
        try {
            final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            dbf.setValidating(false);
            final DocumentBuilder docBuilder = dbf.newDocumentBuilder();

            final InputStream stream = Utils.readXsl(AppConstant.XML_TO_RAW_EDITOR_XSLT);
            final Document xsltDoc = docBuilder.parse(stream);

            final Transformer transformer = TransformerFactory.newInstance().newTransformer(new DOMSource(xsltDoc));
            LOG.debug("transform class is: " + transformer.getClass().getCanonicalName());

            final StringWriter writer = new StringWriter();
            transformer.transform(new DOMSource(metadata.getContent()), new StreamResult(writer));
            return writer.toString();
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
        catch (final SAXException e) {
            throw new WebApplicationException(e, Status.INTERNAL_SERVER_ERROR);
        }
    }
}