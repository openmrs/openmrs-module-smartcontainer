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
import org.openmrs.Patient;
import org.openmrs.activelist.Allergy;
import org.openmrs.activelist.AllergySeverity;
import org.openmrs.activelist.AllergyType;
import org.openmrs.api.context.Context;
import org.openmrs.module.smartcontainer.SmartConceptMap;
import org.openmrs.module.smartcontainer.smartData.CodedValue;
import org.openmrs.module.smartcontainer.smartData.SmartAllergy;
import org.openmrs.module.smartcontainer.util.SmartDataHandlerUtil;

/**
 * Converts {@link Allergy} and/or obs/allergies to {@link SmartAllergy} objects 
 */
public class SmartAllergyHandler implements SmartDataHandler<SmartAllergy> {

	private static final Log log = LogFactory.getLog(SmartAllergyHandler.class);

	private SmartConceptMap snomedMap;

	private SmartConceptMap rxnormMap;
	
	private SmartConceptMap fdaMap;
	
	public SmartConceptMap getSnomedMap() {
		return snomedMap;
	}

	public void setSnomedMap(SmartConceptMap snomedMap) {
		this.snomedMap = snomedMap;
	}

	public SmartConceptMap getRxnormMap() {
		return rxnormMap;
	}

	public void setRxnormMap(SmartConceptMap rxnormMap) {
		this.rxnormMap = rxnormMap;
	}
	
	public SmartConceptMap getFdaMap() {
		return fdaMap;
	}

	public void setFdaMap(SmartConceptMap fdaMap) {
		this.fdaMap = fdaMap;
	}

	public SmartAllergy getForPatient(Patient patient) {
		// not used
		return null;
	}

	/* (non-Javadoc)
	 * @see org.openmrs.module.smartcontainer.smartData.handler.SmartDataHandler#getAllForPatient(org.openmrs.Patient)
	 */
	public List<SmartAllergy> getAllForPatient(Patient patient) {
		List<SmartAllergy> smartAllergies = new ArrayList<SmartAllergy>();

		if (SmartDataHandlerUtil.useAllergyObject()) {
			List<Allergy> openmrsAllergies = Context.getPatientService()
					.getAllergies(patient);
			
			for (Allergy allergy : openmrsAllergies) {
				SmartAllergy smartAllergy = new SmartAllergy();
				
				smartAllergy.setNotes(allergy.getComments());
				smartAllergy.setReaction(SmartDataHandlerUtil.codedValueHelper(allergy.getReaction(), snomedMap));
				smartAllergy.setSeverity(convertSeverity(allergy.getSeverity()));
				
				if (isFoodAllergy(allergy))
					smartAllergy.setSubstance(SmartDataHandlerUtil.codedValueHelper(allergy.getAllergen(), fdaMap));
				else if (isDrugAllergy(allergy))
					smartAllergy.setSubstance(SmartDataHandlerUtil.codedValueHelper(allergy.getAllergen(), snomedMap));
				else {
					// do nothing because unfortunately the patient dashboard does not ask for or set the
					// allergy type.  therefore most impls won't have this set
					//throw new RuntimeException("Don't know how to handle allergy type of : " + allergy.getAllergyType() + " with active list allergy id: " + allergy.getActiveListId());
				}
					
				smartAllergy.setClassOfAllergen(convertAllergyTypeToClass(allergy.getAllergyType()));
				smartAllergy.setCategory(convertAllergyTypeToCategory(allergy));
				
				smartAllergies.add(smartAllergy);
			}
		}
		
		/*
		// look in the obs table for problems added/resolved
		if (SmartDataHandlerUtil.useObsForProblems()) {
			String addedConceptId = Context.getAdministrationService()
					.getGlobalProperty(SmartConstants.GP_PROBLEM_ADDED_CONCEPT,
							"");
			String resolvedConceptId = Context.getAdministrationService()
					.getGlobalProperty(
							SmartConstants.GP_PROBLEM_RESOLVED_CONCEPT, "");

			Concept addedConcept = Context.getConceptService().getConcept(
					addedConceptId);
			Concept resolvedConcept = Context.getConceptService().getConcept(
					resolvedConceptId);

			if (addedConcept == null || resolvedConcept == null) {
				log.error("Unable to use observations for the problem added/resolved because concepts were not defined correctly.  See the Problem Setup admin page for help setting the global properties");
			}
			else {
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
						if (addedObs.getConcept().equals(resolvedObs.getConcept()) &&
							addedObs.getObsDatetime().before(resolvedObs.getObsDatetime())) {
							theFoundObs = resolvedObs;
							foundMatchingResolvedObs = true;
							break;
						}
					}
					
					SmartAllergy problem = new SmartAllergy();
					problem.setProblemName(SmartDataHandlerUtil.codedValueHelper(addedObs.getConcept(), map));
					problem.setOnset(SmartDataHandlerUtil.date(addedObs.getObsDatetime()));
					
					if (foundMatchingResolvedObs) {
						// so we can speed up future iterations
						resolvedObss.remove(theFoundObs);
						
						problem.setResolution(SmartDataHandlerUtil.date(theFoundObs.getObsDatetime()));
					}
					
					smartAllergy.add(problem);
					
				}
			}
			
		}
		*/
		return smartAllergies;
	}

