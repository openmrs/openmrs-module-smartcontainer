<%@ include file="localHeader.jsp" %>

<openmrs:require privilege="Manage SMART Apps" otherwise="/login.htm" redirect="/module/smartcontainer/problemSetup.form" />

<b class="boxHeader"><spring:message code="smartcontainer.problemSetup"/></b>
<div class="box">
	<form method="post">
		<table cellpadding="5" cellspacing="0">
			<tr>
				<td width="25%">
					<spring:message code="smartcontainer.useProblemObject"/>
					<br/>
					<span class="description"><spring:message code="smartcontainer.useProblemObject.help"/></span>
				</td>
				<td>
					<input type="checkbox" name="useProblemObject" <c:if test="${useProblemObject == true}">checked=checked</c:if> value="true" />
				</td>
			</tr>
			<tr>
				<td rowspan="2" valign="top" width="25%">
					<spring:message code="smartcontainer.useObs"/>
					<br/>
					<span class="description"><spring:message code="smartcontainer.useObs.help"/></span>
				</td>
				<td>
					<input type="checkbox" name="useObs" <c:if test="${useObs == true}">checked=checked</c:if> onclick="toggleObsInputs()" value="true" />
				</td>
			</tr>
			<tr>
				<td>
					<table id="obsInputs">
						<tr>
							<td>
								<spring:message code="smartcontainer.problemAdded"/>
							</td>
							<td>
								<openmrs:fieldGen type="org.openmrs.Concept" formFieldName="problemAdded" val="${problemAdded}" />
							</td>
						</tr>
						<tr>
							<td>
								<spring:message code="smartcontainer.problemResolved"/>
							</td>
							<td>
								<openmrs:fieldGen type="org.openmrs.Concept" formFieldName="problemResolved" val="${problemResolved}" />
							</td>
						</tr>
					</table>
				</td>
		</table>
		
		<input type="submit" value='<spring:message code="general.save"/>'/>
	</form>
</div>


<script type="text/javascript">
	
	/**
	 * Show or hide the text boxes for choosing the concepts for problem started and problem stopped
	 */
	function toggleObsInputs() {
		jQuery("#obsInputs").toggle();
	}
	
	<c:if test="${useObs != true}">
		// hiding the obs input boxes because the useObs box is not checked at page load time 
		jQuery("#obsInputs").hide();
	</c:if>
</script>
