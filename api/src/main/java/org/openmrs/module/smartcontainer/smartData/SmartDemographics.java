package org.openmrs.module.smartcontainer.smartData;

/**
 * The class representing SMART datatype
 * <p/>
 * <a href=
 * "http://wiki.chip.org/smart-project/index.php/Developers_Documentation:_SMART_Data_Model#Demographics_RDF"
 * > Demographics</a>
 */
public class SmartDemographics extends BaseSmartData {
	
	private Name name;
	
	private String identifier;
	
	private String identifierType;
	
	private String gender;
	
	private String zipCode;
	
	private String birthDate;
	
	private Address address;
	
	/**
	 * @return the name
	 */
	public Name getName() {
		return name;
	}
	
	/**
	 * @param name the name to set
	 */
	public void setName(Name name) {
		this.name = name;
	}
	
	/**
	 * @return the identifier
	 */
	public String getIdentifier() {
		return identifier;
	}
	
	/**
	 * @param identifier the identifier to set
	 */
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
	
	/**
	 * @return the identifierType
	 */
	public String getIdentifierType() {
		return identifierType;
	}
	
	/**
	 * @param identifierType the identifierType to set
	 */
	public void setIdentifierType(String identifierType) {
		this.identifierType = identifierType;
	}
	
	/**
	 * @return the address
	 */
	public Address getAddress() {
		return address;
	}
	
	/**
	 * @param address the address to set
	 */
	public void setAddress(Address address) {
		this.address = address;
	}
	
	public String getGender() {
		return gender;
	}
	
	public void setGender(String gender) {
		this.gender = gender;
	}
	
	public String getZipCode() {
		return zipCode;
	}
	
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	
	public String getBirthDate() {
		return birthDate;
	}
	
	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}
	
	@Override
	public String toString() {
		return "SmartDemographics [givenName=" + name.getGivenName() + ", familyName=" + name.getFamilyName() + ", gender="
		        + gender + ", zipCode=" + zipCode + ", birthDate=" + birthDate + "]";
	}
	
}
