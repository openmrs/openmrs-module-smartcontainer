<%@ include file="localHeader.jsp" %>

<openmrs:require privilege="Manage SMART Apps" otherwise="/login.htm" redirect="/module/smartcontainer/smartUsers.list" />

<b class="boxHeader"><spring:message code="smartcontainer.admin.manage.user"/></b>

<div class="box">
	<br />
	<table class="defaultSmartTable" cellpadding="5" cellspacing="0" width="60%">
		<tr>
			<th class="userAppRowHeader"><spring:message code="general.name"/></th>
			<th class="userAppRowHeader"><spring:message code="User.username"/></th>
			<th class="userAppRowHeader">&nbsp;</th>
		</tr>
		<c:forEach var="smartUser" items="${smartUsers}" varStatus="varStatus">
		<tr>
			<td <c:if test="${varStatus.index % 2 != 0}">class='smartOddRow'</c:if> valign="top">
				${smartUser.openMRSUser.personName}
			</td>
			<td <c:if test="${varStatus.index % 2 != 0}">class='smartOddRow'</c:if> valign="top">
				${smartUser.openMRSUser.username}
			</td>
			<td <c:if test="${varStatus.index % 2 != 0}">class='smartOddRow'</c:if> valign="top" style="text-align:right">
				<input class="smallButton" type="button" value='<spring:message code="smartcontainer.manageHiddenApps"/>' 
					onclick="javascript:document.location='<openmrs:contextPath />/module/smartcontainer/manageUserHiddenApps.form?systemId=${smartUser.openMRSUser.systemId}'" />
			</td>
		</tr>
		</c:forEach>
	</table>
</div>