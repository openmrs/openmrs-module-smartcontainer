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

import junit.framework.Assert;

import org.junit.Ignore;
import org.junit.Test;
import org.openmrs.api.context.Context;
import org.openmrs.module.smartcontainer.SmartUserService;

/**
 * This test validates the functionality of the User Service
 */
public class SMARTAppUserServiceTest {
	
	@Test
	@Ignore
	public void verifySMARTAppUserService() {
		SmartUserService us = Context.getService(SmartUserService.class);
		Assert.assertNotNull(us.getAllUsers());
		Assert.assertEquals("admin", us.getUserByName("admin").getOpenMRSUser().getName());
	}
}
