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
package org.openmrs.module.smartcontainer.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.api.context.Context;
import org.openmrs.module.smartcontainer.SmartAppService;
import org.openmrs.module.smartcontainer.app.App;
import org.openmrs.test.BaseModuleContextSensitiveTest;

/**
 * @author aja
 */
public class SmartAppServiceImplTest extends BaseModuleContextSensitiveTest {
	
	private SmartAppService appService;
	
	public SmartAppServiceImplTest() {
	}
	
	@Before
	public void setUp() {
		try {
			executeDataSet("smartdataset.xml");
		}
		catch (Exception ex) {
			Logger.getLogger(SmartAppServiceImplTest.class.getName()).log(Level.SEVERE, null, ex);
		}
		appService = Context.getService(SmartAppService.class);
	}
	
	@After
	public void tearDown() {
	}
	
	/**
	 * Test of getAppByName method, of class SmartAppServiceImpl.
	 */
	@Test
	public void testGetAppByName() {
		
		App expResult = appService.getAppByName("PediBPC");
		assertEquals(expResult.getName(), "PediBPC");
		
	}
	
	/**
	 * Test of getAllApps method, of class SmartAppServiceImpl.
	 */
	@Test
	public void testGetAllApps() {
		
		Collection<App> result = appService.getAllApps();
		assertFalse(result.isEmpty());
	}
	
	/**
	 * Test of deleteApp method, of class SmartAppServiceImpl.
	 */
	@Test
	public void testDeleteApp() {
		
		App app = appService.getAppByName("PediBPC");
		appService.deleteApp(app);
		app = appService.getAppByName("PediBPC");
		Assert.assertNull(app);
	}
	
	/**
	 * Test of getAppById method, of class SmartAppServiceImpl.
	 */
	@Test
	public void testGetAppById() {
		App result = appService.getAppById(1);
		assertNotNull(result);
	}
	
	/**
	 * Test of saveApp method, of class SmartAppServiceImpl.
	 */
	@Test
	public void testSaveApp() {
		
		App newApp = new App();
		newApp.setName("BpGraph");
		appService.saveApp(newApp);
		assertNotNull(appService.getAppByName("BpGraph"));
	}
}
