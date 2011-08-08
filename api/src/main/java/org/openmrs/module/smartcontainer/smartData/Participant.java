package org.openmrs.module.smartcontainer.smartData;

public class Participant {
private Person person;
private Organization organization;
private String role;
public Person getPerson() {
	return person;
}
public void setPerson(Person person) {
	this.person = person;
}
public Organization getOrganization() {
	return organization;
}
public void setOrganization(Organization organization) {
	this.organization = organization;
}
public String getRole() {
	return role;
}
public void setRole(String role) {
	this.role = role;
}

}
