package org.openmrs.module.smartcontainer.smartData;

/**
 * The class representing SMART datatype <a href=
 * "http://wiki.chip.org/smart-project/index.php/Developers_Documentation:_SMART_Data_Model#Address_RDF"
 * > Address</a>
 */
public class Address {
	
	private String streetAddress;
	
	private String locality;
	
	private String postalCode;
	
	private String countryName;
	
	public String getStreetAddress() {
		return streetAddress;
	}
	
	public void setStreetAddress(String streetAddress) {
		this.streetAddress = streetAddress;
	}
	
	public String getLocality() {
		return locality;
	}
	
	public void setLocality(String locality) {
		this.locality = locality;
	}
	
	public String getPostalCode() {
		return postalCode;
	}
	
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}
	
	public String getCountryName() {
		return countryName;
	}
	
	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}
	
}
