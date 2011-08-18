package org.openmrs.module.smartcontainer.smartData.handler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.DrugOrder;
import org.openmrs.Patient;
import org.openmrs.api.context.Context;
import org.openmrs.module.smartcontainer.SmartConceptMap;
import org.openmrs.module.smartcontainer.smartData.SmartMedication;
import org.openmrs.module.smartcontainer.util.SmartDataHandlerUtil;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class SmartMedicationHandler implements SmartDataHandler<SmartMedication> {
	
	private static final Log log = LogFactory.getLog(SmartMedicationHandler.class);
	
	private SmartConceptMap map;
	
	private static final Map<String, String> openmrsToSmartFrequencyMap;
	//creates a static final unmodifiable map
	static {
		Map newMap = new HashMap<String, String>();
		newMap.put("daily", "/d");
		newMap.put("weekly", "/wk");
		newMap.put("monthly", "/mo");
		openmrsToSmartFrequencyMap = Collections.unmodifiableMap(newMap);
	}
	
	public List<SmartMedication> getAllForPatient(Patient patient) {
		List<DrugOrder> drugs = Context.getOrderService().getDrugOrdersByPatient(patient);
		List<SmartMedication> medications = new ArrayList<SmartMedication>();
		for (DrugOrder d : drugs) {
			SmartMedication medication = new SmartMedication();
			medication.setDrugName(SmartDataHandlerUtil.codedValueHelper(d.getDrug().getConcept(), getMap()));
			
			if (d.getAutoExpireDate() != null)// may be not expired yet
				medication.setEndDate(SmartDataHandlerUtil.date(d.getAutoExpireDate()));
			
			if (d.getStartDate() != null)
				medication.setStartDate(SmartDataHandlerUtil.date(d.getStartDate()));
			
			//if the medication is already discontinued, use the date when it was discontinued
			if (d.getDiscontinued())
				medication.setEndDate(SmartDataHandlerUtil.date(d.getDiscontinuedDate()));
			else if (d.getAutoExpireDate() != null)
				medication.setEndDate(SmartDataHandlerUtil.date(d.getAutoExpireDate()));
			
			// for quantity
			medication.setQuantity(SmartDataHandlerUtil.valueAndUnitHelper(d.getQuantity(), d.getUnits()));
			
			// for frequency
			if (StringUtils.isNotBlank(d.getFrequency())) {
				boolean isValidFrequency = false;
				String[] valueAndFrequency = d.getFrequency().trim().split("/");
				if (valueAndFrequency.length == 2) {
					try {
						String frequency = valueAndFrequency[1].toLowerCase();
						if (openmrsToSmartFrequencyMap.keySet().contains(frequency)) {
							medication.setFrequency(SmartDataHandlerUtil.valueAndUnitHelper(
							    Integer.valueOf(valueAndFrequency[0]), openmrsToSmartFrequencyMap.get(frequency)));
							isValidFrequency = true;
						}
					}
					catch (NumberFormatException e) {
						// will handle this below
					}
					
				}
				
				if (!isValidFrequency)
					log.warn("Invalid frequency format was found for drug order with id:" + d.getOrderId());
			}
			
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
