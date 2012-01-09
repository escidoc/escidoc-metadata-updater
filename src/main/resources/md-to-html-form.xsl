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
                <meta
                    name="description"
                    content="eSciDoc Metadata Editor" />
                <meta
                    name="keywords"
                    content="escidoc, metadata editor" />
                <link
                  rel="stylesheet"
                  type="text/css"
                  href="http://twitter.github.com/bootstrap/1.4.0/bootstrap.min.css" />
                <style type="text/css">
                    /* Override some defaults */
                    html, body {
                      background-color: #eee;
                    }
                    body {
                      padding-top: 40px; /* 40px to make the container go all the way to the bottom of the topbar */
                    }
                    .container > footer p {
                      text-align: center; /* center align it with the container */
                    }
                    .container {
                      width: 820px; /* downsize our container to make the content feel a bit tighter and more cohesive. NOTE: this removes two full columns from the grid, meaning you only go to 14 columns and not 16. */
                    }

                    /* The white background content wrapper */
                    .content {
                      background-color: #fff;
                      padding: 20px;
                      margin: 0 -20px; /* negative indent the amount of the padding to maintain the grid system */
                      -webkit-border-radius: 0 0 6px 6px;
                         -moz-border-radius: 0 0 6px 6px;
                              border-radius: 0 0 6px 6px;
                      -webkit-box-shadow: 0 1px 2px rgba(0,0,0,.15);
                         -moz-box-shadow: 0 1px 2px rgba(0,0,0,.15);
                              box-shadow: 0 1px 2px rgba(0,0,0,.15);
                    }

                    /* Page header tweaks */
                    .page-header {
                      background-color: #f5f5f5;
                      padding: 20px 20px 10px;
                      margin: -20px -20px 20px;
                    }

                    /* Styles you shouldn't keep as they are for displaying this base example only */
                    .content .span10,
                    .content .span4 {
                      min-height: 500px;
                    }
                    /* Give a quick and non-cross-browser friendly divider */
                    .content .span4 {
                      margin-left: 0;
                      padding-left: 19px;
                      border-left: 1px solid #eee;
                    }

                    .topbar .btn {
                      border: 0;
                    }
                </style>
                <script type="text/javascript">
                    function send(){
                        var xhr = new XMLHttpRequest();
                        xhr.open('GET', getUri(), false);
                        xhr.setRequestHeader('Accept', 'application/xml;charset=UTF-8');
                        xhr.setRequestHeader('Content-Type', 'application/xml;charset=UTF-8');

                        xhr.onreadystatechange = function (oEvent) {
                            if (xhr.readyState === 4) {
                                if (xhr.status === 200) {
                                    var r=xhr.responseText;
                                    console.log(r);
                                    console.log('uri'+getUri());

                                    var rXml=xhr.responseXML;;
                                    console.log(rXml);

                                    console.log(document.getElementsByTagName('input'));
                                    var list=document.getElementsByTagName('input');

                                    Array.prototype.forEach.call(list, function(li, index, nodeList) {
                                        if(li.type !== 'button') {
                                            if(li.defaultValue !== li.value) {
                                                var found= rXml.getElementsByTagName(li.name);
                                                var input=found[0];
                                                input.textContent= li.value;
                                            }
                                        }
                                    });
                                    put(rXml);
                                } else {
                                    console.log("Error", xhr.statusText);
                                }
                            }
                        };
                        xhr.send(null);

                        return false;
                    }

                    function put(xml){
                        var xhr2 = new XMLHttpRequest();
                        xhr2.open('PUT', getUri(), false);
                        xhr2.setRequestHeader('Accept', 'application/xml;charset=UTF-8');
                        xhr2.setRequestHeader('Content-Type', 'application/xml;charset=UTF-8');

                        xhr2.onreadystatechange = function (oEvent) {
                            if (xhr2.readyState === 4) {
                                if (xhr2.status === 200) {
                                    var r=xhr2.responseXML;
                                    console.log("Successfully update the metadata.");
                                    console.log(r);
                                } else {
                                    console.log("Error", xhr2.statusText);
                                }
                            }
                        };
                        xhr2.send(xml);
                    }

                    function getUri(){
                        return window.location.href;
                    }
                </script>
            </head>
            <body>
            <div class="container">
              <div class="content">
                <div class="page-header">
                  <h1>Metadata Editor</h1>
                </div>
                <div class="row">
                  <div class="span12">
                      <form
                          id="metadata-editor"
                          onSubmit="return send();">
                          <fieldset>
                              <legend></legend>
                              <xsl:apply-templates />
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
            </body>
        </html>
    </xsl:template>

	<xsl:template match="//*[not(*)]">
       <xsl:variable
           name="key"
           select="local-name()" />
       <xsl:variable
           name="value"
           select="text()" />
       <div class="clearfix">
           <label for="{$key}">
               <xsl:value-of select="name()" />
           </label>
           <div class="input">
             <input 
               class="span6"
               id="{$key}"
               name="{$key}"
               type="text"
               value="{$value}" />
           </div>
       </div> <!--/clearfix-->
    </xsl:template>
</xsl:stylesheet>
