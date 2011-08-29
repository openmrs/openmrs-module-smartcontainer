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
import org.openmrs.Order;
import org.openmrs.Patient;
import org.openmrs.api.context.Context;
import org.openmrs.module.smartcontainer.SmartConceptMap;
import org.openmrs.module.smartcontainer.smartData.SmartMedication;
import org.openmrs.module.smartcontainer.util.SmartDataHandlerUtil;

public class SmartMedicationHandler implements SmartDataHandler<SmartMedication> {
	
	private static final Log log = LogFactory.getLog(SmartMedicationHandler.class);
	
	private SmartConceptMap map;
	
	private static final Map<String, String> openmrsToSmartFrequencyMap;
	
	private static final String NOT_SPECIFIED = "Not-Specified";
	
	//creates a static final unmodifiable map
	static {
		Map<String, String> newMap = new HashMap<String, String>();
		newMap.put("daily", "/d");
		newMap.put("day", "/d");
		newMap.put("weekly", "/wk");
		newMap.put("week", "/wk");
		newMap.put("monthly", "/mo");
		newMap.put("month", "/mo");
		openmrsToSmartFrequencyMap = Collections.unmodifiableMap(newMap);
	}
	
	/**
	 * @see org.openmrs.module.smartcontainer.smartData.handler.SmartDataHandler#getAllForPatient(org.openmrs.Patient,
	 *      java.lang.String)
	 * @should get the smart medication for a drugorder that matches the specified uuid
	 */
	public List<SmartMedication> getAllForPatient(Patient patient) {
		return getMedications(patient, null);
	}
	
	public SmartConceptMap getMap() {
		return map;
	}
	
	public void setMap(SmartConceptMap map) {
		this.map = map;
	}
	
	/**
	 * @see org.openmrs.module.smartcontainer.smartData.handler.SmartDataHandler#getForPatient(org.openmrs.Patient,
	 *      java.lang.String)
	 */
	public SmartMedication getForPatient(Patient patient, String id) {
		List<SmartMedication> medications = getMedications(patient, id);
		if (medications.size() == 1)
			return medications.get(0);
		
		return null;
	}
	
	/**
	 * Utility method that returns a list of all patient drugOrders. If drugOrderUuid is specified,
	 * then it will return a list that contains only one drugOrder matching the specified uuid or an
	 * empty list if no match is found
	 * 
	 * @param patient
	 * @param drugOrderUuid
	 * @return a list of of drugOrders
	 */
	private List<SmartMedication> getMedications(Patient patient, String drugOrderUuid) {
		Order order = Context.getOrderService().getOrderByUuid(drugOrderUuid);
		
		List<SmartMedication> medications = new ArrayList<SmartMedication>();
		
		if (order != null) {
			DrugOrder drugOrder = Context.getOrderService().getOrder(order.getOrderId(), DrugOrder.class);
			
			if (drugOrder != null) {
				SmartMedication medication = new SmartMedication();
				
				medication.setId(drugOrder.getUuid());
				
				medication.setDrugName(SmartDataHandlerUtil.codedValueHelper(drugOrder.getDrug().getConcept(), getMap()));
				
				if (drugOrder.getAutoExpireDate() != null)// may be not expired yet
					medication.setEndDate(SmartDataHandlerUtil.date(drugOrder.getAutoExpireDate()));
				
				if (drugOrder.getStartDate() != null)
					medication.setStartDate(SmartDataHandlerUtil.date(drugOrder.getStartDate()));
				
				//if the medication is already discontinued, use the date when it was discontinued
				if (drugOrder.getDiscontinued() && drugOrder.getDiscontinuedDate() != null)
					medication.setEndDate(SmartDataHandlerUtil.date(drugOrder.getDiscontinuedDate()));
				else if (drugOrder.getAutoExpireDate() != null)
					medication.setEndDate(SmartDataHandlerUtil.date(drugOrder.getAutoExpireDate()));
				
				// for quantity
				if (drugOrder.getQuantity() != null)
					medication.setQuantity(SmartDataHandlerUtil.valueAndUnitHelper(drugOrder.getQuantity(), drugOrder.getUnits()));
				
				// for frequency
				if (StringUtils.isNotBlank(drugOrder.getFrequency())) {
					boolean validFrequencyOrValue = false;
					String[] valueAndFrequency = drugOrder.getFrequency().trim().split("/");
					try {
						Integer value = Integer.valueOf(valueAndFrequency[0].trim());
						//default value
						String frequency = "{" + NOT_SPECIFIED + "}";
						if (valueAndFrequency.length > 1) {
							frequency = drugOrder.getFrequency().trim();
							//use the entire string after the first occurrence of '/'
							frequency = frequency.substring(frequency.indexOf("/") + 1).toLowerCase();
							if (openmrsToSmartFrequencyMap.keySet().contains(frequency)) {
								frequency = openmrsToSmartFrequencyMap.get(frequency);
							} else {
								//set it to the value from the DB
								frequency = "{" + frequency + "}";
							}
						}
						
						medication.setFrequency(SmartDataHandlerUtil.valueAndUnitHelper(value, frequency));
						validFrequencyOrValue = true;
					}
					catch (NumberFormatException e) {
						// will handle this below since validFrequencyOrValue will be false
					}
					
					if (!validFrequencyOrValue) {
						//the specified value or units were invalid
						log.warn("Invalid frequency value was found for drug order with id:" + drugOrder.getOrderId());
					}
				}
				
				// TODO:if the instruction is not present generate one
				if (drugOrder.getInstructions() != null)
					medication.setInstructions(drugOrder.getInstructions());
				//
				medications.add(medication);
			}
		}
			
		return medications;
	}
}
