<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
  xmlns:mdou="http://purl.org/escidoc/metadata/profiles/0.1/organizationalunit"
  xmlns:dc="http://purl.org/dc/elements/1.1/" xmlns:dcterms="http://purl.org/dc/terms/"
  xmlns:eterms="http://purl.org/escidoc/metadata/terms/0.1/" xmlns:kml="http://www.opengis.net/kml/2.2"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:output encoding="utf-8" indent="yes" method="html" />
  <xsl:template match="mdou:organizational-unit">
    <xsl:text disable-output-escaping='yes'>&lt;!DOCTYPE html></xsl:text>
    <html>
      <head>
        <title>Pubman Organization Metadata Editor</title>
        <meta name="description"
          content="Editor for Pubman Organization Metadata Profile" />
        <meta name="keywords"
          content="escidoc, organization, pubman, metadata, metadata editor" />
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
                <form id="pubman-organization-metadata-editor">
                  <fieldset>
                    <legend>PubMan Organization Metadata</legend>
                    <div class="clearfix">
                      <label for="title">Organization Name</label>
                      <div class="input">
                        <input class="span6" id="title" name="title"
                          type="text" required="required" autofocus="autofocus"
                          placeholder="Name of your organization">
                          <xsl:attribute name="value"><xsl:value-of
                              select="dc:title" /></xsl:attribute>
                        </input>
                      </div>
                    </div>
                    <div class="clearfix">
                      <label for="alternative">Organization Alternative Name
                      </label>
                      <div class="input">
                        <input class="span6" id="alternative"
                          name="alternative" type="text" size="45"
                          placeholder="Alternative name of your organization">
                          <xsl:attribute name="value"><xsl:value-of
                              select="dcterms:alternative" />
                          </xsl:attribute>
                        </input>
                      </div>
                    </div>
                    <div class="clearfix">
                      <label for="type">Organization Type</label>
                      <div class="input">
                        <input class="span6" id="type"
                          name="organization-type" type="text" size="45"
                          placeholder="Type such as: Department, Institute, Project, Group, etc">
                          <xsl:attribute name="value"><xsl:value-of
                              select="eterms:organization-type" />
                          </xsl:attribute>
                        </input>
                      </div>
                    </div>
                    <div class="clearfix">
                      <label for="identifier">Identifier</label>
                      <div class="input">
                        <input class="span6" id="type" name="identifier"
                          type="text" size="45" placeholder="pubman:ext">
                          <xsl:attribute name="value"><xsl:value-of
                              select="dc:identifier" /></xsl:attribute>
                        </input>
                      </div>
                    </div>
                    <div class="clearfix">
                      <label for="description">Short Description</label>
                      <div class="input">
                        <textarea class="span6" id="description"
                          name="description" rows="5"
                          placeholder="Short description about your organization">
                          <xsl:value-of select="dc:description" />
                        </textarea>
                      </div>
                    </div>
                    <div class="clearfix">
                      <label for="city">City</label>
                      <div class="input">
                        <input required="required" class="span6"
                          id="city" name="city" type="text" placeholder="Munich">
                          <xsl:attribute name="value"><xsl:value-of
                              select="eterms:city" />
                          </xsl:attribute>
                        </input>
                      </div>
                    </div>
                    <div class="clearfix">
                      <label for="country">Country</label>
                      <div class="input">
                        <input required="required" class="span6"
                          id="country" name="country" type="text"
                          placeholder="Germany">
                          <xsl:attribute name="value"><xsl:value-of
                              select="eterms:country" />
                          </xsl:attribute>
                        </input>
                      </div>
                    </div>
                    <div class="clearfix">
                      <label for="coordinates">Geographical Location</label>
                      <div class="input">
                        <input class="span6" id="coordinates"
                          name="coordinates" type="text"
                          placeholder="48°21′14″N 011°47′10″E">
                          <xsl:attribute name="value"><xsl:value-of
                              select="kml:coordinates" />
                          </xsl:attribute>
                        </input>
                      </div>
                    </div>
                    <div class="clearfix">
                      <label for="start-date">Start</label>
                      <div class="input">
                        <input required="required" min="1000-01-01"
                          class="span6" id="start-date" name="start-date"
                          type="date" placeholder="2012-03-30">
                          <xsl:attribute name="value"><xsl:value-of
                              select="eterms:start-date" />
                          </xsl:attribute>
                        </input>
                      </div>
                    </div>
                    <div class="clearfix">
                      <label for="end-date">End</label>
                      <div class="input">
                        <input class="span6" id="end-date" name="end-date"
                          type="date">
                          <xsl:attribute name="value"><xsl:value-of
                              select="eterms:end-date" />
                          </xsl:attribute>
                        </input>
                      </div>
                    </div>
                  </fieldset>
                  <div class="actions">
                    <input type="submit" class="btn primary"
                      value="Save changes" />
                    <xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text>
                    <button type="reset" class="btn">Cancel</button>
                  </div>
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