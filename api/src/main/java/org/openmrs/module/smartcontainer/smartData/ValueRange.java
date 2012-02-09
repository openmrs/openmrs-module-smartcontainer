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
 * href="http://wiki.chip.org/smart-project/index.php/Preview_Data_Model#ValueRange_RDF">
 * ValueRange</a>
 */
public class ValueRange {
    private ValueAndUnit minimum;
    private ValueAndUnit maximum;

    public ValueAndUnit getMinimum() {
        return minimum;
    }

    public void setMinimum(ValueAndUnit minimum) {
        this.minimum = minimum;
    }

    public ValueAndUnit getMaximum() {
        return maximum;
    }

    public void setMaximum(ValueAndUnit maximum) {
        this.maximum = maximum;
    }

}
