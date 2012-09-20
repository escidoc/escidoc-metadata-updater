# Updating Component's Content

## Usage

$ curl -v --data-binary "@401.jpg" --request PUT --header "Content-Type:images/jpeg" \
-u"username:password" \
http://localhost:8082/rest/v0.9/items/escidoc:1/files/escidoc:2/blob\?escidocurl\=http://whvmescidev5.fiz-karlsruhe.de:8080
[[u]]

## Design

find component by id
IF NOT succesful
  return 404
ELSE
  store component in to_update_component
  upload file to escidoc staging area
  IF NOT succesful
    return error, the user should try again
  ELSE
    store stagged file URI in staged_file_uri
    replace the content in to_update_component with staged_file_uri
    update the component
    IF NOT succesful
      return error
    ELSE
      return 200

## Implementation
