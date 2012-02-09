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

import java.util.List;

/**
 * The class representing SMART datatype
 * 
 * <a
 *      href="http://wiki.chip.org/smart-project/index.php/Preview_Data_Model#VitalSigns_RDF">
 *      VitalSigns</a>
 *
 */
public class SmartVitalSigns extends BaseSmartData {
	private String date;
	private SmartEncounter encounter;
	private VitalSign height;
	private VitalSign weight;
	private VitalSign bodyMassIndex;
	private VitalSign respiratoryRate;
	private VitalSign heartRate;
	private VitalSign oxygenSaturation;
	private VitalSign temperature;
	private BloodPressure bloodPressure;

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public SmartEncounter getSmartEncounter() {
		return encounter;
	}

	public void setSmartEncounter(SmartEncounter encounter) {
		this.encounter = encounter;
	}

	public VitalSign getHeight() {
		return height;
	}

	public void setHeight(VitalSign height) {
		this.height = height;
	}

	public VitalSign getWeight() {
		return weight;
	}

	public void setWeight(VitalSign weight) {
		this.weight = weight;
	}

	public VitalSign getBodyMassIndex() {
		return bodyMassIndex;
	}

	public void setBodyMassIndex(VitalSign bodyMassIndex) {
		this.bodyMassIndex = bodyMassIndex;
	}

	public VitalSign getRespiratoryRate() {
		return respiratoryRate;
	}

	public void setRespiratoryRate(VitalSign respiratoryRate) {
		this.respiratoryRate = respiratoryRate;
	}

	public VitalSign getHeartRate() {
		return heartRate;
	}

	public void setHeartRate(VitalSign heartRate) {
		this.heartRate = heartRate;
	}

	public VitalSign getOxygenSaturation() {
		return oxygenSaturation;
	}

	public void setOxygenSaturation(VitalSign oxygenSaturation) {
		this.oxygenSaturation = oxygenSaturation;
	}

	public VitalSign getTemperature() {
		return temperature;
	}

	public void setTemperature(VitalSign temperature) {
		this.temperature = temperature;
	}

	public BloodPressure getBloodPressure() {
		return bloodPressure;
	}

	public void setBloodPressure(BloodPressure bloodPressure) {
		this.bloodPressure = bloodPressure;
	}

	public void addAll(List<VitalSign> signList) {
		BloodPressure bloodPressure = new BloodPressure();
		Boolean isBpFound = false;
		for (VitalSign v : signList) {
			String conceptCode = v.getVitalName().getCode();
			if (conceptCode.equals("8462-4")) {
				bloodPressure.setDiastolic(v);
				isBpFound = true;
			} else if (conceptCode.equals("8480-6")) {
				bloodPressure.setSystolic(v);
				isBpFound = true;
			} else if (conceptCode.equals("8302-2")) {

				setHeight(v);

			} else if (conceptCode.equals("3141-9")) {

				setWeight(v);
			} else if (conceptCode.equals("39156-5")) {

				setBodyMassIndex(v);
			} else if (conceptCode.equals("9279-1")) {

				setRespiratoryRate(v);
			} else if (conceptCode.equals("8867-4")) {

				setHeartRate(v);
			} else if (conceptCode.equals("2710-2")) {

				setOxygenSaturation(v);
			} else if (conceptCode.equals("8310-5")) {

				setTemperature(v);

			}
		}
		if (isBpFound)
			setBloodPressure(bloodPressure);

	}

}
