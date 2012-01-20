<%@ include file="localHeader.jsp" %>

<openmrs:require privilege="Manage SMART Apps" otherwise="/login.htm" redirect="/module/smartcontainer/smartUsers.list" />

<openmrs:htmlInclude file="/scripts/jquery/dataTables/css/dataTables_jui.css"/>
<openmrs:htmlInclude file="/scripts/jquery/dataTables/js/jquery.dataTables.min.js"/>

<script type="text/javascript">
	$j(document).ready(function(){
		$j("#smartUsersTable").dataTable({
			bJQueryUI: true
		});		
	});
</script>

<b class="boxHeader"><spring:message code="smartcontainer.admin.manage.user"/></b>

<div class="box">
	<div class="leftAligned">
	<table id="smartUsersTable" cellspacing="0" cellpadding="5" width="100%">
	  <thead>
		<tr>
			<th><spring:message code="general.name"/></th>
			<th><spring:message code="User.username"/></th>
			<th>&nbsp;</th>
		</tr>
	  </thead>
	  <tbody>
		<c:forEach var="smartUser" items="${smartUsers}" varStatus="varStatus">
		<tr>
			<td>
				${smartUser.openMRSUser.personName}
			</td>
			<td>
				${smartUser.openMRSUser.username}
			</td>
			<td>
				<a href="<openmrs:contextPath />/module/smartcontainer/manageUserHiddenApps.form?userName=${smartUser.openMRSUser.username}">
					<spring:message code="smartcontainer.manageHiddenApps"/>
				</a>
			</td>
		</tr>
		</c:forEach>
	  </tbody>
	</table>
	</div>
</div>