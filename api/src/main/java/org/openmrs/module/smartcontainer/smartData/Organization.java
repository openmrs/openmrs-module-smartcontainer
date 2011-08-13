package org.openmrs.module.smartcontainer.smartData;

/**
 * The class representing SMART datatype
 * 
 * <a
 *      href="http://wiki.chip.org/smart-project/index.php/Developers_Documentation:_SMART_Data_Model#Organization_RDF">
 *      Organization</a>
 *
 */
public class Organization {
	private String name;
	private Address address;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

}
