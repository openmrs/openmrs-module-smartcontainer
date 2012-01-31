<%@ include file="localHeader.jsp" %>

<openmrs:require privilege="Manage SMART Apps" otherwise="/login.htm" redirect="/module/smartcontainer/manageSmartApps.form" />

<openmrs:htmlInclude file="/dwr/interface/DWRSmartService.js" />

<script type="text/javascript">
	var oTable;

	$j(document)
			.ready(
					function() {
						$j('#addUpgradePopup')
								.dialog(
										{
											autoOpen : false,
											modal : true,
											title : '<spring:message code="smartcontainer.addOrUpgrade" javaScriptEscape="true"/>',
											width : '90%'
										});

						$j('#addUpgradeButton').click(function() {
							$j('#addUpgradePopup').dialog('open');
						});
					});
	
	/**
	 * Enable or disable an app
	 */
	function enableDisableApp(appId, name, enable) {
		DWRSmartService.enableOrDisableSmartApp(appId, !enable, function(success) {
			if(success){
				location.reload();
			}
			else {
				alert('failed to enable disable app');
			}
		});
	}
	
	function confirmRemoveApp(){
		if(confirm('<spring:message code="smartcontainer.removeApp.confirmation" />'))
			return true;
		
		return false;
	}
	
</script>

<h2><spring:message code="smartcontainer.admin.manage" /></h2>

<c:if test="${fn:length(appList) == 0}">
	<spring:message code="smartcontainer.manage.help.none"/>
</c:if>
<spring:message code="smartcontainer.manage.help"/>

<br />
<br />
<div id="buttonPanel">
<div style="float: left"><input type="button"
	id="addUpgradeButton"
	value="<spring:message code="smartcontainer.addOrUpgrade" javaScriptEscape="true"/>" />
	


<div id="addUpgradePopup"><b class="boxHeader"><spring:message
	code="smartcontainer.add" /></b>
<div class="box">
<form id="moduleAddForm" method="post" enctype="multipart/form-data">
<input type="file" name="moduleFile" size="40" /> <input type="hidden"
	name="action" value="upload" /> <input type="submit"
	value='<spring:message code="smartcontainer.upload"/>' /></form>
</div>
<br />

<div id="findModule"><b class="boxHeader"><spring:message
	code="smartcontainer.findAndDownload" /></b>
<div class="box">
<form method="post" id="uploadFromURLForm"><input type="text"
	name="manifestURL" size="40" /> <input type="hidden" name="action"
	value="upload" /> <input type="hidden" name="updateFromURL"
	value="true" /> <input type="submit"
	value='<spring:message code="smartcontainer.upload"/>' /></form>
</div>
</div>
<br />
</div>
</div>

<div style="clear: both">&nbsp;</div>
</div>
<c:forEach var="app" items="${appList}" varStatus="varStatus">
	<c:if test="${varStatus.first}">
		<b class="boxHeader"><spring:message code="smartcontainer.admin.manage" /></b>
		<div class="box" id="moduleListing">
		<table cellpadding="5" cellspacing="0" style="text-align: left">
			<thead>
				<tr>
					<th></th>
					<th><spring:message code="general.name" /></th>
					<th><spring:message code="general.version" /></th>
					<th><spring:message code="general.author" /></th>
					<th><spring:message code="general.description" /></th>
					<th></th>
					<th></th>
				</tr>
			</thead>
			<tbody>
				</c:if>

				<tr class='${varStatus.index % 2 == 0 ? "oddRow" : "evenRow" }'
					id="${app.appId}">
					<form method="post" onsubmit="return confirmRemoveApp()">
					<input type="hidden" name="action" value="removeApp" /> 
					<input type="hidden" name="appId" value="${app.appId}" />
					<td valign="top"><img src="${app.icon}" /></td>
					<td valign="top">${app.name}</td>
					<td valign="top">${app.version}</td>
					<td valign="top">${app.author}</td>
					<td valign="top">
						${app.description}
					</td>
					<td valign="top">
						<c:choose>
							<c:when test="${app.retired}">
							<input name="enableAction" type="button" value="enable" title="<spring:message code="general.restore" />"
									onclick="enableDisableApp(${app.appId}, '${app.name}', ${app.retired})" />
							</c:when>
							<c:otherwise>
								<input name="enableAction" type="button" value="disable" title="<spring:message code="general.retire" />"
									onclick="enableDisableApp(${app.appId}, '${app.name}', ${app.retired})" />
							</c:otherwise>
						</c:choose>
					</td>
					<td valign="top">
						<input name="remove" type="submit" value="remove" title="<spring:message code="general.purge" />" />
					</td>
					</form>
				</tr>
				<c:if test="${varStatus.last}">
			</tbody>
		</table>
		</div>

	</c:if>

</c:forEach>

<%@ include file="/WEB-INF/template/footer.jsp"%>