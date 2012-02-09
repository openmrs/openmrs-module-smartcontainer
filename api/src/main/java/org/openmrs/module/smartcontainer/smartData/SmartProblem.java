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
 * href="http://wiki.chip.org/smart-project/index.php/Preview_Data_Model#Problem_RDF">
 * Problem</a>
 */
public class SmartProblem extends BaseSmartData {
    private CodedValue problemName;
    private String onset;
    private String resolution;

    public CodedValue getProblemName() {
        return problemName;
    }

    public void setProblemName(CodedValue problemName) {
        this.problemName = problemName;
    }

    public String getOnset() {
        return onset;
    }

    public void setOnset(String onset) {
        this.onset = onset;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    @Override
    public String toString() {
        return "SmartProblem [problemName=" + problemName + ", onset=" + onset
                + ", resolution=" + resolution + "]";
    }

}
