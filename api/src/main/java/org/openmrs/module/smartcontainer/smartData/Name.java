package org.openmrs.module.smartcontainer.smartData;

import org.openmrs.PersonName;

/**
 * The class representing SMART datatype for PersonName
 * 
 * @see <a
 *      href="http://wiki.chip.org/smart-project/index.php/Developers_Documentation:_SMART_Data_Model#Name_RDF">
 *      Name</a>
 */
public class Name {
	
	private String givenName;
	
	private String familyName;
	
	private String middleName;
	
	private String namePrefix;
	
	private String nameSuffix;
	
	/**
	 * Convenience constructor
	 * 
	 * @param personName
	 */
	public Name(PersonName personName) {
		givenName = personName.getGivenName();
		familyName = personName.getFamilyName();
		middleName = personName.getMiddleName();
		namePrefix = personName.getPrefix();
		nameSuffix = personName.getFamilyNameSuffix();
	}
	
	/**
	 * @return the givenName
	 */
	public String getGivenName() {
		return givenName;
	}
	
	/**
	 * @param givenName the givenName to set
	 */
	public void setGivenName(String givenName) {
		this.givenName = givenName;
	}
	
	/**
	 * @return the familyName
	 */
	public String getFamilyName() {
		return familyName;
	}
	
	/**
	 * @param familyName the familyName to set
	 */
	public void setFamilyName(String familyName) {
		this.familyName = familyName;
	}
	
	/**
	 * @return the middleName
	 */
	public String getMiddleName() {
		return middleName;
	}
	
	/**
	 * @param middleName the middleName to set
	 */
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}
	
	/**
	 * @return the namePrefix
	 */
	public String getNamePrefix() {
		return namePrefix;
	}
	
	/**
	 * @param namePrefix the namePrefix to set
	 */
	public void setNamePrefix(String namePrefix) {
		this.namePrefix = namePrefix;
	}
	
	/**
	 * @return the nameSuffix
	 */
	public String getNameSuffix() {
		return nameSuffix;
	}
	
	/**
	 * @param nameSuffix the nameSuffix to set
	 */
	public void setNameSuffix(String nameSuffix) {
		this.nameSuffix = nameSuffix;
	}
}
