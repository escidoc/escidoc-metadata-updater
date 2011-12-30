TODO
    if the client can accept html, it should response with XSLT-processed HTML Form
    if the server thrown Auth Exception, it should response with 303 See other => Login Form
    
== eSciDoc (Item) Metadata Update RESTful Web Service ==

=== Goal ===

A client should be able to fetch and update metadata of an eSciDoc (item) resource. 

=== URI ===

GET/PUT
http://{hostname}:{port}/metadata-updater/items/{item-id}/metadata/{metadata-name}?eu={http://hostname:port}
[1]
Accept: Content-Type: application/xml
Produces: Content-Type: application/xml
    
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

Q: Where does the client get the item metadata from? eSciDoc Browser? 
A: From the Web Service
    
Q: How does the client authentificate and authorize herself?
A: Client has to send the eSciDoc cookies/token with every request

== eSciDoc (Item) Metadata User Interface ==

=== Approach ===

- XSLT Transformation: XML --> XUL / HTML5 / XForms

=== Possible Outcome ===

http://dl.dropbox.com/u/419140/escidoc-metadata-form.html

=== Existing Solutions ===

1. "JAX Front, " http://www.jaxfront.org/pages/demo.html
   XSD Schema ==> HTML Form

-----
References:

[2] "A.4 Trace of Saving a Document Known to Exist - with etag mismatch, " http://www.w3.org/1999/04/Editing/#Appendix4
