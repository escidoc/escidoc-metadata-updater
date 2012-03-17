<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet
  version="1.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

  <xsl:output
    encoding="iso-8859-1"
    indent="yes"
    method="html" />

  <xsl:template match="/">
    <!--TODO
         <xsl:text disable-output-escaping="yes"><!DOCTYPE html></xsl:text>
         -->
        <html>
          <head>
            <title>eSciDoc Metadata Editor</title>
            <meta name="description" content="eSciDoc Metadata Editor"/>
            <meta
              name="keywords"
              content="escidoc, metadata editor" />
            <link
              rel="stylesheet"
              type="text/css"
              href="/rest/static/css/bootstrap.min.css" />
            <link
              rel="stylesheet"
              type="text/css"
              href="/rest/static/css/override-bootstrap.css" />
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
                              <xsl:copy-of select="."/>
                            </textarea>
                          </div>
                        </div> <!--/clearfix-->
                        <div class="actions">
                          <input
                            type="submit"
                            class="btn primary"
                            value="Save changes" />
                          <xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text>
                          <button
                            type="reset"
                            class="btn">Cancel</button>
                        </div>
                      </fieldset>
                    </form>
                  </div>
                </div>
              </div>
              <footer></footer>
            </div><!--/container-->
            <script type="text/javascript" src="/rest/static/js/http-io.js"></script>
          </body>
        </html>
      </xsl:template>
    </xsl:stylesheet>
