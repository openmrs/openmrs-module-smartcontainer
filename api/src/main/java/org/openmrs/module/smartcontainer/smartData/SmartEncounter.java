package org.openmrs.module.smartcontainer.smartData;



public class SmartEncounter {
private String startDate;
private String endDate;
private CodedValue encounterType;
public String getStartDate() {
	return startDate;
}
public void setStartDate(String startDate) {
	this.startDate = startDate;
}
public String getEndDate() {
	return endDate;
}
public void setEndDate(String endDate) {
	this.endDate = endDate;
}
public CodedValue getEncounterType() {
	return encounterType;
}
public void setEncounterType(CodedValue encounterType) {
	this.encounterType = encounterType;
}

}
