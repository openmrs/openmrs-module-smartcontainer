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
package org.openmrs.module.smartcontainer.web.controller;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;
import org.openmrs.module.smartcontainer.AppService;
import org.openmrs.module.smartcontainer.app.App;
import org.openmrs.web.controller.PortletController;


/**
 *
 */
public class smartcontainerPortletController extends PortletController {
	public Log log=LogFactory.getLog(getClass());

	/**
     * @see org.openmrs.web.controller.PortletController#populateModel(javax.servlet.http.HttpServletRequest, java.util.Map)
     */
    @Override
    protected void populateModel(HttpServletRequest request, Map<String, Object> model) {
    	@SuppressWarnings("unchecked")
		List<App> apps = (List<App>) Context.getService(AppService.class).getAllApps();
    	model.put("appList",apps);
    	request.setAttribute("list", apps);
    	log.info("XXXXXXXXXXXXXXXXXXXX inside portlet");
	    //super.populateModel(request, model);
    }

}
