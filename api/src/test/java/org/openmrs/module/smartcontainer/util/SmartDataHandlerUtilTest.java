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
package org.openmrs.module.smartcontainer.util;

import junit.framework.Assert;

import org.junit.Test;
import org.openmrs.GlobalProperty;
import org.openmrs.api.context.Context;
import org.openmrs.test.BaseModuleContextSensitiveTest;

public class SmartDataHandlerUtilTest extends BaseModuleContextSensitiveTest {
	
	@Test
	public void useProblemObject_shouldReturnTrueIfGPStoresTrue() {
		Context.getAdministrationService().saveGlobalProperty(new GlobalProperty(SmartConstants.GP_USE_OBS_FOR_PROBLEM, "true"));
		Assert.assertTrue(SmartDataHandlerUtil.useObsForProblems());
		
		Context.getAdministrationService().saveGlobalProperty(new GlobalProperty(SmartConstants.GP_USE_PROBLEM_OBJECT_NAME, "true"));
		Assert.assertTrue(SmartDataHandlerUtil.useProblemObject());
	}
	
	@Test
	public void useProblemObject_shouldReturnFalseIfGPStoresTrue() {
		Context.getAdministrationService().saveGlobalProperty(new GlobalProperty(SmartConstants.GP_USE_OBS_FOR_PROBLEM, " "));
		Assert.assertFalse(SmartDataHandlerUtil.useObsForProblems());
		
		Context.getAdministrationService().saveGlobalProperty(new GlobalProperty(SmartConstants.GP_USE_PROBLEM_OBJECT_NAME, "f"));
		Assert.assertFalse(SmartDataHandlerUtil.useProblemObject());
	}
}
