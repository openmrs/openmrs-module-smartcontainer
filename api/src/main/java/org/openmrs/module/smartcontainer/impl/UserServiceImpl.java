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
import org.openmrs.module.smartcontainer.SMARTAppUser;
import org.openmrs.module.smartcontainer.UserService;
import org.openmrs.module.smartcontainer.db.UserDAO;

/**
 * Implementation of User service
 */
public class UserServiceImpl implements UserService {
	
	private UserDAO userDAO;
	
	/**
	 * @return the userDAO
	 */
	public UserDAO getUserDAO() {
		return userDAO;
	}
	
	/**
	 * @param userDAO the userDAO to set
	 */
	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}
	
	/**
	 * @see org.openmrs.module.smartcontainer.UserService#getUserByName(java.lang.String)
	 */
	public SMARTAppUser getUserByName(String name) throws APIException {
		
		return userDAO.getUserByName(name);
	}
	
	/**
	 * @see org.openmrs.module.smartcontainer.UserService#getAllUsers()
	 */
	public Collection<SMARTAppUser> getAllUsers() throws APIException {
		
		return userDAO.getAllUsers();
	}
	
}
