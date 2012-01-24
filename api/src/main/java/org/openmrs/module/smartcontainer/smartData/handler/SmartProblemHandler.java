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

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Concept;
import org.openmrs.Obs;
import org.openmrs.Patient;
import org.openmrs.activelist.Problem;
import org.openmrs.api.context.Context;
import org.openmrs.module.smartcontainer.SmartConceptMapCodeSource;
import org.openmrs.module.smartcontainer.TransientSmartConceptMap;
import org.openmrs.module.smartcontainer.smartData.SmartProblem;
import org.openmrs.module.smartcontainer.util.SmartConstants;
import org.openmrs.module.smartcontainer.util.SmartDataHandlerUtil;
import org.openmrs.util.OpenmrsUtil;

public class SmartProblemHandler implements SmartDataHandler<SmartProblem> {
	
	private static final Log log = LogFactory.getLog(SmartProblemHandler.class);
	
	private SmartConceptMapCodeSource map;
	
	public SmartConceptMapCodeSource getMap() {
		return map;
	}
	
	public void setMap(SmartConceptMapCodeSource map) {
		this.map = map;
	}
	
	/**
	 * @see org.openmrs.module.smartcontainer.smartData.handler.SmartDataHandler#getForPatient(org.openmrs.Patient,
	 *      java.lang.String)
	 */
	public SmartProblem getForPatient(Patient patient, String id) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * @should get all the patient smart problems
	 * @should get all the patient smart problems using observations
	 * @should set the resolution date for resolved problems when getting smart problems
	 * @see org.openmrs.module.smartcontainer.smartData.handler.SmartDataHandler#getAllForPatient(org.openmrs.Patient)
	 */
	public List<SmartProblem> getAllForPatient(Patient patient) {
		List<SmartProblem> smartProblems = new ArrayList<SmartProblem>();
		if (SmartDataHandlerUtil.useProblemObject()) {
			List<Problem> openmrsProblems = Context.getPatientService().getProblems(patient);
			for (Problem p : openmrsProblems) {
				SmartProblem problem = new SmartProblem();
				problem.setProblemName(SmartDataHandlerUtil.codedValueHelper(p.getProblem(), map,
				    SmartDataHandlerUtil.getLinkedSnomedConceptSource(), false));// coded value
				problem.setOnset(SmartDataHandlerUtil.date(p.getStartDate()));
				if (p.getEndDate() != null) {
					problem.setResolution(SmartDataHandlerUtil.date(p.getEndDate()));
				}
				
				smartProblems.add(problem);
			}
		}
		
		// look in the obs table for problems added/resolved
		if (SmartDataHandlerUtil.useObsForProblems()) {
			String addedConceptId = Context.getAdministrationService().getGlobalProperty(
			    SmartConstants.GP_PROBLEM_ADDED_CONCEPT, "");
			String resolvedConceptId = Context.getAdministrationService().getGlobalProperty(
			    SmartConstants.GP_PROBLEM_RESOLVED_CONCEPT, "");
			
			Concept addedConcept = Context.getConceptService().getConcept(addedConceptId);
			Concept resolvedConcept = Context.getConceptService().getConcept(resolvedConceptId);
			
			if (addedConcept == null || resolvedConcept == null) {
				log.error("Unable to use observations for the problem added/resolved because concepts were not defined correctly.  See the Problem Setup admin page for help setting the global properties");
			} else {
				// we have valid concepts.  look up the observations
				
				// these obs should be sorted from most recent to oldest
				List<Obs> addedObss = Context.getObsService().getObservationsByPersonAndConcept(patient, addedConcept);
				List<Obs> resolvedObss = Context.getObsService().getObservationsByPersonAndConcept(patient, resolvedConcept);
				
				// TODO might be faster to switch the ordering to oldest to most recent
				
				// this could be slow with patients with a lot of problems.  its almost O(n)^2
				for (Obs addedObs : addedObss) {
					boolean foundMatchingResolvedObs = false;
					Obs theFoundObs = null;
					
					for (Obs resolvedObs : resolvedObss) {
						// if the concepts match and the problem was added before the problem was resolved,
						// then we have an end date
						if (OpenmrsUtil.nullSafeEquals(addedObs.getValueCoded(), resolvedObs.getValueCoded())
						        && (addedObs.getObsDatetime() != null && resolvedObs.getObsDatetime() != null && addedObs
						                .getObsDatetime().before(resolvedObs.getObsDatetime()))) {
							theFoundObs = resolvedObs;
							foundMatchingResolvedObs = true;
							break;
						}
					}
					
					SmartProblem problem = new SmartProblem();
					problem.setProblemName(SmartDataHandlerUtil.codedValueHelper(addedObs.getValueCoded(), map,
					    SmartDataHandlerUtil.getLinkedSnomedConceptSource(), false));
					problem.setOnset(SmartDataHandlerUtil.date(addedObs.getObsDatetime()));
					
					if (foundMatchingResolvedObs) {
						// so we can speed up future iterations
						resolvedObss.remove(theFoundObs);
						
						problem.setResolution(SmartDataHandlerUtil.date(theFoundObs.getObsDatetime()));
					}
					
					smartProblems.add(problem);
					
				}
			}
		}
		
		return smartProblems;
	}
	
	/**
	 * @see org.openmrs.module.smartcontainer.smartData.handler.SmartDataHandler#getRequiredConceptMappings()
	 */
	@Override
	public List<TransientSmartConceptMap> getRequiredConceptMappings() {
		return null;
	}
}
