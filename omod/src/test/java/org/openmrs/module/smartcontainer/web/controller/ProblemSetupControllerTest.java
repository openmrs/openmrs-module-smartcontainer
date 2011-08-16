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
package org.openmrs.module.smartcontainer.web.controller;

import junit.framework.Assert;

import org.junit.Test;
import org.openmrs.Concept;
import org.openmrs.api.context.Context;
import org.openmrs.module.smartcontainer.util.SmartConstants;
import org.openmrs.module.smartcontainer.util.SmartDataHandlerUtil;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.ui.ModelMap;

/**
 *
 */
public class ProblemSetupControllerTest extends BaseModuleContextSensitiveTest {

	@Test
	public void showForm_shouldSetVariables() {
		ProblemSetupController controller = new ProblemSetupController();
		
		ModelMap map = new ModelMap();
		controller.showForm(map);
		
		Assert.assertTrue(map.containsKey("useProblemObject"));
		Assert.assertTrue(map.containsKey("useObs"));
		Assert.assertTrue(map.containsKey("problemAdded"));
		Assert.assertTrue(map.containsKey("problemResolved"));
	}
	
	@Test
	public void saveValues_shouldSaveValues() { // :-)
		ProblemSetupController controller = new ProblemSetupController();
		
		controller.saveValues(true, true, new Concept(123), new Concept(1234), new MockHttpSession());
		
		Assert.assertTrue(SmartDataHandlerUtil.useObsForProblems());
		Assert.assertTrue(SmartDataHandlerUtil.useProblemObject());
		
		String addedConceptId = Context.getAdministrationService().getGlobalProperty(SmartConstants.GP_PROBLEM_ADDED_CONCEPT, "");
		Assert.assertEquals("123", addedConceptId);

		String resolvedConceptId = Context.getAdministrationService().getGlobalProperty(SmartConstants.GP_PROBLEM_RESOLVED_CONCEPT, "");
		Assert.assertEquals("1234", resolvedConceptId);
	}
	
}
