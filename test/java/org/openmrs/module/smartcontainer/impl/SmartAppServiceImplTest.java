package org.openmrs.module.smartcontainer.impl;


import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;
import org.openmrs.api.context.Context;
import org.openmrs.module.smartcontainer.SmartAppService;
import org.openmrs.module.smartcontainer.app.App;
import org.openmrs.test.BaseModuleContextSensitiveTest;

public class SmartAppServiceImplTest extends BaseModuleContextSensitiveTest {
	/**
	 * @see SmartAppServiceImpl#getAppsByUserName(User)
	 * @verifies give all app assigned to a user
	 */
	@Test
	public void getAppsByUserName_shouldGiveAllAppAssignedToAUser()
			throws Exception {
		executeDataSet("");
		Collection<App> list=Context.getService(SmartAppService.class).getAppsByUserName(Context.getAuthenticatedUser());
		Assert.assertNotNull(list);
		Assert.assertFalse(list.isEmpty());
	}

	/**
	 * @see SmartAppServiceImpl#getAllApps()
	 * @verifies get all apps
	 */
	@Test
	public void getAllApps_shouldGetAllApps() throws Exception {
		//TODO auto-generated
		Assert.fail("Not yet implemented");
	}

	/**
	 * @see SmartAppServiceImpl#getAppById(Integer)
	 * @verifies give app by id
	 */
	@Test
	public void getAppById_shouldGiveAppById() throws Exception {
		//TODO auto-generated
		Assert.fail("Not yet implemented");
	}
}