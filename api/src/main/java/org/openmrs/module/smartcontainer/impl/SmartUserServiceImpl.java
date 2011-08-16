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

import org.openmrs.api.APIException;
import org.openmrs.module.smartcontainer.SmartUser;
import org.openmrs.module.smartcontainer.SmartUserService;
import org.openmrs.module.smartcontainer.db.UserDAO;

import java.util.Collection;

/**
 * Implementation of User service
 */
public class SmartUserServiceImpl implements SmartUserService {

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
     * @see org.openmrs.module.smartcontainer.SmartUserService#getUserByName(java.lang.String)
     */
    public SmartUser getUserByName(String name) throws APIException {

        return userDAO.getUserByName(name);
    }

    /**
     * @see org.openmrs.module.smartcontainer.SmartUserService#getAllUsers()
     */
    public Collection<SmartUser> getAllUsers() throws APIException {

        return userDAO.getAllUsers();
    }

    /**
     * @see org.openmrs.module.smartcontainer.SmartUserService#saveUser(org.openmrs.module.smartcontainer.SmartUser)
     */
    public void saveUser(SmartUser user) throws APIException {
        userDAO.saveUser(user);

    }


}
