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
import org.junit.Test;
import org.openmrs.User;
import org.openmrs.api.UserService;
import org.openmrs.api.context.Context;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.openmrs.test.Verifies;

/**
 * Contains tests for the the {@link SmartAppService}
 */
public class SmartAppServiceTest extends BaseModuleContextSensitiveTest {
	
	private UserService userService;
	
	private SmartAppService appService;
	
	@Before
	public void before() throws Exception {
		executeDataSet("smartdataset.xml");
		userService = Context.getUserService();
		appService = Context.getService(SmartAppService.class);
	}
	
	/**
	 * @see {@link SmartAppService#getUserVisibleApps(User)}
	 */
	@Test
	@Verifies(value = "should get all the un retired none hidden apps for the specified user", method = "getUserVisibleApps(User)")
	public void getUserVisibleApps_shouldGetAllTheUnRetiredNoneHiddenAppsForTheSpecifiedUser() throws Exception {
		executeDataSet("SmartAppServiceTest-otherApps.xml");
		Assert.assertEquals(1,
		    appService.getUserVisibleApps(userService.getUserByUuid("ae6bcaf6-bb77-11e0-9815-001d723850fa")).size());
	}
	
	/**
	 * @see {@link SmartAppService#getUserVisibleApps(User)}
	 */
	@Test
	@Verifies(value = "should return all un retired apps if the user has no hidden apps", method = "getUserVisibleApps(SmartUser)")
	public void getUserVisibleApps_shouldReturnAllUnRetiredAppsIfTheUserHasNoHiddenApps() throws Exception {
		Assert.assertEquals(3,
		    appService.getUserVisibleApps(userService.getUserByUuid("A4F30A1B-5EB9-11DF-A648-37A07F9C90FB")).size());
	}
	
	/**
	 * @see {@link SmartAppService#getUserHiddenApps(User)}
	 */
	@Test
	@Verifies(value = "should get all the hidden un retired apps for the specified user", method = "getUserHiddenApps(User)")
	public void getUserHiddenApps_shouldGetAllTheHiddenUnRetiredAppsForTheSpecifiedUser() throws Exception {
		executeDataSet("SmartAppServiceTest-otherApps.xml");
		Assert.assertEquals(5,
		    appService.getUserHiddenApps(userService.getUserByUuid("ae6bcaf6-bb77-11e0-9815-001d723850fa")).size());
	}
}
