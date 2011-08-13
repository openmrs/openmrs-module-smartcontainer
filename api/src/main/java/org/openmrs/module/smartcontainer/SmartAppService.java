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
package org.openmrs.module.smartcontainer;

import java.util.Collection;

import org.openmrs.User;
import org.openmrs.api.APIException;
import org.openmrs.module.smartcontainer.app.App;
import org.openmrs.module.smartcontainer.db.AppDAO;
import org.springframework.transaction.annotation.Transactional;

/**
 * contains service methods to retrieve the SMART Apps
 */
@Transactional
public interface SmartAppService {

	/**
	 * setter method for DAO
	 * 
	 * @param dao
	 * @throws APIException
	 */
	public void setDao(AppDAO dao) throws APIException;

	/**
	 * get SMART App by name
	 * 
	 * @param name
	 * @return
	 * @throws APIException
	 */
	public App getAppByName(String name) throws APIException;

	/**
	 * Get all SMART Apps
	 * 
	 * @return
	 * @throws APIException
	 * 
	 */
	public Collection<App> getAllApps() throws APIException;

	/**
	 * Delete app by id
	 * 
	 * @param id
	 * @throws APIException
	 * 
	 */
	public void deleteApp(App app) throws APIException;

	/**
	 * Get App by Id
	 * 
	 * @param id
	 * @return
	 * 
	 */
	public App getAppById(Integer id);

	/**
	 * Auto generated method comment
	 * 
	 * @param newApp
	 * 
	 */
	public void saveApp(App newApp);

	/**
	 * Get all apps corresponding to a OpenMRS user
	 * 
	 * @param user
	 * @return
	 * 
	 */
	public Collection<App> getAppsByUserName(User user);

}