<%@ include file="/WEB-INF/template/include.jsp"%>

<!--<script-->
<!--	src="http://ajax.googleapis.com/ajax/libs/jquery/1.6.1/jquery.min.js"></script>-->
<openmrs:htmlInclude file="/moduleResources/smartcontainer/class.js" />
<openmrs:htmlInclude file="/moduleResources/smartcontainer/smart-api-container.js" />
<openmrs:htmlInclude file="/moduleResources/smartcontainer/jschannel.js" />
<openmrs:htmlInclude file="/moduleResources/smartcontainer/smart.css" />
<openmrs:htmlInclude file="/dwr/interface/DWRSmartService.js" />

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

.activity_iframe {
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
	var SMART_HELPER = {};
	var already_running = {};
	var selectedAppId;
	var appIdAccessTokenMap = new Object();
	var needToReload = false;

	SMART_HELPER.handle_record_info = function(app, callback) {
		callback({
			'user' : {
				'id' : '${model.currentUser.userId}',
				'full_name' : '${model.currentUser.personName}'
			},
			'record' : {
				'full_name' : '${model.patient.personName}',
				'id' : '${model.patient.patientId}'
			},
			'credentials' : {
				'token' : '',
				'secret' : '',
				'oauth_cookie' : ''
			}
		});
	};

	var appURL

	SMART_HELPER.handle_start_activity = function(activity, callback) {

		var url = appURL;
		var frame_id = "app_content_iframe_" + randomUUID();
		var h = $j('#iframe_holder');
		$j("iframe", h).hide();

		var frame = $j('<iframe SEAMLESS class="activity_iframe" src="'+url+'" id="'+frame_id+'">  </iframe>')
		h.append(frame);
		callback(frame[0]);

		$j(window).resize();
		already_running[activity.app] = frame_id;
	};
	$j(window).resize(function() {
		var h = $j('#iframe_holder');
		var o = $j("#app_selector").offset();
		var available_h = window.innerHeight - o.top - 20;
		h.height(available_h);
		$j('.activity_iframe').height(available_h);
	});
	SMART_HELPER.handle_resume_activity = function(activity, callback) {
		console.log("resume activity");
	};

	SMART_HELPER.handle_api = function(activity, api_call, callback) {
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
		}

		else {
			var array = api_call.func.split("/");
			var pid = array[2].valueOf();
			var type = array[3];
			if (!activity.params)
				activity.params = {};
			//send the name of the param as the authorization header value
			activity.params['appId'] = selectedAppId;
			activity.params['accessToken'] = appIdAccessTokenMap[selectedAppId];
			$j.ajax({
				dataType : "text",
				url : "${pageContext.request.contextPath}"
						+ "/ws/smartcontainer/api" +api_call.func,
				contentType : activity.contentType,
				data : activity.params,
				type : activity.method,
				success : callback,
				error : function(data) {
					alert("error");
				}
			});
		}

	};

	SMART = new SMART_CONTAINER(SMART_HELPER);

	var appSelected = function(app_id, url, containerAppId) {
		selectedAppId = containerAppId;
		if (already_running[app_id] == null) {

			appURL = url
			SMART.start_activity("main", app_id);
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

	function showOrHideSmartApp(appId, hide){
		
		DWRSmartService.showOrHideSmartApp(appId, hide, null, function(success) {
			if(success && hide){
				location.reload();
			}else if(success && !hide){
				//set to true so that on closing the dialog, we trigger a page refresh
				needToReload = true;
				//hide the row for the app that has been made visible
				$j('#'+appId+'-row').hide();
			}
			else if(!success){
				var id = (hide)?"#errorMsg-hide":"#errorMsg-show";
				$j(id).show();
			}
		});
	}
</script>
<c:if test="${fn:length(model.visibleApps) == 0}">
	<spring:message code="smartcontainer.noappsinstalledOrshowHidden"/>
</c:if>
<div id="main">
<div id="app_selector" style="float: left">
<span id="errorMsg-hide" class="error errorMsg"><spring:message code="smartcontainer.failedToSaveChanges"/></span>

<c:if test="${fn:length(model.hiddenApps) > 0}">
<input class="smallButton" type="button" value='<spring:message code="smartcontainer.manageHiddenApps" />' 
	onclick="javascript:$j('#hiddenApps').dialog('open')" /><br /><br />
</c:if>
<table>
	<tbody>
		<c:forEach items="${model.visibleApps}" var="app" varStatus="status">
			<tr>
				<td onclick="appSelected('${app.sMARTAppId}','${app.activity.activityURL}', '${app.appId}')">
					<input type="image" src="${app.icon}" />
				</td>
				<td onclick="appSelected('${app.sMARTAppId}','${app.activity.activityURL}', '${app.appId}')">
					<a>${app.name}</a>
				</td>
				<td>
					<input class="faded" type="image" src="images/trash.gif" title="<spring:message code="smartcontainer.hide"/>" 
							onclick="showOrHideSmartApp('${app.appId}', true)" />
				</td>
			</tr>
			<script type="text/javascript">
				appIdAccessTokenMap['${app.appId}'] = '${model.appTokenMap[app.appId]}';
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
			<span class="error"><spring:message code="smartcontainer.failedToSaveChanges"/></span>
		</div>
		<table class="defaultSmartTable" cellpadding="3" cellspacing="0" width="100%">
			<c:forEach var="hiddenApp" items="${model.hiddenApps}" varStatus="varStatus">
				<tr id="${hiddenApp.appId}-row">
					<td width="100%" <c:if test="${varStatus.index % 2 == 0}">class='evenRow'</c:if> valign="top">${hiddenApp.name}</td>
					<td <c:if test="${varStatus.index % 2 == 0}">class='evenRow'</c:if> valign="top">
						<input class="smallButton" type="button" value="<spring:message code="smartcontainer.show"/>" 
							onclick="showOrHideSmartApp('${hiddenApp.appId}', false)" />
					</td>
				</tr>
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