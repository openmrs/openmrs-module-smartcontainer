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

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Concept;
import org.openmrs.ConceptMap;

/**
 * Class is used to retrieve and cache the mapped concepts
 */
public class SmartConceptMap {

	/**
	 * Hash map to cache source codes for concepts which were already
	 * looked up. Saves processing when accessed multiple times.
	 */
	private Map<Concept, String> conceptMap = new HashMap<Concept, String>();
	
	private final Log log = LogFactory.getLog(getClass());
	
	/**
	 * Variable set through the moduleapplicationcontext.xml; this represents the
	 * concept source name (LOINC vs SNOMED-CT vs ICD9)
	 */
	private String conceptSourceName;
	
	/**
	 * Variable set through the moduleapplicationcontext.xml;this represents the
	 * name space URL for a Concept source
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
	 * Find the source code on the given concept for source <code>{@link #getConceptSourceName()}</code>
	 * 
	 * TODO: We really should add this as a convenience method on concept.  Something like <code>String getSourceCode(sourceName)</code>
	 * 
	 * @param concept
	 * @return the source code on this concept or an exception if not found
	 * @throws ConceptMappingNotFoundException if the given concept does 
	 * 			not have a mapping to the current source code name
	 */
	public String lookUp(Concept concept)
			throws ConceptMappingNotFoundException {
		log.info("concept source name: " + conceptSourceName + ".");
		
		// look in our cache for the code first to see if we've already looked up this concept
		String sourceCode = conceptMap.get(concept);
		if (sourceCode != null)
			return sourceCode;
			
		// we don't have a cache of the code, search for a concept mapping 
		// which has our concept source name
		ConceptMap map = null;
		
		for (ConceptMap cm : concept.getConceptMappings()) {
			if (cm.getSource().getName().equals(getConceptSourceName())) {
				map = cm;
				break;
			}
		}
		
		// if the mapping is found, cache it. otherwise throw an exception
		if (map != null) {
			sourceCode = map.getSourceCode();
			conceptMap.put(concept, sourceCode);
			return sourceCode;
			
		} else {
			// TODO there should be better way of indicating the failure of
			// finding mapped concept code.
			throw new ConceptMappingNotFoundException(
					"Can not find Concept mapping for concept " + concept + " with source: " + getConceptSourceName());
		}

	}

	/**
	 * resets the cache
	 */
	public void resetCache() {
		conceptMap.clear();
	}

}
