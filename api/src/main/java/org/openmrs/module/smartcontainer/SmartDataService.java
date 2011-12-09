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

import java.util.List;
import java.util.Map;

import org.openmrs.Patient;
import org.openmrs.module.smartcontainer.smartData.SmartBaseData;
import org.openmrs.module.smartcontainer.smartData.SmartDemographics;
import org.openmrs.module.smartcontainer.smartData.handler.SmartDataHandler;
import org.springframework.transaction.annotation.Transactional;

/**
 * This is an interface for Smart Data Service which is used to retrieve the mapped Smart data from
 * OpenMRS data.
 */
@Transactional
public interface SmartDataService {
	
	/**
	 * Returns a list of Smart data of a patient(eg list of Smart problems)
	 * 
	 * @param patient openMRS patient
	 * @param Class corresponding to Smart data
	 * @return list of Smart data
	 */
	public <T extends SmartBaseData> List<T> getAllForPatient(Patient patient, Class<T> clazz);
	
	/**
	 * Returns the smart object matching the specified class and unique identifier to be matched
	 * against. If the class is {@link SmartDemographics}, the id argument will be ignored. Note
	 * that the unique identifier can be matched in different ways for different implementations of
	 * this method.
	 * 
	 * @param patient
	 * @param clazz
	 * @param id the unique identifier to be matched for the Smart object to fetch
	 * @return
	 */
	public <T extends SmartBaseData> T getForPatient(Patient patient, Class<T> clazz, String id);
	
	/**
	 * Gets a map of all registered smart data handlers
	 * 
	 * @return a map of smart data handlers
	 */
	public Map<String, SmartDataHandler<? extends SmartBaseData>> getHandlers();
	
}
