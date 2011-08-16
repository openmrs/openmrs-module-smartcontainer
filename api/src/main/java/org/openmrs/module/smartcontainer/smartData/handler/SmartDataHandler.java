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
package org.openmrs.module.smartcontainer.smartData.handler;

import org.openmrs.Patient;
import org.openmrs.module.smartcontainer.smartData.SmartBaseData;

import java.util.List;

/**
 *
 *
 */
public interface SmartDataHandler<T extends SmartBaseData> {

    /**
     * @param patient
     * @return
     */
    public T getForPatient(Patient patient);

    /**
     * @param patient
     * @return
     */
    public List<T> getAllForPatient(Patient patient);
}
