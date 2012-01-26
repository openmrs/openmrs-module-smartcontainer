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
import org.junit.Before;
import org.junit.Test;
import org.openmrs.api.context.Context;
import org.openmrs.module.smartcontainer.rdfsource.DemographicsRDFSource;
import org.openmrs.module.smartcontainer.smartData.SmartDemographics;
import org.openmrs.test.BaseModuleContextSensitiveTest;

/**
 */
public class SmartDemographicsHandlerTest extends BaseModuleContextSensitiveTest {
	
	@Before
	public void setUp() throws Exception {
		executeDataSet("patient.xml");
	}
	
	/**
	 * @verifies return SmartDemographics
	 * @see SmartDemographicsHandler#getForPatient(org.openmrs.Patient)
	 */
	@Test
	public void getForPatient_shouldReturnSmartDemographics() throws Exception {
		SmartDemographicsHandler smartDataHandler = new SmartDemographicsHandler();
		SmartDemographics smartDemographics = smartDataHandler
		        .getForPatient(Context.getPatientService().getPatient(2), null);
		Assert.assertNotNull(smartDemographics);
		Assert.assertNotNull(smartDemographics.getName());
		Assert.assertNotNull(smartDemographics.getAddress());
		Assert.assertNotNull(smartDemographics.getBirthDate());
		Assert.assertNotNull(smartDemographics.getGender());
		Assert.assertNotNull(smartDemographics.getZipCode());
		Assert.assertNotNull(smartDemographics.getIdentifier());
		Assert.assertNotNull(smartDemographics.getIdentifierType());
		String rdf = new DemographicsRDFSource().getRDF(smartDemographics);
	}
}
