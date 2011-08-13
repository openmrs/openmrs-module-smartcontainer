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
 * Represents Activity corresponding to a SMART App
 */
public class Activity {

	private Integer activityId;

	private String activityName;

	private String activityURL;

	/**
	 * @return the activityId
	 */
	public Integer getActivityId() {
		return activityId;
	}

	/**
	 * @param activityId
	 *            the activityId to set
	 */
	public void setActivityId(Integer activityId) {
		this.activityId = activityId;
	}

	/**
	 * @return the activityName
	 */
	public String getActivityName() {
		return activityName;
	}

	/**
	 * @param activityName
	 * @param activityURL
	 */
	public Activity(String activityName, String activityURL) {
		super();
		this.activityName = activityName;
		this.activityURL = activityURL;
	}

	/**
	 * @return the activityURL
	 */
	public String getActivityURL() {
		return activityURL;
	}

	/**
	 * @param activityName
	 *            the activityName to set
	 */
	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	/**
	 * @param activityURL
	 *            the activityURL to set
	 */
	public void setActivityURL(String activityURL) {
		this.activityURL = activityURL;
	}

	/**
	 * no-arg constructor
	 */
	public Activity() {

	}
}
