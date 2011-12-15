package org.openmrs.module.smartcontainer.smartData;

/**
 * The class representing SMART datatype
 * <p/>
 * <a
 * href="http://wiki.chip.org/smart-project/index.php/Preview_Data_Model#Lab_Result_RDF">
 * Lab_Result</a>
 */
public class SmartLabResult extends BaseSmartData {
    private CodedValue labName;
    private QuantitativeResult quantitativeResult;
    private String externalID;
    private CodedValue status;
    private CodedValue abnormalInterpretation;
    private Attribution specimenCollected;
    private Attribution specimenReceived;
    private Attribution resulted;
    private String comments;

    public CodedValue getLabName() {
        return labName;
    }

    public void setLabName(CodedValue labName) {
        this.labName = labName;
    }

    public QuantitativeResult getQuantitativeResult() {
        return quantitativeResult;
    }

    public void setQuantitativeResult(QuantitativeResult quantitativeResult) {
        this.quantitativeResult = quantitativeResult;
    }

    public String getExternalID() {
        return externalID;
    }

    public void setExternalID(String externalID) {
        this.externalID = externalID;
    }

    public CodedValue getStatus() {
        return status;
    }

    public void setStatus(CodedValue status) {
        this.status = status;
    }

    public CodedValue getAbnormalInterpretation() {
        return abnormalInterpretation;
    }

    public void setAbnormalInterpretation(CodedValue abnormalInterpretation) {
        this.abnormalInterpretation = abnormalInterpretation;
    }

    public Attribution getSpecimenCollected() {
        return specimenCollected;
    }

    public void setSpecimenCollected(Attribution specimenCollected) {
        this.specimenCollected = specimenCollected;
    }

    public Attribution getSpecimenReceived() {
        return specimenReceived;
    }

    public void setSpecimenReceived(Attribution specimenReceived) {
        this.specimenReceived = specimenReceived;
    }

    public Attribution getResulted() {
        return resulted;
    }

    public void setResulted(Attribution resulted) {
        this.resulted = resulted;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

}
