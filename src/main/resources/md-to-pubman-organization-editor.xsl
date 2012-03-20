<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
 xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
 <xsl:output encoding="utf-8" indent="yes" method="html" />
 <xsl:template match="/">
  <xsl:text disable-output-escaping='yes'>&lt;!DOCTYPE html></xsl:text>
  <html>
   <head>
    <title>Pubman Organization Metadata Editor</title>
    <meta name="description" content="Editor for Pubman Organization Metadata Profile" />
    <meta name="keywords"
     content="escidoc, organization, pubman, metadata, metadata editor" />
    <link rel="stylesheet" type="text/css"
     href="/rest/static/css/bootstrap.min.css" />
    <link rel="stylesheet" type="text/css"
     href="/rest/static/css/override-bootstrap.css" />
   </head>
   <body>
    <div class="container">
     <div class="content">
      <div class="page-header">
       <h1>Metadata Editor</h1>
      </div>
      <div class="row">
       <div class="span12">
        <form id="pubman-organization-metadata-editor">
         <fieldset>
          <legend>PubMan Organization Metadata Editor</legend>
          <div class="clearfix">
           <label for="title">Organization Name</label>
           <div class="input">
            <input class="span6" id="title" name="title" type="text"
             placeholder="Name of your organization">
            </input>
           </div>
          </div>
         </fieldset>
        </form>
       </div>
      </div><!-- end row -->
     </div><!-- end contain -->
    </div><!--/container -->
    <script type="text/javascript" src="/rest/static/js/jquery-1.7.1.min.js"></script>
    <script type="text/javascript" src="/rest/static/js/http-io.js"></script>
   </body>
  </html>
 </xsl:template>
</xsl:stylesheet>