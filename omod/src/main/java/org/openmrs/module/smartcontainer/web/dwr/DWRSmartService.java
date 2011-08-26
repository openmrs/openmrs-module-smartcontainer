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
import org.openmrs.api.context.Context;
import org.openmrs.module.smartcontainer.SmartAppService;
import org.openmrs.module.smartcontainer.SmartUser;
import org.openmrs.module.smartcontainer.SmartUserService;
import org.openmrs.module.smartcontainer.app.App;

/**
 * Contains methods for processing DWR requests for the module
 */
public class DWRSmartService {
	
	private static final Log log = LogFactory.getLog(DWRSmartService.class);
	
	/**
	 * Process requests to add or remove a smart app to or from the user's list of hidden apps
	 * 
	 * @param systemId the systemId of the user
	 * @param appId the appId of the app to be added or removed
	 * @param hide specifies if an app is to be removed or added
	 * @return true if the app was successfully added or removed otherwise false
	 */
	public boolean showOrHideSmartApp(Integer appId, boolean hide, String systemId) {
		if (log.isDebugEnabled())
			log.debug("In DWRSmartService........");
		
		SmartUserService userService = Context.getService(SmartUserService.class);
		if (StringUtils.isBlank(systemId))
			systemId = Context.getAuthenticatedUser().getSystemId();
		
		SmartUser smartUser = userService.getUserByName(systemId);
		App app = Context.getService(SmartAppService.class).getAppById(appId);
		if (app != null) {
			if (hide)
				smartUser.getHiddenApps().add(app);
			else
				smartUser.getHiddenApps().remove(app);
			
			Context.getService(SmartUserService.class).saveUser(smartUser);
			if (log.isDebugEnabled())
				log.debug(((hide) ? "Hidden" : "Showing") + " smart app " + ((hide) ? "from" : "to") + " user....");
			
			return true;
		}
		
		return false;
	}
}
