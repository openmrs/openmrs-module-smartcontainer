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

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.simple.parser.ParseException;
import org.openmrs.api.context.Context;
import org.openmrs.module.smartcontainer.SmartAppService;
import org.openmrs.module.smartcontainer.SmartUser;
import org.openmrs.module.smartcontainer.SmartUserService;
import org.openmrs.module.smartcontainer.app.App;
import org.openmrs.module.smartcontainer.app.AppFactory;
import org.openmrs.web.WebConstants;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 */
@Controller
@RequestMapping(value = "module/smartcontainer/smartcontainerLink.form")
public class SmartAppListController {
	
	/**
	 * Logger for this class and subclasses
	 */
	protected final Log log = LogFactory.getLog(getClass());
	
	/**
	 * Map for app-ids and a their access tokens. Map entries are of the form Map<Integer appId,
	 * String token>
	 */
	private static Map<Integer, String> appAccessTokenMap;
	
	/**
	 * Success form view name
	 */
	private final String SUCCESS_FORM_VIEW = "/module/smartcontainer/smartcontainerForm";
	
	/**
	 * Initially called after the formBackingObject method to get the landing form name
	 * 
	 * @return String form view name
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String showForm() {
		return SUCCESS_FORM_VIEW;
		
	}
	
	/**
	 * All the parameters are optional based on the necessity
	 * 
	 * @param action
	 * @param request
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView deleteApp(@RequestParam("action") String action, HttpServletRequest request) {
		
		ModelAndView modelAndView = new ModelAndView();
		SmartAppService appService = Context.getService(SmartAppService.class);
		SmartUserService userService = Context.getService(SmartUserService.class);
		Boolean isUploadFromURL = ServletRequestUtils.getBooleanParameter(request, "updateFromURL", false);
		HttpSession httpSession = request.getSession();
		//
		if ("removeApp".equals(action)) {
			Integer appId = null;
			try {
				appId = ServletRequestUtils.getIntParameter(request, "appId");
			}
			catch (ServletRequestBindingException e) {
				
				log.error("Unable to get appId parameter from request", e);
			}
			
			App app = appService.getAppById(appId);
			Collection<App> userHiddenApps = null;
			//delete all referencing rows in the user hidden app table
			for (SmartUser user : userService.getAllUsers()) {
				
				userHiddenApps = user.getHiddenApps();
				userHiddenApps.remove(app);
				userService.saveUser(user);
				
			}
			
			appService.deleteApp(app);
			//remove this app's access token
			appAccessTokenMap.remove(app.getAppId());
			
		} else if ("upload".equals(action)) {
			App newApp = null;
			if (isUploadFromURL) {
				String url = ServletRequestUtils.getStringParameter(request, "manifestURL", "");
				log.info("URL stirng :" + url);
				
				try {
					newApp = AppFactory.getAppFromUrl(url);
					log.info("APP  :" + newApp);
					List<App> apps = (List<App>) appService.getAllApps();
					if (!apps.contains(newApp)) {
						newApp.setRetire(false);
						appService.saveApp(newApp);
						//generate an access token for this app
						appAccessTokenMap.put(newApp.getAppId(), generateRandomAccessToken());
					}
				}
				catch (MalformedURLException e) {
					httpSession.setAttribute(WebConstants.OPENMRS_ERROR_ATTR, "Malformed URL");
					log.error("Malformed URL", e);
				}
				catch (ParseException e) {
					httpSession.setAttribute(WebConstants.OPENMRS_ERROR_ATTR, "smartcontainer.upload.invalidFile");
					log.error("The manifest file you uploaded appears to be invalid", e);
				}
				catch (IOException e) {
					httpSession.setAttribute(WebConstants.OPENMRS_ERROR_ATTR, "Malformed Manifest file");
					log.error("Malformed Manifest file", e);
				}
				
			} else {
				MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
				MultipartFile multipartFile = multipartRequest.getFile("moduleFile");
				
				try {
					newApp = AppFactory.getAppFromLocalFile(multipartFile.getInputStream());
					List<App> apps = (List<App>) appService.getAllApps();
					if (!apps.contains(newApp)) {
						newApp.setRetire(false);
						appService.saveApp(newApp);
						//generate an access token for this app
						appAccessTokenMap.put(newApp.getAppId(), generateRandomAccessToken());
					} else {

					}
				}
				catch (ParseException e) {
					httpSession.setAttribute(WebConstants.OPENMRS_ERROR_ATTR, "smartcontainer.upload.invalidFile");
					log.error("The manifest file you uploaded appears to be invalid", e);
				}
				catch (IOException e1) {
					httpSession.setAttribute(WebConstants.OPENMRS_ERROR_ATTR, "Malformed Manifest file");
					log.error("Malformed Manifest file", e1);
				}
				
			}
		}
		//
		Collection<App> apps = Context.getService(SmartAppService.class).getAllApps();
		modelAndView.setViewName(SUCCESS_FORM_VIEW);
		modelAndView.addObject("appList", apps);
		return modelAndView;
	}
	
	/**
	 * This class returns the form backing object. This can be a string, a boolean, or a normal java
	 * pojo. The bean name defined in the ModelAttribute annotation and the type can be just defined
	 * by the return type of this method
	 */
	@ModelAttribute("appList")
	protected Collection<App> formBackingObject(HttpServletRequest request) throws Exception {
		
		Collection<App> apps = Context.getService(SmartAppService.class).getAllApps();
		// Context.getService(UserService.class).saveUser(new SMARTAppUser());
		
		return apps;
	}
	
	/**
	 * Returns an unmodifiable map of the user apps and their access tokens
	 * 
	 * @return
	 */
	public static Map<Integer, String> getAppAccessTokenMap() {
		if (appAccessTokenMap == null) {
			appAccessTokenMap = new HashMap<Integer, String>();
			
			Collection<App> allApps = Context.getService(SmartAppService.class).getAllApps();
			//Grant access to all uploaded apps
			for (App app : allApps) {
				appAccessTokenMap.put(app.getAppId(), generateRandomAccessToken());
			}
		}
		//we don't want callers to make changes to the map
		return Collections.unmodifiableMap(appAccessTokenMap);
	}
	
	/**
	 * Utility method that generates access tokens randomly consisting of 12 characters
	 * 
	 * @return
	 */
	private static String generateRandomAccessToken() {
		return RandomStringUtils.randomAlphanumeric(12);
	}
	
}
