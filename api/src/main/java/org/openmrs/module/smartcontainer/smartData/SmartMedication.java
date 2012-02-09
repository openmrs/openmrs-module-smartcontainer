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
 * The class representing SMART datatype
 * <p/>
 * <a
 * href="http://wiki.chip.org/smart-project/index.php/Preview_Data_Model#Medication_RDF">
 * Medication</a>
 */
public class SmartMedication extends BaseSmartData {

	private String id;
    private CodedValue drugName;
    private String startDate;
    private String endDate;
    private String instructions;
    private ValueAndUnit quantity;
    private ValueAndUnit frequency;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
    public CodedValue getDrugName() {
        return drugName;
    }

    public void setDrugName(CodedValue drugName) {
        this.drugName = drugName;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public ValueAndUnit getQuantity() {
        return quantity;
    }

    public void setQuantity(ValueAndUnit quantity) {
        this.quantity = quantity;
    }

    public ValueAndUnit getFrequency() {
        return frequency;
    }

    public void setFrequency(ValueAndUnit frequency) {
        this.frequency = frequency;
    }

}
