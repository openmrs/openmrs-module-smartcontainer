<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>

<openmrs:require privilege="Manage SMART Apps" otherwise="/login.htm" redirect="/module/smartcontainer/manageUserHiddenApps.form" />
<openmrs:htmlInclude file="/moduleResources/smartcontainer/smart.css" />

<h2><spring:message code="smartcontainer.manageUserHiddenApps"/></h2>

<b class="boxHeader"><spring:message code="smartcontainer.smartAppList"/></b>

<div class="box" id="moduleListing">
	<br />
	<c:choose>
	<c:when test="${fn:length(apps) > 0}">
	<form method="post">
		<input type="hidden" name="uuid" value='<request:parameter name="uuid"/>' />
		<table class="defaultSmartTable" cellpadding="3" cellspacing="0" width="100%">
			<tr>
				<th class="userAppRowHeader"><spring:message code="smartcontainer.appName"/></th>
				<th class="userAppRowHeader centerAligned" style="max-width: 40px"><spring:message code="smartcontainer.isHiddenFromUser"/></th>
			</tr>	
			<c:forEach var="app" items="${apps}" varStatus="varStatus">
				<tr>
					<td <c:if test="${varStatus.index % 2 != 0}">class='smartOddRow'</c:if> valign="top">${app.name}</td>
					<td class='centerAligned<c:if test="${varStatus.index % 2 != 0}"> smartOddRow</c:if>' valign="top">
						<input type="checkbox" name="appIdsToHide" value="${app.appId}" 
						<c:if test="${fn:contains(hiddenApps, app)}">checked="checked"</c:if> />
					</td>
				</tr>
			</c:forEach>
		</table>
		<br />
		<input type="submit" align="right" name="save" value="<spring:message code="smartcontainer.addAppUserLevel.save" javaScriptEscape="true"/>" >
	</form>
	</c:when>
	<c:otherwise><spring:message code="smartcontainer.noAppsInstalledCanInstall"/></c:otherwise>
	</c:choose>
</div>