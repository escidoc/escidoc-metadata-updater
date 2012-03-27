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

                    <!-- FIXME after reading the XML file, mark checkbox as checked -->
                    <div class="clearfix">
                      <label>Allowed Genres</label>
                      <div class="input">
                        <div style="padding: 16px 19px">
                          <input id="select-all-genres" class="btn small" type="button" name="CheckAll" value="all"/>
                          <input id="unselect-all-genres" class="btn small" type="button" name="UnCheckAll" value="none"/><br/>
                        </div>
                        <ul id="genre" class="inputs-list">
                          <li><label><input type="checkbox" name="genresList" checked="checked" value="http://purl.org/eprint/type/Book"/>Book</label></li>
                          <li><label><input type="checkbox" name="genresList" value="http://purl.org/eprint/type/BookItem" />BookItem</label></li>
                          <li><label><input type="checkbox" name="genresList" value="http://purl.org/eprint/type/ConferencePaper"/>Conference Paper</label></li>
                          <li><label><input type="checkbox" name="genresList" value="http://purl.org/eprint/type/ConferencePoster"/> Conference Poster</label></li>
                          <li><label><input type="checkbox" name="genresList" value="http://purl.org/eprint/type/Patent"/> Patent</label></li>
                          <li><label><input type="checkbox" name="genresList" value="http://purl.org/eprint/type/Report"/> Report</label></li>
                          <li><label><input type="checkbox" name="genresList" value="http://purl.org/eprint/type/Thesis"/> Thesis</label></li>
                          <li><label><input type="checkbox" name="genresList" value="http://purl.org/escidoc/metadata/ves/publication-types/article"/> Article</label></li>
                          <li><label><input type="checkbox" name="genresList" value="http://purl.org/eprint/type/BookReview"/> Book Review</label></li>
                          <li><label><input type="checkbox" name="genresList" value="http://purl.org/escidoc/metadata/ves/publication-types/case-note"/> Case Note</label></li>
                          <li><label><input type="checkbox" name="genresList" value="http://purl.org/escidoc/metadata/ves/publication-types/case-study"/> Case Study</label></li>
                          <li><label><input type="checkbox" name="genresList" value="http://purl.org/escidoc/metadata/ves/publication-types/collected-edition"/> Collected Edition</label></li>
                          <li><label><input type="checkbox" name="genresList" value="http://purl.org/escidoc/metadata/ves/publication-types/commentary"/> Commentary</label></li>
                          <li><label><input type="checkbox" name="genresList" value="http://purl.org/escidoc/metadata/ves/publication-types/conference-report"/> Conference Report</label></li>
                          <li><label><input type="checkbox" name="genresList" value="http://purl.org/escidoc/metadata/ves/publication-types/contribution-to-selected-edition"/> Contribution to selected edition</label></li>
                          <li><label><input type="checkbox" name="genresList" value="http://purl.org/escidoc/metadata/ves/publication-types/contribution-to-commentary"/> Contribution to Commentary</label></li>
                          <li><label><input type="checkbox" name="genresList" value="http://purl.org/escidoc/metadata/ves/publication-types/contribution-to-encyclopedia"/> Contribution to Encyclopedia</label></li>
                          <li><label><input type="checkbox" name="genresList" value="http://purl.org/escidoc/metadata/ves/publication-types/contribution-to-festschrift"/> Contribution to Festschrift</label></li>
                          <li><label><input type="checkbox" name="genresList" value="http://purl.org/escidoc/metadata/ves/publication-types/contribution-to-handbook"/> Contribution to Handbook</label></li>
                          <li><label><input type="checkbox" name="genresList" value="http://purl.org/escidoc/metadata/ves/publication-types/handbook"/> Handbook</label></li>
                          <li><label><input type="checkbox" name="genresList" value="http://purl.org/escidoc/metadata/ves/publication-types/issue"/> Issue</label></li>
                          <li><label><input type="checkbox" name="genresList" value="http://purl.org/escidoc/metadata/ves/publication-types/journal"/> Journal</label></li>
                          <li><label><input type="checkbox" name="genresList" value="http://purl.org/escidoc/metadata/ves/publication-types/manual"/> Manual</label></li>
                          <li><label><input type="checkbox" name="genresList" value="http://purl.org/escidoc/metadata/ves/publication-types/manuscript"/> Manuscript</label></li>
                          <li><label><input type="checkbox" name="genresList" value="http://purl.org/escidoc/metadata/ves/publication-types/meeting-abstract"/> Meeting Abstract</label></li>
                          <li><label><input type="checkbox" name="genresList" value="http://purl.org/escidoc/metadata/ves/publication-types/monograph"/> Monograph</label></li>
                          <li><label><input type="checkbox" name="genresList" value="http://purl.org/escidoc/metadata/ves/publication-types/multi-volume"/> Multiple Volume</label></li>
                          <li><label><input type="checkbox" name="genresList" value="http://purl.org/escidoc/metadata/ves/publication-types/newspaper"/> Newspaper</label></li>
                          <li><label><input type="checkbox" name="genresList" value="http://purl.org/escidoc/metadata/ves/publication-types/newspaper-article"/> Newspaper Article</label></li>
                          <li><label><input type="checkbox" name="genresList" value="http://purl.org/escidoc/metadata/ves/publication-types/opinion"/> Opinion</label></li>
                          <li><label><input type="checkbox" name="genresList" value="http://purl.org/escidoc/metadata/ves/publication-types/other"/> Other</label></li>
                          <li><label><input type="checkbox" name="genresList" value="http://purl.org/escidoc/metadata/ves/publication-types/paper"/> Paper</label></li>
                          <li><label><input type="checkbox" name="genresList" value="http://purl.org/escidoc/metadata/ves/publication-types/proceedings"/> Proceedings</label></li>
                          <li><label><input type="checkbox" name="genresList" value="http://purl.org/escidoc/metadata/ves/publication-types/series"/> Series</label></li>
                          <li><label><input type="checkbox" name="genresList" value="http://purl.org/escidoc/metadata/ves/publication-types/talk-at-event"/> Talk at Event</label></li>
                        </ul>
                      </div>
                    </div>

                    <!-- FIXME after reading the XML file, mark checkbox as checked -->
                    <div class="clearfix">
                      <label>Allowed Subject Classification</label>
                      <div class="input">
                        <div style="padding: 16px 19px">
                          <input id="select-all-subjects" class="btn small" type="button" name="CheckAll" value="all"/>
                          <input id="unselect-all-subjects" class="btn small" type="button" name="UnCheckAll" value="none"/><br/>
                        </div>
                        <ul id="subject" class="inputs-list">
                          <li>
                          <label>
                              <input type="checkbox" name="subjectList" value="http://purl.org/escidoc/metadata/terms/0.1/DDC">
                                <xsl:attribute name="checked">checked</xsl:attribute>
                                DDC
                              </input>
                            </label>
                          </li>
                          <li><label><input type="checkbox" name="subjectList" value="http://purl.org/escidoc/metadata/terms/0.1/MPIPKS"/>MPIPKS</label></li>
                          <li><label><input type="checkbox" name="subjectList" value="http://purl.org/escidoc/metadata/terms/0.1/ISO639-3"/>ISO639-3</label></li>
                          <li><label><input type="checkbox" name="subjectList" value="http://purl.org/escidoc/metadata/terms/0.1/JUS"/>JUS</label></li>
                          <li><label><input type="checkbox" name="subjectList" value="http://purl.org/escidoc/metadata/terms/0.1/PACS"/>PACS</label></li>
                        </ul>
                      </div>
                    </div><!-- end clearfix -->

                    <div class="clearfix">
                      <label>Workflow</label>
                      <div class="input">
                        <xsl:template match="workflow">
                          <select id="workflow" name="workflow">
                            <option value="standard">
                              <xsl:attribute name="selected"><xsl:value-of select="workflow"/></xsl:attribute>
                              Standard
                            </option>
                            <option value="simple">
                              <xsl:attribute name="selected"><xsl:value-of select="workflow"/></xsl:attribute>
                              Simple
                            </option>
                          </select>
                        </xsl:template>
                      </div>
                    </div><!-- end clearfix -->

                    <div class="clearfix">
                      <label>Validation Schema</label>
                      <div class="input">
                        <xsl:template match="validation-schema">
                          <select id="validation-schema" name="validation-schema">
                            <option value="publication">
                              <xsl:if test="validation-schema = 'publication'">
                                <xsl:attribute name="selected">selected</xsl:attribute>
                              </xsl:if>
                              Publication
                            </option>
                            <option value="greymaterial">
                              <xsl:if test="validation-schema = 'greymaterial'">
                                <xsl:attribute name="selected">selected</xsl:attribute>
                              </xsl:if>
                              Grey Material
                            </option>
                            <option value="jus">
                              <xsl:if test="validation-schema = 'jus'">
                                <xsl:attribute name="selected">selected</xsl:attribute>
                              </xsl:if>
                              JUS
                            </option>
                            <option value="externalgreymaterial">
                              <xsl:if test="validation-schema = 'externalgreymaterial'">
                                <xsl:attribute name="selected">selected</xsl:attribute>
                              </xsl:if>
                              External Grey Material
                            </option>
                            <option value="simple">
                              <xsl:if test="validation-schema = 'simple'">
                                <xsl:attribute name="selected">selected</xsl:attribute>
                              </xsl:if>
                              Simple
                            </option>
                            <option value="yearbook">
                              <xsl:if test="validation-schema = 'yearbook'">
                                <xsl:attribute name="selected">selected</xsl:attribute>
                              </xsl:if>
                              Year Book
                            </option>
                          </select>

                        </xsl:template>
                      </div>
                    </div><!-- end clearfix -->

                    <div class="clearfix">
                      <label>Contact Mail</label>
                      <div class="input">
                        <input class="xlarge" name="contact-email" type="email" >
                          <xsl:attribute name="value"><xsl:value-of select="contact-email" /></xsl:attribute>
                        </input>
                      </div>
                    </div><!-- end clearfix -->

                    <div class="actions">
                      <input type="submit" class="btn primary" value="Save changes" />
                      <xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text>
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
