package org.escidoc.core.service.metadata.resources;

import com.google.common.base.Preconditions;

import org.escidoc.core.service.metadata.repository.ItemRepository;
import org.escidoc.core.service.metadata.repository.internal.InMemoryItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;

import java.io.StringWriter;

import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;
import de.escidoc.core.resources.common.MetadataRecord;
import de.escidoc.core.resources.common.MetadataRecords;
import de.escidoc.core.resources.om.item.Item;

@Path("items/{item-id}/metadata/{metadata-name}")
public class ItemMetadataResource {

  private final static Logger LOG = LoggerFactory.getLogger(ItemMetadataResource.class);
  private final ItemRepository ir = new InMemoryItemRepository();

  @GET
  @Produces("text/plain")
  public String getAsText(@PathParam("item-id") final String itemId,
      @PathParam("metadata-name") final String metadataName,
      @QueryParam("eu") final String escidocUri) {
    Preconditions.checkNotNull(itemId, "itemId is null: %s", itemId);
    Preconditions.checkNotNull(metadataName, "m is null: %s", metadataName);

    final String msg = "Get a request for item with the id: " + itemId + " metadata name: "
        + metadataName + ", server uri: " + escidocUri;
    LOG.debug(msg);
    if (escidocUri == null || escidocUri.isEmpty()) {
      throw new WebApplicationException(400);
    }

    final Item item = fetchItem(itemId);
    if (item == null) {
      throw new WebApplicationException(404);
    }

    final MetadataRecords mrList = item.getMetadataRecords();
    if (mrList == null || mrList.isEmpty()) {
      throw new WebApplicationException(404);
    }

    final MetadataRecord mr = mrList.get(metadataName);
    if (mr == null) {
      throw new WebApplicationException(404);
    }

    final String asString = asString(metadataName, mr);
    // FIXME:!!!
    if (asString.isEmpty()) {
      return Response.status(Status.NO_CONTENT).build().toString();
    }
    return asString;
  }

  private static String asString(final String name, final MetadataRecord mr) {
    final Element node = mr.getContent();
    final TransformerFactory transFactory = TransformerFactory.newInstance();
    Transformer transformer;
    try {
      transformer = transFactory.newTransformer();
      final StringWriter buffer = new StringWriter();
      transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
      transformer.transform(new DOMSource(node), new StreamResult(buffer));
      final String str = buffer.toString();
      LOG.debug("The content of metadata with the name " + name + " is: " + str);
      return str;
    } catch (final TransformerConfigurationException e) {
      throw new WebApplicationException(500);
    } catch (final TransformerException e) {
      throw new WebApplicationException(500);
    }
  }

  private Item fetchItem(final String itemId) {
    try {
      return ir.find(itemId);
    } catch (final EscidocException e) {
      throw new WebApplicationException(404);
    } catch (final InternalClientException e) {
      throw new WebApplicationException(404);
    } catch (final TransportException e) {
      throw new WebApplicationException(404);
    }
  }

  @PUT
  @Produces("application/xml")
  public String update() {
    throw new WebApplicationException(500);
  }
}