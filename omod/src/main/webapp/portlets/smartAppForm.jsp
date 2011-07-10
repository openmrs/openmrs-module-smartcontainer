<%@ page language="java" contentType="text/html; charset=US-ASCII"
	pageEncoding="US-ASCII"%>
<%@ include file="/WEB-INF/template/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=US-ASCII">
<title>SMART Apps in OpenMRS</title>
<!--<script-->
<!--	src="http://ajax.googleapis.com/ajax/libs/jquery/1.6.1/jquery.min.js"></script>-->
<script
	src="http://sandbox.smartplatforms.org/static/smart_ui_server/resources/class.js"></script>
<script
	src="http://sandbox.smartplatforms.org/static/smart_ui_server/resources/smart-api-container.js"></script>

<script
	src="http://sample-apps.smartplatforms.org/framework/smart/scripts/jschannel.js"></script>
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
	width: 100%; //
	height: 100%;
	border: 0px;
	box-sizing: border-box;
	-moz-box-sizing: border-box;
	-webkit-box-sizing: border-box;
	vertical-align: top;
	z-index: 5;
}

#iframe_holder iframe {
	width: 100%; //
	height: 100%;
	border: 2px dotted #ddd;
}

.activity_iframe {
	width: 100%; //
	height: 100%;
	border: 0px;
}
</style>
<script type="text/javascript">
	var SMART_HELPER = {};
	var already_running = {};

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
			callback("<?xml version='1.0'?>\
	                   <rdf:RDF xmlns:rdf='http://www.w3.org/1999/02/22-rdf-syntax-ns#' >\
	                  </rdf:RDF>");
		}

		else {
			var array = api_call.func.split("/");
			var pid = array[2].valueOf();
			var type = array[3];
			$j.ajax({
				beforeSend : function(xhr) {
					xhr.setRequestHeader("Authorization", " ");
				},
				dataType : "text",
				url : "${pageContext.request.contextPath}"
						+ "/module/smartcontainer/" + type + ".form" + "?pid="
						+ pid,
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

	var appSelected = function(app_id, url) {

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
</head>
<div id="main">
<div id="app_selector" style="float: left">
<table>
	<tbody>
		<c:forEach items="${model.list}" var="app" varStatus="status">
			<tr
				onclick="appSelected('${app.sMARTAppId}','${app.activity.activityURL}')">
				<td><input type="image" src="${app.icon}" /></td>
				<td><a>${app.name}</a></td>
			</tr>
		</c:forEach>
	</tbody>
</table>
</div>

<div id="iframe_holder"></div>
</div>

</html>