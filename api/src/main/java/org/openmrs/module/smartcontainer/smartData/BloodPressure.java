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
