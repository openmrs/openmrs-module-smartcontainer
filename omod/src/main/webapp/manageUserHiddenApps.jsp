<%@ include file="localHeader.jsp" %>

<openmrs:require privilege="Manage SMART Apps" otherwise="/login.htm" redirect="/module/smartcontainer/manageUserAppLink.form" />

<b class="boxHeader"><spring:message code="smartcontainer.manageUserHiddenApps"/></b>

<div class="box" id="moduleListing">
	<br />
	<c:choose>
	<c:when test="${fn:length(apps) > 0}">
	<form method="post">
		<input type="hidden" name="systemId" value='<request:parameter name="systemId"/>' />
		<table class="userAppTable" cellpadding="3" cellspacing="0" width="70%">
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
	<c:otherwise><spring:message code="smartcontainer.noappsinstalled"/></c:otherwise>
	</c:choose>
</div>