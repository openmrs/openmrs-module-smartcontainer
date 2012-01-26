/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.module.smartcontainer.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Concept;
import org.openmrs.ConceptNumeric;
import org.openmrs.ConceptSource;
import org.openmrs.GlobalProperty;
import org.openmrs.User;
import org.openmrs.api.APIException;
import org.openmrs.api.ConceptService;
import org.openmrs.api.GlobalPropertyListener;
import org.openmrs.api.context.Context;
import org.openmrs.module.smartcontainer.ConceptMappingNotFoundException;
import org.openmrs.module.smartcontainer.SmartConceptMapCodeSource;
import org.openmrs.module.smartcontainer.TransientSmartConceptMap;
import org.openmrs.module.smartcontainer.smartData.CodeProvenance;
import org.openmrs.module.smartcontainer.smartData.CodedValue;
import org.openmrs.module.smartcontainer.smartData.ValueAndUnit;
import org.openmrs.module.smartcontainer.smartData.ValueRange;
import org.openmrs.module.smartcontainer.smartData.VitalSign;
import org.springframework.format.datetime.DateFormatter;

public class SmartDataHandlerUtil implements GlobalPropertyListener {
	
	private static final Log log = LogFactory.getLog(SmartDataHandlerUtil.class);
	
	private static ConceptSource linkedSnomedConceptSource;
	
	private static ConceptSource linkedLoincConceptSource;
	
	private static ConceptSource linkedRxNormConceptSource;
	
	private static ConceptSource linkedFdaConceptSource;
	
	/**
	 * Helper method to convert Date into ISO-8601 string
	 * 
	 * @param date
	 * @return
	 */
	public static String date(Date date) {
		
		DateFormatter parser = new DateFormatter("yyyy-MM-dd 00:00:00");
		return parser.print(date, Context.getLocale());
		
	}
	
	/**
	 * @param concept
	 * @param smartMapSource
	 * @param conceptSource
	 * @param isCodeRequired
	 * @return
	 */
	public static CodedValue codedValueHelper(Concept concept, SmartConceptMapCodeSource smartMapSource,
	                                          ConceptSource conceptSource, boolean isCodeRequired) {
		CodedValue code = codedValueHelper(codedValueNameHelper(concept),
		    codedValueCodeHelper(concept, smartMapSource, conceptSource, isCodeRequired), smartMapSource);
		code.setCodeProvenance(codedValueProvenanceHelper(concept, smartMapSource, conceptSource));
		return code;
	}
	
	/**
	 * Also see {@link #codedValueCodeHelper(Concept, SmartConceptMapCodeSource)}
	 * 
	 * @param title
	 * @param codedValue
	 * @param smartMapSource
	 * @return
	 */
	public static CodedValue codedValueHelper(String title, String codedValue, SmartConceptMapCodeSource smartMapSource) {
		CodedValue code = new CodedValue();
		code.setTitle(title);
		code.setCode(codedValue);
		code.setCodeBaseURL(smartMapSource.getBaseURL());
		return code;
	}
	
