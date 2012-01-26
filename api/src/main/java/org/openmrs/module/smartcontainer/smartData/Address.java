package org.openmrs.module.smartcontainer.smartData;

import org.openmrs.PersonAddress;

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
	
	private String region;
	
	private boolean preferred;
	
	/**
	 * Convenience constructor
	 * 
	 * @param address
	 */
	public Address(PersonAddress address) {
		streetAddress = address.getAddress1();
		locality = address.getCityVillage();
		postalCode = address.getPostalCode();
		countryName = address.getCountry();
		region = address.getAddress6();
		preferred = address.isPreferred();
	}
	
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
	
	/**
	 * @return the region
	 */
	public String getRegion() {
		return region;
	}
	
	/**
	 * @param region the region to set
	 */
	public void setRegion(String region) {
		this.region = region;
	}
	
	/**
	 * @return the preferred
	 */
	public boolean isPreferred() {
		return preferred;
	}
	
	/**
	 * @param preferred the preferred to set
	 */
	public void setPreferred(boolean preferred) {
		this.preferred = preferred;
	}
	
}
