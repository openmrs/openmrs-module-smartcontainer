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
import org.junit.Test;
import org.openmrs.Patient;
import org.openmrs.api.context.Context;
import org.openmrs.module.smartcontainer.smartData.SmartProblem;
import org.openmrs.test.Verifies;

public class SmartProblemHandlerTest extends SmartDataHandlerTest {
	
	/**
	 * @see {@link SmartProblemHandler#getAllForPatient(Patient)}
	 */
	@Test
	@Verifies(value = "should get all the patient smart problems", method = "getAllForPatient(Patient)")
	public void getAllForPatient_shouldGetAllThePatientSmartProblems() throws Exception {
		Assert.assertEquals(2, sds.getAllForPatient(Context.getPatientService().getPatient(2), SmartProblem.class).size());
	}
	
	/**
	 * @see {@link SmartProblemHandler#getAllForPatient(Patient)}
	 */
	@Test
	@Verifies(value = "should get all the patient smart problems using observations", method = "getAllForPatient(Patient)")
	public void getAllForPatient_shouldGetAllThePatientSmartProblemsUsingObservations() throws Exception {
		Assert.assertEquals(1, sds.getAllForPatient(Context.getPatientService().getPatient(7), SmartProblem.class).size());
	}
	
	/**
	 * @see {@link SmartProblemHandler#getAllForPatient(Patient)}
	 */
	@Test
	@Verifies(value = "should set the resolution date for resolved problems when getting smart problems", method = "getAllForPatient(Patient)")
	public void getAllForPatient_shouldSetTheResolutionDateForResolvedProblemsWhenGettingSmartProblems() throws Exception {
		List<SmartProblem> problems = sds.getAllForPatient(Context.getPatientService().getPatient(7), SmartProblem.class);
		Assert.assertEquals(1, problems.size());
		//the resolution should be set for the smart problem
		Assert.assertNotNull(problems.get(0).getResolution());
	}
}
