package org.openmrs.module.smartcontainer.smartData;

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
