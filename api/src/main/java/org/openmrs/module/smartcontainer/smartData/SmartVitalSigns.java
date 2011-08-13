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
public class SmartVitalSigns implements SmartBaseData {
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
		BloodPressure bloodPreassure = new BloodPressure();
		Boolean isBpFound = false;
		for (VitalSign v : signList) {
			String conceptCode = v.getVitalName().getCode();
			if (conceptCode.equals("8462-4")) {
				bloodPreassure.setDiastolic(v);
				isBpFound = true;
			} else if (conceptCode.equals("8480-6")) {
				bloodPreassure.setSystolic(v);
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
			setBloodPressure(bloodPreassure);

	}

}
