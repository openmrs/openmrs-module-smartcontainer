package org.openmrs.module.smartcontainer.web.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;
import org.openmrs.module.smartcontainer.SmartAppService;
import org.openmrs.module.smartcontainer.SmartUser;
import org.openmrs.module.smartcontainer.SmartUserService;
import org.openmrs.module.smartcontainer.app.App;
import org.openmrs.web.controller.OptionsFormController;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

public class SmartAppOptionController extends OptionsFormController {
	
	Log log = LogFactory.getLog(getClass());
	
	Collection<App> allApps = null;
	
	Collection<App> userApps = null;
	
	protected ModelAndView processFormSubmission(HttpServletRequest request, HttpServletResponse response, Object object,
	        BindException errors) throws Exception {
		return super.processFormSubmission(request, response, object, errors);
		
	}
	
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object obj,
	        BindException errors) throws Exception {
		
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
		
		return super.onSubmit(request, response, obj, errors);
		
	}
	
	protected Object formBackingObject(HttpServletRequest request) throws ServletException {
		log.error("inside sucessfully");
		
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
		request.setAttribute("userApps", userApps);
		request.setAttribute("allApps", allApps);
		return super.formBackingObject(request);
		
	}
	
	protected Map<String, Object> referenceData(HttpServletRequest request) throws Exception {
		return super.referenceData(request);
		
	}
}
