<%@ include file="/WEB-INF/template/include.jsp"%>

<openmrs:htmlInclude file="/moduleResources/smartcontainer/smart-api-container.js" />
<openmrs:htmlInclude file="/dwr/interface/DWRSmartService.js" />
<openmrs:htmlInclude file="/moduleResources/smartcontainer/smart.css" />

<style type="text/css">
#main {
	position: relative;
	top: 0px;
	left: 0px;
	width: 100%;
	height: 100%;
}

#app_selector {
	position: absolute;
	top: 0px;
	left: 0px;
	width: 200px;
	hight: 100%;
	padding-top: 5px;
	padding-left: 10px;
	z-index: 10;
}

#app_selector a {
	cursor: pointer;
}

#iframe_holder {
	position: absolute;
	top: 0px;
	left: 0px;
	padding: 10px;
	padding-left: 205px;
	padding-bottom: 11px;
	width: 100%; 
	height: 100%;
	border: 0px;
	box-sizing: border-box;
	-moz-box-sizing: border-box;
	-webkit-box-sizing: border-box;
	vertical-align: top;
	z-index: 5;
}

#iframe_holder iframe {
	width: 100%; 
	height: 100%;
	border: 2px dotted #ddd;
}

.display_iframe {
	width: 100%; 
	height: 100%;
	border: 0px;
}
.errorMsg{
	display: none;
}
.faded{
	opacity:0.5; filter:alpha(opacity=50);
}
</style>
<script type="text/javascript">
	var already_running = {};
	var selectedAppId;
	var appIdAccessTokenMap = new Object();
	var needToReload = false;
	var SMART_HOST = new SMART_CONNECT_HOST();
	var smartIdManifestMap = new Object();
	
	var simple_context = { 
		record: {
			full_name : '${model.patient.personName}',
			id: '${model.patient.patientId}'
		},
		user: {
			id : '${model.currentUser.userId}',
			full_name : '${model.currentUser.personName}'
		}
	};
	
	SMART_HOST.get_credentials = function (app_instance, callback){
		callback({});
	};
	
	SMART_HOST.get_iframe = function (app_instance, callback){
	   	var frame_id = "app_content_iframe_" + app_instance.uuid;
		var h = $j('#iframe_holder');
		$j("iframe", h).hide();

		var frame = $j('<iframe SEAMLESS class="display_iframe" src="about:blank" id="'+frame_id+'">  </iframe>')
		h.append(frame);
		callback(frame[0]);

		$j(window).resize();
		already_running[app_instance.manifest.id] = frame_id;
	};

	SMART_HOST.handle_api = function(app_instance, api_call, callback) {
		if (api_call.method == "GET"
				&& api_call.func.match(/^\/capabilities\/$/)) {
			callback("<?xml version='1.0' encoding='utf-8'?>\
					<rdf:RDF xmlns:rdf='http://www.w3.org/1999/02/22-rdf-syntax-ns#'\
				         xmlns:sp='http://smartplatforms.org/terms#'>\
				  <rdf:Description rdf:about='"+"${pageContext.request.contextPath}"+"'>\
				    <rdf:type rdf:resource='http://smartplatforms.org/terms#Container'/>\
				    <sp:capability rdf:resource='http://smartplatforms.org/capability/SNOMED/lookup'/>\
				    </rdf:Description>\
				</rdf:RDF>");
			
		}else {
			var array = api_call.func.split("/");
			var pid = array[2].valueOf();
			var type = array[3];
			if (!api_call.params)
				api_call.params = {};
			//send the name of the param as the authorization header value
			api_call.params['appId'] = selectedAppId;
			api_call.params['accessToken'] = appIdAccessTokenMap[selectedAppId];
			
			$j.ajax({
				dataType : "text",
				url : "${pageContext.request.contextPath}"
						+ "/ws/smartcontainer/api" +api_call.func,
				contentType : api_call.contentType,
				data : api_call.params,
				type : api_call.method,
				success : callback,
				error : function(data) {
					$j('#appNameHolder').html(smartIdManifestMap[selectedAppId].name);
					$j('#appError').show();
				}
			});
		}
	};
	
	var appSelected = function(app_id, containerAppId) {
		$j('#appNameHolder').html("");
		$j('#appError').hide();
		if(selectedAppId)
			$j('#arrow_image_'+selectedAppId).hide();
		$j('#arrow_image_'+containerAppId).show();
		selectedAppId = containerAppId;
		if (already_running[app_id] == null) {
			SMART_HOST.launch_app(smartIdManifestMap[selectedAppId], simple_context);
		} else {
			var h = $j('#iframe_holder');
			$j("iframe", h).hide();
			$j("#" + already_running[app_id]).show();
		}
	};
	
	$j(document).ready(function() {
		$j("#hiddenApps").dialog({
			autoOpen: false,
			resizable: false,
			width:500,
			height:'auto',
			modal: true,
			beforeClose: function(event, ui){
				//refresh the page to reflect the changes
				if(needToReload)
					location.reload();
				
				//hide the error message just in case it is visible due to a
				//previous unsuccessful attempt to make a hidden app visible
				$j('#errorMsg-show').hide();
			}
		});
	});

	function showOrHideSmartApp(appId, hide, userUuid){
		var appNameHolderId = (hide)?"#appNameHolder-hide":"#appNameHolder-show";
		$j(appNameHolderId).html("");
		var id = (hide)?"#errorMsg-hide":"#errorMsg-show";
		$j(id).hide();
		
		DWRSmartService.showOrHideSmartApp(appId, hide, userUuid, userUuid, function(success) {
			if(success && hide){
				location.reload();
			}else if(success && !hide){
				//set to true so that on closing the dialog, we trigger a page refresh
				needToReload = true;
				//hide the row for the app that has been made visible
				$j('#'+appId+'-row').hide();
			}
			else if(!success){
				var appNameHolderId = (hide)?"#appNameHolder-hide":"#appNameHolder-show";
				$j(appNameHolderId).html(smartIdManifestMap[appId].name);
				var id = (hide)?"#errorMsg-hide":"#errorMsg-show";
				$j(id).show();
			}
		});
	}
	
	$j(window).resize(function() {
		var h = $j('#iframe_holder');
		var o = $j("#app_selector").offset();
		var available_h = window.innerHeight - o.top - 20;
		h.height(available_h);
		$j('.display_iframe').height(available_h);
	});
