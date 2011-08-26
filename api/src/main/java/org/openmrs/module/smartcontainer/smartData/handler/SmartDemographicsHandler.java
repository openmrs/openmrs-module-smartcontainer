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
import org.openmrs.module.smartcontainer.smartData.SmartDemographics;
import org.openmrs.module.smartcontainer.util.SmartDataHandlerUtil;

/**
 * Default Handler Implementation for SMART Demographics
 */
public class SmartDemographicsHandler implements SmartDataHandler<SmartDemographics> {
	
	/**
	 * @see org.openmrs.module.smartcontainer.smartData.handler.SmartDataHandler#getForPatient(org.openmrs.Patient,
	 *      java.lang.String)
	 */
	public SmartDemographics getForPatient(Patient patient, String id) {
		SmartDemographics demographics = new SmartDemographics();
		demographics.setFamilyName(patient.getFamilyName()); // Free text
		demographics.setGivenName(patient.getGivenName()); // Free text
		if (patient.getGender() != null) // avoid null
			demographics.setGender(patient.getGender().equals("M") ? "male" : "female"); // 'male' or 'female'
		if (patient.getBirthdate() != null) // avoid null
			demographics.setBirthDate(SmartDataHandlerUtil.date(patient.getBirthdate())); // ISO-8601 string
		if (patient.getPersonAddress() != null)
			demographics.setZipCode(patient.getPersonAddress().getPostalCode()); // Free
		// text
		return demographics;
	}
	
	/**
	 * No need to implements this
	 * 
	 * @see org.openmrs.module.smartcontainer.smartData.handler.SmartDataHandler#getAllForPatient(org.openmrs.Patient)
	 */
	public List<SmartDemographics> getAllForPatient(Patient patient) {
		return null;
	}
}
