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
package org.openmrs.module.smartcontainer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Concept;
import org.openmrs.ConceptMap;

/**
 * An instance of this class acts as a source for source codes for concept mappings.
 */
public class SmartConceptMapCodeSource {
	
	private static final Log log = LogFactory.getLog(SmartConceptMapCodeSource.class);
	
	/**
	 * Variable set through the moduleapplicationcontext.xml; this represents the concept source
	 * name (LOINC vs SNOMED-CT vs ICD9)
	 */
	private String conceptSourceName;
	
	/**
	 * Variable set through the moduleapplicationcontext.xml;this represents the name space URL for
	 * a Concept source
	 */
	private String baseURL;
	
	public String getBaseURL() {
		return baseURL;
	}
	
	public void setBaseURL(String base_URL) {
		this.baseURL = base_URL;
	}
	
	public String getConceptSourceName() {
		return conceptSourceName;
	}
	
	public void setConceptSourceName(String conceptSourceName) {
		this.conceptSourceName = conceptSourceName;
	}
	
	/**
	 * Find the source code on the given concept for source
	 * <code>{@link #getConceptSourceName()}</code> TODO: We really should add this as a convenience
	 * method on concept. Something like <code>String getSourceCode(sourceName)</code>
	 * 
	 * @param concept
	 * @param sourceName the concept source name of the local concept map to search
	 * @return the source code on this concept or an exception if not found
	 * @throws ConceptMappingNotFoundException if the given concept does not have a mapping to the
	 *             current source code name
	 */
	public String lookUp(Concept concept, String sourceName) throws ConceptMappingNotFoundException {
		log.info("concept source name: " + conceptSourceName + ".");
		ConceptMap map = null;
		
		for (ConceptMap cm : concept.getConceptMappings()) {
			//use the name of the linked local conceptSource defined by the related GP just 
			//in case it differs from what was defined in the module application context
			if (cm.getSource().getName().equals(sourceName) || cm.getSource().getName().equals(getConceptSourceName())) {
				map = cm;
				break;
			}
		}
		
		// if the mapping is found, cache it. otherwise throw an exception
		if (map == null) {
			// TODO there should be better way of indicating the failure of
			// finding mapped concept code.
			throw new ConceptMappingNotFoundException("Cannot find concept mapping to source " + getConceptSourceName()
			        + " on concept " + concept);
			
		}
		
		return map.getSourceCode();
	}
}
