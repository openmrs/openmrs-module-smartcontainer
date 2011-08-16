package org.openmrs.module.smartcontainer.smartData;

/**
 * A helper class to represent the SMART vital sign Blood Pressure
 */
public class BloodPressure {
    private VitalSign systolic;
    private VitalSign diastolic;
    private CodedValue bodyPosition;
    private CodedValue bodySite;

    public VitalSign getSystolic() {
        return systolic;
    }

    public void setSystolic(VitalSign systolic) {
        this.systolic = systolic;
    }

    public VitalSign getDiastolic() {
        return diastolic;
    }

    public void setDiastolic(VitalSign diastolic) {
        this.diastolic = diastolic;
    }

    public CodedValue getBodyPosition() {
        return bodyPosition;
    }

    public void setBodyPosition(CodedValue bodyPosition) {
        this.bodyPosition = bodyPosition;
    }

    public CodedValue getBodySite() {
        return bodySite;
    }

    public void setBodySite(CodedValue bodySite) {
        this.bodySite = bodySite;
    }

}
