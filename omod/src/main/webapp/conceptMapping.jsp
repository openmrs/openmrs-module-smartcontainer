<%@ include file="localHeader.jsp" %>

<openmrs:require privilege="Manage Concept Mapping" otherwise="/login.htm" redirect="/module/smartcontainer/conceptMapping.form" />

<style>
tr.firstRow td, th{
	border-top: solid 1px #D9D7D7; 
}
table.mappingsTable{
	border: solid 1px #D9D7D7;
}
th.appHeader{
	padding-right: 15px;
}
th.appColumn{
	padding-right: 15px; color: #242485;
}
.otherColumn{
	padding-left: 10px;
}
.smart_info{
	margin-top: 5px;
	margin-bottom: 5px;
	border: 1px dashed lightgrey;
	padding: 5px 5px 5px 18px;
	background-color: lightyellow;
}
</style>

<h1><spring:message code="smartcontainer.admin.conceptMapping.title"/></h1>
<b class="boxHeader"><spring:message code="smartcontainer.admin.required.conceptMappings"/></b>
<div class="box">
<c:if test="${canManageMappings != true}">
	<div class="smart_info">
		<img src="<openmrs:contextPath />/images/info.gif" /> 
		<spring:message code="smartcontainer.cannotManageConceptMappings.info" />
	</div>
</c:if>
<form  method="post">
<table class="mappingsTable" cellpadding="5" cellspacing="0">
	<tr bgcolor="#D9D7D7">
		<th class="appHeader"><spring:message code="smartcontainer.appName" /></th>
		<th class="otherColumn"><spring:message code="smartcontainer.conceptName" /></th>
		<c:if test="${canManageMappings == true}">
		<th class="otherColumn"><spring:message code="Concept" /></th>
		</c:if>
		<th class="otherColumn"><spring:message code="smartcontainer.sourceCode" /></th>
		<th class="otherColumn"><spring:message code="smartcontainer.conceptSourceName" /></th>
	</tr>
	<!-- Need to maintain our own count to get the total count of mapping for all handlers -->
	<c:set var="mapCount" value="0" scope="page" />
	<c:forEach var="handlerListOfMappingsMapEntry" items="${handlerListOfMappingsMap}">
	
	<c:forEach var="conceptMapping" items="${handlerListOfMappingsMapEntry.value}" varStatus="varStatus">
	<tr <c:if test="${varStatus.index == 0}">class="firstRow"</c:if>>
		<c:if test="${varStatus.index == 0}">
		<th class="appColumn" rowspan="${fn:length(handlerListOfMappingsMapEntry.value)}" valign="top">${handlerListOfMappingsMapEntry.key}</th>
		</c:if>
		<td>${conceptMapping.conceptName}</td>
		<c:if test="${canManageMappings == true}">
		<td class="otherColumn">
			<c:choose>
			<c:when test="${empty conceptMapping.conceptId}">
			<openmrs_tag:conceptField formFieldName="conceptIds[${mapCount}]" />
			</c:when>
			<c:otherwise>
			<openmrs:format conceptId="${conceptMapping.conceptId}" />
			<input type="hidden"" name="conceptIds[${mapCount}]" value="" />
			</c:otherwise>
			</c:choose>
		</td>
		</c:if>
		<td class="otherColumn">
			${conceptMapping.sourceCode}
			<input type="hidden" name="sourceCodes" value="${conceptMapping.sourceCode}" />
		</td>
		<td class="otherColumn">
			${conceptMapping.conceptSourceName}
			<input type="hidden" name="sourceNames" value="${conceptMapping.conceptSourceName}" />
		</td>
	 </tr>
	 <c:set var="mapCount" value="${mapCount+1}" />
	</c:forEach>
	</c:forEach>
</table>
<c:if test="${canManageMappings == true}">
<br />
<div style="width: 50%">
	<i><spring:message code="smartcontainer.manageConceptMappings.info" /></i>
</div>
<br /><br />
<input type="submit" value='<spring:message code="general.save"/>'/>
</c:if>
</form>
</div>