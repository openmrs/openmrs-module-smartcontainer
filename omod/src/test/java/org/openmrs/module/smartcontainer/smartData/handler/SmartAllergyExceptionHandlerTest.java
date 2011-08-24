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
import org.openmrs.Patient;
import org.openmrs.api.context.Context;
import org.openmrs.module.smartcontainer.SmartDataService;
import org.openmrs.module.smartcontainer.smartData.SmartAllergyException;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.openmrs.test.Verifies;

public class SmartAllergyExceptionHandlerTest extends BaseModuleContextSensitiveTest {
	
	protected SmartDataService sds;
	
	@Before
	public void runBeforeAllTests() throws Exception {
		sds = Context.getService(SmartDataService.class);
		executeDataSet("allergyExceptions.xml");
	}
	
	/**
	 * @see {@link SmartAllergyExceptionHandler#getAllForPatient(Patient)}
	 */
	@Test
	@Verifies(value = "should get all the patient smart allergy exceptions", method = "getAllForPatient(Patient)")
	public void getAllForPatient_shouldGetAllThePatientSmartAllergyExceptions() throws Exception {
		List<SmartAllergyException> smartAllergyExceptions = sds.getAllForPatient(Context.getPatientService().getPatient(7),
		    SmartAllergyException.class);
		Assert.assertEquals(2, smartAllergyExceptions.size());
		Assert.assertEquals("No known food allergy", smartAllergyExceptions.get(0).getException().getTitle());
		Assert.assertEquals("NO_KNOWN_FOOD_CODE", smartAllergyExceptions.get(0).getException().getCode());
		Assert.assertEquals("No known allergies", smartAllergyExceptions.get(1).getException().getTitle());
		Assert.assertEquals("NO_KNOWN_CODE", smartAllergyExceptions.get(1).getException().getCode());
	}
}
