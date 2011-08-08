package org.openmrs.module.smartcontainer.smartData;

public class VitalSign {
private CodedValue vitalName;
private String value;
private String unit;
public CodedValue getVitalName() {
	return vitalName;
}
public void setVitalName(CodedValue vitalName) {
	this.vitalName = vitalName;
}
public String getValue() {
	return value;
}
public void setValue(String value) {
	this.value = value;
}
public String getUnit() {
	return unit;
}
public void setUnit(String unit) {
	this.unit = unit;
}


}