	/**
	 * @param concept
	 * @param smartMapSource
	 * @param conceptSource
	 * @return
	 */
	private static CodeProvenance codedValueProvenanceHelper(Concept concept, SmartConceptMapCodeSource smartMapSource,
	                                                         ConceptSource conceptSource) {
		CodeProvenance provenance = new CodeProvenance();
		provenance.setSourceCode(codedValueCodeHelper(concept, smartMapSource, conceptSource, false));
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
	 * @param smartMapSource
	 * @param conceptSource
	 * @return
	 */
	private static String codedValueCodeHelper(Concept concept, SmartConceptMapCodeSource smartMapSource,
	                                           ConceptSource conceptSource, boolean isCodeRequired) {
		String code = Context.getMessageSourceService().getMessage("smartcontainer.mappingRequired");
		if (StringUtils.isNotBlank(code))
			code = code + "*";
		
		try {
			code = smartMapSource.lookUp(concept, (conceptSource != null) ? conceptSource.getName() : null);
		}
		catch (ConceptMappingNotFoundException e) {
			if (isCodeRequired)
				throw new APIException("Concept mapping not found for: " + concept.getDisplayString(), e);
		}
		
		return code;
	}
	
	/**
	 * @param concept
	 * @return
	 */
	private static String codedValueNameHelper(Concept concept) {
		
		return concept.getDisplayString();
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
	 * @param mapSource
	 * @param conceptSource
	 * @return
	 */
	public static VitalSign vitalSignHelper(Double valueNumeric, ConceptNumeric cn, SmartConceptMapCodeSource mapSource,
	                                        ConceptSource conceptSource) {
		VitalSign sign = new VitalSign();
		//we need the actual source code because we need to compare it to the codes used in the required mappings
		sign.setVitalName(codedValueHelper(cn, mapSource, conceptSource, true));
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
	
	/**
	 * Utility method that gets concept mappings for the names and source codes in the specified map
	 * to the concept source matching the specified source name
	 * 
	 * @param sourceName the concept source name of the concept source to search
	 * @param nameSourceCodeMap a mapping for the name and source code to look up
	 * @return a list of {@link TransientSmartConceptMap}s
	 */
	public static List<TransientSmartConceptMap> getConceptMappings(String sourceName, Map<String, String> nameSourceCodeMap) {
		List<TransientSmartConceptMap> conceptMappings = new ArrayList<TransientSmartConceptMap>();
		if (StringUtils.isNotBlank(sourceName) && nameSourceCodeMap != null) {
			ConceptService cs = Context.getConceptService();
			for (Map.Entry<String, String> entry : nameSourceCodeMap.entrySet()) {
				Concept concept = cs.getConceptByMapping(entry.getValue(), sourceName);
				conceptMappings.add(new TransientSmartConceptMap(entry.getValue(), sourceName, entry.getKey(),
				        (concept != null) ? concept.getConceptId() : null));
			}
		}
		
		return conceptMappings;
	}
	
	/**
	 * Gets the user name if not empty, else the systemId, for a given user.
	 * 
	 * @param user the user.
	 * @return the user name or systemId.
	 */
	public static String getUserNameOrSystemId(User user) {
		return StringUtils.isBlank(user.getUsername()) ? user.getSystemId() : user.getUsername();
	}
	
	/**
	 * @return the linkedSnomedConceptSource
	 */
	public static ConceptSource getLinkedSnomedConceptSource() {
		if (linkedSnomedConceptSource == null)
			linkedSnomedConceptSource = getConceptSourceByGlobalProperty(SmartConstants.GP_LOCAL_SNOMED_CONCEPT_SOURCE_ID);
		return linkedSnomedConceptSource;
	}
	
	/**
	 * @return the linkedLoincConceptSource
	 */
	public static ConceptSource getLinkedLoincConceptSource() {
		if (linkedLoincConceptSource == null)
			linkedLoincConceptSource = getConceptSourceByGlobalProperty(SmartConstants.GP_LOCAL_LOINC_CONCEPT_SOURCE_ID);
		return linkedLoincConceptSource;
	}
	
	/**
	 * @return the linkedRxNormConceptSource
	 */
	public static ConceptSource getLinkedRxNormConceptSource() {
		if (linkedRxNormConceptSource == null)
			linkedRxNormConceptSource = getConceptSourceByGlobalProperty(SmartConstants.GP_LOCAL_RxNORM_CONCEPT_SOURCE_ID);
		return linkedRxNormConceptSource;
	}
	
	/**
	 * @return the linkedFdaConceptSource
	 */
	public static ConceptSource getLinkedFdaConceptSource() {
		if (linkedFdaConceptSource == null)
			linkedFdaConceptSource = getConceptSourceByGlobalProperty(SmartConstants.GP_LOCAL_FDA_CONCEPT_SOURCE_ID);
		return linkedFdaConceptSource;
	}
	
	/**
	 * Convenience method that gets the concept source with an id matching the value of the
	 * specified global property name
	 * 
	 * @param globalPropertyName the name of the global property
	 * @return {@link ConceptSource}
	 */
	public static ConceptSource getConceptSourceByGlobalProperty(String globalPropertyName) {
		String gpValue = Context.getAdministrationService().getGlobalProperty(globalPropertyName);
		if (StringUtils.isNotBlank(gpValue)) {
			try {
				return Context.getConceptService().getConceptSource(Integer.valueOf(gpValue));
			}
			catch (NumberFormatException e) {
				log.warn("The value of the global property '" + globalPropertyName + "' should be a positive integer");
			}
		}
		return null;
	}
	
	@Override
	public void globalPropertyChanged(GlobalProperty gp) {
		if (SmartConstants.GP_LOCAL_SNOMED_CONCEPT_SOURCE_ID.equals(gp.getProperty()))
			linkedSnomedConceptSource = getConceptSourceByGlobalProperty(gp.getProperty());
		else if (SmartConstants.GP_LOCAL_LOINC_CONCEPT_SOURCE_ID.equals(gp.getProperty()))
			linkedLoincConceptSource = getConceptSourceByGlobalProperty(gp.getProperty());
		else if (SmartConstants.GP_LOCAL_RxNORM_CONCEPT_SOURCE_ID.equals(gp.getProperty()))
			linkedRxNormConceptSource = getConceptSourceByGlobalProperty(gp.getProperty());
		else if (SmartConstants.GP_LOCAL_FDA_CONCEPT_SOURCE_ID.equals(gp.getProperty()))
			linkedFdaConceptSource = getConceptSourceByGlobalProperty(gp.getProperty());
	}
	
	@Override
	public void globalPropertyDeleted(String gpName) {
		if (SmartConstants.GP_LOCAL_SNOMED_CONCEPT_SOURCE_ID.equals(gpName))
			linkedSnomedConceptSource = null;
		else if (SmartConstants.GP_LOCAL_LOINC_CONCEPT_SOURCE_ID.equals(gpName))
			linkedLoincConceptSource = null;
		else if (SmartConstants.GP_LOCAL_RxNORM_CONCEPT_SOURCE_ID.equals(gpName))
			linkedRxNormConceptSource = null;
		else if (SmartConstants.GP_LOCAL_FDA_CONCEPT_SOURCE_ID.equals(gpName))
			linkedFdaConceptSource = null;
	}
	
	@Override
	public boolean supportsPropertyName(String gpName) {
		return SmartConstants.GP_LOCAL_SNOMED_CONCEPT_SOURCE_ID.equals(gpName)
		        || SmartConstants.GP_LOCAL_LOINC_CONCEPT_SOURCE_ID.equals(gpName)
		        || SmartConstants.GP_LOCAL_RxNORM_CONCEPT_SOURCE_ID.equals(gpName)
		        || SmartConstants.GP_LOCAL_FDA_CONCEPT_SOURCE_ID.equals(gpName);
	}
}
