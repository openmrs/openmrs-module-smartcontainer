package org.openmrs.module.smartcontainer.smartData;

/**
 * The class representing SMART datatype
 * 
 *  <a
 *      href="http://wiki.chip.org/smart-project/index.php/Developers_Documentation:_SMART_Data_Model#Demographics_RDF">
 *      Demographics</a>
 *
 */
public class SmartDemographics implements SmartBaseData {
	private String givenName;
	private String familyName;
	private String gender;
	private String zipCode;
	private String birthDate;

	public String getGivenName() {
		return givenName;
	}

	public void setGivenName(String givenName) {
		this.givenName = givenName;
	}

	public String getFamilyName() {
		return familyName;
	}

	public void setFamilyName(String familyName) {
		this.familyName = familyName;
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
		return "SmartDemographics [givenName=" + givenName + ", familyName="
				+ familyName + ", gender=" + gender + ", zipCode=" + zipCode
				+ ", birthDate=" + birthDate + "]";
	}

}
