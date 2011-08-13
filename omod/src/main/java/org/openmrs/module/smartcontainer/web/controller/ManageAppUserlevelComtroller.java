package org.openmrs.module.smartcontainer.web.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
 * @author aja
 *
 */
@Controller
@RequestMapping(value = "module/smartcontainer/manageUserAppLink.form")
public class ManageAppUserlevelComtroller {
Log log = LogFactory.getLog(getClass());
	
	Collection<App> allApps = null;
	
	Collection<App> userApps = null;
	private final String SUCCESS_FORM_VIEW = "/module/smartcontainer/addAppUserLevel";
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
	 * @param httpSession
	 * @param anyRequestObject
	 * @param errors
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST)
	public String deleteApp(HttpServletRequest request) {
		String add[] = request.getParameterValues("add");
		String remove[] = request.getParameterValues("remove");
		log.error("saving users........... add " + add + "remove :" + remove);
		if (add != null) {
			for (String app : add) {
				for (App a : allApps) {
					if (a.getName().equals(app)) {
						userApps.add(a);
					}
				}
				
			}
		}
		if (remove != null) {
			for (String app : remove) {
				for (App a : allApps) {
					if (a.getName().equals(app)) {
						userApps.remove(a);
					}
				}
				
			}
		}
		SmartUser user = Context.getService(SmartUserService.class)
		        .getUserByName(Context.getAuthenticatedUser().getSystemId());
		user.setApps((Set<App>) userApps);
		Context.getService(SmartUserService.class).saveUser(user);
		log.error("saved user........... add " + user.getOpenMRSUser().getSystemId());
		return SUCCESS_FORM_VIEW;
		
	}
	/**
	 * This class returns the form backing object. This can be a string, a boolean, or a normal java
	 * pojo. The bean name defined in the ModelAttribute annotation and the type can be just defined
	 * by the return type of this method
	 */
	@ModelAttribute("userApps")
	protected Collection<App> formBackingObject(HttpServletRequest request) throws Exception {
		SmartUser smartUser = null;
		SmartAppService service = Context.getService(SmartAppService.class);
		allApps = service.getAllApps();
		try {
			smartUser = Context.getService(SmartUserService.class).getUserByName(Context.getAuthenticatedUser().getSystemId());
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
}
