<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:output encoding="utf-8" indent="yes" method="html" />
  <xsl:template match="/pubman-admin-descriptor">
    <xsl:text disable-output-escaping='yes'>&lt;!DOCTYPE html></xsl:text>
    <html>
      <head>
        <title>Pubman context Metadata Editor</title>
        <meta name="description"
          content="Editor for Pubman Context Metadata Profile" />
        <meta name="keywords"
          content="escidoc, context, pubman, metadata, metadata editor" />
        <link rel="stylesheet" type="text/css"
          href="/rest/static/css/bootstrap.min.css" />
        <link rel="stylesheet" type="text/css"
          href="/rest/static/css/override-bootstrap.css" />
        <style rel="stylesheet" type="text/css"> .alert { padding: 8px 35px
          8px 14px; margin-bottom: 18px; text-shadow: 0 1px 0 rgba(255,
          255, 255, 0.5); background-color: #FCF8E3; border: 1px solid
          #FBEED5; -webkit-border-radius: 4px; -moz-border-radius: 4px;
          border-radius: 4px; color: #C09853; }
        </style>
      </head>
      <body>
        <div class="container">
          <div class="alert fade in">
            <a class="close" data-dismiss="alert" href="#">×</a>
            <strong>Something went wrong</strong>
          </div>
          <div class="content">
            <div class="page-header">
              <h1>Metadata Editor</h1>
            </div>
            <div class="row">
              <div class="span12">
                <form id="pubman-context-metadata-editor">
                  <fieldset>
                    <legend>PubMan Context Metadata/Admin Descriptor</legend>

                    <div class="clearfix">
                      <label>Validation Schema</label>
                      <div class="input">
                        <select id="validation-schema"
                          name="validation-schema">
                          <xsl:attribute name="selected"><xsl:value-of select="validation-schema" /></xsl:attribute>
                          <option value="publication">Publication</option>
                          <option value="greymaterial">Grey Material</option>
                          <option value="jus">JUS</option>
                          <option value="externalgreymaterial">External Grey Material</option>
                          <option value="simple">Simple</option>
                          <option value="yearbook">Year Book</option>
                        </select>
                      </div>
                    </div><!-- end clearfix -->

                    <div class="clearfix">
                      <label>Contact Mail</label>
                      <div class="input">
                        <input class="xlarge" name="contact-email" type="text" >
                          <xsl:attribute name="value"><xsl:value-of select="contact-email" /></xsl:attribute>
                        </value>
                      </div>
                    </div><!-- end clearfix -->

                    <div class="actions">
                      <button onclick="getDescriptor()" class="btn primary">get XML</button>
                      <button onclick="updateDescriptor()" class="btn primary">put XML</button>
                      <button class="btn" type="reset">Cancel</button>
                    </div><!-- end actions -->
                  </fieldset>
                </form>
              </div>
            </div><!-- end row -->
          </div><!-- end content -->
        </div><!--end container -->
        <script type="text/javascript"
          src="//ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js"></script>
        <script type="text/javascript" src="/rest/static/js/app.js"></script>
        <script type="text/javascript" src="/rest/static/js/http-io.js"></script>
        <script type="text/javascript" src="/rest/static/js/bootstrap-alert.js"></script>
      </body>
    </html>
  </xsl:template>
</xsl:stylesheet>
