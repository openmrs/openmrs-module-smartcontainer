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

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.api.context.Context;
import org.openmrs.module.smartcontainer.rdfsource.AllergyRDFSource;
import org.openmrs.module.smartcontainer.smartData.SmartAllergy;
import org.openmrs.test.BaseModuleContextSensitiveTest;

/**
 * Tests the {@link SmartAllergyHandler}
 */
public class SmartAllergyHandlerTest extends BaseModuleContextSensitiveTest {
	
	@Before
	public void setUp() throws Exception {
		executeDataSet("patient.xml");
		executeDataSet("allergies.xml");
	}

	/**
	 * @verifies return SmartDemographics
	 * @see SmartAllergyHandler#getForPatient(org.openmrs.Patient)
	 */
	@Test
	public void getForPatient_shouldReturnSmartAllergies() throws Exception {
		SmartAllergyHandler smartDataHandler = (SmartAllergyHandler) applicationContext.getBean("allergyHandler");
		List<SmartAllergy> smartAllergies = smartDataHandler
				.getAllForPatient(Context.getPatientService().getPatient(2));
		
		SmartAllergy smartAllergy = smartAllergies.get(0);
		Assert.assertNotNull(smartAllergy);
		Assert.assertNull(smartAllergy.getNotes());
		// TODO figure out how to calculate this in the handler
		Assert.assertEquals(null, smartAllergy.getClassOfAllergen());
		Assert.assertEquals("HEADACHE_12345", smartAllergy.getReaction().getCode());
		Assert.assertEquals("Severe", smartAllergy.getSeverity().getTitle());
		Assert.assertEquals("24484000", smartAllergy.getSeverity().getCode());
		Assert.assertEquals("12345", smartAllergy.getSubstance().getCode());
	}
	
	@Test
	public void getForPatient_shouldRetasurnSmartAllergies() throws Exception {
		SmartAllergyHandler smartDataHandler = (SmartAllergyHandler) applicationContext.getBean("allergyHandler");
		List<SmartAllergy> smartAllergies = smartDataHandler
				.getAllForPatient(Context.getPatientService().getPatient(2));
		
		AllergyRDFSource source = new AllergyRDFSource();
		System.out.println("allergies: " + source.getRDF(smartAllergies));
	}
}
