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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Concept;
import org.openmrs.ConceptNumeric;
import org.openmrs.Encounter;
import org.openmrs.Obs;
import org.openmrs.Patient;
import org.openmrs.api.context.Context;
import org.openmrs.module.smartcontainer.ConceptMappingNotFoundException;
import org.openmrs.module.smartcontainer.SmartConceptMapCodeSource;
import org.openmrs.module.smartcontainer.TransientSmartConceptMap;
import org.openmrs.module.smartcontainer.smartData.CodedValue;
import org.openmrs.module.smartcontainer.smartData.SmartEncounter;
import org.openmrs.module.smartcontainer.smartData.SmartVitalSigns;
import org.openmrs.module.smartcontainer.smartData.VitalSign;
import org.openmrs.module.smartcontainer.util.SmartDataHandlerUtil;

public class SmartVitalSignsHandler implements SmartDataHandler<SmartVitalSigns> {
	
	Log log = LogFactory.getLog(getClass());
	
	private static final Map<String, String> LOINC_NAME_CODE_MAP;
	static {
		LOINC_NAME_CODE_MAP = new HashMap<String, String>();
		LOINC_NAME_CODE_MAP.put("Height", "8302-2");
		LOINC_NAME_CODE_MAP.put("Weight", "3141-9");
		LOINC_NAME_CODE_MAP.put("Body Mass Index", "39156-5");
		LOINC_NAME_CODE_MAP.put("Respiratory Rate", "9279-1");
		LOINC_NAME_CODE_MAP.put("Heart Rate", "8867-4");
		LOINC_NAME_CODE_MAP.put("Oxygen Saturation", "2710-2");
		LOINC_NAME_CODE_MAP.put("Diastolic Blood Pressure", "8310-5");
		LOINC_NAME_CODE_MAP.put("Systolic Blood Pressure", "8480-6");
		LOINC_NAME_CODE_MAP.put("Intravascular diastolic", "8462-4");
	}
	
	//Mapping of require terms
	private static final Map<String, String> SNOMED_NAME_CODE_MAP;
	static {
		SNOMED_NAME_CODE_MAP = new HashMap<String, String>();
		SNOMED_NAME_CODE_MAP.put("Sitting position", "33586001");
		SNOMED_NAME_CODE_MAP.put("Right upper arm structure", "368209003");
	}
	
	// set by the moduleApplicationContext
	private SmartConceptMapCodeSource loincMap;
	
	private SmartConceptMapCodeSource snomedMap;
	
	public SmartConceptMapCodeSource getLoincMap() {
		return loincMap;
	}
	
	public void setLoincMap(SmartConceptMapCodeSource loincMap) {
		this.loincMap = loincMap;
	}
	
	/**
	 * @see org.openmrs.module.smartcontainer.smartData.handler.SmartDataHandler#getForPatient(org.openmrs.Patient,
	 *      java.lang.String)
	 */
	public SmartVitalSigns getForPatient(Patient patient, String id) {
		return null;
	}
	
	public SmartConceptMapCodeSource getSnomedMap() {
		return snomedMap;
	}
	
	public void setSnomedMap(SmartConceptMapCodeSource snomedMap) {
		this.snomedMap = snomedMap;
	}
	
	/**
	 * @see org.openmrs.module.smartcontainer.smartData.handler.SmartDataHandler#getAllForPatient(org.openmrs.Patient)
	 * @should get all the patient vital signs
	 */
	public List<SmartVitalSigns> getAllForPatient(Patient patient) {
		List<Encounter> encounters = Context.getEncounterService().getEncountersByPatient(patient);
		List<SmartVitalSigns> smartVitalSigns = new ArrayList<SmartVitalSigns>();
		
		for (Encounter e : encounters) {
			
			SmartVitalSigns signs = new SmartVitalSigns();
			signs.setDate(SmartDataHandlerUtil.date(e.getEncounterDatetime()));
			SmartEncounter encounter = new SmartEncounter();
			// TODO fix this to use the OpenMRS encounter's type
			CodedValue code = new CodedValue();
			code.setCode("ambulatory");
			code.setCodeBaseURL("http://smartplatforms.org/terms/code/encounterType#");
			code.setTitle("Ambulatory encounter");
			encounter.setEncounterType(code);
			setDates(encounter, e);
			signs.setSmartEncounter(encounter);
			signs.addAll(getAllVitalSigns(e));
			smartVitalSigns.add(signs);
		}
		return smartVitalSigns;
	}
	
	private void setDates(SmartEncounter encounter, Encounter e) {
		Method getVisit = getVisitMethod("getVisit", e.getClass());
		Date start = null;
		Date stop = null;
		Object[] args = new Object[0];
		try {
			if (getVisit != null) {
				// we're in 1.9+ openmrs, so there is an Encounter.getVisit method
				Object visit = getVisit.invoke(e, args);
				if (visit != null) {
					start = (Date) visit.getClass().getDeclaredMethod("getStartDatetime", new Class[0]).invoke(visit, args);
					stop = (Date) visit.getClass().getDeclaredMethod("getStopDatetime", new Class[0]).invoke(visit, args);
				}
			}
		}
		catch (SecurityException e1) {
			throw new RuntimeException(e1);
		}
		catch (NoSuchMethodException e2) {
			throw new RuntimeException(e2);
		}
		catch (IllegalArgumentException e3) {
			throw new RuntimeException(e3);
		}
		catch (IllegalAccessException e4) {
			throw new RuntimeException(e4);
		}
		catch (InvocationTargetException e5) {
			throw new RuntimeException(e5);
		}
		
		if (start != null)
			encounter.setStartDate(SmartDataHandlerUtil.date(start));
		if (stop != null)
			encounter.setEndDate(SmartDataHandlerUtil.date(stop));
		
	}
	
