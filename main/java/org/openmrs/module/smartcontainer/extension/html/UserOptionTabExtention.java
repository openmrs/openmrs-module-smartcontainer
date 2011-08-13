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
package org.openmrs.module.smartcontainer.extension.html;

import org.openmrs.module.Extension;
import org.openmrs.module.web.extension.UserOptionExtension;

/**
 * This class defines a horizontal tab that will appear on the user option page
 * 
 */
public class UserOptionTabExtention extends UserOptionExtension {
	/**
	 * @see org.openmrs.module.web.extension.UserOptionExtension#getPortletUrl()
	 */
	public String getPortletUrl() {

		return "userOptionTab";
	}

	/**
	 * @see org.openmrs.module.web.extension.UserOptionExtension#getRequiredPrivilege()
	 */
	public String getRequiredPrivilege() {

		return "View Patients";
	}

	/**
	 * @see org.openmrs.module.web.extension.UserOptionExtension#getTabId()
	 */
	public String getTabId() {

		return "smartcontainerId";
	}

	/**
	 * @see org.openmrs.module.web.extension.UserOptionExtension#getTabName()
	 */
	public String getTabName() {

		return "manage SMART Apps";
	}

	@Override
	public MEDIA_TYPE getMediaType() {

		return Extension.MEDIA_TYPE.html;
	}

}
