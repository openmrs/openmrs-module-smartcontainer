package org.openmrs.module.smartcontainer.smartData.handler;

import java.util.ArrayList;
import java.util.List;

import org.openmrs.Concept;
import org.openmrs.ConceptNumeric;
import org.openmrs.Obs;
import org.openmrs.Patient;
import org.openmrs.api.context.Context;
import org.openmrs.module.smartcontainer.SmartConceptMap;
import org.openmrs.module.smartcontainer.smartData.Attribution;
import org.openmrs.module.smartcontainer.smartData.QuantitativeResult;
import org.openmrs.module.smartcontainer.smartData.SmartBaseData;
import org.openmrs.module.smartcontainer.smartData.SmartLabResult;
import org.openmrs.module.smartcontainer.smartData.ValueAndUnit;
import org.openmrs.module.smartcontainer.util.SmartDataHandlerUtil;

public class SmartLabResultHandler implements SmartDataHandler {
	private SmartConceptMap map;

	public SmartConceptMap getMap() {
		return map;
	}

	public void setMap(SmartConceptMap map) {
		this.map = map;
	}

	public SmartBaseData getForPatient(Patient patient) {

		return null;
	}

	public List<? extends SmartBaseData> getAllForPatient(Patient patient) {
		List<Obs> obs = Context.getObsService()
				.getObservationsByPerson(patient);
		List<SmartLabResult> smartLabs = new ArrayList<SmartLabResult>();
		for (Obs o : obs) {
			Concept concept = o.getConcept();

			if (isLabTest(concept) && !o.isObsGrouping()) {
				SmartLabResult result = new SmartLabResult();
				result.setLabName(SmartDataHandlerUtil.codedValueHelper(
						concept, map));

				ConceptNumeric cn = getNumericConcept(concept);
					if (cn != null) {
						QuantitativeResult quantity = new QuantitativeResult();
						quantity.setValueAndUnit(SmartDataHandlerUtil
								.valueAndUnitHelper(o.getValueNumeric(),
										cn.getUnits()));
						quantity.setNormalRange(SmartDataHandlerUtil
								.rangeHelper(cn.getHiAbsolute(),
										cn.getLowAbsolute(), cn.getUnits()));
						quantity.setNonCriticalRange(SmartDataHandlerUtil
								.rangeHelper(cn.getHiCritical(),
										cn.getLowCritical(), cn.getUnits()));
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

	private ConceptNumeric getNumericConcept(Concept concept) {
		if (concept.isNumeric()) {
			return Context.getConceptService().getConceptNumeric(
					concept.getConceptId());
		} else {
			return null;
		}
	}

	private boolean isLabTest(Concept concept) {

		return concept.getConceptClass().getName().equals("Test");
	}

}