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
import org.openmrs.api.context.Context;
import org.openmrs.module.smartcontainer.SmartConceptMapCodeSource;
import org.openmrs.module.smartcontainer.TransientSmartConceptMap;
import org.openmrs.module.smartcontainer.smartData.SmartAllergyException;
import org.openmrs.module.smartcontainer.util.SmartConstants;
import org.openmrs.module.smartcontainer.util.SmartDataHandlerUtil;

/**
 * Default Handler Implementation for SMART {@link SmartAllergyException}
 */
public class SmartAllergyExceptionHandler implements SmartDataHandler<SmartAllergyException> {
	
	private static final Log log = LogFactory.getLog(SmartAllergyExceptionHandler.class);
	
	private SmartConceptMapCodeSource map;
	
	/**
	 * @return the map
	 */
	public SmartConceptMapCodeSource getMap() {
		return map;
	}
	
	/**
	 * @param map the map to set
	 */
	public void setMap(SmartConceptMapCodeSource map) {
		this.map = map;
	}
	
	/**
	 * @see org.openmrs.module.smartcontainer.smartData.handler.SmartDataHandler#getForPatient(org.openmrs.Patient,
	 *      java.lang.String)
	 */
	public SmartAllergyException getForPatient(Patient patient, String id) {
		// not used
		return null;
	}
	
	/**
	 * @should get all the patient smart allergy exceptions
	 * @see org.openmrs.module.smartcontainer.smartData.handler.SmartDataHandler#getAllForPatient(org.openmrs.Patient)
	 */
	public List<SmartAllergyException> getAllForPatient(Patient patient) {
		List<SmartAllergyException> smartAllergyExceptions = new ArrayList<SmartAllergyException>();
		Concept allergyExceptionConcept = SmartDataHandlerUtil
		        .getConceptByGlobalProperty(SmartConstants.GP_ALLERGY_EXCEPTION_CONCEPT);
		if (allergyExceptionConcept != null) {
			List<Obs> allergyExceptionObs = Context.getObsService().getObservationsByPersonAndConcept(patient,
			    allergyExceptionConcept);
			for (Obs obs : allergyExceptionObs) {
				if (obs.getValueCoded() == null) {
					log.warn("No valude coded value for allergy exception concept with id:"
					        + obs.getConcept().getConceptId());
					continue;
				}
				
				SmartAllergyException allergyException = new SmartAllergyException();
				allergyException.setException(SmartDataHandlerUtil.codedValueHelper(obs.getValueCoded(), map,
				    SmartDataHandlerUtil.getLinkedSnomedConceptSource(), false));
				smartAllergyExceptions.add(allergyException);
			}
		}
		
		return smartAllergyExceptions;
	}
	
	/**
	 * @see org.openmrs.module.smartcontainer.smartData.handler.SmartDataHandler#getRequiredConceptMappings()
	 */
	@Override
	public List<TransientSmartConceptMap> getRequiredConceptMappings() {
		return null;
	}
}
