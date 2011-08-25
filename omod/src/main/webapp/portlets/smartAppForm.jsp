<%@ include file="/WEB-INF/template/include.jsp"%>

<!--<script-->
<!--	src="http://ajax.googleapis.com/ajax/libs/jquery/1.6.1/jquery.min.js"></script>-->
<openmrs:htmlInclude
	file="/moduleResources/smartcontainer/class.js" />
<openmrs:htmlInclude
	file="/moduleResources/smartcontainer/smart-api-container.js" />
<openmrs:htmlInclude
	file="/moduleResources/smartcontainer/jschannel.js" />

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
</style>
<script type="text/javascript">
	var SMART_HELPER = {};
	var already_running = {};
	var selectedAppId;
	var appIdAccessTokenMap = new Object();

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
</script>

<div id="main">
<div id="app_selector" style="float: left">
<table>
	<tbody>
		<c:forEach items="${model.list}" var="app" varStatus="status">
			<tr onclick="appSelected('${app.sMARTAppId}','${app.activity.activityURL}', '${app.appId}')">
				<td><input type="image" src="${app.icon}" /></td>
				<td><a>${app.name}</a></td>
			</tr>
			<script type="text/javascript">
				appIdAccessTokenMap['${app.appId}'] = '${model.appTokenMap[app.appId]}';
			</script>
		</c:forEach>
		<c:if test="${fn:length(model.list) == 0}">
			<tr><td><spring:message code="smartcontainer.noappsinstalledforusertochoose"/></td></tr>
		</c:if>
	</tbody>
</table>
</div>

<div id="iframe_holder"></div>
</div>
