[eSciDoc Sub Resource Update Service](https://github.com/escidoc/escidoc-metadata-updater)
====================================

## Description

a RESTful Web Service for retrieving and updating eSciDoc[https://escidoc.org] Sub 
Resources i.e. Metadata, Properties, Files, etc

## Quick Start

Clone the repo, `git clone git@github.com:escidoc/https://github.com/escidoc/escidoc-metadata-updater.git ,
$mvn clean jetty:run

or [download the latest release](https://github.com/downloads/escidoc/escidoc-metadata-updater/rest.war)
and put the war in your favorite Java Web Container, e.g., Tomcat, etc

## USAGE

+ **Retrieve**

Retrieve an item's metadata with the item id:{item-id} and the metadata name:{metadata-name}

$`curl` --user name:password -http://{service-hostname}:{portnumber}/rest/v0.9/items/{item-id}/metadata/{metadata-name}?escidocurl=http://{escidoc-hostname:portnumber}

+ **Update**

$`curl` --user name:password --upload-file metadata.xml http://{service-hostname}:{portnumber}/rest/v0.9/items/{item-id}/metadata/{metadata-name}?escidocurl=http://{escidoc-hostname:portnumber}

## Example's Use:

+ $`curl` --user admin:swordfish https://api.escidoc.org:80/rest/v0.9/items/escidoc:1/metadata/mine?escidocurl=https://core.escidoc.org:80 > metadata.xml

+ $`vi` metadata.xml
  edit, edit, edit and save

+ $`curl` --user admin:swordfish --upload-file metadata.xml https://api.escidoc.org:80/rest/v0.9/items/escidoc:1/metadata/mine?escidocurl=https://core.escidoc.org:80


Bug tracker
-----------

Have a bug? Please create an issue on eSciDoc JIRA!

https://www.escidoc.org/jira/browse/GENCLIENT
Component: MD Update Service

Authors
-------

**Frank Schwichtenberg**

+ https://github.com/fschwic

**Christian Herlambang**

+ https://github.com/crh

**Christian Steiger**

+ https://github.com/cst1977

Copyright and license
---------------------

Copyright 2012 Fachinformationszentrum Karlsruhe Gesellschaft
fuer wissenschaftlich-technische Information mbH and Max-Planck-
Gesellschaft zur Foerderung der Wissenschaft e.V.
All rights reserved.  Use is subject to license terms.

You can obtain a copy of the license at license/ESCIDOC.LICENSE
or https://www.escidoc.org/license/ESCIDOC.LICENSE .
See the License for the specific language governing permissions
limitations under the License.

When distributing Covered Code, include this CDDL HEADER in each
file and include the License file at license/ESCIDOC.LICENSE.
If applicable, add the following below this CDDL HEADER, with the
fields enclosed by brackets "[]" replaced with your own identifying
information: Portions Copyright [yyyy] [name of copyright owner]
