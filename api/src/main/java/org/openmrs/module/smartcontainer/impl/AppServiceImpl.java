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

import org.openmrs.api.APIException;
import org.openmrs.module.smartcontainer.AppService;
import org.openmrs.module.smartcontainer.app.App;
import org.openmrs.module.smartcontainer.db.AppDAO;

/**
 * Implementation of App Service
 */
public class AppServiceImpl implements AppService {
	
	private AppDAO dao;
	
	/**
	 * @see org.openmrs.module.smartcontainer.AppService#setSMARTUserAppDao(org.openmrs.module.smartcontainer.db.AppDAO)
	 */
	public void setDao(AppDAO dao) throws APIException {
		this.dao = dao;
		
	}
	
	/**
	 * @see org.openmrs.module.smartcontainer.AppService#getSMARTAppUserByName(java.lang.String)
	 */
	public App getAppByName(String name) throws APIException {
		return dao.getAppByName(name);
	}
	
	/**
	 * @see org.openmrs.module.smartcontainer.AppService#getAllSMARTAppUsers()
	 */
	public Collection<App> getAllApps() throws APIException {
		return dao.getAllApps();
	}
	
}
