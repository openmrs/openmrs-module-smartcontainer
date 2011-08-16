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

import org.openmrs.api.APIException;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

/**
 * Contains methods to work with SMART App User
 */
@Transactional
public interface SmartUserService {

    /**
     * get User by name
     *
     * @param name
     * @return
     * @throws APIException
     */
    public SmartUser getUserByName(String name) throws APIException;

    /**
     * Get all Users
     *
     * @return
     * @throws APIException
     */
    public Collection<SmartUser> getAllUsers() throws APIException;

    /**
     * Save a SMART user
     *
     * @param user
     * @throws APIException
     */
    public void saveUser(SmartUser user) throws APIException;

}
