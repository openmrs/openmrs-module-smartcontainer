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
package org.openmrs.module.smartcontainer.web.controller.api;

import org.openmrs.module.smartcontainer.RdfSource;

/**
 * Superclass class for SMART controllers that process requests from SMART apps
 */
public class BaseSmartController<T extends RdfSource> {
	
	//this is set by spring
	private T resource;
	
	/**
	 * @return the resource
	 */
	public T getResource() {
		return resource;
	}
	
	/**
	 * @param resource the resource to set
	 */
	public void setResource(T resource) {
		this.resource = resource;
	}
}
