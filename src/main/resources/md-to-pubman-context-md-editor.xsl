<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:output encoding="UTF-8" indent="yes" method="html" />

  <xsl:template match="pubman-admin-descriptor">
    <xsl:text disable-output-escaping='yes'>&lt;!DOCTYPE html></xsl:text>
    <html>
      <head>
        <title>Pubman context Metadata Editor</title>
        <meta name="description" content="Editor for Pubman Context Metadata Profile" />
        <meta name="keywords"
          content="escidoc, context, pubman, metadata, metadata editor" />
        <link rel="stylesheet" type="text/css"
          href="/rest/static/css/bootstrap.min.css" />
        <link rel="stylesheet" type="text/css"
          href="/rest/static/css/override-bootstrap.css" />
        <link rel="stylesheet" type="text/css" href="/rest/static/css/alert.css" />
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
                    <legend>PubMan Context Metadata/Admin Descriptor
                    </legend>
                    <div class="clearfix">
                      <label>Allowed Genres</label>
                      <div class="input">
                        <div style="padding: 16px 19px">
                          <input id="select-all-genres" class="btn small"
                            type="button" name="CheckAll" value="all" />
                          <input id="unselect-all-genres" class="btn small"
                            type="button" name="UnCheckAll" value="none" />
                          <br />
                        </div>
                        <ul id="genre" class="inputs-list">
                          <xsl:apply-templates select="allowed-genres" />
                        </ul>
                      </div>
                    </div>
                    <div class="clearfix">
                      <label>Allowed Subject Classification</label>
                      <div class="input">
                        <div style="padding: 16px 19px">
                          <input id="select-all-subjects" class="btn small"
                            type="button" name="CheckAll" value="all" />
                          <input id="unselect-all-subjects" class="btn small"
                            type="button" name="UnCheckAll" value="none" />
                          <br />
                        </div>
                        <ul id="subject" class="inputs-list">
                          <xsl:apply-templates select="allowed-subject-classifications" />
                        </ul>
                      </div>
                    </div><!-- end clearfix -->
                    <div class="clearfix">
                      <label>Workflow</label>
                      <div class="input">
                        <xsl:apply-templates select="workflow" />
                      </div>
                    </div><!-- end clearfix -->
                    <div class="clearfix">
                      <label>Validation Schema</label>
                      <div class="input">
                        <xsl:apply-templates select="validation-schema" />
                      </div>
                    </div><!-- end clearfix -->
                    <div class="clearfix">
                      <label>Contact Mail</label>
                      <div class="input">
                        <input class="xlarge" name="contact-email"
                          type="email">
                          <xsl:attribute name="value"><xsl:value-of
                              select="contact-email" /></xsl:attribute>
                        </input>
                      </div>
                    </div><!-- end clearfix -->
                    <div class="actions">
                      <input type="submit" class="btn primary"
                        value="Save changes" />
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

  <xsl:template match="allowed-genres">
    <li>
      <label>
        <input type="checkbox" name="genresList" value="http://purl.org/eprint/type/Book" >
          <xsl:if test="allowed-genre = 'http://purl.org/eprint/type/Book' ">
            <xsl:attribute name="checked">checked</xsl:attribute>
          </xsl:if>
        </input>
        Book
      </label>
    </li>
    <li>
      <label>
        <input type="checkbox" name="genresList" value="http://purl.org/eprint/type/BookItem" >
          <xsl:if test="allowed-genre = 'http://purl.org/eprint/type/BookItem' ">
            <xsl:attribute name="checked">checked</xsl:attribute>
          </xsl:if>
        </input>
        BookItem
      </label>
    </li>
    <li>
      <label>
        <input type="checkbox" name="genresList" value="http://purl.org/eprint/type/ConferencePaper" >
          <xsl:if test="allowed-genre = 'http://purl.org/eprint/type/ConferencePaper' ">
            <xsl:attribute name="checked">checked</xsl:attribute>
          </xsl:if>
        </input>
        Conference Paper
      </label>
    </li>
    <li>
      <label>
        <input type="checkbox" name="genresList" value="http://purl.org/eprint/type/ConferencePoster" >
          <xsl:if test="allowed-genre = 'http://purl.org/eprint/type/ConferencePoster' ">
            <xsl:attribute name="checked">checked</xsl:attribute>
          </xsl:if>
        </input>
        Conference Poster
      </label>
    </li>
    <li>
      <label>
        <input type="checkbox" name="genresList" value="http://purl.org/eprint/type/Patent" >
          <xsl:if test="allowed-genre = 'http://purl.org/eprint/type/Patent' ">
            <xsl:attribute name="checked">checked</xsl:attribute>
          </xsl:if>
        </input>
        Patent
      </label>
    </li>
    <li>
      <label>
        <input type="checkbox" name="genresList" value="http://purl.org/eprint/type/Report" >
          <xsl:if test="allowed-genre = 'http://purl.org/eprint/type/Report' ">
            <xsl:attribute name="checked">checked</xsl:attribute>
          </xsl:if>
        </input>
        Report
      </label>
    </li>
    <li>
      <label>
        <input type="checkbox" name="genresList" value="http://purl.org/eprint/type/Thesis">
          <xsl:if test="allowed-genre = 'http://purl.org/eprint/type/Thesis' ">
            <xsl:attribute name="checked">checked</xsl:attribute>
          </xsl:if>
        </input>
        Thesis
      </label>
    </li>
    <li>
      <label>
        <input type="checkbox" name="genresList" value="http://purl.org/escidoc/metadata/ves/publication-types/article" >
          <xsl:if test="allowed-genre = 'http://purl.org/escidoc/metadata/ves/publication-types/article' ">
            <xsl:attribute name="checked">checked</xsl:attribute>
          </xsl:if>
        </input>
        Article
      </label>
    </li>
    <li>
      <label>
        <input type="checkbox" name="genresList" value="http://purl.org/eprint/type/BookReview" >
          <xsl:if test="allowed-genre = 'http://purl.org/eprint/type/BookReview' ">
            <xsl:attribute name="checked">checked</xsl:attribute>
          </xsl:if>
        </input>
        Book Review
      </label>
    </li>
    <li>
      <label>
        <input type="checkbox" name="genresList" value="http://purl.org/escidoc/metadata/ves/publication-types/case-note" >
          <xsl:if test="allowed-genre = 'http://purl.org/escidoc/metadata/ves/publication-types/case-note' ">
            <xsl:attribute name="checked">checked</xsl:attribute>
          </xsl:if>
        </input>
        Case Note
      </label>
    </li>
    <li>
      <label>
        <input type="checkbox" name="genresList" value="http://purl.org/escidoc/metadata/ves/publication-types/case-study" >
          <xsl:if test="allowed-genre = 'http://purl.org/escidoc/metadata/ves/publication-types/case-study' ">
            <xsl:attribute name="checked">checked</xsl:attribute>
          </xsl:if>
        </input>
        Case Study
      </label>
    </li>
    <li>
      <label>
        <input type="checkbox" name="genresList" value="http://purl.org/escidoc/metadata/ves/publication-types/collected-edition" >
          <xsl:if test="allowed-genre = 'http://purl.org/escidoc/metadata/ves/publication-types/collected-edition' ">
            <xsl:attribute name="checked">checked</xsl:attribute>
          </xsl:if>
        </input>
        Collected Edition
      </label>
    </li>
    <li>
      <label>
        <input type="checkbox" name="genresList" value="http://purl.org/escidoc/metadata/ves/publication-types/commentary" >
          <xsl:if test="allowed-genre = 'http://purl.org/escidoc/metadata/ves/publication-types/commentary' ">
            <xsl:attribute name="checked">checked</xsl:attribute>
          </xsl:if>
        </input>
        Commentary
      </label>
    </li>
    <li>
      <label>
        <input type="checkbox" name="genresList" value="http://purl.org/escidoc/metadata/ves/publication-types/conference-report" >
          <xsl:if test="allowed-genre = 'http://purl.org/escidoc/metadata/ves/publication-types/conference-report' ">
            <xsl:attribute name="checked">checked</xsl:attribute>
          </xsl:if>
        </input>
        Conference Report
      </label>
    </li>
    <li>
      <label>
        <input type="checkbox" name="genresList" value="http://purl.org/escidoc/metadata/ves/publication-types/contribution-to-selected-edition" >
          <xsl:if test="allowed-genre = 'http://purl.org/escidoc/metadata/ves/publication-types/contribution-to-selected-edition' ">
            <xsl:attribute name="checked">checked</xsl:attribute>
          </xsl:if>
        </input>
        Contribution to selected edition
      </label>
    </li>
    <li>
      <label>
        <input type="checkbox" name="genresList"
          value="http://purl.org/escidoc/metadata/ves/publication-types/contribution-to-commentary" >
          <xsl:if test="allowed-genre = 'http://purl.org/escidoc/metadata/ves/publication-types/contribution-to-commentary' ">
            <xsl:attribute name="checked">checked</xsl:attribute>
          </xsl:if>
        </input>
        Contribution to Commentary
      </label>
    </li>
    <li>
      <label>
        <input type="checkbox" name="genresList"
          value="http://purl.org/escidoc/metadata/ves/publication-types/contribution-to-encyclopedia" >
          <xsl:if test="allowed-genre = 'http://purl.org/escidoc/metadata/ves/publication-types/contribution-to-encyclopedia' ">
            <xsl:attribute name="checked">checked</xsl:attribute>
          </xsl:if>
        </input>
        Contribution to Encyclopedia
      </label>
    </li>
    <li>
      <label>
        <input type="checkbox" name="genresList"
          value="http://purl.org/escidoc/metadata/ves/publication-types/contribution-to-festschrift" >
          <xsl:if test="allowed-genre = 'http://purl.org/escidoc/metadata/ves/publication-types/contribution-to-festschrift' ">
            <xsl:attribute name="checked">checked</xsl:attribute>
          </xsl:if>
        </input>
        Contribution to Festschrift
      </label>
    </li>
    <li>
      <label>
        <input type="checkbox" name="genresList"
          value="http://purl.org/escidoc/metadata/ves/publication-types/contribution-to-handbook" >
          <xsl:if test="allowed-genre = 'http://purl.org/escidoc/metadata/ves/publication-types/contribution-to-handbook' ">
            <xsl:attribute name="checked">checked</xsl:attribute>
          </xsl:if>
        </input>
        Contribution to Handbook
      </label>
    </li>
    <li>
      <label>
        <input type="checkbox" name="genresList"
          value="http://purl.org/escidoc/metadata/ves/publication-types/handbook" >
          <xsl:if test="allowed-genre = 'http://purl.org/escidoc/metadata/ves/publication-types/handbook' ">
            <xsl:attribute name="checked">checked</xsl:attribute>
          </xsl:if>
        </input>
        Handbook
      </label>
    </li>
    <li>
      <label>
        <input type="checkbox" name="genresList" value="http://purl.org/escidoc/metadata/ves/publication-types/issue" >
          <xsl:if test="allowed-genre = 'http://purl.org/escidoc/metadata/ves/publication-types/issue' ">
            <xsl:attribute name="checked">checked</xsl:attribute>
          </xsl:if>
        </input>
        Issue
      </label>
    </li>
    <li>
      <label>
        <input type="checkbox" name="genresList" value="http://purl.org/escidoc/metadata/ves/publication-types/journal" >
          <xsl:if test="allowed-genre = 'http://purl.org/escidoc/metadata/ves/publication-types/journal' ">
            <xsl:attribute name="checked">checked</xsl:attribute>
          </xsl:if>
        </input>
        Journal
      </label>
    </li>
    <li>
      <label>
        <input type="checkbox" name="genresList" value="http://purl.org/escidoc/metadata/ves/publication-types/manual" >
          <xsl:if test="allowed-genre = 'http://purl.org/escidoc/metadata/ves/publication-types/manual' ">
            <xsl:attribute name="checked">checked</xsl:attribute>
          </xsl:if>
        </input>
        Manual
      </label>
    </li>
    <li>
      <label>
        <input type="checkbox" name="genresList"
          value="http://purl.org/escidoc/metadata/ves/publication-types/manuscript" >
          <xsl:if test="allowed-genre = 'http://purl.org/escidoc/metadata/ves/publication-types/manuscript' ">
            <xsl:attribute name="checked">checked</xsl:attribute>
          </xsl:if>
        </input>
        Manuscript
      </label>
    </li>
    <li>
      <label>
        <input type="checkbox" name="genresList"
          value="http://purl.org/escidoc/metadata/ves/publication-types/meeting-abstract" >
          <xsl:if test="allowed-genre = 'http://purl.org/escidoc/metadata/ves/publication-types/meeting-abstract' ">
            <xsl:attribute name="checked">checked</xsl:attribute>
          </xsl:if>
        </input>
        Meeting Abstract
      </label>
    </li>
    <li>
      <label>
        <input type="checkbox" name="genresList"
          value="http://purl.org/escidoc/metadata/ves/publication-types/monograph" >
          <xsl:if test="allowed-genre = 'http://purl.org/escidoc/metadata/ves/publication-types/monograph' ">
            <xsl:attribute name="checked">checked</xsl:attribute>
          </xsl:if>
        </input>
        Monograph
      </label>
    </li>
    <li>
      <label>
        <input type="checkbox" name="genresList"
          value="http://purl.org/escidoc/metadata/ves/publication-types/multi-volume" >
          <xsl:if test="allowed-genre = 'http://purl.org/escidoc/metadata/ves/publication-types/multi-volume' ">
            <xsl:attribute name="checked">checked</xsl:attribute>
          </xsl:if>
        </input>
        Multiple Volume
      </label>
    </li>
    <li>
      <label>
        <input type="checkbox" name="genresList"
          value="http://purl.org/escidoc/metadata/ves/publication-types/newspaper" >
          <xsl:if test="allowed-genre = 'http://purl.org/escidoc/metadata/ves/publication-types/newspaper' ">
            <xsl:attribute name="checked">checked</xsl:attribute>
          </xsl:if>
        </input>
        Newspaper
      </label>
    </li>
    <li>
      <label>
        <input type="checkbox" name="genresList"
          value="http://purl.org/escidoc/metadata/ves/publication-types/newspaper-article" >
          <xsl:if test="allowed-genre = 'http://purl.org/escidoc/metadata/ves/publication-types/newspaper-article' ">
            <xsl:attribute name="checked">checked</xsl:attribute>
          </xsl:if>
        </input>

        Newspaper Article
      </label>
    </li>
    <li>
      <label>
        <input type="checkbox" name="genresList"
          value="http://purl.org/escidoc/metadata/ves/publication-types/opinion" >
          <xsl:if test="allowed-genre = 'http://purl.org/escidoc/metadata/ves/publication-types/opinion' ">
            <xsl:attribute name="checked">checked</xsl:attribute>
          </xsl:if>
        </input>
        Opinion
      </label>
    </li>
    <li>
      <label>
        <input type="checkbox" name="genresList"
          value="http://purl.org/escidoc/metadata/ves/publication-types/other" >
          <xsl:if test="allowed-genre = 'http://purl.org/escidoc/metadata/ves/publication-types/other' ">
            <xsl:attribute name="checked">checked</xsl:attribute>
          </xsl:if>
        </input>
        Other
      </label>
    </li>
    <li>
      <label>
        <input type="checkbox" name="genresList"
          value="http://purl.org/escidoc/metadata/ves/publication-types/paper" >
          <xsl:if test="allowed-genre = 'http://purl.org/escidoc/metadata/ves/publication-types/paper' ">
            <xsl:attribute name="checked">checked</xsl:attribute>
          </xsl:if>
        </input>
        Paper
      </label>
    </li>
    <li>
      <label>
        <input type="checkbox" name="genresList"
          value="http://purl.org/escidoc/metadata/ves/publication-types/proceedings" >
          <xsl:if test="allowed-genre = 'http://purl.org/escidoc/metadata/ves/publication-types/proceedings' ">
            <xsl:attribute name="checked">checked</xsl:attribute>
          </xsl:if>
        </input>
        Proceedings
      </label>
    </li>
    <li>
      <label>
        <input type="checkbox" name="genresList"
          value="http://purl.org/escidoc/metadata/ves/publication-types/series" >
          <xsl:if test="allowed-genre = 'http://purl.org/escidoc/metadata/ves/publication-types/series' ">
            <xsl:attribute name="checked">checked</xsl:attribute>
          </xsl:if>
        </input>
        Series
      </label>
    </li>
    <li>
      <label>
        <input type="checkbox" name="genresList" value="http://purl.org/escidoc/metadata/ves/publication-types/talk-at-event" >
          <xsl:if test="allowed-genre = 'http://purl.org/escidoc/metadata/ves/publication-types/talk-at-event' ">
            <xsl:attribute name="checked">checked</xsl:attribute>
          </xsl:if>
        </input>
        Talk at Event
      </label>
    </li>
  </xsl:template>

  <xsl:template match="allowed-subject-classifications">
    <li>
      <label>
        <input type="checkbox" name="subjectList" value="http://purl.org/escidoc/metadata/terms/0.1/DDC">
          <xsl:if test="allowed-subject-classification = 'http://purl.org/escidoc/metadata/terms/0.1/DDC' ">
            <xsl:attribute name="checked">checked</xsl:attribute>
          </xsl:if>
        </input>
          DDC
      </label>
    </li>
    <li>
      <label>
        <input type="checkbox" name="subjectList" value="http://purl.org/escidoc/metadata/terms/0.1/MPIPKS" >
          <xsl:if test="allowed-subject-classification = 'http://purl.org/escidoc/metadata/terms/0.1/MPIPKS' ">
            <xsl:attribute name="checked">checked</xsl:attribute>
          </xsl:if>
        </input>
        MPIPKS
      </label>
    </li>
    <li>
      <label>
        <input type="checkbox" name="subjectList"
          value="http://purl.org/escidoc/metadata/terms/0.1/ISO639-3" >

          <xsl:if test="allowed-subject-classification= 'http://purl.org/escidoc/metadata/terms/0.1/ISO639-3' ">
            <xsl:attribute name="checked">checked</xsl:attribute>
          </xsl:if>
        </input>
        ISO639-3
      </label>
    </li>
    <li>
      <label>
        <input type="checkbox" name="subjectList"
          value="http://purl.org/escidoc/metadata/terms/0.1/JUS" >

          <xsl:if test="allowed-subject-classification= 'http://purl.org/escidoc/metadata/terms/0.1/JUS' ">
            <xsl:attribute name="checked">checked</xsl:attribute>
          </xsl:if>
          
        </input>
        JUS
      </label>
    </li>
    <li>
      <label>
        <input type="checkbox" name="subjectList"
          value="http://purl.org/escidoc/metadata/terms/0.1/PACS" >

          <xsl:if test="allowed-subject-classification= 'http://purl.org/escidoc/metadata/terms/0.1/PACS' ">
            <xsl:attribute name="checked">checked</xsl:attribute>
          </xsl:if>

        </input>
        PACS
      </label>
    </li>

  </xsl:template>

  <xsl:template match="workflow">
    <select id="workflow" name="workflow">
      <option value="standard">
        <xsl:if test=". = 'standard'">
          <xsl:attribute name="selected">selected</xsl:attribute>
        </xsl:if>
        Standard
      </option>
      <option value="simple">
        <xsl:if test=". = 'simple'">
          <xsl:attribute name="selected">selected</xsl:attribute>
        </xsl:if>
        Simple
      </option>
    </select>
  </xsl:template>
  <xsl:template match="validation-schema">
    <select id="validation-schema" name="validation-schema">
      <option value="publication">
        <xsl:if test=".  = 'publication'">
          <xsl:attribute name="selected">selected</xsl:attribute>
        </xsl:if>
        Publication
      </option>
      <option value="greymaterial">
        <xsl:if test=".  = 'greymaterial'">
          <xsl:attribute name="selected">selected</xsl:attribute>
        </xsl:if>
        Grey Material
      </option>
      <option value="jus">
        <xsl:if test=". = 'jus'">
          <xsl:attribute name="selected">selected</xsl:attribute>
        </xsl:if>
        JUS
      </option>
      <option value="externalgreymaterial">
        <xsl:if test=".  = 'externalgreymaterial'">
          <xsl:attribute name="selected">selected</xsl:attribute>
        </xsl:if>
        External Grey Material
      </option>
      <option value="simple">
        <xsl:if test=".  = 'simple'">
          <xsl:attribute name="selected">selected</xsl:attribute>
        </xsl:if>
        Simple
      </option>
      <option value="yearbook">
        <xsl:if test=".  = 'yearbook'">
          <xsl:attribute name="selected">selected</xsl:attribute>
        </xsl:if>
        Year Book
      </option>
    </select>
  </xsl:template>
</xsl:stylesheet>
