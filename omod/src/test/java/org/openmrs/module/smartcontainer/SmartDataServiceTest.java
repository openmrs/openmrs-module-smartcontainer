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

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.openmrs.GlobalProperty;
import org.openmrs.Patient;
import org.openmrs.api.AdministrationService;
import org.openmrs.api.context.Context;
import org.openmrs.module.smartcontainer.smartData.SmartDemographics;
import org.openmrs.module.smartcontainer.smartData.SmartLabResult;
import org.openmrs.module.smartcontainer.smartData.SmartMedication;
import org.openmrs.module.smartcontainer.smartData.SmartProblem;
import org.openmrs.module.smartcontainer.smartData.SmartVitalSigns;
import org.openmrs.module.smartcontainer.util.SmartConstants;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.openmrs.test.Verifies;

public class SmartDataServiceTest extends BaseModuleContextSensitiveTest {
	
	private SmartDataService sds;
	
	@Before
	public void runBeforeAllTests() throws Exception {
		sds = Context.getService(SmartDataService.class);
		//includes addition of the required concept sources
		executeDataSet(MODULE_TEST_DATA_XML);
	}
	
	private static final String MODULE_TEST_DATA_XML = "moduleTestData.xml";
	
	/**
	 * @see {@link SmartDataService#getAllForPatient(Patient,Class)}
	 */
	@Test
	@Verifies(value = "should get all the patient smart lab results", method = "getAllForPatient(Patient,Class)")
	public void getAllForPatient_shouldGetAllThePatientSmartLabResults() throws Exception {
		Assert.assertEquals(5, sds.getAllForPatient(Context.getPatientService().getPatient(7), SmartLabResult.class).size());
	}
	
	/**
	 * @see {@link SmartDataService#getAllForPatient(Patient,Class)}
	 */
	@Test
	@Verifies(value = "should get all the patient smart medications", method = "getAllForPatient(Patient,Class)")
	public void getAllForPatient_shouldGetAllThePatientSmartMedications() throws Exception {
		Assert.assertEquals(4, sds.getAllForPatient(Context.getPatientService().getPatient(2), SmartMedication.class).size());
	}
	
	/**
	 * @see {@link SmartDataService#getAllForPatient(Patient,Class)}
	 */
	@Test
	@Verifies(value = "should get all the patient smart problems", method = "getAllForPatient(Patient,Class)")
	public void getAllForPatient_shouldGetAllThePatientSmartProblems() throws Exception {
		//set the global property so that we can fetch problems using the ActiveListService
		addGlobalProperty(SmartConstants.GP_USE_PROBLEM_OBJECT_NAME, "true");
		Assert.assertEquals(2, sds.getAllForPatient(Context.getPatientService().getPatient(2), SmartProblem.class).size());
	}
	
	/**
	 * @see {@link SmartDataService#getAllForPatient(Patient,Class)}
	 */
	@Test
	@Verifies(value = "should get all the patient smart problems using observations", method = "getAllForPatient(Patient,Class)")
	public void getAllForPatient_shouldGetAllThePatientSmartProblemsUsingObservations() throws Exception {
		//set the global property so that we can generate problems using the obs table for obs 
		//with concept id matching the global property values for added and resolved concept ids
		addGlobalProperty(SmartConstants.GP_USE_OBS_FOR_PROBLEM, "true");
		//set the global property for added and resolved concepts
		addGlobalProperty(SmartConstants.GP_PROBLEM_ADDED_CONCEPT, "5089");
		addGlobalProperty(SmartConstants.GP_PROBLEM_RESOLVED_CONCEPT, "19");
		Assert.assertEquals(3, sds.getAllForPatient(Context.getPatientService().getPatient(7), SmartProblem.class).size());
	}
	
	/**
	 * @see {@link SmartDataService#getAllForPatient(Patient,Class)}
	 */
	@Test
	@Verifies(value = "should get all the patient vital signs", method = "getAllForPatient(Patient,Class)")
	public void getAllForPatient_shouldGetAllThePatientVitalSigns() throws Exception {
		Assert.assertEquals(3, sds.getAllForPatient(Context.getPatientService().getPatient(7), SmartVitalSigns.class).size());
	}
	
	/**
	 * @see {@link SmartDataService#getForPatient(Patient,Class)}
	 */
	@Test
	@Verifies(value = "should get the patient smart demographics", method = "getForPatient(Patient,Class)")
	public void getForPatient_shouldGetThePatientSmartDemographics() throws Exception {
		SmartDemographics sd = sds.getForPatient(Context.getPatientService().getPatient(2), SmartDemographics.class);
		Assert.assertNotNull(sd);
	}
	
	/**
	 * Utility method that inserts a row into the global_property table
	 * 
	 * @param property
	 * @param value
	 */
	private static void addGlobalProperty(String property, String value) {
		AdministrationService as = Context.getAdministrationService();
		GlobalProperty gp = as.getGlobalPropertyObject(property);
		if (gp == null)
			gp = new GlobalProperty(property);
		gp.setPropertyValue(value);
		as.saveGlobalProperty(gp);
	}
	
	/**
	 * @see {@link SmartDataService#getAllForPatient(Patient,Class<QT;>)}
	 */
	@Test
	@Ignore
	@Verifies(value = "should set the resolution date for resolved problems when getting smart problems", method = "getAllForPatient(Patient,Class<QT;>)")
	public void getAllForPatient_shouldSetTheResolutionDateForResolvedProblemsWhenGettingSmartProblems() throws Exception {
		//TODO Add implementation code after fixing the logic that sets the resolution date in SmartProblemHandler.getAllForPatient
	}
	
}
