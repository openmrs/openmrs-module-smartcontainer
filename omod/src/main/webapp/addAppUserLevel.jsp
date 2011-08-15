
<%@ include file="localHeader.jsp" %>
<html>
<b class="boxHeader"><spring:message code="smartcontainer.addAppUserLevel"/></b>
<div class="box" id="moduleListing">
<form method="post">
<table cellpadding="5" cellspacing="0">
<c:forEach var="app" items="${allApps}" varStatus="varStatus">
<tr>
<td valign="top"><input type="image" src="${app.icon}" /></td>
<td valign="top">${app.name}</td>
<td valign="top">${fn:substring(fn:escapeXml(app.description),0,
					200)}...</td>
<c:choose>
			<c:when test="${fn:contains(userApps, app)}"><td valign="top"><input type="checkbox" name="remove" value="${app.name}"/>remove</td></c:when>
			<c:otherwise><td valign="top"><input type="checkbox"  name="add" value="${app.name}"/>add</td></c:otherwise>
</c:choose>
</tr>

</c:forEach>

</table>
<input type="submit" align="right" name="save" value="<spring:message code="smartcontainer.addAppUserLevel.save" javaScriptEscape="true"/>" >
</form>
</div>
</html>