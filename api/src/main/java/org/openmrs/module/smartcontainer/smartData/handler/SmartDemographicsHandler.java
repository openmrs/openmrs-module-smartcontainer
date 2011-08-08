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

import java.util.Date;
import java.util.List;

import org.openmrs.Patient;
import org.openmrs.api.context.Context;
import org.openmrs.module.smartcontainer.smartData.SmartDemographics;
import org.openmrs.module.smartcontainer.smartData.SmartBaseData;
import org.springframework.format.datetime.DateFormatter;

/**
 * Default Handler Implementation for SMART Demographics
 *
 */
public class SmartDemographicsHandler implements SmartDataHandler {

	/**
	 * @see org.openmrs.module.smartcontainer.smartData.handler.SmartDataHandler#get(org.openmrs.Patient)
	 */
	public SmartBaseData get(Patient patient) {
		SmartDemographics demographics=new SmartDemographics();
		demographics.setFamilyName(patient.getFamilyName());                     //Free text
		demographics.setGivenName(patient.getGivenName());                       //Free text
		demographics.setGender(patient.getGender().equals("M")?"male":"female"); //'male' or 'female'
		demographics.setBirthDate(date(patient.getBirthdate()));                 //ISO-8601 string
		demographics.setZipCode(patient.getPersonAddress().getPostalCode());     //Free text
		return demographics;
	}
	
/**
 * Helper method to convert Date into ISO-8601 string
 * @param date
 * @return
 */
protected String date(Date date) {
		
		DateFormatter parser = new DateFormatter("yyyy-MM-dd");
		return parser.print(date, Context.getLocale());
		
	}
/**
 * No need to implements this
 * @see org.openmrs.module.smartcontainer.smartData.handler.SmartDataHandler#getAllForPatient(org.openmrs.Patient)
 */
public List<? extends SmartBaseData> getAllForPatient(Patient patient) {
		return null;
}
}
