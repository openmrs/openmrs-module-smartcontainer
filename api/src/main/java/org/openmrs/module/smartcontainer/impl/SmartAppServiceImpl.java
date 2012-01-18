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
package org.openmrs.module.smartcontainer.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.openmrs.api.APIException;
import org.openmrs.api.context.Context;
import org.openmrs.module.smartcontainer.SmartAppService;
import org.openmrs.module.smartcontainer.SmartUser;
import org.openmrs.module.smartcontainer.app.App;
import org.openmrs.module.smartcontainer.db.AppDAO;

/**
 * Implementation of App Service
 */
public class SmartAppServiceImpl implements SmartAppService {
	
	private AppDAO dao;
	
	/**
	 * @see org.openmrs.module.smartcontainer.SmartAppService#setSMARTUserAppDao(org.openmrs.module.smartcontainer.db.AppDAO)
	 */
	public void setDao(AppDAO dao) throws APIException {
		this.dao = dao;
		
	}
	
	/**
	 * @should get App by name
	 * @see org.openmrs.module.smartcontainer.SmartAppService#getSMARTAppUserByName(java.lang.String)
	 */
	public App getAppByName(String name) throws APIException {
		return dao.getAppByName(name);
	}
	
	/**
	 * @should get all apps
	 * @see org.openmrs.module.smartcontainer.SmartAppService#getAllSMARTAppUsers()
	 */
	public List<App> getAllApps() throws APIException {
		return dao.getAllApps();
	}
	
	/**
	 * @should delete selected app
	 * @see org.openmrs.module.smartcontainer.SmartAppService#DeleteApp(org.openmrs.module.smartcontainer.app.App)
	 */
	public void deleteApp(App app) throws APIException {
		app.setRetire(true);
		saveApp(app);
		
	}
	
	/**
	 * @should give app by id
	 * @see org.openmrs.module.smartcontainer.SmartAppService#getAppById(java.lang.Integer)
	 */
	public App getAppById(Integer id) {
		return dao.getAppById(id);
	}
	
	/**
	 * @should save an app
	 * @see org.openmrs.module.smartcontainer.SmartAppService#saveApp(org.openmrs.module.smartcontainer.app.App)
	 */
	public void saveApp(App newApp) {
		dao.save(newApp);
		
	}
	
	/**
	 * @see org.openmrs.module.smartcontainer.SmartAppService#getUserVisibleApps(org.openmrs.User)
	 */
	@Override
	public Collection<App> getUserVisibleApps(SmartUser smartUser) {
		if (smartUser != null) {
			Collection<App> hiddenApps = smartUser.getHiddenApps();
			if (hiddenApps.size() == 0)
				return Context.getService(SmartAppService.class).getAllApps();
			
			List<Integer> appIdsToExclude = new ArrayList<Integer>(hiddenApps.size());
			for (App app : smartUser.getHiddenApps()) {
				appIdsToExclude.add(app.getAppId());
			}
			return dao.getApps(appIdsToExclude);
		}
		
		return Collections.emptyList();
	}
}
