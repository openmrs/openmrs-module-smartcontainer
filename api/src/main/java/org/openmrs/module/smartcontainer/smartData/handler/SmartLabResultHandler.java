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

import org.openmrs.Concept;
import org.openmrs.ConceptNumeric;
import org.openmrs.Obs;
import org.openmrs.Patient;
import org.openmrs.api.context.Context;
import org.openmrs.module.smartcontainer.SmartConceptMapCodeSource;
import org.openmrs.module.smartcontainer.TransientSmartConceptMap;
import org.openmrs.module.smartcontainer.smartData.Attribution;
import org.openmrs.module.smartcontainer.smartData.QuantitativeResult;
import org.openmrs.module.smartcontainer.smartData.SmartLabResult;
import org.openmrs.module.smartcontainer.smartData.ValueAndUnit;
import org.openmrs.module.smartcontainer.util.SmartDataHandlerUtil;

public class SmartLabResultHandler implements SmartDataHandler<SmartLabResult> {
	
	private SmartConceptMapCodeSource map;
	
	public SmartConceptMapCodeSource getMap() {
		return map;
	}
	
	public void setMap(SmartConceptMapCodeSource map) {
		this.map = map;
	}
	
	public SmartLabResult getForPatient(Patient patient, String id) {
		return null;
	}
	
	/**
	 * Return labResult for a patient. Looks for Obs whose concept is value numeric or coded value
	 * and concept class is test.
	 * 
	 * @param patient
	 * @return
	 * @should return LabResult whose OpenMRS concept class is test
	 */
	public List<SmartLabResult> getAllForPatient(Patient patient) {
		List<Obs> obs = Context.getObsService().getObservationsByPerson(patient);
		List<SmartLabResult> smartLabs = new ArrayList<SmartLabResult>();
		for (Obs o : obs) {
			Concept concept = o.getConcept();
			
			if (isLabTest(concept) && !o.isObsGrouping()) {
				SmartLabResult result = new SmartLabResult();
				result.setLabName(SmartDataHandlerUtil.codedValueHelper(concept, map,
				    SmartDataHandlerUtil.getLinkedLoincConceptSource(), false));
				
				ConceptNumeric cn = getNumericConcept(concept);
				if (cn != null) {
					QuantitativeResult quantity = new QuantitativeResult();
					quantity.setValueAndUnit(SmartDataHandlerUtil.valueAndUnitHelper(o.getValueNumeric(), cn.getUnits()));
					
					quantity.setNormalRange(SmartDataHandlerUtil.rangeHelper(cn.getHiNormal(), cn.getLowNormal(),
					    cn.getUnits()));
					
					result.setQuantitativeResult(quantity);
					
				} else if (concept.getDatatype().isCoded()) {
					QuantitativeResult quantity = new QuantitativeResult();
					ValueAndUnit val = new ValueAndUnit();
					val.setValue(o.getValueCodedName().getName());
					quantity.setValueAndUnit(val);
				}
				Attribution att = new Attribution();
				att.setStartTime(SmartDataHandlerUtil.date(o.getObsDatetime()));
				result.setSpecimenCollected(att);
				result.setExternalID(o.getAccessionNumber());
				smartLabs.add(result);
				
			}
		}
		return smartLabs;
	}
	
	/**
	 * @param concept a concept (that should be datatype=numeric)
	 * @return a ConceptNumeric fetched from the database, or null if <code>concept</code> is not a
	 *         numeric
	 */
	private ConceptNumeric getNumericConcept(Concept concept) {
		if (concept.isNumeric()) {
			return Context.getConceptService().getConceptNumeric(concept.getConceptId());
		} else {
			return null;
		}
	}
	
	private boolean isLabTest(Concept concept) {
		return concept.getConceptClass().getName().equals("Test");
	}
	
	/**
	 * @see org.openmrs.module.smartcontainer.smartData.handler.SmartDataHandler#getRequiredConceptMappings()
	 */
	@Override
	public List<TransientSmartConceptMap> getRequiredConceptMappings() {
		return null;
	}
	
}
