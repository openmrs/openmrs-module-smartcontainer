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
package org.openmrs.module.smartcontainer.web.controller;

import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Concept;
import org.openmrs.GlobalProperty;
import org.openmrs.api.context.Context;
import org.openmrs.module.smartcontainer.util.SmartConstants;
import org.openmrs.module.smartcontainer.util.SmartDataHandlerUtil;
import org.openmrs.web.WebConstants;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Controller behind the problemSetup.jsp page
 */
@Controller
@RequestMapping(value = "module/smartcontainer/problemsetup.form")
public class ProblemSetupController {
	private static final Log log = LogFactory.getLog(ProblemSetupController.class);

	private final String SUCCESS_FORM_VIEW = "/module/smartcontainer/problemsetup";

	/**
	 * Called on page load
	 * 
	 * @return String form view name
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String showForm(ModelMap map) {
		log.info("showing the problem setup page");
		
		map.put("useProblemObject", SmartDataHandlerUtil.useProblemObject());
		
		map.put("useObs", SmartDataHandlerUtil.useObsForProblems());
		
		String addedConceptId = Context.getAdministrationService().getGlobalProperty(SmartConstants.GP_PROBLEM_ADDED_CONCEPT, "");
		map.put("problemAdded", addedConceptId);
		
		String resolvedConceptId = Context.getAdministrationService().getGlobalProperty(SmartConstants.GP_PROBLEM_RESOLVED_CONCEPT, "");
		map.put("problemResolved", resolvedConceptId);
		
		return SUCCESS_FORM_VIEW;
	}
	
	/**
	 * Called when the user submits the form on problemSetup.form jsp page 
	 * 
	 */
	@RequestMapping(method = RequestMethod.POST)
	public String saveValues(@RequestParam(value="useProblemObject", required=false, defaultValue="false") Boolean useProblemObject,
			@RequestParam(value="useObs", required=false, defaultValue="false") Boolean useObs,
			@RequestParam("problemAdded") Concept problemAddedConcept,
			@RequestParam("problemResolved") Concept problemResolvedConcept,
			HttpSession session) {
		
		log.info("saving values on the problem setup page");
		
		saveGlobalProperty(SmartConstants.GP_USE_PROBLEM_OBJECT_NAME, useProblemObject.toString());
		
		saveGlobalProperty(SmartConstants.GP_USE_OBS_FOR_PROBLEM, useObs.toString());
		
		String problemAddedId = "";
		if (problemAddedConcept != null)
			problemAddedId = problemAddedConcept.getConceptId().toString();
		saveGlobalProperty(SmartConstants.GP_PROBLEM_ADDED_CONCEPT, problemAddedId);
		
		String problemResolvedId = "";
		if (problemResolvedConcept != null)
			problemResolvedId = problemResolvedConcept.getConceptId().toString();
		
		saveGlobalProperty(SmartConstants.GP_PROBLEM_RESOLVED_CONCEPT, problemResolvedId);
		
		session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "smartcontainer.problemSavedSuccessfully");
		return "redirect:" + SUCCESS_FORM_VIEW + ".form";

	}
	
	protected void saveGlobalProperty(String key, String value) {
		GlobalProperty gp = Context.getAdministrationService().getGlobalPropertyObject(key);
		if (gp == null)
			gp = new GlobalProperty(key);
		gp.setPropertyValue(value);
		Context.getAdministrationService().saveGlobalProperty(gp);
	}

}
