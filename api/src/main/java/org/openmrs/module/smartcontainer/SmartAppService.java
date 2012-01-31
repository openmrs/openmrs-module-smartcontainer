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
import java.util.List;

import org.openmrs.User;
import org.openmrs.annotation.Authorized;
import org.openmrs.api.APIException;
import org.openmrs.module.smartcontainer.app.App;
import org.openmrs.module.smartcontainer.app.UserHiddenAppMap;
import org.openmrs.module.smartcontainer.db.AppDAO;
import org.openmrs.module.smartcontainer.util.SmartConstants;
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
	 * Get SMART App by its unique SMART id
	 * 
	 * @param smartId
	 * @return
	 * @throws APIException
	 */
	public App getAppBySmartId(String smartId) throws APIException;
	
	/**
	 * Get all SMART Apps
	 * 
	 * @param includeRetired specifies whether retired apps should be returned or not
	 * @return
	 * @throws APIException
	 */
	public Collection<App> getApps(boolean includeRetired) throws APIException;
	
	/**
	 * Deletes the app with the specified id from the database
	 * 
	 * @param id
	 * @throws APIException
	 */
	@Authorized(SmartConstants.PRIV_MANAGE_SMART_APPS)
	public void deleteApp(App app) throws APIException;
	
	/**
	 * Get App by Id
	 * 
	 * @param id
	 * @return
	 */
	public App getAppById(Integer id);
	
	/**
	 * Auto generated method comment
	 * 
	 * @param newApp
	 */
	@Authorized(SmartConstants.PRIV_MANAGE_SMART_APPS)
	public void saveApp(App newApp);
	
	/**
	 * Stores the specified {@link UserHiddenAppMap} to the database
	 * 
	 * @param userHiddenAppMap the {@link UserHiddenAppMap} to save
	 * @return the created {@link UserHiddenAppMap}
	 */
	@Authorized(SmartConstants.PRIV_MANAGE_SMART_APPS)
	public UserHiddenAppMap saveUserHiddenAppMap(UserHiddenAppMap userHiddenAppMap);
	
	/**
	 * Deletes the {@link UserHiddenAppMap} matching the specified user and app
	 * 
	 * @param user the user to match against
	 * @param app the app to match against
	 */
	@Authorized(SmartConstants.PRIV_MANAGE_SMART_APPS)
	public void deleteUserHiddenAppMap(User user, App app);
	
	/**
	 * Gets all the none hidden apps for the specified user
	 * 
	 * @param user the {@link User} whose visible apps to fetch
	 * @return a list of visible {@link App}s
	 * @should get all the un retired none hidden apps for the specified user
	 * @should return all un retired apps if the user has no hidden apps
	 */
	public List<App> getUserVisibleApps(User user);
	
	/**
	 * Gets all the none hidden apps for the specified user
	 * 
	 * @param user the {@link User} whose hidden apps to fetch
	 * @return a list of {@link App}s
	 * @should get all the hidden un retired apps for the specified user
	 */
	public List<App> getUserHiddenApps(User user);
}
