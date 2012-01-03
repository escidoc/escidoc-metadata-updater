<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet
	version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:md="http://www.escidoc.de/schemas/metadatarecords/0.5">

	<xsl:output
		encoding="iso-8859-1"
		indent="yes"
		method="html" />

	<xsl:template match="/">
		<html>
			<head>
				<meta
					http-equiv="Content-Type"
					content="text/html; charset=utf-8" />
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
                                    xhr.open('PUT', getUri(), false);
                                    xhr.setRequestHeader('Accept', 'application/xml;charset=UTF-8');
                                    xhr.setRequestHeader('Content-Type', 'application/xml;charset=UTF-8');
                                    xhr.send('<xml/>');
                                } else {  
                                    console.log("Error", xhr.statusText);  
                                }  
                            }  
                        }; 
                        xhr.send(null);
                        return false;
                    }

                    function getUri(){
                        return window.location.href;
                    }
                </script>
            </head>
            <body>
                <xsl:apply-templates />
            </body>
		</html>
	</xsl:template>

	<!-- TODO find a better match patter for root node -->
	<xsl:template match="/node()[position()=1]">
		<div class="content">
			<div class="span12">
				<form
					id="metadata-editor"
					onSubmit="return send();">
					<fieldset>
						<legend>
							<xsl:value-of select="local-name()" />
						</legend>
						<xsl:for-each select="/node()/*">
							<div class="clearfix">
								<label>
									<xsl:value-of select="local-name()" />
								</label>
								<div class="input">
									<xsl:variable
										name="value"
										select="text()" />
									<input
										name="local-name()"
										type="text"
										placeholder="{$value}" />
								</div>
							</div>
						</xsl:for-each>
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
	</xsl:template>
</xsl:stylesheet>
