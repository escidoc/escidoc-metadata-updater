#eSciDoc (Item) Metadata Update Service

## Goal 

updating metadata of escidoc resource. // we will start with item

URI:
    PUT http://{hostname}:{port}/items/{item-id}/metadata/{metadata-name}
Accept: 
    Content-Type: application/xml
Produces: 
    Content-Type: application/xml
    
Flow:
    Client send update metadata of an item to an URI X,
    Server send a GET request to fetch item with a given id
        it checks for its last-modification-date
        if client.last-modification-date.milisecconds < server.last-modification-date.miliseconds
            denied? 
                item has modified by others
                send the current state of metadata
            proceed?
                others' modification is replaced 
                server send PUT Request to update the whole item.

Question:
    Where does the client get the item metadata from? eSciDoc Browser?         

