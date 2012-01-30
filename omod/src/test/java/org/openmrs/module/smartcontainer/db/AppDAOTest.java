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
package org.openmrs.module.smartcontainer.db;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.api.UserService;
import org.openmrs.api.context.Context;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.openmrs.test.Verifies;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Contains tests for the {@link AppDAO} class
 */
public class AppDAOTest extends BaseModuleContextSensitiveTest {
	
	@Autowired
	private AppDAO dao;
	
	private UserService userService;
	
	private static final String DAEMON_UUID = "A4F30A1B-5EB9-11DF-A648-37A07F9C90FB";
	
	@Before
	public void before() throws Exception {
		executeDataSet("smartdataset.xml");
		userService = Context.getUserService();
	}
	
	/**
	 * @see {@link AppDAO#getUserHiddenApps(User,null)}
	 */
	@Test
	@Verifies(value = "should exclude apps hidden by other users if includeHiddenByOtherUsers is false", method = "getUserHiddenApps(User,null)")
	public void getUserHiddenApps_shouldExcludeAppsHiddenByOtherUsersIfIncludeHiddenByOtherUsersIsFalse() throws Exception {
		Assert.assertEquals(1, dao.getUserHiddenApps(userService.getUserByUuid(DAEMON_UUID), false).size());
	}
	
	/**
	 * @see {@link AppDAO#getUserHiddenApps(User,null)}
	 */
	@Test
	@Verifies(value = "should return all apps hidden from the user if includeHiddenByOtherUsers is true", method = "getUserHiddenApps(User,null)")
	public void getUserHiddenApps_shouldReturnAllAppsHiddenFromTheUserIfIncludeHiddenByOtherUsersIsTrue() throws Exception {
		Assert.assertEquals(2, dao.getUserHiddenApps(userService.getUserByUuid(DAEMON_UUID), true).size());
	}
}
