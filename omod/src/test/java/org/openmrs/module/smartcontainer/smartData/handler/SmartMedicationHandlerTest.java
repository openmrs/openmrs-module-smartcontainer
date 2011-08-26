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
import org.openmrs.module.smartcontainer.smartData.SmartMedication;
import org.openmrs.test.Verifies;

public class SmartMedicationHandlerTest extends SmartDataHandlerTest {
	
	/**
	 * @see {@link SmartMedicationHandler#getAllForPatient(Patient)}
	 */
	@Test
	@Verifies(value = "should get all the patient smart medications", method = "getAllForPatient(Patient)")
	public void getAllForPatient_shouldGetAllThePatientSmartMedications() throws Exception {
		Assert.assertEquals(4, sds.getAllForPatient(Context.getPatientService().getPatient(2), SmartMedication.class).size());
	}
	
	/**
	 * @see {@link SmartMedicationHandler#getAllForPatient(Patient)}
	 */
	@Test
	@Verifies(value = "should get the smart medication for a drugorder that matches the specified uuid", method = "getAllForPatient(Patient)")
	public void getAllForPatient_shouldGetTheSmartMedicationForADrugorderThatMatchesTheSpecifiedUuid() throws Exception {
		SmartMedication medication = sds.getForPatient(Context.getPatientService().getPatient(2), SmartMedication.class,
		    "dfca4077-493c-496b-8312-856ee5d1cc26");
		Assert.assertNotNull(medication);
		Assert.assertEquals("STAVUDINE LAMIVUDINE AND NEVIRAPINE", medication.getDrugName().getTitle());
		Assert.assertEquals("TRIMUNE_454545", medication.getDrugName().getCode());
		Assert.assertEquals("1", medication.getFrequency().getValue());
		Assert.assertEquals("{day x 7 days/week}", medication.getFrequency().getUnit());
	}
}
