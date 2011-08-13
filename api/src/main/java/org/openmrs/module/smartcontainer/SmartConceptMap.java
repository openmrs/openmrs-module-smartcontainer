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
 * 
 */
public class SmartConceptMap {

	/**
	 * Hash map to store a concept and corresponding mapped code which already
	 * looked up for later use
	 */
	private Map<Concept, String> conceptMap = new HashMap<Concept, String>();
	private final Log log = LogFactory.getLog(getClass());
	/**
	 * Variable set through the moduleapplicationcontext.xml;this represents the
	 * concept source name
	 */
	private String conceptSourceName;
	/**
	 * Variable set through the moduleapplicationcontext.xml;this represents the
	 * name space URL for a Concept source
	 */
	private String baseURL;
	private ConceptMap map = null;

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
	 * Actual method to look up the mapped concept source code for a concept
	 * 
	 * @param concept
	 * @return
	 * @throws ConceptMappingNotFoundException
	 */
	public String lookUp(Concept concept)
			throws ConceptMappingNotFoundException {
		log.info("concept source name: " + conceptSourceName + ".");
		String conceptSource = null;
		// get the concept source of the concept
		conceptSource = conceptMap.get(concept);
		// search for a concept map which has concept source name of
		if (conceptSource == null) {
			for (ConceptMap cm : concept.getConceptMappings()) {
				if (cm.getSource().getName().equals(getConceptSourceName())) {
					map = cm;
				}
			}
			// if the mapping is found cache it otherwise throw an exception
			if (map != null) {
				conceptSource = map.getSourceCode();
				conceptMap.put(concept, conceptSource);

			} else {
				// TODO there should be better way of indicating the failure of
				// finding mapped concept code.
				throw new ConceptMappingNotFoundException(
						"Can not find Concept mapping for concept " + concept);
			}
		}
		return conceptSource;

	}

	/**
	 * resets the cache
	 */
	public void resetCache() {
		conceptMap.clear();
	}

}
