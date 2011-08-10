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
package org.openmrs.module.smartcontainer.impl;

import java.util.List;
import java.util.Map;

import org.openmrs.Patient;
import org.openmrs.module.smartcontainer.SmartDataService;
import org.openmrs.module.smartcontainer.smartData.SmartBaseData;
import org.openmrs.module.smartcontainer.smartData.handler.SmartDataHandler;

/**
 * Actual implementation of SmartDataService
 * 
 */
public class SmartDataServiceImpl implements SmartDataService {
	private Map<String, SmartDataHandler> handlers;

	public Map<String, SmartDataHandler> getHandlers() {
		return handlers;
	}

	/**
	 * This setter acts more like an "appender". If the list already has
	 * elements, calling this method will add to the list of handlers instead of
	 * replacing it.
	 * 
	 * @param h
	 */
	public void setHandlers(Map<String, SmartDataHandler> h) {
		if (this.handlers == null) {
			this.handlers = h;
		} else {
			for (String s : h.keySet()) {
				if (!handlers.containsKey(s)) {
					handlers.put(s, h.get(s));
				}
			}
		}
	}

	/**
	 * @see org.openmrs.module.smartcontainer.SmartDataService#getAllForPatient(org.openmrs.Patient,
	 *      java.lang.Class)
	 */
	public List<? extends SmartBaseData> getAllForPatient(Patient patient,
			Class clazz) {
		// Get a handler from the handlers which is mapped to simple name of the
		// clazz and call getAllForPatient(patient)
		return handlers.get(clazz.getSimpleName()).getAllForPatient(patient);
	}

	/**
	 * @see org.openmrs.module.smartcontainer.SmartDataService#getForPatient(org.openmrs.Patient,
	 *      java.lang.Class)
	 */
	public SmartBaseData getForPatient(Patient patient, Class clazz) {
		// Get a handler from the handlers which is mapped to simple name of the
		// clazz and call getForPatient(patient)
		return handlers.get(clazz.getSimpleName()).get(patient);

	}

}
