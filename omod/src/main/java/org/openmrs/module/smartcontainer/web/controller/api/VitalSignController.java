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
package org.openmrs.module.smartcontainer.web.controller.api;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Patient;
import org.openmrs.api.context.Context;
import org.openmrs.module.smartcontainer.SmartDataService;
import org.openmrs.module.smartcontainer.rdfsource.VitalSignRDFSource;
import org.openmrs.module.smartcontainer.smartData.SmartVitalSigns;
import org.openmrs.module.smartcontainer.util.SmartConstants;
import org.openrdf.rio.RDFHandlerException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(method = RequestMethod.GET, value = SmartConstants.REST_PATH)
public class VitalSignController extends BaseSmartController<VitalSignRDFSource> {
	
	private static final Log log = LogFactory.getLog(VitalSignController.class);
	
	@RequestMapping(method = RequestMethod.GET, value = "records/{pid}/vital_signs")
	public ModelAndView handle(@PathVariable("pid") Patient patient, HttpServletResponse resp) {
		if (log.isDebugEnabled())
			log.debug("In the Vital Sign Controller");
		resp.setContentType("text/xml"); // actually I use a constant
		Writer writer;
		try {
			writer = resp.getWriter();
			List<SmartVitalSigns> signs = Context.getService(SmartDataService.class).getAllForPatient(patient,
			    SmartVitalSigns.class);
			writer.write(getResource().getRDF(signs)); // get the object
			writer.close();
		}
		catch (IOException e) {
			
			log.error("Unable to write out VitalSign for pid: " + patient.getId(), e);
		}
		catch (RDFHandlerException e) {
			log.error("Unable to write out VitalSign for pid: " + patient.getId(), e);
		}
		return null; // indicates this controller did all necessary processing
		
	}
	
}
