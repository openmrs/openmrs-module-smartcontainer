<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>
<ul id="menu">
	<li class="first">
		<a href="${pageContext.request.contextPath}/admin"><spring:message code="admin.title.short"/></a>
	</li>
		<openmrs:hasPrivilege privilege="Manage Modules">
		<li <c:if test='<%= request.getRequestURI().contains("smartcontainerLink") %>'>class="active"</c:if>>
			<a href="${pageContext.request.contextPath}/module/smartcontainer/smartcontainerLink.form">
				<spring:message code="smartcontainer.admin.manage"/>
			</a>
		</li>
	</openmrs:hasPrivilege>
		<openmrs:hasPrivilege privilege="Manage Modules">
		<li <c:if test='<%= request.getRequestURI().contains("manageUserAppLink") %>'>class="active"</c:if>>
			<a href="${pageContext.request.contextPath}/module/smartcontainer/manageUserAppLink.form">
				<spring:message code="smartcontainer.admin.manage.user"/>
			</a>
		</li>
	</openmrs:hasPrivilege>
</ul>