</script>
<c:if test="${fn:length(model.visibleApps) == 0}">
	${model.noVisibleAppsMsg}
</c:if>
<div id="appError" class="error errorMsg leftAligned">
	<spring:message code="smartcontainer.error.whileExecutingApp"/>&nbsp; <span id="appNameHolder"></span>
</div>
<div id="errorMsg-hide" class="error errorMsg leftAligned">
	<spring:message code="smartcontainer.error.failedToSaveChangesFor"/>&nbsp; <span id="appNameHolder-hide"></span>
</div>
<div id="main">
<div id="app_selector" style="float: left">

<c:if test="${fn:length(model.hiddenApps) > 0}">
<input class="smallButton" type="button" value='<spring:message code="smartcontainer.manageHiddenApps" />' 
	onclick="javascript:$j('#hiddenApps').dialog('open')" /><br /><br />
</c:if>
<table>
	<tbody>
		<c:forEach items="${model.visibleApps}" var="app" varStatus="status">
			<tr>
				<td valign="top" onclick="appSelected('${app.sMARTAppId}', '${app.appId}')">
					<input type="image" src="${app.icon}" />
				</td>
				<td valign="top" onclick="appSelected('${app.sMARTAppId}', '${app.appId}')">
					<a>${app.name}</a>
				</td>
				<td valign="top">
					<input class="faded" type="image" src="images/trash.gif" title="<spring:message code="smartcontainer.hide"/>" 
							onclick="showOrHideSmartApp('${app.appId}', true, '${model.currentUser.uuid}')" 
							style="border: none;"/>
				</td>
				<td valign="top">
					<input id="arrow_image_${app.appId}" type="image" src="moduleResources/smartcontainer/left_arrow.gif" 
							style="display: none; border: none;"/>
				</td>
			</tr>
			<script type="text/javascript">
				appIdAccessTokenMap['${app.appId}'] = '${model.appTokenMap[app.appId]}';
				smartIdManifestMap['${app.appId}'] = ${app.manifest};
			</script>
		</c:forEach>
	</tbody>
</table>
</div>

<c:if test="${fn:length(model.hiddenApps) > 0}">
<%-- Dialog for hidden apps --%>
<div id="hiddenApps" title="<spring:message code="smartcontainer.manageHiddenApps"/>">
	<form method="post">
		<div id="errorMsg-show" class="errorMsg" style="text-align: center;">
			<span class="error">
				<spring:message code="smartcontainer.error.failedToSaveChangesFor"/>&nbsp; <span id="appNameHolder-show"></span>
			</span>
		</div>
		<table class="defaultSmartTable" cellpadding="3" cellspacing="0" width="100%">
			<c:forEach var="hiddenApp" items="${model.hiddenApps}" varStatus="varStatus">
				<tr id="${hiddenApp.appId}-row">
					<td width="100%" <c:if test="${varStatus.index % 2 == 0}">class='evenRow'</c:if> valign="top">${hiddenApp.name}</td>
					<td <c:if test="${varStatus.index % 2 == 0}">class='evenRow'</c:if> valign="top">
						<input class="smallButton" type="button" value="<spring:message code="smartcontainer.show"/>" 
							onclick="showOrHideSmartApp('${hiddenApp.appId}', false, '${model.currentUser.uuid}')" />
					</td>
				</tr>
				<script type="text/javascript">
					smartIdManifestMap['${hiddenApp.appId}'] = ${hiddenApp.manifest};
				</script>
			</c:forEach>
			<tr>
				<td colspan="2" style="padding-top: 20px" align="center">
					<input type="button" align="right" value="<spring:message code="general.done" javaScriptEscape="true"/>"
						onclick="javascript:$j('#hiddenApps').dialog('close')" />
				</td>
			</tr>
		</table>
	</form>
</div>
</c:if>

<div id="iframe_holder"></div>
</div>