<%@ include file="/WEB-INF/template/include.jsp"%>

<%@ include file="/WEB-INF/template/header.jsp"%>

<h2><spring:message code="smartcontainer.admin.manage" /></h2>

<br/>
<table>
  <tr>
   <th>Patient Id</th>
   <th>Name</th>
   <th>Identifier</th>
  </tr>
  <c:forEach var="app" items="${theAppList}">
      <tr>
        <td>${app.openMRSUser}</td>
        <td>${app.openMRSUser.username}</td>
        <td>${app.openMRSUser.username}</td>
      </tr>		
  </c:forEach>
</table>

<%@ include file="/WEB-INF/template/footer.jsp"%>
