<%@ include file="localHeader.jsp" %>

<openmrs:require privilege="Manage Concept Mapping" otherwise="/login.htm" redirect="/module/smartcontainer/problemSetup.form" />
<h1><spring:message code="smartcontainer.admin.conceptMapping.title"/></h1>
<b class="boxHeader"><spring:message code="smartcontainer.admin.conceptMapping.vital"/></b>
<div class="box">
<form  method="post">
<table cellpadding="5" cellspacing="0">
<c:forEach var="concept" items="${mappedVitals}" varStatus="varStatus">
<tr>
<td>
${concept.key}
</td>
<td>
<openmrs:fieldGen type="org.openmrs.Concept" formFieldName="${fn:replace(concept.key,' ','')}" val="${concept.value}" />
</td>
</tr>
</c:forEach>
</table>
<input type="submit" value='<spring:message code="general.save"/>'/>

</form>
</div>