	private boolean isDrugAllergy(Allergy allergy) {
		if (allergy.getAllergyType() != null)
			return allergy.getAllergyType().equals(AllergyType.DRUG);
		else {
			// allergy type is nullable, lets guess from the allergen(concept).datatype
			return allergy.getAllergen().getConceptClass().getUuid().equals("8d490dfc-c2cc-11de-8d13-0010c6dffd0f");
		}
	}

	private boolean isFoodAllergy(Allergy allergy) {
		if (allergy.getAllergyType() != null) {
			return allergy.getAllergyType().equals(AllergyType.FOOD);
		}
		
		return false;
	}

	/**
	 * @param allergyType
	 * @return
	 */
	private CodedValue convertAllergyTypeToClass(AllergyType allergyType) {
		// TODO: I have no idea how to calculate this
		// uses rxnorm mapping
		return new CodedValue();
	}

	/**
	 * Converts openmrs AllergyType class into the SMART class of allergen in rxnorm codes
	 * 
	 * @param allergyType
	 * @param severity
	 * @return a classOfAllergen
	 */
	private CodedValue convertAllergyTypeToCategory(Allergy allergy) {
		AllergyType allergyType = allergy.getAllergyType();
		AllergySeverity severity = allergy.getSeverity();
		
		// based off of snomed mappings to these codes
		String title = null;
		String code = null;
		
		if (allergyType == null) {
			// temp hack in case allergy type is null (which will be most of the time in openmrs)
			if (isDrugAllergy(allergy)) {
				// TODO combine this with the logic from the switch statement so we're not duplicating code
				if (AllergySeverity.INTOLERANCE == severity) {
					title = "drug intolerance";
					code = "59037007";
				}
				else {
					title = "drug allergy";
					code = "416098002";
				}
			}
			else if (isFoodAllergy(allergy)) {
				if (AllergySeverity.INTOLERANCE == severity) {
					title = "food intolerance";
					code = "235719002";
				}
				else {
					title = "food allergy";
					code = "6736007";
				}
			}
		}
		
		else {
			// allergy type is not null
			switch (allergyType) {
				case DRUG:
					if (AllergySeverity.INTOLERANCE == severity) {
						title = "drug intolerance";
						code = "59037007";
					}
					else {
						title = "drug allergy";
						code = "416098002";
					}
					break;
				case FOOD:
					if (AllergySeverity.INTOLERANCE == severity) {
						title = "food intolerance";
						code = "235719002";
					}
					else {
						title = "food allergy";
						code = "6736007";
					}
					break;
				case ENVIRONMENT:
					// TODO: Is this right ?!
					title = "propensity to adverse reactions";
					code = "420134006";
					break;
				case ANIMAL:
					// TODO: Is this right ?!
					title = "propensity to adverse reactions";
					code = "420134006";
					break;
				case PLANT:
				case POLLEN:
				case OTHER:
					title = "allergy to substance";
					code = "419199007";
					break;
				default:
					throw new RuntimeException("Wha??!?!");
			}
			
			/*
			 * These are listed on the SMART website but I am not sure about how to fit them in
			 * http://smartplatforms.org/terms#Allergy
			Code=http://www.ihtsdo.org/snomed-ct/concepts/420134006, Title="propensity to adverse reactions"
			Code=http://www.ihtsdo.org/snomed-ct/concepts/418038007, Title="propensity to adverse reactions to substance"
			Code=http://www.ihtsdo.org/snomed-ct/concepts/419511003, Title="propensity to adverse reactions to drug"
			Code=http://www.ihtsdo.org/snomed-ct/concepts/418471000, Title="propensity to adverse reactions to food"
			Code=http://www.ihtsdo.org/snomed-ct/concepts/419199007, Title="allergy to substance"
			*/
		}
		
		return SmartDataHandlerUtil.codedValueHelper(title, code, snomedMap);
	}

	/**
	 * Converts openmrs AllergySeverity into the SMART severity code
	 * 
	 * @param severity
	 * @return a smart severity
	 */
	private CodedValue convertSeverity(AllergySeverity severity) {
		// based off of snomed mappings to these codes
		String title = null;
		String code = null;
		
		switch (severity) {
			case MILD:
				title = "Mild";
				code = "255604002";
				break;
			case MODERATE:
				title = "Moderate";
				code = "6736007";
				break;
			case SEVERE:
				title = "Severe";
				code = "24484000";
				break;
			case INTOLERANCE:
				// TODO : Is this right ?!
				title = "399166001";
				code = "Fatal";
				break;
			case UNKNOWN:
				throw new RuntimeException("Wha??!?!");
			
		}
		// not used in openmrs severities
		//http://www.ihtsdo.org/snomed-ct/concepts/371923003, Title="Mild to moderate"
		//http://www.ihtsdo.org/snomed-ct/concepts/371924009, Title="Moderate to severe"

		return SmartDataHandlerUtil.codedValueHelper(title, code, snomedMap);
	}

}
