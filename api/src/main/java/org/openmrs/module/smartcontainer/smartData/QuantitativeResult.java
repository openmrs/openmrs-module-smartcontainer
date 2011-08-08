package org.openmrs.module.smartcontainer.smartData;

public class QuantitativeResult {
private ValueAndUnit valueAndUnit;
private ValueRange normalRange;
private ValueRange nonCriticalRange;
public ValueAndUnit getValueAndUnit() {
	return valueAndUnit;
}
public void setValueAndUnit(ValueAndUnit valueAndUnit) {
	this.valueAndUnit = valueAndUnit;
}
public ValueRange getNormalRange() {
	return normalRange;
}
public void setNormalRange(ValueRange normalRange) {
	this.normalRange = normalRange;
}
public ValueRange getNonCriticalRange() {
	return nonCriticalRange;
}
public void setNonCriticalRange(ValueRange nonCriticalRange) {
	this.nonCriticalRange = nonCriticalRange;
}

}
