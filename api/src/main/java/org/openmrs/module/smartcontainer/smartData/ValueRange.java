package org.openmrs.module.smartcontainer.smartData;

/**
 * The class representing SMART datatype
 * 
 *  <a
 *      href="http://wiki.chip.org/smart-project/index.php/Preview_Data_Model#ValueRange_RDF">
 *      ValueRange</a>
 *
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
