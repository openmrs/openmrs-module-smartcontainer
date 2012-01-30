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
package org.openmrs.module.smartcontainer.db;

import java.util.List;

import org.openmrs.User;
import org.openmrs.api.db.DAOException;
import org.openmrs.module.smartcontainer.SmartAppService;
import org.openmrs.module.smartcontainer.app.App;
import org.openmrs.module.smartcontainer.app.UserHiddenAppMap;

/**
 * DAO for App Service
 */
public interface AppDAO {
	
	/**
	 * to get App by name
	 * 
	 * @param name
	 * @return
	 * @throws DAOException
	 */
	public App getAppByName(String name) throws DAOException;
	
	/**
	 * @see SmartAppService#getApps(boolean)
	 */
	public List<App> getApps(boolean includeRetired) throws DAOException;
	
	/**
	 * Delete App by id
	 * 
	 * @param id
	 */
	public App getAppById(Integer id);
	
	/**
	 * Auto generated method comment
	 * 
	 * @param newApp
	 */
	public void save(App newApp);
	
	/**
	 * Deletes a given app.
	 * 
	 * @param app the app to delete.
	 */
	public void deleteApp(App app);
	
	/**
	 * @see SmartAppService#saveUserHiddenAppMap(UserHiddenAppMap)
	 */
	public UserHiddenAppMap saveUserHiddenAppMap(UserHiddenAppMap userHiddenAppMap);
	
	/**
	 * @see SmartAppService#deleteUserHiddenAppMap(User, App)
	 */
	public void deleteUserHiddenAppMap(User user, App app);
	
	/**
	 * @see SmartAppService#getUserVisibleApps(User)
	 */
	public List<App> getUserVisibleApps(User user);
	
	/**
	 * Gets all the none hidden apps for the specified user, excludes apps not hidden by the
	 * specified user if includeHiddenByOtherUsers is false
	 * 
	 * @param includeHiddenByOtherUsers specifies if apps hidden by other users other than the
	 *            passed in user should be included
	 * @see SmartAppService#getUserHiddenApps(User)
	 * @return
	 * @should exclude apps hidden by other users if includeHiddenByOtherUsers is false
	 * @should return all apps hidden from the user if includeHiddenByOtherUsers is true
	 */
	public List<App> getUserHiddenApps(User user, boolean includeHiddenByOtherUsers);
}
