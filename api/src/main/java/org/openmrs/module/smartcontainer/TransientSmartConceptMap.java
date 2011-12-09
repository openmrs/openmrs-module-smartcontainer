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

/**
 * An Instance of this class holds information for a smart concept mapping
 */
public class TransientSmartConceptMap {
	
	/**
	 * The name of the concept source this mapping is associated to
	 */
	private String conceptSourceName;
	
	/**
	 * The concept name for the mapping
	 */
	private String conceptName;
	
	/**
	 * The conceptId of the concept in the local concept dictionary to map to
	 */
	private Integer conceptId;
	
	/**
	 * The unique source code for the concept in the source we are mapping to
	 */
	private String sourceCode;
	
	/**
	 * Convenience constructor that takes in a source code, source name, concept name and the
	 * conceptId of the concept to map to
	 * 
	 * @param sourceCode
	 * @param conceptSourceName
	 * @param conceptName
	 * @param conceptId
	 */
	public TransientSmartConceptMap(String sourceCode, String conceptSourceName, String conceptName, Integer conceptId) {
		this.sourceCode = sourceCode;
		this.conceptSourceName = conceptSourceName;
		this.conceptName = conceptName;
		this.conceptId = conceptId;
	}
	
	/**
	 * @return the conceptSourceName
	 */
	public String getConceptSourceName() {
		return conceptSourceName;
	}
	
	/**
	 * @param conceptSourceName the conceptSourceName to set
	 */
	public void setConceptSourceName(String conceptSourceName) {
		this.conceptSourceName = conceptSourceName;
	}
	
	/**
	 * @return the conceptName
	 */
	public String getConceptName() {
		return conceptName;
	}
	
	/**
	 * @param conceptName the conceptName to set
	 */
	public void setConceptName(String conceptName) {
		this.conceptName = conceptName;
	}
	
	/**
	 * @return the conceptId
	 */
	public Integer getConceptId() {
		return conceptId;
	}
	
	/**
	 * @param conceptId the conceptId to set
	 */
	public void setConceptId(Integer conceptId) {
		this.conceptId = conceptId;
	}
	
	/**
	 * @return the sourceCode
	 */
	public String getSourceCode() {
		return sourceCode;
	}
	
	/**
	 * @param sourceCode the sourceCode to set
	 */
	public void setSourceCode(String sourceCode) {
		this.sourceCode = sourceCode;
	}
	
}
