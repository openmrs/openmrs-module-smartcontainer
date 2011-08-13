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

import java.util.Collection;
import java.util.List;

import org.openmrs.User;
import org.openmrs.api.APIException;
import org.openmrs.module.smartcontainer.SmartAppService;
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
	 * @see org.openmrs.module.smartcontainer.SmartAppService#getSMARTAppUserByName(java.lang.String)
	 * @should get App by name
	 */
	public App getAppByName(String name) throws APIException {
		return dao.getAppByName(name);
	}

	/**
	 * @see org.openmrs.module.smartcontainer.SmartAppService#getAllSMARTAppUsers()
	 * @should get all apps
	 */
	public List<App> getAllApps() throws APIException {
		return dao.getAllApps();
	}

	/**
	 * @see org.openmrs.module.smartcontainer.SmartAppService#DeleteApp(org.openmrs.module.smartcontainer.app.App)
	 * @should delete selected app
	 */
	public void deleteApp(App app) throws APIException {
		app.setRetire(true);
		saveApp(app);

	}

	/**
	 * @see org.openmrs.module.smartcontainer.SmartAppService#getAppById(java.lang.Integer)
	 * @should give app by id
	 */
	public App getAppById(Integer id) {
		return dao.getAppById(id);
	}

	/**
	 * @see org.openmrs.module.smartcontainer.SmartAppService#saveApp(org.openmrs.module.smartcontainer.app.App)
	 * @should save an app
	 */
	public void saveApp(App newApp) {
		dao.save(newApp);

	}

	/**
	 * @see org.openmrs.module.smartcontainer.SmartAppService#getAppsByUserName(org.openmrs.User)
	 * @should give all app assigned to a user
	 */
	public Collection<App> getAppsByUserName(User user) {

		return dao.getAppsByUserName(user);
	}

}