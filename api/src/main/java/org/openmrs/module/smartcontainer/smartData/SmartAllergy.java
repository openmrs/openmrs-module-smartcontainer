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
 * The class represents the SMART datatype for "Allergy"
 * 
 * <a href=
 * "http://wiki.chip.org/smart-project/index.php/Developers_Documentation:_SMART_Data_Model#Allergy_RDF"
 * > SMART Allergy documentation</a>
 * 
 */
public class SmartAllergy extends BaseSmartData {
	private CodedValue category;
	private CodedValue severity;
	private CodedValue classOfAllergen;
	private CodedValue substance;
	private String notes;
	private CodedValue reaction;

	public CodedValue getCategory() {
		return category;
	}

	public void setCategory(CodedValue category) {
		this.category = category;
	}

	public CodedValue getSeverity() {
		return severity;
	}

	public void setSeverity(CodedValue severity) {
		this.severity = severity;
	}

	public CodedValue getClassOfAllergen() {
		return classOfAllergen;
	}

	public void setClassOfAllergen(CodedValue classOfAllergen) {
		this.classOfAllergen = classOfAllergen;
	}

	public CodedValue getSubstance() {
		return substance;
	}

	public void setSubstance(CodedValue substance) {
		this.substance = substance;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public CodedValue getReaction() {
		return reaction;
	}

	public void setReaction(CodedValue reaction) {
		this.reaction = reaction;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("SmartAllergy [");
		sb.append("category=").append(category);
		sb.append(",severity=").append(severity);
		sb.append(",classOfAllergen=").append(classOfAllergen);
		sb.append(",substance=").append(substance);
		sb.append(",notes=").append(notes);
		sb.append(",reaction=").append(reaction);
		sb.append("]");
		return sb.toString();
	}

}
