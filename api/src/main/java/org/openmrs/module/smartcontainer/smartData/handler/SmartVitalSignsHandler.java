package org.openmrs.module.smartcontainer.smartData.handler;

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
import org.openmrs.module.smartcontainer.SMARTConceptMap;
import org.openmrs.module.smartcontainer.smartData.BloodPressure;
import org.openmrs.module.smartcontainer.smartData.CodedValue;
import org.openmrs.module.smartcontainer.smartData.SmartBaseData;
import org.openmrs.module.smartcontainer.smartData.SmartEncounter;
import org.openmrs.module.smartcontainer.smartData.SmartLabResult;
import org.openmrs.module.smartcontainer.smartData.SmartVitalSigns;
import org.openmrs.module.smartcontainer.smartData.ValueAndUnit;
import org.openmrs.module.smartcontainer.smartData.VitalSign;
import org.springframework.format.datetime.DateFormatter;

public class SmartVitalSignsHandler implements SmartDataHandler {

	Log log=LogFactory.getLog(getClass());
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


	private SMARTConceptMap loincMap;
	private SMARTConceptMap snomedMap;
	public SMARTConceptMap getLoincMap() {
		return loincMap;
	}

	public void setLoincMap(SMARTConceptMap loincMap) {
		this.loincMap = loincMap;
	}
	public SmartBaseData get(Patient patient) {
		
		return null;
	}
	public SMARTConceptMap getSnomedMap() {
		return snomedMap;
	}

	public void setSnomedMap(SMARTConceptMap snomedMap) {
		this.snomedMap = snomedMap;
	}

	
	private String loincCodes[] = { "8302-2", "3141-9", "39156-5", "9279-1",
			"8867-4", "2710-2", "8310-5", "8480-6", "8462-4" };
	private String snomedCodes[] = { "33586001", "368209003" };

	public List<? extends SmartBaseData> getAllForPatient(Patient patient) {
		List<Encounter> encounters = Context.getEncounterService().getEncountersByPatient(patient);
		List<SmartVitalSigns> smartVitalSigns=new ArrayList<SmartVitalSigns>();
		String conceptCode=null;
		SmartVitalSigns signs=null;
		SmartEncounter encounter=null;
		VitalSign sign=null;
		CodedValue code=null;
		//ValueAndUnit sign=null;
		BloodPressure bloodPreassure=null;
		for (Encounter e : encounters) {
			signs=new SmartVitalSigns();
			signs.setDate(date(e.getEncounterDatetime()));
			encounter=new SmartEncounter();
			code=new CodedValue();
			code.setCode("ambulatory");
			code.setCodeBaseURL("http://smartplatforms.org/terms/code/encounterType#");
			code.setTitle("Ambulatory encounter");
			encounter.setEncounterType(code);
			if (hasVisit(e) && e.getVisit() != null) {
				encounter.setStartDate(date(e.getVisit().getStartDatetime()));
				encounter.setEndDate(date(e.getVisit().getStopDatetime()));
			}
			signs.setSmartEncounter(encounter);
			bloodPreassure=new BloodPressure();
			for (Obs o : e.getAllObs()) {
				
				if (!o.isObsGrouping()) {
					conceptCode = getVitalSignCode(o.getConcept());
					Concept concept = o.getConcept();
					ConceptNumeric cn;
					if (concept.isNumeric()) {
						cn = Context.getConceptService()
								.getConceptNumeric(
										concept.getConceptId());
						if (cn != null) {
					if (conceptCode != null) {
						sign=new VitalSign();
						code=new CodedValue();
						code.setCode(conceptCode);
						code.setCodeBaseURL("http://loinc.org/codes/");
						code.setTitle(cn.getName().getName());
						sign.setVitalName(code);
						
						
						if (conceptCode.equals("8462-4")) {
							sign.setValue(o.getValueNumeric().toString());
							sign.setUnit(cn.getUnits());
							bloodPreassure.setDiastolic(sign);
							log.error("diastolic");
							
						}else if(conceptCode.equals("8480-6")){
							sign.setValue(Double.toString(o.getValueNumeric()));
							sign.setUnit(cn.getUnits());
							bloodPreassure.setSystolic(sign);
						}else if(conceptCode.equals("8302-2")){
							sign.setValue(Double.toString(o.getValueNumeric()));
							sign.setUnit(cn.getUnits());
							signs.setHeight(sign);
						}else if(conceptCode.equals("3141-9")){
							sign.setValue(Double.toString(o.getValueNumeric()));
							sign.setUnit(cn.getUnits());
							signs.setWeight(sign);	
						}else if(conceptCode.equals("39156-5")){
							sign.setValue(Double.toString(o.getValueNumeric()));
							sign.setUnit(cn.getUnits());
							signs.setBodyMassIndex(sign);
						}else if(conceptCode.equals("9279-1")){
							sign.setValue(Double.toString(o.getValueNumeric()));
							sign.setUnit(cn.getUnits());
							signs.setRespiratoryRate(sign);
						}else if(conceptCode.equals("8867-4")){
							sign.setValue(Double.toString(o.getValueNumeric()));
							sign.setUnit(cn.getUnits());
							signs.setHeartRate(sign);
						}else if(conceptCode.equals("2710-2")){
							sign.setValue(Double.toString(o.getValueNumeric()));
							sign.setUnit(cn.getUnits());
							signs.setOxygenSaturation(sign);
						}else if(conceptCode.equals("8310-5")){
							sign.setValue(Double.toString(o.getValueNumeric()));
							sign.setUnit(cn.getUnits());
							signs.setTemperature(sign);
						
						}
						signs.setBloodPressure(bloodPreassure);
						
					}
					
						}
				}
			}
		}
		smartVitalSigns.add(signs);
		}
		return smartVitalSigns;
	}
protected String date(Date date) {
		
		DateFormatter parser = new DateFormatter("yyyy-MM-dd");
		return parser.print(date, Context.getLocale());
		
	}
private String getConceptCode(Concept c) {
	String ConceptCode = null;
	try {
		ConceptCode = loincMap.lookUp(c);
	} catch (ConceptMappingNotFoundException e) {

		e.printStackTrace();
	}
	return ConceptCode;

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

			e.printStackTrace();
		}
		for (String code : snomedCodes) {
			if (conceptCode.equals(code)) {
				found = true;
				break;
			}
		}
	}
	if(found)
	return conceptCode;
	else
		return null;
}
Boolean isVitalSignEncounter(Encounter encounter) {
	Boolean found = false;
	for (Obs o : encounter.getAllObs()) {
		if (getVitalSignCode(o.getConcept()) != null) {
			found = true;
			break;
		}
	}
	return found;
}
private Boolean hasVisit(Encounter e){
	Method methods[]=e.getClass().getMethods();
	for(Method m:methods){
		if(m.getName().equals("getVisit"))
			return true;
	}
	return false;
}

}
