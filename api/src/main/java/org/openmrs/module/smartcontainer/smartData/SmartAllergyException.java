package org.openmrs.module.smartcontainer.smartData;

/**
 * The class representing SMART datatype for AllergyException
 * <p>
 * <a href=
 * "http://wiki.chip.org/smart-project/index.php/Developers_Documentation:_SMART_Data_Model#AllergyException_RDF"
 * > AllergyException</a>
 * </p>
 */
public class SmartAllergyException extends BaseSmartData {
	
	private CodedValue exception;
	
	/**
	 * @return the exception
	 */
	public CodedValue getException() {
		return exception;
	}
	
	/**
	 * @param exception the exception to set
	 */
	public void setException(CodedValue exception) {
		this.exception = exception;
	}
	
	@Override
	public String toString() {
		return exception.getTitle();
	}
}
