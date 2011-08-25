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

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.APIException;
import org.openmrs.api.context.Context;
import org.openmrs.module.smartcontainer.SmartAppService;
import org.openmrs.module.smartcontainer.SmartUser;
import org.openmrs.module.smartcontainer.SmartUserService;
import org.openmrs.module.smartcontainer.app.App;
import org.openmrs.web.WebConstants;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;

/**
 * Controller for listing all smart users and for handling requests for managing users' hidden smart
 * apps by the admin
 */
@Controller
public class ManageSmartUserAndAppController {
	
	private static final Log log = LogFactory.getLog(ManageSmartUserAndAppController.class);
	
	private final String SMART_USER_URL = "/module/smartcontainer/smartUsers";
	
	private final String MANAGE_USER_APPS_FORM = "/module/smartcontainer/manageUserHiddenApps";
	
	@RequestMapping(method = RequestMethod.GET, value = SMART_USER_URL)
	public String showSmartUserList(ModelMap model) {
		Collection<SmartUser> smartUsers = getSmartUserService().getAllUsers();
		//don't include this user
		smartUsers.remove(smartUsers.remove(getSmartUserService()
		        .getUserByName(Context.getAuthenticatedUser().getUsername())));
		model.addAttribute("smartUsers", getSmartUserService().getAllUsers());
		return SMART_USER_URL;
		
	}
	
	/**
	 * Processes requests for displaying a list of smart users
	 * 
	 * @param model
	 * @param username
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = MANAGE_USER_APPS_FORM)
	public String showManageUserHiddenAppsForm(ModelMap model, @RequestParam("systemId") String systemId) {
		SmartUser smartUser = getSmartUserService().getUserByName(systemId);
		Collection<App> hiddenApps = new ArrayList<App>();
		if (smartUser != null)
			hiddenApps = smartUser.getHiddenApps();
		model.addAttribute("hiddenApps", hiddenApps);
		
		return MANAGE_USER_APPS_FORM;
		
	}
	
	/**
	 * Process requests for saving changes when hiding/showing user smart apps
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST)
	public String handleSubmission(WebRequest request) {
		String appIdsToRemove[] = request.getParameterValues("appIdsToHide");
		Collection<App> hiddenApps = new LinkedHashSet<App>();
		if (appIdsToRemove != null) {
			Collection<App> allApps = getSmartService().getAllApps();
			for (App app : allApps) {
				if (ArrayUtils.contains(appIdsToRemove, app.getAppId().toString()))
					hiddenApps.add(app);
			}
			
		}
		
		String systemId = request.getParameter("systemId");
		try {
			SmartUser smartUser = getSmartUserService().getUserByName(systemId);
			if (smartUser != null) {
				smartUser.setHiddenApps(hiddenApps);
				Context.getService(SmartUserService.class).saveUser(smartUser);
			}
			request.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "smartcontainer.user.apps.saved", WebRequest.SCOPE_SESSION);
			
			return "redirect:" + SMART_USER_URL + ".list";
		}
		catch (APIException e) {
			log.warn("Error while saving user hidden apps:" + e.getMessage());
			request.setAttribute(WebConstants.OPENMRS_ERROR_ATTR, "smartcontainer.user.apps.save.error",
			    WebRequest.SCOPE_SESSION);
			
		}
		
		return MANAGE_USER_APPS_FORM;
	}
	
	@ModelAttribute("apps")
	protected Collection<App> getAllApps() {
		return getSmartService().getAllApps();
	}
	
	private SmartAppService getSmartService() {
		return Context.getService(SmartAppService.class);
	}
	
	private SmartUserService getSmartUserService() {
		return Context.getService(SmartUserService.class);
	}
}
