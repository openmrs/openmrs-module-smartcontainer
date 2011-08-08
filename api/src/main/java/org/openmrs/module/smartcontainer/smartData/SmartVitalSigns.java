package org.openmrs.module.smartcontainer.smartData;

import java.util.Date;

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

}
