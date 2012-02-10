# eSciDoc Sub-Resource Update Service

a RESTful Web Service for updating eSciDoc Sub Resources i.e. Metadata,
Properties, etc

## How to run 
$mvn clean jetty:run

Fetch an item metadata with the item id:{item-id} and the metadata name:{metadata-name}
$curl --user name:password -http://{service-hostname}:{portnumber}/rest/v0.9/items/{item-id}/metadata/{metadata-name}?eu=http://{escidoc-hostname:portnumber}

Update
$curl --user name:password --upload-file metadata.xml http://{service-hostname}:{portnumber}/rest/v0.9/items/{item-id}/metadata/{metadata-name}?eu=http://{escidoc-hostname:portnumber}

## Example's Use:
$curl --user admin:swordfish https://api.escidoc.org:80/rest/v0.9/items/escidoc:1/metadata/mine?eu=https://core.escidoc.org:80 > metadata.xml
$vi metadata.xml
  edit, edit, edit and save
$curl --user admin:swordfish --upload-file metadata.xml https://api.escidoc.org:80/rest/v0.9/items/escidoc:1/metadata/mine?eu=https://core.escidoc.org:80
