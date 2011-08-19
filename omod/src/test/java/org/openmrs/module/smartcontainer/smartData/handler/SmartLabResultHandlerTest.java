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

import org.junit.Assert;
import org.junit.Test;
import org.openmrs.Patient;
import org.openmrs.api.context.Context;
import org.openmrs.module.smartcontainer.smartData.SmartLabResult;
import org.openmrs.test.Verifies;

public class SmartLabResultHandlerTest extends SmartDataHandlerTest {
	
	/**
	 * @see {@link SmartLabResultHandler#getAllForPatient(Patient)}
	 */
	@Test
	@Verifies(value = "should get all the patient smart lab results", method = "getAllForPatient(Patient)")
	public void getAllForPatient_shouldGetAllThePatientSmartLabResults() throws Exception {
		Assert.assertEquals(5, sds.getAllForPatient(Context.getPatientService().getPatient(7), SmartLabResult.class).size());
	}
}
