== eSciDoc (Item) Metadata Update RESTful Web Service ==

=== Goal ===

A client should be able to fetch and update metadata of an eSciDoc (item) resource. 

=== URI ===

GET
http://{hostname}:{port}/metadata-updater/items/{item-id}/metadata/{metadata-name}?eu={http://hostname:port}
Content-Type: application/xml, text/plain, text/html

PUT
http://{hostname}:{port}/metadata-updater/items/{item-id}/metadata/{metadata-name}?eu={http://hostname:port}
Produces: Content-Type: application/xml
Accept: Content-Type: application/xml
    
=== Protocol Interactions ===

1. The client send a HTTP GET request to fetch metadata of an item to an URI [1]
  the client modify the representation and
2. it send a HTTP PUT request to update including
    last modification date of the item:
        in header: If-Modified-Since
        in item metadata representation?
3. The server then send a HTTP GET request to eSciDoc Infrastructure in order
to fetch the item with a given id. Then:
    it checks for its last-modification-date
    if client.last-modification-date != server.last-modification-date
      Service reactions:
        a. Response Code 409/Conflict
          item has modified by other user
          server responses by listing the difference between current state of the resource and the 
            modification in its response entity. diff-like?
          the user then edit its modification and resend the request
        b. Response Code 303/See Other 
          others' modification is replaced 
          server send PUT Request to update the whole item.
            successful?
             Response Code 303/"See Other" to 
             http://{escidoc-host}:{escidoc-port}/ir/item/{item-id}/md-records/md-record/{metadata-name}
        c. combination of both
          the server offers two options to the client to either download the current version and adapt the change manually or
          directly replace the current version with her modification. [2]


=== RUN ===

How to run the service in dev. mode:
$mvn clean package jetty:run

=== TODO ===

# determine, if we should use eSciDoc client library/ijc or directly access the eSciDoc infrastructure using HTTP client.
# there are a lot of similar/copy and paste code between in memory and real server implementation.
# determine, which URI is better:
## http://{hostname}:{port}/metadata-updater/items/{item-id}/metadata/{metadata-name}?eu={http://hostname:port}
## http://{hostname}:{port}/metadata-updater?source=http://{hostname}:{port}/ir/items/{item-id}/md-records/md-record/{metadata-name}}

=== Q&A ===

Q: Does the client always have to be authorized to use the service?
A: ???

Q: How does the client authenticate and authorize herself?
A: use the cookie she gets somewhere or service offers HTTP Basic Auth.

Q: How does the service reacts, if the client fetch a resource with a version X
modifies it, authentificates and update the resource? It means the client is not
updating based on the latest resource.
A: ???

Q: What are the use cases for metadata update service?
A: ???

== eSciDoc (Item) Metadata User Interface ==

=== Approach ===

- XSLT Transformation: XML --> XUL / HTML5 / XForms
There are a couple of alternatives for building/serving the Metadata Editor UI.
1. a HTML page that GET/PUT the metadata XML and apply the XSLT on the client.
2. a HTML page that generated on the server by applying XSLT to XML.

=== Possible Outcome ===

http://dl.dropbox.com/u/419140/escidoc-metadata-form.html

=== Existing Solutions ===

1. "JAX Front, " http://www.jaxfront.org/pages/demo.html
   XSD Schema ==> HTML Form

-----
References:

[2] "A.4 Trace of Saving a Document Known to Exist - with etag mismatch, " http://www.w3.org/1999/04/Editing/#Appendix4
