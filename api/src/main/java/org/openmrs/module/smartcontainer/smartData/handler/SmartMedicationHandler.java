package org.openmrs.module.smartcontainer.smartData.handler;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.openmrs.DrugOrder;
import org.openmrs.Patient;
import org.openmrs.api.context.Context;
import org.openmrs.module.smartcontainer.ConceptMappingNotFoundException;
import org.openmrs.module.smartcontainer.SMARTConceptMap;
import org.openmrs.module.smartcontainer.smartData.CodedValue;
import org.openmrs.module.smartcontainer.smartData.SmartBaseData;
import org.openmrs.module.smartcontainer.smartData.SmartMedication;
import org.openmrs.module.smartcontainer.smartData.ValueAndUnit;
import org.springframework.format.datetime.DateFormatter;

public class SmartMedicationHandler implements SmartDataHandler {
	private SMARTConceptMap map;
	
	public List<? extends SmartBaseData> getAllForPatient(Patient patient) {
		List<DrugOrder> drugs=Context.getOrderService().getDrugOrdersByPatient(patient);
		List<SmartMedication> medications=new ArrayList<SmartMedication>();
		SmartMedication medication;
		ValueAndUnit valueAndUnit;
		CodedValue codedValue;
		for(DrugOrder d:drugs){
			medication=new SmartMedication();
			codedValue=new CodedValue();
			codedValue.setTitle(d.getDrug().getName());
			try {
				codedValue.setCode(map.lookUp(d.getDrug().getConcept()));
			} catch (ConceptMappingNotFoundException e) {
				
				e.printStackTrace();
			}
			codedValue.setCodeBaseURL(map.getBaseURL());
			medication.setDrugName(codedValue);
			if(d.getAutoExpireDate()!=null)//may be not expired yet
			medication.setEndDate(date(d.getAutoExpireDate()));
			medication.setStartDate(date(d.getStartDate()));
			//for quantity
			valueAndUnit=new ValueAndUnit();
			valueAndUnit.setValue((d.getQuantity().toString()));
			
			//TODO:Converts to appropriate unit
			valueAndUnit.setUnit(d.getUnits());
			medication.setQuantity(valueAndUnit);
			//for frequency
			valueAndUnit=new ValueAndUnit();
			//TODO:set appropriate value and unit parsing the frequency
			valueAndUnit.setValue(d.getFrequency());
			valueAndUnit.setUnit(d.getUnits());
			medication.setFrequency(valueAndUnit);
			//TODO:if the instruction is not present generate one
			medication.setInstructions(d.getInstructions());
			//
			medications.add(medication);
			
			
		}
		return medications;
	}

	
	public SMARTConceptMap getMap() {
		return map;
	}
	
	public void setMap(SMARTConceptMap map) {
		this.map = map;
	}
protected String date(Date date) {
		
		DateFormatter parser = new DateFormatter("yyyy-MM-dd");
		return parser.print(date, Context.getLocale());
		
	}


public SmartBaseData get(Patient patient) {
	// TODO Auto-generated method stub
	return null;
}
}
