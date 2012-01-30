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

import java.util.List;

import org.openmrs.User;
import org.openmrs.api.APIException;
import org.openmrs.api.context.Context;
import org.openmrs.module.smartcontainer.SmartAppService;
import org.openmrs.module.smartcontainer.app.App;
import org.openmrs.module.smartcontainer.app.UserHiddenAppMap;
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
	public List<App> getApps(boolean includeRetired) throws APIException {
		return dao.getApps(includeRetired);
	}
	
	/**
	 * @should delete selected app
	 * @see org.openmrs.module.smartcontainer.SmartAppService#deleteApp(App)
	 */
	public void deleteApp(App app) throws APIException {
		dao.deleteApp(app);
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
	 * @see org.openmrs.module.smartcontainer.SmartAppService#getUserVisibleApps(User)
	 */
	@Override
	public List<App> getUserVisibleApps(User user) {
		return dao.getUserVisibleApps(user);
	}
	
	/**
	 * @see org.openmrs.module.smartcontainer.SmartAppService#getUserHiddenApps(org.openmrs.User)
	 */
	@Override
	public List<App> getUserHiddenApps(User user) {
		return dao.getUserHiddenApps(user, Context.getAuthenticatedUser() != null
		        && Context.getAuthenticatedUser().isSuperUser());
	}
	
	/**
	 * @see org.openmrs.module.smartcontainer.SmartAppService#saveUserHiddenAppMap(org.openmrs.module.smartcontainer.app.UserHiddenAppMap)
	 */
	@Override
	public UserHiddenAppMap saveUserHiddenAppMap(UserHiddenAppMap userHiddenAppMap) {
		return dao.saveUserHiddenAppMap(userHiddenAppMap);
	}
	
	/**
	 * @see org.openmrs.module.smartcontainer.SmartAppService#deleteUserHiddenAppMap(org.openmrs.User,
	 *      org.openmrs.module.smartcontainer.app.App)
	 */
	@Override
	public void deleteUserHiddenAppMap(User user, App app) {
		dao.deleteUserHiddenAppMap(user, app);
	}
}
