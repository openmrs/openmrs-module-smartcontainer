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

/**
 * This is a basic class for representing the SMART Apps in the module
 */
public final class App {
	
	private String name;
	
	private Integer appId;
	
	private String description;
	
	private String author;
	
	private String mode;
	
	private String version;
	
	private String baseURL;
	
	private String icon;
	
	private String sMARTAppId;
	
	private Activity activity;
	
	private WebHook webHook;
	
	private Boolean defaultApp;
	
	/**
     * 
     */
	
	public App() {
		
	}
	
	/**
	 * @param name
	 * @param appId
	 * @param description
	 * @param author
	 * @param mode
	 * @param version
	 * @param baseURL
	 * @param icon
	 * @param sMARTAppId
	 * @param defaultApp
	 */
	public App(String name, String description, String author, String mode, String version, String baseURL, String icon,
	    String sMARTAppId) {
		super();
		this.name = name;
		
		this.description = description;
		this.author = author;
		this.mode = mode;
		this.version = version;
		this.baseURL = baseURL;
		this.icon = icon;
		this.sMARTAppId = sMARTAppId;
		
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @return the appId
	 */
	public Integer getAppId() {
		return appId;
	}
	
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * @param appId the appId to set
	 */
	public void setAppId(Integer appId) {
		this.appId = appId;
	}
	
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * @return the author
	 */
	public String getAuthor() {
		return author;
	}
	
	/**
	 * @return the mode
	 */
	public String getMode() {
		return mode;
	}
	
	/**
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}
	
	/**
	 * @return the baseURL
	 */
	public String getBaseURL() {
		return baseURL;
	}
	
	/**
	 * @return the icon
	 */
	public String getIcon() {
		return icon;
	}
	
	/**
	 * @return the sMARTAppId
	 */
	public String getsMARTAppId() {
		return sMARTAppId;
	}
	
	/**
	 * @return the activity
	 */
	public Activity getActivity() {
		return activity;
	}
	
	/**
	 * @return the webHook
	 */
	public WebHook getWebHook() {
		return webHook;
	}
	
	/**
	 * @return the defaultApp
	 */
	public Boolean getDefaultApp() {
		return defaultApp;
	}
	
	/**
	 * @param author the author to set
	 */
	public void setAuthor(String author) {
		this.author = author;
	}
	
	/**
	 * @param mode the mode to set
	 */
	public void setMode(String mode) {
		this.mode = mode;
	}
	
	/**
	 * @param version the version to set
	 */
	public void setVersion(String version) {
		this.version = version;
	}
	
	/**
	 * @param baseURL the baseURL to set
	 */
	public void setBaseURL(String baseURL) {
		this.baseURL = baseURL;
	}
	
	/**
	 * @param icon the icon to set
	 */
	public void setIcon(String icon) {
		this.icon = icon;
	}
	
	/**
	 * @param sMARTAppId the sMARTAppId to set
	 */
	public void setsMARTAppId(String sMARTAppId) {
		this.sMARTAppId = sMARTAppId;
	}
	
	/**
	 * @param activity the activity to set
	 */
	public void setActivity(Activity activity) {
		this.activity = activity;
	}
	
	/**
	 * @param webHook the webHook to set
	 */
	public void setWebHook(WebHook webHook) {
		this.webHook = webHook;
	}
	
	/**
	 * @param defaultApp the defaultApp to set
	 */
	public void setDefaultApp(Boolean defaultApp) {
		this.defaultApp = defaultApp;
	}

	/**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
	    final int prime = 31;
	    int result = 1;
	    result = prime * result + ((name == null) ? 0 : name.hashCode());
	    return result;
    }

	/**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
	    if (this == obj)
		    return true;
	    if (obj == null)
		    return false;
	    if (getClass() != obj.getClass())
		    return false;
	    App other = (App) obj;
	    if (name == null) {
		    if (other.name != null)
			    return false;
	    } else if (!name.equals(other.name))
		    return false;
	    return true;
    }
	
}
