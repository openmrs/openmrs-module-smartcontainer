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
import org.openmrs.module.smartcontainer.util.SmartDataHandlerUtil;
import org.openmrs.web.controller.OptionsFormController;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

public class SmartAppOptionController extends OptionsFormController {
	
	Log log = LogFactory.getLog(getClass());
	
	private SmartAppService service;
	
	protected ModelAndView processFormSubmission(HttpServletRequest request, HttpServletResponse response, Object object,
	                                             BindException errors) throws Exception {
		return super.processFormSubmission(request, response, object, errors);
		
	}
	
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object obj,
	                                BindException errors) throws Exception {
		Collection<App> allApps = service.getAllApps();
		Collection<App> userHiddenApps = Context.getService(SmartUserService.class)
		        .getUserByName(SmartDataHandlerUtil.getUserNameOrSystemId(Context.getAuthenticatedUser())).getHiddenApps();
		String add[] = request.getParameterValues("add");
		String remove[] = request.getParameterValues("remove");
		log.debug("saving users........... add " + add + "remove :" + remove);
		if (add != null) {
			for (String app : add) {
				for (App a : allApps) {
					if (a.getName().equals(app)) {
						userHiddenApps.add(a);
					}
				}
				
			}
		}
		if (remove != null) {
			for (String app : remove) {
				for (App a : allApps) {
					if (a.getName().equals(app)) {
						userHiddenApps.remove(a);
					}
				}
				
			}
		}
		SmartUser user = Context.getService(SmartUserService.class).getUserByName(
				SmartDataHandlerUtil.getUserNameOrSystemId(Context.getAuthenticatedUser()));
		user.setHiddenApps((Set<App>) userHiddenApps);
		Context.getService(SmartUserService.class).saveUser(user);
		log.debug("saved user........... add " + SmartDataHandlerUtil.getUserNameOrSystemId(user.getOpenMRSUser()));
		
		return super.onSubmit(request, response, obj, errors);
		
	}
	
	protected Object formBackingObject(HttpServletRequest request) throws ServletException {
		
		SmartUser smartUser = null;
		service = Context.getService(SmartAppService.class);
		Collection<App> allApps = service.getAllApps();
		try {
			smartUser = Context.getService(SmartUserService.class).getUserByName(
					SmartDataHandlerUtil.getUserNameOrSystemId(Context.getAuthenticatedUser()));
		}
		catch (IndexOutOfBoundsException ex) {
			if (smartUser == null) {
				smartUser = new SmartUser();
				smartUser.setOpenMRSUser(Context.getAuthenticatedUser());
				Context.getService(SmartUserService.class).saveUser(smartUser);
				
			}
		}
		Collection<App> userApps = smartUser.getHiddenApps();
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
