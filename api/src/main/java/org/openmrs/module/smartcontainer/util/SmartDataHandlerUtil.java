package org.openmrs.module.smartcontainer.util;

import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
	
	private static final Log log = LogFactory.getLog(SmartDataHandlerUtil.class);
	
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
	 * @param concept
	 * @param map
	 * @return
	 */
	public static CodedValue codedValueHelper(Concept concept, SmartConceptMap map) {
		CodedValue code = codedValueHelper(codedValueNameHelper(concept), codedValueCodeHelper(concept, map), map);
		code.setCodeProvenance(codedValueProvenanceHelper(concept, map));
		return code;
	}
	
	/**
	 * Also see {@link #codedValueCodeHelper(Concept, SmartConceptMap)}
	 * 
	 * @param title
	 * @param codedValue
	 * @param map
	 * @return
	 */
	public static CodedValue codedValueHelper(String title, String codedValue, SmartConceptMap map) {
		CodedValue code = new CodedValue();
		code.setTitle(title);
		code.setCode(codedValue);
		code.setCodeBaseURL(map.getBaseURL());
		return code;
	}
	
	/**
	 * @param concept
	 * @param map
	 * @return
	 */
	private static CodeProvenance codedValueProvenanceHelper(Concept concept, SmartConceptMap map) {
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
		provenance.setTranslationFidelityBaseURL("http://smartplatforms.org/terms/code/fidelity#");
		return provenance;
	}
	
	/**
	 * @param concept
	 * @return
	 */
	private static String codedValueSourceCodeHelper(Concept concept) {
		
		return concept.getConceptId().toString();
	}
	
	/**
	 * @param concept
	 * @param map
	 * @return
	 */
	private static String codedValueCodeHelper(Concept concept, SmartConceptMap map) {
		String code = null;
		try {
			code = map.lookUp(concept);
		}
		catch (ConceptMappingNotFoundException e) {
			throw new RuntimeException(e);
		}
		return code;
	}
	
	/**
	 * @param concept
	 * @return
	 */
	private static String codedValueNameHelper(Concept concept) {
		
		return concept.getName().getName();
	}
	
	/**
	 * @param valueNumeric
	 * @param units
	 * @return
	 */
	public static ValueAndUnit valueAndUnitHelper(Double valueNumeric, String units) {
		ValueAndUnit val = new ValueAndUnit();
		val.setValue(valueNumeric.toString());
		val.setUnit(units);
		
		return val;
	}
	
	/**
	 * @param high
	 * @param low
	 * @param unit
	 * @return
	 */
	public static ValueRange rangeHelper(Double high, Double low, String unit) {
		ValueRange range = new ValueRange();
		if (high != null)
			range.setMaximum(SmartDataHandlerUtil.valueAndUnitHelper(high, unit));
		if (low != null)
			range.setMinimum(SmartDataHandlerUtil.valueAndUnitHelper(low, unit));
		return range;
	}
	
	/**
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
	 * @param valueNumeric
	 * @param cn
	 * @param smartConceptMap
	 * @return
	 */
	public static VitalSign vitalSignHelper(Double valueNumeric, ConceptNumeric cn, SmartConceptMap smartConceptMap) {
		VitalSign sign = new VitalSign();
		sign.setVitalName(SmartDataHandlerUtil.codedValueHelper(cn, smartConceptMap));
		sign.setValue(valueNumeric.toString());
		sign.setUnit(cn.getUnits());
		return sign;
	}
	
	/**
	 * @return true or false based on the global property object for whether to use the problem
	 * @see SmartConstants#GP_USE_PROBLEM_OBJECT_NAME
	 */
	public static boolean useProblemObject() {
		String useProblemObjectGP = Context.getAdministrationService().getGlobalProperty(
		    SmartConstants.GP_USE_PROBLEM_OBJECT_NAME, "true");
		return Boolean.parseBoolean(useProblemObjectGP);
	}
	
	/**
	 * @return true or false based on the global property object for whether to use
	 * @see SmartConstants#GP_USE_OBS_FOR_PROBLEM
	 */
	public static boolean useObsForProblems() {
		String useObsForProblems = Context.getAdministrationService().getGlobalProperty(
		    SmartConstants.GP_USE_OBS_FOR_PROBLEM, "false");
		return Boolean.parseBoolean(useObsForProblems);
	}
	
	/**
	 * @return true or false based on the global property object for whether to use the allergy
	 *         activelist object/table
	 * @see SmartConstants#GP_USE_ALLERGY_OBJECT
	 */
	public static boolean useAllergyObject() {
		String useAllergyObjectGP = Context.getAdministrationService().getGlobalProperty(
		    SmartConstants.GP_USE_ALLERGY_OBJECT, "true");
		return Boolean.parseBoolean(useAllergyObjectGP);
	}
	
	/**
	 * Gets a concept that has a conceptId matching the value of the global property that has a name
	 * matching the specified name
	 * 
	 * @param globalPropertyName the name of the global property to match against
	 * @return the concept
	 */
	public static Concept getConceptByGlobalProperty(String globalPropertyName) {
		Concept concept = null;
		if (StringUtils.isNotBlank(globalPropertyName)) {
			String conceptIdGP = Context.getAdministrationService().getGlobalProperty(globalPropertyName);
			if (StringUtils.isNotBlank(conceptIdGP)) {
				try {
					concept = Context.getConceptService().getConcept(Integer.valueOf(conceptIdGP));
				}
				catch (NumberFormatException e) {
					log.warn(globalPropertyName + " global property has and invalid conceptId value:" + conceptIdGP);
				}
			}
		}
		
		return concept;
	}
}
