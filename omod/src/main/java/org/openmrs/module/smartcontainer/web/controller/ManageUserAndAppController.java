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
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.User;
import org.openmrs.api.APIException;
import org.openmrs.api.UserService;
import org.openmrs.api.context.Context;
import org.openmrs.module.smartcontainer.SmartAppService;
import org.openmrs.module.smartcontainer.app.App;
import org.openmrs.module.smartcontainer.app.UserHiddenAppMap;
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
public class ManageUserAndAppController {
	
	private static final Log log = LogFactory.getLog(ManageUserAndAppController.class);
	
	private final String SMART_USER_URL = "/module/smartcontainer/users";
	
	private final String MANAGE_USER_APPS_FORM = "/module/smartcontainer/manageUserHiddenApps";
	
	@RequestMapping(method = RequestMethod.GET, value = SMART_USER_URL)
	public String showUserList(ModelMap model) {
		List<User> users = getUserService().getAllUsers();
		//don't include this user and daemon user
		users.remove(Context.getAuthenticatedUser());
		User daemonUser = getUserService().getUserByUuid("A4F30A1B-5EB9-11DF-A648-37A07F9C90FB");
		//remove the daemon users too
		if (daemonUser != null)
			users.remove(daemonUser);
		
		model.addAttribute("users", users);
		
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
	public String showManageUserHiddenAppsForm(ModelMap model, @RequestParam("uuid") String uuid) {
		User user = getUserService().getUserByUuid(uuid);
		Collection<App> hiddenApps = new ArrayList<App>();
		if (user != null)
			hiddenApps = getSmartAppService().getUserHiddenApps(user);
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
		String appIdsToHide[] = request.getParameterValues("appIdsToHide");
		String uuid = request.getParameter("uuid");
		if (StringUtils.isNotBlank(uuid)) {
			User user = getUserService().getUserByUuid(uuid);
			if (user != null) {
				try {
					SmartAppService service = getSmartAppService();
					List<App> hiddenApps = service.getUserHiddenApps(user);
					List<Integer> remainingHiddenAppIds = new ArrayList<Integer>();
					//unhide the hidden apps
					for (App app : hiddenApps) {
						if (ArrayUtils.contains(appIdsToHide, app.getAppId()))
							remainingHiddenAppIds.add(app.getAppId());
						else
							service.deleteUserHiddenAppMap(user, app);
					}
					
					//hide the remaining matching apps
					if (appIdsToHide != null) {
						for (String appId : appIdsToHide) {
							//this is already hidden
							if (remainingHiddenAppIds.contains(appId))
								continue;
							App app = service.getAppById(Integer.valueOf(appId));
							if (app != null)
								service.saveUserHiddenAppMap(new UserHiddenAppMap(user, app, Context.getAuthenticatedUser()));
						}
					}
					
					request.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "smartcontainer.user.apps.saved",
					    WebRequest.SCOPE_SESSION);
					
					return "redirect:" + SMART_USER_URL + ".list";
				}
				catch (APIException e) {
					log.warn("Error while saving user hidden apps:" + e.getMessage());
					request.setAttribute(WebConstants.OPENMRS_ERROR_ATTR, "smartcontainer.user.apps.save.error",
					    WebRequest.SCOPE_SESSION);
					
				}
			}
		}
		
		return MANAGE_USER_APPS_FORM;
	}
	
	@ModelAttribute("apps")
	protected Collection<App> getAllApps() {
		return getSmartAppService().getApps(true);
	}
	
	private SmartAppService getSmartAppService() {
		return Context.getService(SmartAppService.class);
	}
	
	private UserService getUserService() {
		return Context.getUserService();
	}
}
