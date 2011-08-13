package org.openmrs.module.smartcontainer.smartData.handler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
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
import org.openmrs.module.smartcontainer.smartData.SmartBaseData;
import org.openmrs.module.smartcontainer.smartData.SmartEncounter;
import org.openmrs.module.smartcontainer.smartData.SmartVitalSigns;
import org.openmrs.module.smartcontainer.smartData.VitalSign;
import org.openmrs.module.smartcontainer.util.SmartDataHandlerUtil;

public class SmartVitalSignsHandler implements SmartDataHandler {

	Log log = LogFactory.getLog(getClass());

	public String[] getLoincCodes() {
		return loincCodes;
	}

	public void setLoincCodes(String[] loincCodes) {
		this.loincCodes = loincCodes;
	}

	public String[] getSnomedCodes() {
		return snomedCodes;
	}

	public void setSnomedCodes(String[] snomedCodes) {
		this.snomedCodes = snomedCodes;
	}

	private SmartConceptMap loincMap;
	private SmartConceptMap snomedMap;

	public SmartConceptMap getLoincMap() {
		return loincMap;
	}

	public void setLoincMap(SmartConceptMap loincMap) {
		this.loincMap = loincMap;
	}

	public SmartBaseData getForPatient(Patient patient) {

		return null;
	}

	public SmartConceptMap getSnomedMap() {
		return snomedMap;
	}

	public void setSnomedMap(SmartConceptMap snomedMap) {
		this.snomedMap = snomedMap;
	}

	private String loincCodes[] = { "8302-2", "3141-9", "39156-5", "9279-1",
			"8867-4", "2710-2", "8310-5", "8480-6", "8462-4" };
	private String snomedCodes[] = { "33586001", "368209003" };

	public List<? extends SmartBaseData> getAllForPatient(Patient patient) {
		List<Encounter> encounters = Context.getEncounterService()
				.getEncountersByPatient(patient);
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
				Object visit = getVisit.invoke(e, args);
				if (visit != null) {
					start = (Date) visit
							.getClass()
							.getDeclaredMethod("getStartDatetime", new Class[0])
							.invoke(visit, args);
					stop = (Date) visit.getClass()
							.getDeclaredMethod("getStopDatetime", new Class[0])
							.invoke(visit, args);
				}
			}
		} catch (SecurityException e1) {
			throw new RuntimeException(e1);
		} catch (NoSuchMethodException e2) {
			throw new RuntimeException(e2);
		} catch (IllegalArgumentException e3) {
			throw new RuntimeException(e3);
		} catch (IllegalAccessException e4) {
			throw new RuntimeException(e4);
		} catch (InvocationTargetException e5) {
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
				String conceptCode = getVitalSignCode(o.getConcept());
				ConceptNumeric cn = getNumericConcept(o.getConcept());

				if (cn != null && conceptCode != null) {

					if (conceptCode.equals("8302-2")) {
						if (cn.getUnits().toLowerCase().equals("cm")) {
							Double value = null;
							if (o.getValueNumeric() != 0) {
								value = (o.getValueNumeric() / 100.0);
							} else {
								value = 0.0;
							}
							cn.setUnits("m");
							signList.add(SmartDataHandlerUtil.vitalSignHelper(
									value, cn, loincMap));
						} else {

							signList.add(SmartDataHandlerUtil.vitalSignHelper(
									o.getValueNumeric(), cn, loincMap));
						}

					} else {
						signList.add(SmartDataHandlerUtil.vitalSignHelper(
								o.getValueNumeric(), cn, loincMap));
					}
				}
			}
		}

		return signList;
	}

	private ConceptNumeric getNumericConcept(Concept concept) {
		if (concept.isNumeric()) {
			return Context.getConceptService().getConceptNumeric(
					concept.getConceptId());
		} else {
			return null;
		}

	}

	private String getVitalSignCode(Concept c) {
		String conceptCode = null;
		Boolean found = false;
		try {
			conceptCode = loincMap.lookUp(c);
		} catch (ConceptMappingNotFoundException e) {

			e.printStackTrace();
		}
		for (String code : loincCodes) {
			if (conceptCode.equals(code)) {
				found = true;
				break;
			}
		}
		if (!found) {
			try {
				conceptCode = snomedMap.lookUp(c);
			} catch (ConceptMappingNotFoundException e) {

				throw new RuntimeException(e);
			}
			for (String code : snomedCodes) {
				if (conceptCode.equals(code)) {
					found = true;
					break;
				}
			}
		}
		if (found)
			return conceptCode;
		else
			return null;
	}

	

}