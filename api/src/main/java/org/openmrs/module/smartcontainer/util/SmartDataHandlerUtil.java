package org.openmrs.module.smartcontainer.util;

import java.util.Date;

import org.openmrs.Concept;
import org.openmrs.ConceptNumeric;
import org.openmrs.api.context.Context;
import org.openmrs.module.smartcontainer.ConceptMappingNotFoundException;
import org.openmrs.module.smartcontainer.SmartConceptMap;
import org.openmrs.module.smartcontainer.smartData.CodeProvenance;
import org.openmrs.module.smartcontainer.smartData.CodedValue;
import org.openmrs.module.smartcontainer.smartData.ValueAndUnit;
import org.openmrs.module.smartcontainer.smartData.ValueRange;
import org.openmrs.module.smartcontainer.smartData.VitalSign;
import org.springframework.format.datetime.DateFormatter;

public class SmartDataHandlerUtil {

	/**
	 * Helper method to convert Date into ISO-8601 string
	 * 
	 * @param date
	 * @return
	 */
	public static String date(Date date) {

		DateFormatter parser = new DateFormatter("yyyy-MM-dd");
		return parser.print(date, Context.getLocale());

	}

	/**
	 * 
	 * @param concept
	 * @param map
	 * @return
	 */
	public static CodedValue codedValueHelper(Concept concept,
			SmartConceptMap map) {
		CodedValue code = new CodedValue();
		code.setTitle(codedValueNameHelper(concept));
		code.setCode(codedValueCodeHelper(concept, map));
		code.setCodeBaseURL(map.getBaseURL());
		code.setCodeProvenance(codedValueProvenanceHelper(concept, map));
		return code;
	}

	/**
	 * 
	 * @param concept
	 * @param map
	 * @return
	 */
	private static CodeProvenance codedValueProvenanceHelper(Concept concept,
			SmartConceptMap map) {
		CodeProvenance provenance = new CodeProvenance();
		provenance.setSourceCode(codedValueSourceCodeHelper(concept));
		provenance.setTitle(codedValueNameHelper(concept));
		provenance.setSourceCodeBaseURL("http://openmrs.org/codes/");
		/*
		 * Translation Fidelity is one of
		 * 1.http://smartplatforms.org/terms/code/fidelity#unknown
		 * 2.http://smartplatforms.org/terms/code/fidelity#automated
		 * 3.http://smartplatforms.org/terms/code/fidelity#verified
		 */
		provenance.setTranslationFidelity("verified");
		provenance
				.setTranslationFidelityBaseURL("http://smartplatforms.org/terms/code/fidelity#");
		return provenance;
	}

	/**
	 * 
	 * @param concept
	 * @return
	 */
	private static String codedValueSourceCodeHelper(Concept concept) {

		return concept.getConceptId().toString();
	}

	/**
	 * 
	 * @param concept
	 * @param map
	 * @return
	 */
	private static String codedValueCodeHelper(Concept concept,
			SmartConceptMap map) {
		String code = null;
		try {
			code = map.lookUp(concept);
		} catch (ConceptMappingNotFoundException e) {
			throw new RuntimeException(e);
		}
		return code;
	}

	/**
	 * 
	 * @param concept
	 * @return
	 */
	private static String codedValueNameHelper(Concept concept) {

		return concept.getName().getName();
	}

	/**
	 * 
	 * @param valueNumeric
	 * @param units
	 * @return
	 */
	public static ValueAndUnit valueAndUnitHelper(Double valueNumeric,
			String units) {
		ValueAndUnit val = new ValueAndUnit();
		val.setValue(valueNumeric.toString());
		val.setUnit(units);

		return val;
	}

	/**
	 * 
	 * @param high
	 * @param low
	 * @param unit
	 * @return
	 */
	public static ValueRange rangeHelper(Double high, Double low, String unit) {
		ValueRange range = new ValueRange();
		if (high != null)
			range.setMaximum(SmartDataHandlerUtil
					.valueAndUnitHelper(high, unit));
		if (low != null)
			range.setMinimum(SmartDataHandlerUtil.valueAndUnitHelper(low, unit));
		return range;
	}

	/**
	 * 
	 * @param quantity
	 * @param units
	 * @return
	 */
	public static ValueAndUnit valueAndUnitHelper(Integer quantity, String units) {
		ValueAndUnit val = new ValueAndUnit();
		val.setValue(quantity.toString());
		val.setUnit(units);
		return val;
	}

	/**
	 * 
	 * @param valueNumeric
	 * @param cn
	 * @param smartConceptMap
	 * @return
	 */
	public static VitalSign vitalSignHelper(Double valueNumeric,
			ConceptNumeric cn, SmartConceptMap smartConceptMap) {
		VitalSign sign = new VitalSign();
		sign.setVitalName(SmartDataHandlerUtil.codedValueHelper(cn,
				smartConceptMap));
		sign.setValue(valueNumeric.toString());
		sign.setUnit(cn.getUnits());
		return sign;
	}

	/**
	 * @return true or false based on the global property object for whether to use the problem 
	 * @see SmartConstants#GP_USE_PROBLEM_OBJECT_NAME
	 */
	public static boolean useProblemObject() {
		String useProblemObjectGP = Context.getAdministrationService().getGlobalProperty(SmartConstants.GP_USE_PROBLEM_OBJECT_NAME, "false");
		return Boolean.parseBoolean(useProblemObjectGP);
	}
	
	/**
	 * @return true or false based on the global property object for whether to use 
	 * @see SmartConstants#GP_USE_OBS_FOR_PROBLEM
	 */
	public static boolean useObsForProblems() {
		String useObsForProblems = Context.getAdministrationService().getGlobalProperty(SmartConstants.GP_USE_OBS_FOR_PROBLEM, "false");
		return Boolean.parseBoolean(useObsForProblems);
	}
}
