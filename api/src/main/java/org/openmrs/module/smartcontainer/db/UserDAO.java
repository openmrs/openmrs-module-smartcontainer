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

import java.util.Collection;

import org.openmrs.api.APIException;
import org.openmrs.api.db.DAOException;
import org.openmrs.module.smartcontainer.SmartUser;

/**
 * DAO for User service
 */
public interface UserDAO {

	/**
	 * to get User by username
	 * 
	 * @param name
	 * @return
	 * @throws DAOException
	 */
	public SmartUser getUserByName(String name) throws DAOException;

	/**
	 * to get all Users
	 * 
	 * @return
	 * @throws DAOException
	 */
	public Collection<SmartUser> getAllUsers() throws DAOException;

	/**
	 * Save a SMART user
	 * 
	 * @param user
	 * @throws APIException
	 */
	public void saveUser(SmartUser user) throws APIException;

}
