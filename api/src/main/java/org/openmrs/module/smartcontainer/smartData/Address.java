/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
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
		region = address.getRegion();
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
