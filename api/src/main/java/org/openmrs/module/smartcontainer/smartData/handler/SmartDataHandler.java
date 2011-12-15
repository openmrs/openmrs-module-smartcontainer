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
package org.openmrs.module.smartcontainer.smartData.handler;

import java.util.List;

import org.openmrs.Patient;
import org.openmrs.module.smartcontainer.TransientSmartConceptMap;
import org.openmrs.module.smartcontainer.smartData.SmartData;

/**
 *
 *
 */
public interface SmartDataHandler<T extends SmartData> {
	
	/**
	 * @param patient
	 * @return
	 */
	public T getForPatient(Patient patient, String id);
	
	/**
	 * @param patient
	 * @param id
	 * @return
	 */
	public List<T> getAllForPatient(Patient patient);
	
	/**
	 * Gets a list of smart concept mappings that are required by the SMART app associated to the
	 * Implementing handler.
	 * 
	 * @return a list of {@link TransientSmartConceptMap}s
	 */
	public List<TransientSmartConceptMap> getRequiredConceptMappings();
}
