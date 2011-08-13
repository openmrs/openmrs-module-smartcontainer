package org.openmrs.module.smartcontainer.smartData;

/**
 * The class representing SMART datatype
 * 
 *  <a
 *      href="http://wiki.chip.org/smart-project/index.php/Developers_Documentation:_SMART_Data_Model#QuantitativeResult_RDF">
 *      QuantitativeResult</a>
 *
 */
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
