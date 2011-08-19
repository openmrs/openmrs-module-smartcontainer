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

import org.openmrs.Patient;
import org.openmrs.module.smartcontainer.smartData.SmartBaseData;
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
	 * Returns a Smart data(eg demographics)
	 * 
	 * @param patient
	 * @param clazz
	 * @return
	 */
	public <T extends SmartBaseData> T getForPatient(Patient patient, Class<T> clazz);
	
}
