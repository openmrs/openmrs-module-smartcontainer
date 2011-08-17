package org.openmrs.module.smartcontainer.smartData.handler;

import java.util.ArrayList;
import java.util.List;

import org.openmrs.DrugOrder;
import org.openmrs.Patient;
import org.openmrs.api.context.Context;
import org.openmrs.module.smartcontainer.SmartConceptMap;
import org.openmrs.module.smartcontainer.smartData.SmartMedication;
import org.openmrs.module.smartcontainer.util.SmartDataHandlerUtil;

public class SmartMedicationHandler implements SmartDataHandler<SmartMedication> {
	
	private SmartConceptMap map;
	
	public List<SmartMedication> getAllForPatient(Patient patient) {
		List<DrugOrder> drugs = Context.getOrderService().getDrugOrdersByPatient(patient);
		List<SmartMedication> medications = new ArrayList<SmartMedication>();
		for (DrugOrder d : drugs) {
			SmartMedication medication = new SmartMedication();
			medication.setDrugName(SmartDataHandlerUtil.codedValueHelper(d.getDrug().getConcept(), getMap()));
			
			if (d.getAutoExpireDate() != null)// may be not expired yet
				medication.setEndDate(SmartDataHandlerUtil.date(d.getAutoExpireDate()));
			
			// TODO: what if it isn't started yet?  Is startDate allowed to be null?
			medication.setStartDate(SmartDataHandlerUtil.date(d.getStartDate()));
			
			// for quantity
			medication.setQuantity(SmartDataHandlerUtil.valueAndUnitHelper(d.getQuantity(), d.getUnits()));
			
			// for frequency
			String frequency = d.getFrequency().split(" ")[0];
			Integer val = Integer.valueOf(frequency.split("/")[0]);
			
			// TODO: why are we presuming per day here ?!
			
			medication.setFrequency(SmartDataHandlerUtil.valueAndUnitHelper(val, "/d"));
			
			// TODO:if the instruction is not present generate one
			medication.setInstructions(d.getInstructions());
			//
			medications.add(medication);
			
		}
		return medications;
	}
	
	public SmartConceptMap getMap() {
		return map;
	}
	
	public void setMap(SmartConceptMap map) {
		this.map = map;
	}
	
	public SmartMedication getForPatient(Patient patient) {
		
		return null;
	}
}
