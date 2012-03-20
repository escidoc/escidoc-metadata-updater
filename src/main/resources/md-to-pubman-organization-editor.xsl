<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet
	version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:output
		encoding="iso-8859-1"
		indent="yes"
		method="xml" />

	<xsl:template match="/">
		<!--TODO <xsl:text disable-output-escaping="yes"><!DOCTYPE html></xsl:text> -->
		<html>
			<head>
				<title>Pubman Organization Metadata Editor</title>
				<meta
					name="description"
					content="Editor for Pubman Organization Metadata Profile" />
				<meta
					name="keywords"
					content="escidoc, organization, pubman, metadata, metadata editor" />
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
												<input
													class="span6"
													id="title"
													name="title"
													type="text"
													autofocus
													required
													placeholder="Name of your organization" />
											</div>
										</div>
										<div class="clearfix">
											<label for="alternative">Organization Alternative Name</label>
											<div class="input">
												<input
													class="span6"
													id="alternative"
													name="alternative"
													type="text"
													size="45"
													placeholder="Alternative name of your organization" />
											</div>
										</div>
										<div class="clearfix">
											<label for="type">Organization Type</label>
											<div class="input">
												<input
													class="span6"
													id="type"
													name="type"
													type="text"
													size="45"
													placeholder="Type such as: Department, Institute, Project, Group, etc" />
											</div>
										</div>
										<div class="clearfix">
											<label for="description">Short Description</label>
											<div class="input">
												<textarea
													class="span6"
													id="description"
													name="description"
													rows="5"
													placeholder="Short description about your organization"></textarea>
											</div>
										</div>
										<div class="clearfix">
											<label for="city">City</label>
											<div class="input">
												<input
													required
													class="span6"
													id="city"
													name="city"
													type="text"
													placeholder="Munich" />
											</div>
										</div>
										<div class="clearfix">
											<label for="country">Country</label>
											<div class="input">
												<input
													required
													class="span6"
													id="country"
													name="country"
													type="text"
													placeholder="Germany" />
											</div>
										</div>
										<div class="clearfix">
											<label for="coordinates">Geographical Location</label>
											<div class="input">
												<input
													class="span6"
													id="coordinates"
													name="coordinates"
													type="text"
													placeholder="48°21′14″N 011°47′10″E" />
											</div>
										</div>
										<div class="clearfix">
											<label for="start-date">Start</label>
											<div class="input">
												<input
													required
													min="1000-01-01"
													class="span6"
													id="start-date"
													name="start-date"
													type="date"
													placeholder="2012-03-30" />
											</div>
										</div>
										<div class="clearfix">
											<label for="end-date">End</label>
											<div class="input">
												<input
													class="span6"
													id="end-date"
													name="end-date"
													type="date" />
											</div>
										</div>
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
				</div><!--/container -->
				<script
					type="text/javascript"
					src="/rest/static/js/jquery-1.7.1.min.js"></script>
				<script
					type="text/javascript"
					src="/rest/static/js/http-io.js"></script>
			</body>
		</html>
	</xsl:template>

	<xsl:template match="/">
	</xsl:template>
</xsl:stylesheet>