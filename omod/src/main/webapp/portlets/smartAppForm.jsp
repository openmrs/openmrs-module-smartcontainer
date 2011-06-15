<%@ page language="java" contentType="text/html; charset=US-ASCII"
    pageEncoding="US-ASCII"%>
<%@ include file="/WEB-INF/template/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=US-ASCII">
<title>hello World</title>
<script src="http://sandbox.smartplatforms.org/static/smart_ui_server/resources/class.js"></script> 
<script src="http://sandbox.smartplatforms.org/static/smart_ui_server/resources/smart-api-container.js"></script> 
<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.4.2/jquery.min.js"></script> 
<script src="http://sample-apps.smartplatforms.org/framework/smart/scripts/jschannel.js"></script> 
<style type="text/css">
#main { position: relative;}
#app_selector { position: absolute; top: 0px; left: 0px; width: 200px; padding-top: 5px; padding-left: 10px; z-index: 10;}
#iframe_holder {
  position: absolute;
  top: 0px; 
  left: 0px;
  padding-left: 210px;
  padding-top: 5px;
  width: 100%; height: 100%;
  border: none; 
  box-sizing: border-box; -moz-box-sizing: border-box; -webkit-box-sizing: border-box;  
  vertical-align: top;
//  border: 10px dotted #ddd;
  z-index: 5;
// makes scrollbar disappear!  padding-left: 15px;
}
</style>
<script> 
var SMART_HELPER = {};

SMART_HELPER.handle_record_info = function(app, callback) {
    callback({
            'record' : {
                'full_name' : 'Anonymous',
                'id' : '1000000012'
                 },
            'user' : {
                'full_name' : 'Logged.In.User',
                'id' : 'some_user_id'
                 }
    });
};
 
 
app_lookup = {
  'medlist@apps.smart.org' : 'http://sample-apps.smartplatforms.org/framework/med_list/bootstrap.html',
  'smart-problems@apps.smart.org' :'http://sample-apps.smartplatforms.org/framework/problem_list/bootstrap.html',
  'got-statins@apps.smart.org' : 'http://sample-apps.smartplatforms.org/framework/got_statins/bootstrap.html'
};
var appURL
 
SMART_HELPER.handle_start_activity = function(activity, callback) {
 
 
  var url = appURL;
  var frame_id = "app_content_iframe_"+randomUUID();
  var frame = $('<iframe SEAMLESS src="'+url+'" id="'+frame_id+'" hight="100%" width="100%" >  </iframe><br>')
  $('#iframe_holder').append(frame);
  var iframe = $("#"+frame_id);
  var cont = $("#iframe_holder");
  $("#iframe_holder").hide();

  cont.show();
  cont.css("border", "0px");
  $("iframe", cont).hide();
  iframe.show();
  callback(frame[0]);
 
};
 
 
SMART_HELPER.handle_api = function(activity, api_call, callback)
{
      if (api_call.method=="GET" && api_call.func.match(/^\/capabilities\/$/))
      {
        callback("<?xml version='1.0'?>\
                   <rdf:RDF xmlns:rdf='http://www.w3.org/1999/02/22-rdf-syntax-ns#' >\
                  </rdf:RDF>");
      }
 
 
      // Return a fake medication list containing just simvastatin...
      else if (api_call.method=="GET" && api_call.func.match(/^.*\/medications\/$/))
      {
//		  $.get('http://174.129.42.191'+api_call.func, callback);
		  $.get('sample_data.xml', callback, 'text');
      }
     else
        alert("Function " + api_call.func + " not implemented yet.");
 
};
 
SMART = new SMART_CONTAINER(SMART_HELPER);
 
var appSelected = function(app_id,url) {
	appURL=url
  SMART.start_activity("main", app_id);
};
</script> 
</head>
<body>
<div id="main">
<div id="app_selector" style="float:left">
<table>
<tbody>
<c:forEach  items="${list}" var="app" varStatus="status" >
<tr onclick="appSelected('${app.sMARTAppId}','${app.activity.activityURL}')" ><td><input type="image" src="${app.icon}"/></td><td><a >${app.name}</a></td></tr>
</c:forEach>
</tbody>
</table>
</div>

<div id="iframe_holder" style="float:right;hight:450;width:900">

</div>
</div>
</body>
</html>