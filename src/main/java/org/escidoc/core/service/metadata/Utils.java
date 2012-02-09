package org.escidoc.core.service.metadata;

import com.google.common.base.Preconditions;

import com.sun.jersey.core.util.Base64;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.InputStream;
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

public class Utils {

  public static Element buildSimpleMetadata() throws ParserConfigurationException {
    final Document doc = createNewDocument();
    final Element e = doc.createElementNS(AppConstant.DC_URI, "dc");
    final Element t = doc.createElementNS(AppConstant.DC_URI, "title");
    t.setPrefix("dc");
    t.setTextContent("test title");
    e.appendChild(t);
    return e;
  }

  public static Document createNewDocument() throws ParserConfigurationException {
    return DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
  }

  public static String asString(final MetadataRecord mr) {
    final Element node = mr.getContent();
    try {
      final Transformer transformer = TransformerFactory.newInstance().newTransformer();
      final StringWriter sw = new StringWriter();
      transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
      transformer.transform(new DOMSource(node), new StreamResult(sw));
      return sw.toString();
    } catch (final TransformerConfigurationException e) {
      throw new WebApplicationException(500);
    } catch (final TransformerException e) {
      throw new WebApplicationException(500);
    }
  }

  // extracted from ItemMetadataResource
  public static String transformToHtml(final MetadataRecord mr) {
    try {
      final StringWriter s = new StringWriter();
      TransformerFactory.newInstance().newTransformer(new StreamSource(readXsl())).transform(
          new DOMSource(mr.getContent()), new StreamResult(s));
      return s.toString();
    } catch (final TransformerConfigurationException e) {
      throw new WebApplicationException(e, Status.INTERNAL_SERVER_ERROR);
    } catch (final TransformerException e) {
      throw new WebApplicationException(e, Status.INTERNAL_SERVER_ERROR);
    } catch (final TransformerFactoryConfigurationError e) {
      throw new WebApplicationException(e, Status.INTERNAL_SERVER_ERROR);
    }
  }

  public static String loginToEscidoc(final String escidocUri, final String[] creds) throws AuthenticationException,
      TransportException, MalformedURLException {
    return new Authentication(new URL(escidocUri), getUserName(creds), getPassword(creds)).getHandle();
  }

  public static String getPassword(final String[] creds) {
    return decodeHandle(creds[1]).split(":")[1];
  }

  public static String getUserName(final String[] creds) {
    return decodeHandle(creds[1]).split(":")[0];
  }

  public static boolean notEmpty(final String[] creds) {
    return decodeHandle(creds[1]).split(":").length > 0;
  }

  public static boolean useHttpBasicAuth(final String[] creds) {
    return creds[0].contains(AppConstant.BASIC);
  }

  public static boolean has(final String encodedHandle) {
    return encodedHandle != null;
  }

  public static String computeDigest(final byte[] content) {
    try {
      final MessageDigest md = MessageDigest.getInstance("SHA");
      final byte[] digest = md.digest(content);
      final BigInteger bi = new BigInteger(digest);
      return bi.toString(16);
    } catch (final Exception e) {
      return "";
    }
  }

  public static InputStream readXsl() {
    return Thread.currentThread().getContextClassLoader().getResourceAsStream(AppConstant.XSLT_FILE);
  }

  public static Response response401() {
    // @formatter:off
    return Response
        .status(Status.UNAUTHORIZED)
        .header("WWW-Authenticate", "Basic realm=\"" + AppConstant.BASIC_REALM + "\"")
        .type("text/plain")
        .entity("Authentification credentials are required")
        .build();
     // @formatter:on
  }

  public static String decodeHandle(final String handle) {
    if (has(handle)) {
      return Base64.base64Decode(handle);
    }
    return "";
  }

  public static void checkPreconditions(final String id, final String metadataName, final String escidocUri,
      final HttpServletRequest request) {
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

  public static Date getLastModificationDate(final GenericResource r) {
    return r.getLastModificationDate().toDate();
  }

  public static String getHandleIfAny(final HttpServletRequest sr, final String escidocUri, final String encodedHandle)
      throws AuthenticationException, TransportException, MalformedURLException {
    if (has(encodedHandle)) {
      return decodeHandle(encodedHandle);
    } else if (hasAuthHeader(sr)) {
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

}
