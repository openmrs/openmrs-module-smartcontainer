package org.openmrs.module.smartcontainer.smartData.handler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Concept;
import org.openmrs.ConceptNumeric;
import org.openmrs.Encounter;
import org.openmrs.Obs;
import org.openmrs.Patient;
import org.openmrs.api.context.Context;
import org.openmrs.module.smartcontainer.ConceptMappingNotFoundException;
import org.openmrs.module.smartcontainer.SmartConceptMap;
import org.openmrs.module.smartcontainer.smartData.CodedValue;
import org.openmrs.module.smartcontainer.smartData.SmartEncounter;
import org.openmrs.module.smartcontainer.smartData.SmartVitalSigns;
import org.openmrs.module.smartcontainer.smartData.VitalSign;
import org.openmrs.module.smartcontainer.util.SmartDataHandlerUtil;

public class SmartVitalSignsHandler implements SmartDataHandler<SmartVitalSigns> {
	
	Log log = LogFactory.getLog(getClass());
	
	private final List<String> LOINC_CODES = Arrays.asList("8302-2", "3141-9", "39156-5", "9279-1", "8867-4", "2710-2",
	    "8310-5", "8480-6", "8462-4");
	
	private final List<String> SNOMED_CODES = Arrays.asList("33586001", "368209003");
	
	// set by the moduleApplicationContext
	private SmartConceptMap loincMap;
	
	private SmartConceptMap snomedMap;
	
	public SmartConceptMap getLoincMap() {
		return loincMap;
	}
	
	public void setLoincMap(SmartConceptMap loincMap) {
		this.loincMap = loincMap;
	}
	
	public SmartVitalSigns getForPatient(Patient patient) {
		return null;
	}
	
	public SmartConceptMap getSnomedMap() {
		return snomedMap;
	}
	
	public void setSnomedMap(SmartConceptMap snomedMap) {
		this.snomedMap = snomedMap;
	}
	
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
			signs.addAll(getAllVitalSign(e));
			smartVitalSigns.add(signs);
		}
		return smartVitalSigns;
	}
	
	private void setDates(SmartEncounter encounter, Encounter e) {
		Method getVisit = null;
		Date start = null;
		Date stop = null;
		Object[] args = new Object[0];
		try {
			getVisit = e.getClass().getDeclaredMethod("getVisit", new Class[0]);
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
	
	private List<VitalSign> getAllVitalSign(Encounter e) {
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
								
								VitalSign vital = SmartDataHandlerUtil.vitalSignHelper(value, cn, loincMap);
								vital.setUnit("m");
								signList.add(vital);
							} else {
								
								signList.add(SmartDataHandlerUtil.vitalSignHelper(o.getValueNumeric(), cn, loincMap));
							}
							
						} else {
							signList.add(SmartDataHandlerUtil.vitalSignHelper(o.getValueNumeric(), cn, loincMap));
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
			conceptCode = loincMap.lookUp(c);
			
			if (LOINC_CODES.contains(conceptCode))
				return conceptCode;
			
		}
		catch (ConceptMappingNotFoundException e) {
			// only doing a debug here so we don't litter the logs when getting snomed codes
			log.debug("Unable to find loinc code on concept: " + c.getConceptId());
		}
		
		try {
			conceptCode = snomedMap.lookUp(c);
			
			if (SNOMED_CODES.contains(conceptCode))
				return conceptCode;
			
		}
		catch (ConceptMappingNotFoundException e) {
			log.debug("Unable to find snomed code on concept: " + c.getConceptId());
		}
		
		return null;
	}
	
}
