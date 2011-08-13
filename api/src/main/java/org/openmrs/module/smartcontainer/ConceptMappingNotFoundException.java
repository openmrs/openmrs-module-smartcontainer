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
 * The Exception thrown when the SMARTConceptMap is not able to find appropriate
 * mapped concept code
 * 
 */
public class ConceptMappingNotFoundException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * No arg constructor
	 */
	public ConceptMappingNotFoundException() {
		super();

	}

	/**
	 * @param message
	 * @param cause
	 */
	public ConceptMappingNotFoundException(String message, Throwable cause) {
		super(message, cause);

	}

	/**
	 * @param message
	 */
	public ConceptMappingNotFoundException(String message) {
		super(message);

	}

	/**
	 * @param cause
	 */
	public ConceptMappingNotFoundException(Throwable cause) {
		super(cause);

	}

}
