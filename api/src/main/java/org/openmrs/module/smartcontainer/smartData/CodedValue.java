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
 * href="http://wiki.chip.org/smart-project/index.php/Developers_Documentation:_SMART_Data_Model#CodedValue_RDF">
 * CodedValue</a>
 */
public class CodedValue {
    private String code;
    private String codeBaseURL;
    private String title;
    private CodeProvenance codeProvenance;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCodeBaseURL() {
        return codeBaseURL;
    }

    public void setCodeBaseURL(String codeBaseURL) {
        this.codeBaseURL = codeBaseURL;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public CodeProvenance getCodeProvenance() {
        return codeProvenance;
    }

    public void setCodeProvenance(CodeProvenance codeProvenance) {
        this.codeProvenance = codeProvenance;
    }

}
