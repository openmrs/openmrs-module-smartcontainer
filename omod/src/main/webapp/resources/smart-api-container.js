/*
 * The SMArt container side of the API
 *
 * Josh Mandel
 * Ben Adida
 */

// simple pattern to match URLs with http or https
var __SMART_URL_PATTERN = /^(https?:\/\/[^/]+)/;

// extract a postMessage appropriate origin from a URL
function __SMART_extract_origin(url) {
    var match = url.match(__SMART_URL_PATTERN);
    if (match)
	return match[1];
    else
	return null;
}

SMART_CONTAINER = Class.extend({

    init: function(SMART_HELPER) {
	    this.debug = false;
	    this.SMART_HELPER = SMART_HELPER;
	    this.activities = {};
	},


    callback: function(f, params) {
	    var _this = this;
	    return function() {return f.apply(_this, arguments);};
    },

    context_changed: function() {
    	jQuery.each(this.activities, function(aid, a){
    		var c = a.channel;
    		if (c)  {
        	    c.call({method: "activitydestroy", success: function(){}});
    			c.destroy();
    		}
    	});

	    this.activities = {};	    
    },

    receive_api_call: function(activity, message, callback) {
	    this.SMART_HELPER.handle_api(activity, message, function(r){callback({contentType: "xml",  data: r})});	    
    },

    receive_ready: function(activity, callback) {	    
	    var _this = this;

	    var message = activity.context;
	    activity.ready = true;
		message.activity_id = activity.uuid;
		message.ready_data = activity.ready_data;
	        message.iframe_width = jQuery(activity.iframe).width();
   	        message.iframe_height = jQuery(activity.iframe).height();
		callback(message);
		activity.channel.destroy();
		
		activity.channel  = Channel.build(
	          {window: activity.iframe.contentWindow, 
	           origin: activity.origin, 
	           scope: activity.uuid, 
	           debugOutput: _this.debug});
		
		activity.channel.bind("api_call", function(t, p) {
			t.delayReturn(true);
			_this.receive_api_call(activity, p, t.complete);
		});

		activity.channel.bind("adjust_size", function(t, p) {
			_this.SMART_HELPER.handle_adjust_size(activity, p);
  	        });

		activity.channel.bind("start_activity", function(t, p) {
			t.delayReturn(true);
			_this.receive_start_activity_message(activity, p, t.complete);
		});

		activity.channel.bind("end_activity", function(t, p) {
			t.delayReturn(true);
			_this.receive_end_activity_message(activity,p);
		});

		activity.channel.bind("restart_activity", function(t, p) {
			t.delayReturn(true);
			_this.receive_restart_activity_message(activity,t.complete);
		});
    },

    foreground_activity: function(activity_id){
    	var activity = this.activities[activity_id];
	if (activity.channel !== undefined)
	    activity.channel.call({method: "activityforeground", success: function(){}});
    },

    background_activity: function(activity_id){
    	var activity = this.activities[activity_id];
	if (activity.channel !== undefined)
	    activity.channel.call({method: "activitybackground", success: function(){}});
    },
    
    start_activity: function(activity_name, app){
    	var message = {
			   name: activity_name,
			   app: app
			   };
    	
    	this.receive_start_activity_message(null, message);
    },
    

    end_activity: function(activity_id){
    	var activity = this.activities[activity_id];
    	this.receive_end_activity_message(activity, {response: null});
    },

    receive_restart_activity_message: function(activity, callback) {
	    var _this = this;
    	callback(true);
    	activity.channel.destroy();
	
    	this.clear_unbound_channels();
    	
	    activity.channel  = Channel.build({window: activity.iframe.contentWindow, 
	    								   origin: activity.origin, 
	    								   scope: "not_ready", 
	    								   debugOutput: _this.debug});

	    activity.channel.bind("ready", function(t, p) {
		    t.delayReturn(true);
		    _this.receive_ready(activity, t.complete);
	      	});
    },

    
    receive_end_activity_message: function(activity, message) {
    	var caller = activity.caller;
    	var response = message.response;    	
	
    	if (caller === undefined) return;

    	this.SMART_HELPER.handle_resume_activity(
						 caller, 
						 function() {
							 caller.callbacks[activity.uuid]({contentType: "xml", data: response});
						 });
    },

    receive_start_activity_message: function(activity, message, callback) {
    	var uuid = randomUUID();
    	if (activity !== null) {
    		if (activity.callbacks === undefined)
    			activity.callbacks = {};
    		activity.callbacks[uuid] = callback;
    	}

    	this.clear_unbound_channels();
    	
    	var new_activity = this.activities[uuid]= {
    		uuid: uuid,
    		caller: activity,
			name: message.name,
			app: message.app,
			ready_data: message.ready_data,
			ready: false
		};
    	
	var _this = this;
	this.SMART_HELPER.handle_start_activity(
			new_activity, 
			function(iframe) {
			    var origin  = __SMART_extract_origin(jQuery(iframe).attr('src'));
			    new_activity.origin = origin;
			    new_activity.iframe = iframe;
		    	new_activity.channel  = Channel.build({window: iframe.contentWindow, origin: origin, scope: "not_ready", debugOutput: _this.debug});							    

		    	// Make sure we've received context var before letting the app call "ready" 
		    	// to avoid a race condition.  (We could, alternatively, poll on  context 
		    	// when "ready" is called.)
			    _this.SMART_HELPER.handle_record_info(new_activity, function(context) {
			    	new_activity.context = context;

			    	new_activity.channel.bind("ready", function(t, p) {
					    t.delayReturn(true);
					    _this.receive_ready(new_activity, t.complete);
				      	});
			    });
		});
    },
    
    clear_unbound_channels: function() {
	    jQuery.each(this.activities, function(aid, activity) {
	    	if (activity.ready === false && activity.channel !== undefined)
	    	{	
	    		activity.channel.destroy();
	    		activity.channel = undefined;
	    	}
	    });
    }

 });

function randomUUID() {
	var s = [], itoh = '0123456789ABCDEF';
	// Make array of random hex digits. The UUID only has 32 digits in it, but
	// we
	// allocate an extra items to make room for the '-'s we'll be inserting.
	for ( var i = 0; i < 36; i++)
		s[i] = Math.floor(Math.random() * 0x10);
	// Conform to RFC-4122, section 4.4
	s[14] = 4; // Set 4 high bits of time_high field to version
	s[19] = (s[19] & 0x3) | 0x8; // Specify 2 high bits of clock sequence
	// Convert to hex chars
	for ( var i = 0; i < 36; i++)
		s[i] = itoh[s[i]];
	// Insert '-'s
	s[8] = s[13] = s[18] = s[23] = '-';
	return s.join('');
};