	private List<VitalSign> getAllVitalSigns(Encounter e) {
		List<VitalSign> signList = new ArrayList<VitalSign>();
		for (Obs o : e.getAllObs()) {
			
			if (!o.isObsGrouping()) {
				ConceptNumeric cn = getNumericConcept(o.getConcept());
				if (cn != null) {
					// we have a numeric concept, look up the codes
					String conceptCode = getVitalSignCode(o.getConcept());
					
					if (conceptCode != null) {
						
						// TODO: Do we want to convert to standard units across the board? Why just this one concept?
						// TODO #2: Put this into a constant string on this class and ref from here and in the LOINC_CODES
						if (conceptCode.equals("8302-2")) {
							if (cn.getUnits().toLowerCase().equals("cm")) {
								Double value = 0.0;
								if (o.getValueNumeric() != 0)
									value = (o.getValueNumeric() / 100.0);
								
								VitalSign vital = SmartDataHandlerUtil.vitalSignHelper(value, cn, loincMap,
								    SmartDataHandlerUtil.getLinkedLoincConceptSource());
								vital.setUnit("m");
								signList.add(vital);
							} else {
								
								signList.add(SmartDataHandlerUtil.vitalSignHelper(o.getValueNumeric(), cn, loincMap,
								    SmartDataHandlerUtil.getLinkedLoincConceptSource()));
							}
							
						} else {
							signList.add(SmartDataHandlerUtil.vitalSignHelper(o.getValueNumeric(), cn, loincMap,
							    SmartDataHandlerUtil.getLinkedLoincConceptSource()));
						}
					}
				}
			}
		}
		
		return signList;
	}
	
	private ConceptNumeric getNumericConcept(Concept concept) {
		if (concept.isNumeric()) {
			return Context.getConceptService().getConceptNumeric(concept.getConceptId());
		} else {
			return null;
		}
		
	}
	
	/**
	 * Looks at the given LOINC and then SNOMED codes for the given <code>c</code> Concept. If the
	 * concept has a mapping to LOINC or SNOMED and that code is in the {@link #LOINC_CODES} or
	 * {@link #SNOMED_CODES} list, return that code.
	 * 
	 * @param c concept to look at mappings of
	 * @return
	 */
	private String getVitalSignCode(Concept c) {
		String conceptCode = null;
		try {
			conceptCode = loincMap.lookUp(c,
			    (SmartDataHandlerUtil.getLinkedLoincConceptSource() != null) ? SmartDataHandlerUtil
			            .getLinkedLoincConceptSource().getName() : null);
			
			if (LOINC_NAME_CODE_MAP.values().contains(conceptCode))
				return conceptCode;
			
		}
		catch (ConceptMappingNotFoundException e) {
			// only doing a debug here so we don't litter the logs when getting snomed codes
			log.debug("Unable to find loinc code on concept: " + c.getConceptId());
		}
		
		try {
			conceptCode = snomedMap.lookUp(c,
			    (SmartDataHandlerUtil.getLinkedSnomedConceptSource() != null) ? SmartDataHandlerUtil
			            .getLinkedSnomedConceptSource().getName() : null);
			
			if (SNOMED_NAME_CODE_MAP.values().contains(conceptCode))
				return conceptCode;
			
		}
		catch (ConceptMappingNotFoundException e) {
			log.debug("Unable to find snomed code on concept: " + c.getConceptId());
		}
		
		return null;
	}
	
	/**
	 * Convenience method that checks of the specified class has the specified method
	 * 
	 * @param methodName
	 * @param clazz
	 * @return the method if found otherwise null
	 */
	@SuppressWarnings("rawtypes")
	private static Method getVisitMethod(String methodName, Class clazz) {
		for (Method method : clazz.getMethods()) {
			if (method.getName().equals(methodName) && method.getParameterTypes().length == 0) {
				return method;
			}
		}
		
		return null;
	}
	
	/**
	 * @see org.openmrs.module.smartcontainer.smartData.handler.SmartDataHandler#getRequiredConceptMappings()
	 */
	@Override
	public List<TransientSmartConceptMap> getRequiredConceptMappings() {
		List<TransientSmartConceptMap> vitalSignMappings = new ArrayList<TransientSmartConceptMap>();
		vitalSignMappings.addAll(SmartDataHandlerUtil.getConceptMappings(getLoincMap().getConceptSourceName(),
		    LOINC_NAME_CODE_MAP));
		vitalSignMappings.addAll(SmartDataHandlerUtil.getConceptMappings(getSnomedMap().getConceptSourceName(),
		    SNOMED_NAME_CODE_MAP));
		
		return vitalSignMappings;
	}
}
