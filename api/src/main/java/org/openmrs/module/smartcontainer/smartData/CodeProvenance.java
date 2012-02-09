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
 * href="http://wiki.chip.org/smart-project/index.php/Developers_Documentation:_SMART_Data_Model#CodeProvenance_RDF">
 * CodeProvenance</a>
 */
public class CodeProvenance {
    private String sourceCode;
    private String sourceCodeBaseURL;
    private String title;
    private String translationFidelity;
    private String translationFidelityBaseURL;

    public String getSourceCode() {
        return sourceCode;
    }

    public void setSourceCode(String sourceCode) {
        this.sourceCode = sourceCode;
    }

    public String getSourceCodeBaseURL() {
        return sourceCodeBaseURL;
    }

    public void setSourceCodeBaseURL(String sourceCodeBaseURL) {
        this.sourceCodeBaseURL = sourceCodeBaseURL;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTranslationFidelity() {
        return translationFidelity;
    }

    public void setTranslationFidelity(String translationFidelity) {
        this.translationFidelity = translationFidelity;
    }

    public String getTranslationFidelityBaseURL() {
        return translationFidelityBaseURL;
    }

    public void setTranslationFidelityBaseURL(String translationFidelityBaseURL) {
        this.translationFidelityBaseURL = translationFidelityBaseURL;
    }

}
