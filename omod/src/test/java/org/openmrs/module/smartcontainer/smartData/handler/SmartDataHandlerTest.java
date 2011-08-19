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

import org.junit.Before;
import org.openmrs.api.context.Context;
import org.openmrs.module.smartcontainer.SmartDataService;
import org.openmrs.test.BaseModuleContextSensitiveTest;

/**
 * Superclass for all test classes for subclasses of SmartDataHandler
 */
public abstract class SmartDataHandlerTest extends BaseModuleContextSensitiveTest {
	
	protected SmartDataService sds;
	
	private static final String MODULE_TEST_DATA_XML = "moduleTestData.xml";
	
	@Before
	public void runBeforeAllTests() throws Exception {
		sds = Context.getService(SmartDataService.class);
		//includes addition of the required concept sources
		executeDataSet(MODULE_TEST_DATA_XML);
	}
}
