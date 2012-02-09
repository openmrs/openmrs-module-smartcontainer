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
package org.openmrs.module.smartcontainer.smartData;

/**
 * The class representing SMART datatype for AllergyException
 * <p>
 * <a href=
 * "http://wiki.chip.org/smart-project/index.php/Developers_Documentation:_SMART_Data_Model#AllergyException_RDF"
 * > AllergyException</a>
 * </p>
 */
public class SmartAllergyException extends BaseSmartData {
	
	private CodedValue exception;
	
	/**
	 * @return the exception
	 */
	public CodedValue getException() {
		return exception;
	}
	
	/**
	 * @param exception the exception to set
	 */
	public void setException(CodedValue exception) {
		this.exception = exception;
	}
	
	@Override
	public String toString() {
		return exception.getTitle();
	}
}
