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
package org.openmrs.module.smartcontainer.app;

import java.io.File;
import java.net.URL;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Assert;
import org.junit.Test;



/**
 *This test validates the functionality of the Manifest parser
 */

public class ManifestParserTest {
	private Log log=LogFactory.getLog(this.getClass());
	/**
	 * verify the parsing
	 * 
	 */
	@Test
	public void testparser() {
	URL url=this.getClass().getClassLoader().getResource("smart_manifest.json");
		File file = new File(url.getPath());
	ManifestParser pa=new ManifestParser();
	pa.parse(file);
	Assert.assertNotNull("API Playground", (String)pa.get(AppFactory.NAME));
}
}