package org.openmrs.module.smartcontainer.smartData;


/**
 * The class representing SMART datatype
 * <p/>
 * <a
 * href="http://wiki.chip.org/smart-project/index.php/Preview_Data_Model#Medication_RDF">
 * Medication</a>
 */
public class SmartMedication implements SmartBaseData {
    private CodedValue drugName;
    private String startDate;
    private String endDate;
    private String instructions;
    private ValueAndUnit quantity;
    private ValueAndUnit frequency;

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
