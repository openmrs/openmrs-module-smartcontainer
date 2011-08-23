package org.openmrs.module.smartcontainer.web.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.User;
import org.openmrs.api.context.Context;
import org.openmrs.module.smartcontainer.SmartAppService;
import org.openmrs.module.smartcontainer.SmartUser;
import org.openmrs.module.smartcontainer.SmartUserService;
import org.openmrs.module.smartcontainer.app.App;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Lets users choose which apps they see on their homepage
 */
@Controller
@RequestMapping(value = "module/smartcontainer/manageUserAppLink.form")
public class ManageAppUserlevelController {
	
	Log log = LogFactory.getLog(getClass());
	
	private final String SUCCESS_FORM_VIEW = "/module/smartcontainer/addAppUserLevel";
	
	/**
	 * Map for user id-app-ids and a their access tokens. Map entries are of the form Map<String
	 * userId-appId, String token>
	 */
	private static Map<String, String> userAppAccessTokensMap = null;
	
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
	 * @param request
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST)
	public String deleteApp(HttpServletRequest request) {
		SmartUser smartUser = Context.getService(SmartUserService.class).getUserByName(
		    Context.getAuthenticatedUser().getSystemId());
		Collection<App> allApps = getSmartService().getAllApps();
		Collection<App> userApps = smartUser.getApps();
		if (userApps == null) {
			userApps = new ArrayList<App>();
		}
		
		String add[] = request.getParameterValues("add");
		String remove[] = request.getParameterValues("remove");
		log.debug("saving users........... add " + add + "remove :" + remove);
		if (add != null) {
			for (String app : add) {
				for (App a : allApps) {
					if (a.getName().equals(app)) {
						userApps.add(a);
						//register an access token for the started app
						userAppAccessTokensMap.put(Context.getAuthenticatedUser().getUserId() + "-" + a.getAppId(),
						    generateRandomAccessToken());
					}
				}
				
			}
		}
		if (remove != null) {
			for (String app : remove) {
				for (App a : allApps) {
					if (a.getName().equals(app)) {
						userApps.remove(a);
						//remove the token for the removed app
						userAppAccessTokensMap.remove(Context.getAuthenticatedUser().getUserId() + "-" + a.getAppId());
					}
				}
				
			}
		}
		SmartUser user = Context.getService(SmartUserService.class).getUserByName(
		    Context.getAuthenticatedUser().getSystemId());
		user.setApps((Set<App>) userApps);
		Context.getService(SmartUserService.class).saveUser(user);
		log.debug("saved user........... add " + user.getOpenMRSUser().getSystemId());
		return SUCCESS_FORM_VIEW;
		
	}
	
	/**
	 * This class returns the form backing object. This can be a string, a boolean, or a normal java
	 * pojo. The bean name defined in the ModelAttribute annotation and the type can be just defined
	 * by the return type of this method
	 */
	@ModelAttribute("userApps")
	protected Collection<App> formBackingObject(HttpServletRequest request) throws Exception {
		SmartAppService service = getSmartService();
		SmartUser smartUser = null;
		Collection<App> allApps = service.getAllApps();
		Collection<App> userApps = null;
		
		try {
			smartUser = Context.getService(SmartUserService.class).getUserByName(
			    Context.getAuthenticatedUser().getSystemId());
		}
		catch (IndexOutOfBoundsException ex) {
			if (smartUser == null) {
				smartUser = new SmartUser();
				smartUser.setOpenMRSUser(Context.getAuthenticatedUser());
				Context.getService(SmartUserService.class).saveUser(smartUser);
				
			}
		}
		userApps = smartUser.getApps();
		if (userApps == null) {
			userApps = new ArrayList<App>();
		}
		//request.setAttribute("userApps", userApps);
		request.setAttribute("allApps", allApps);
		return userApps;
	}
	
	private SmartAppService getSmartService() {
		return Context.getService(SmartAppService.class);
	}

	/**
	 * Returns an unmodifiable map of the user apps and their access tokens
	 * 
	 * @return
	 */
	public static Map<String, String> getUserAppAccessTokenMap() {
		if (userAppAccessTokensMap == null) {
			userAppAccessTokensMap = new HashMap<String, String>();
			//Grant access to all apps already started for the current user
			User user = Context.getAuthenticatedUser();
			Set<App> apps = (Set<App>) Context.getService(SmartUserService.class).getUserByName(user.getSystemId())
			        .getApps();
			for (App app : apps) {
				userAppAccessTokensMap.put(user.getUserId() + "-" + app.getAppId(), generateRandomAccessToken());
			}
		}
		//we don't want callers to make changes to the map
		return Collections.unmodifiableMap(userAppAccessTokensMap);
	}
	
	/**
	 * Utility method that generates access tokens randomly consisting of 9 characters
	 * 
	 * @return
	 */
	private static String generateRandomAccessToken() {
		return RandomStringUtils.randomAlphanumeric(9);
	}
}
