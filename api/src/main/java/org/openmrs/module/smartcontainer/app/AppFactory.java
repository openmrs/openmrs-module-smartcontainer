/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.module.smartcontainer.app;

import java.io.File;
import java.util.Map;

/**
 * Used to construct a SMART App from its manifest file
 */
public class AppFactory {
	
	public static final String NAME = "name";
	
	public static final String DESCRIPTION = "description";
	
	public static final String AUTHOR = "author";
	
	public static final String MODE = "mode";
	
	public static final String VERSION = "version";
	
	public static final String BASEURL = "base_url";
	
	public static final String ICON = "icon";
	
	public static final String SMARTAPPID = "id";
	
	public static final String ACTIVITY = "activities";
	
	public static final String WEBHOOK = "web_hooks";
	
	public static final String DEFAULTAPP = "enabled_by_default";
	
	private Activity activity;
	
	private WebHook webHook;
	
	private ManifestParser pa;
	
	String temp = null;
	
	/**
	 * Method to construct the App
	 * 
	 * @param maniFile
	 * @return
	 */
	public App getApp(File maniFile) {
		
		App app = new App();
		pa = new ManifestParser();
		pa.parse(maniFile);
		return getApp(app, pa);
		
	}
	
	/**
	 * helper method
	 * 
	 * @param app2
	 * @param pa
	 * @return
	 */
	private App getApp(App app, ManifestParser pa) {
		
		app.setName((String) pa.get(NAME));
		
		app.setAuthor((String) pa.get(AUTHOR));
		app.setBaseURL((String) pa.get(BASEURL));
		app.setDefaultApp(Boolean.valueOf((String) pa.get(DEFAULTAPP)));
		app.setDescription((String) pa.get(DESCRIPTION));
		temp = (String) pa.get(ICON);
		removeBaseURL(temp, app.getBaseURL());
		app.setIcon(temp);
		app.setMode((String) pa.get(MODE));
		app.setsMARTAppId((String) pa.get(SMARTAPPID));
		app.setVersion((String) pa.get(VERSION));
		
		setActivity(app, (Map) pa.get(ACTIVITY));
		
		setWebhook(app, (Map) pa.get(WEBHOOK));
		return app;
	}
	
	/**
	 * helper method to set the web hooks
	 * 
	 * @param app2
	 * @param string
	 */
	private void setWebhook(App app, Map map) {
		if (!map.isEmpty()) {
			//pa.parse(webhookString);
			webHook = new WebHook();
			webHook.setName((String) map.keySet().toArray()[0]);
			map = (Map) (map.get(webHook.getName()));
			webHook.setDescription((String) map.get("description"));
			temp = (String) map.get("url");
			removeBaseURL(temp, app.getBaseURL());
			webHook.setURL(temp);
			app.setWebHook(webHook);
		}
		
	}
	
	/**
	 * helper method to set the activity
	 * 
	 * @param app2
	 * @param string
	 */
	private void setActivity(App app, Map map) {
		if (!map.isEmpty()) {
			
			activity = new Activity();
			activity.setActivityName((String) map.keySet().toArray()[0]);
			temp = (String) map.get(activity.getActivityName());
			removeBaseURL(temp, app.getBaseURL());
			activity.setActivityURL(temp);
			app.setActivity(activity);
		}
	}
	
	/**
	 * helper method to insert base url
	 * 
	 * @param url
	 * @param base
	 * @return
	 */
	private String removeBaseURL(String url, String base) {
		
		return url.replace("{base_url}", base);
	}
}
