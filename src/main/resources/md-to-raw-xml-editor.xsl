<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:output encoding="utf-8" indent="yes" method="xml" />
  <xsl:template match="*">
    <xsl:text disable-output-escaping='yes'>&lt;!DOCTYPE html></xsl:text>
    <html lang="en">
      <head>
        <title>eSciDoc Metadata Editor</title>
        <meta charset="utf-8"/>
        <meta name="description" content="eSciDoc Metadata Editor" />
        <meta name="keywords" content="escidoc, metadata editor" />

        <link rel="stylesheet" href="/rest/static/css/bootstrap.min.css" />
        <link rel="stylesheet" href="/rest/static/css/override-bootstrap.css" />

        <style>.CodeMirror {border-top: 1px solid black; border-bottom: 1px solid black;}</style>
      </head>
      <body>
        <div class="container">
          <div class="content">
            <div class="page-header">
              <h1>Metadata Editor</h1>
            </div>
            <div class="row">
              <div class="span12">
                <form id="raw-xml-metadata-editor">

                  <!-- Le twitter bootstrap alert -->
                  <div id="notification">
                    <div id="fail-message" class="alert-message error" style="display: none;">
                      <a class="close" data-dismiss="alert" href="#">×</a>
                      <p><strong>Oops!</strong> Failed to update</p>
                    </div>
                    <div id="success-message" class="alert-message success" style="display: none;">
                      <a class="close" data-dismiss="alert" href="#">×</a>
                      <p><strong>Successfully Update!</strong></p>
                    </div>
                  </div><!-- end bootstrap alert -->

                  <fieldset>
                    <legend>XML Metadata Editor</legend>
                    <div class="clearfix">
                      <label for="content">Content</label>
                      <div class="input">
                        <textarea class="span8" id="content" name="content" rows="30">
                          <xsl:copy-of select="." />
                        </textarea>
                      </div>
                    </div> <!--/clearfix -->
                    <div class="actions">
                      <input type="submit" class="btn primary" value="Save changes" ></input>
                      <xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text>
                      <button id="cancel" type="reset" class="btn">Cancel</button>
                    </div>
                  </fieldset>
                </form>
              </div>
            </div>
          </div>
          <footer></footer>
        </div><!--/container -->
        <script src="//ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.js">
          <xsl:comment>Comment added so script is recognised</xsl:comment>
        </script>
        <script src="/rest/static/js/http-io.js">
          <xsl:comment>Comment added so script is recognised</xsl:comment>
        </script>
        <script src="/rest/static/js/bootstrap-alert.js">
          <xsl:comment>Comment added so script is recognised</xsl:comment>
        </script>
      </body>
    </html>
  </xsl:template>
</xsl:stylesheet>
