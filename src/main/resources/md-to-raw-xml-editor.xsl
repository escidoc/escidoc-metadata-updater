<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
 xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
 <xsl:output encoding="utf-8" indent="yes" method="html" />
<<<<<<< HEAD
 <xsl:template match="*">
=======
 <xsl:template match="/">
>>>>>>> 8b345e9... generate better doctype
  <xsl:text disable-output-escaping='yes'>&lt;!DOCTYPE html></xsl:text>
  <html>
   <head>
    <title>eSciDoc Metadata Editor</title>
    <meta name="description" content="eSciDoc Metadata Editor" />
    <meta name="keywords" content="escidoc, metadata editor" />
<<<<<<< HEAD
    <link rel="stylesheet" type="text/css" href="/rest/static/css/bootstrap.min.css" />
    <link rel="stylesheet" type="text/css" href="/rest/static/css/override-bootstrap.css" />
=======
    <link rel="stylesheet" type="text/css"
     href="/rest/static/css/bootstrap.min.css" />
    <link rel="stylesheet" type="text/css"
     href="/rest/static/css/override-bootstrap.css" />
>>>>>>> 8b345e9... generate better doctype
   </head>
   <body>
    <div class="container">
     <div class="alert"></div>
     <div class="content">
      <div class="page-header">
       <h1>Metadata Editor</h1>
      </div>
      <div class="row">
       <div class="span12">
        <form id="metadata-editor" onSubmit="return sendRawXml();">
         <fieldset>
          <div class="clearfix">
           <label for="content">Content</label>
           <div class="input">
            <textarea class="input-xlarge" id="content" rows="10">
             <xsl:copy-of select="." />
            </textarea>
           </div>
          </div> <!--/clearfix -->
          <div class="actions">
           <input type="submit" class="btn primary" value="Save changes" />
           <xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text>
           <button type="reset" class="btn">Cancel</button>
          </div>
         </fieldset>
        </form>
       </div>
      </div>
     </div>
     <footer></footer>
    </div><!--/container -->
<<<<<<< HEAD
    <script type="text/javascript" src="//ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js"></script>
=======
>>>>>>> 8b345e9... generate better doctype
    <script type="text/javascript" src="/rest/static/js/http-io.js"></script>
   </body>
  </html>
 </xsl:template>
</xsl:stylesheet>