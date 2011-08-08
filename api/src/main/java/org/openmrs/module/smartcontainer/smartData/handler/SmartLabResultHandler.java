package org.openmrs.module.smartcontainer.smartData.handler;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.openmrs.Concept;
import org.openmrs.ConceptNumeric;
import org.openmrs.Obs;
import org.openmrs.Patient;
import org.openmrs.api.context.Context;
import org.openmrs.module.smartcontainer.ConceptMappingNotFoundException;
import org.openmrs.module.smartcontainer.SMARTConceptMap;
import org.openmrs.module.smartcontainer.smartData.Attribution;
import org.openmrs.module.smartcontainer.smartData.CodeProvenance;
import org.openmrs.module.smartcontainer.smartData.CodedValue;
import org.openmrs.module.smartcontainer.smartData.QuantitativeResult;
import org.openmrs.module.smartcontainer.smartData.SmartBaseData;
import org.openmrs.module.smartcontainer.smartData.SmartLabResult;
import org.openmrs.module.smartcontainer.smartData.ValueAndUnit;
import org.openmrs.module.smartcontainer.smartData.ValueRange;
import org.springframework.format.datetime.DateFormatter;

public class SmartLabResultHandler implements SmartDataHandler {
	private SMARTConceptMap map;
	
	public SMARTConceptMap getMap() {
		return map;
	}

	public void setMap(SMARTConceptMap map) {
		this.map = map;
	}

	public SmartBaseData get(Patient patient) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<? extends SmartBaseData> getAllForPatient(Patient patient) {
		List<Obs> obs = Context.getObsService().getObservationsByPerson(patient);
		List<SmartLabResult> smartLabs=new ArrayList<SmartLabResult>();
		SmartLabResult result=null;
		CodedValue code=null;
		CodeProvenance provenance=null;
		ValueAndUnit val=null;
		ValueRange range=null;
		QuantitativeResult quantity=null;
		Attribution att=null;
		
		for(Obs o:obs){
			Concept concept=Context.getConceptService().getConcept(String.valueOf(o.getConcept().getConceptId()));
			if(concept.getConceptClass().getName().equals("Test") && !o.isObsGrouping()){
		result=new SmartLabResult();
		//
		code=new CodedValue();
		code.setTitle(concept.getName().getName());
		try {
			code.setCode(map.lookUp(concept));
		} catch (ConceptMappingNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		code.setCodeBaseURL(map.getBaseURL());
		provenance=new CodeProvenance();
		provenance.setSourceCode(Integer.toString(concept.getConceptId()));
		provenance.setTitle(concept.getName().getName());
		provenance.setSourceCodeBaseURL("http://openmrs.org/codes/");
		provenance.setTranslationFidelity("verified");
		provenance.setTranslationFidelityBaseURL("http://smartplatforms.org/terms/code/fidelity#");
		code.setCodeProvenance(provenance);
		//
		result.setLabName(code);
		ConceptNumeric cn;
		if (concept.isNumeric()) {
			cn=Context.getConceptService().getConceptNumeric( concept.getConceptId());
			if(cn!=null){
			quantity=new QuantitativeResult();
			val=new ValueAndUnit();
			val.setValue(o.getValueNumeric().toString());
			val.setUnit(cn.getUnits());
			quantity.setValueAndUnit(val);
			range=new ValueRange();
			val=new ValueAndUnit();
			if(cn.getHiAbsolute()!=null)
			val.setValue(Double.toString(cn.getHiAbsolute()));
			val.setUnit(cn.getUnits());
			range.setMaximum(val);
			val=new ValueAndUnit();
			if(cn.getLowAbsolute()!=null)
			val.setValue(Double.toString(cn.getLowAbsolute()));
			val.setUnit(cn.getUnits());
			range.setMinimum(val);
			quantity.setNormalRange(range);
			//
			range=new ValueRange();
			val=new ValueAndUnit();
			if(cn.getHiCritical()!=null)
			val.setValue(Double.toString(cn.getHiCritical()));
			val.setUnit(cn.getUnits());
			range.setMaximum(val);
			val=new ValueAndUnit();
			if(cn.getLowCritical()!=null)
			val.setValue(Double.toString(cn.getLowCritical()));
			val.setUnit(cn.getUnits());
			range.setMinimum(val);
			quantity.setNonCriticalRange(range);
			//
			result.setQuantitativeResult(quantity);
				
			}
		}else if(concept.getDatatype().isCoded()){
			quantity=new QuantitativeResult();
			val=new ValueAndUnit();
			val.setValue(o.getValueCodedName().getName());
			quantity.setValueAndUnit(val);
			}
		att=new Attribution();
		att.setStartTime(date(o.getObsDatetime()));
		result.setSpecimenCollected(att);
		result.setExternalID(o.getAccessionNumber());
		smartLabs.add(result);
		
		
			}
		}
		return smartLabs;
	}
protected String date(Date date) {
		
		DateFormatter parser = new DateFormatter("yyyy-MM-dd");
		return parser.print(date, Context.getLocale());
		
	}
}
