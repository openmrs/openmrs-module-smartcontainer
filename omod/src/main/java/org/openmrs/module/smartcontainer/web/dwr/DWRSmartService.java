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
package org.openmrs.module.smartcontainer.web.dwr;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.User;
import org.openmrs.api.UserService;
import org.openmrs.api.context.Context;
import org.openmrs.module.smartcontainer.SmartAppService;
import org.openmrs.module.smartcontainer.app.App;
import org.openmrs.module.smartcontainer.app.UserHiddenAppMap;

/**
 * Contains methods for processing DWR requests for the module
 */
public class DWRSmartService {
	
	private static final Log log = LogFactory.getLog(DWRSmartService.class);
	
	/**
	 * Process requests to add or remove a smart app to or from the user's list of hidden apps
	 * 
	 * @param userName the user name
	 * @param appId the appId of the app to be added or removed
	 * @param hide specifies if an app is to be removed or added
	 * @return true if the app was successfully added or removed otherwise false
	 */
	public boolean showOrHideSmartApp(Integer appId, boolean hide, String uuid) {
		if (log.isDebugEnabled())
			log.debug("In DWRSmartService........");
		if (StringUtils.isNotBlank(uuid)) {
			UserService userService = Context.getUserService();
			SmartAppService appService = Context.getService(SmartAppService.class);
			User user = userService.getUserByUuid(uuid);
			App app = appService.getAppById(appId);
			if (app != null && user != null) {
				if (hide)
					appService.saveUserHiddenAppMap(new UserHiddenAppMap(user, app));
				else
					appService.deleteUserHiddenAppMap(user, app);
				
				if (log.isDebugEnabled())
					log.debug(((hide) ? "Added" : "Removed") + " smart app " + ((hide) ? "to" : "from")
					        + " user's hidden apps....");
				
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Process requests to enable or disable a smart app.
	 * 
	 * @param appId the appId of the app to be enabled or disabled
	 * @param enable specifies if an app is to be enabled or disabled
	 * @return true if the app was successfully enabled or disabled otherwise false
	 */
	public boolean enableOrDisableSmartApp(Integer appId, boolean enable) {
		SmartAppService service = Context.getService(SmartAppService.class);
		App app = service.getAppById(appId);
		if (app != null) {
			app.setRetired(enable);
			service.saveApp(app);
			return true;
		}
		
		return false;
	}
}
