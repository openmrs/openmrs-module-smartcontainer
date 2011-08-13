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
package org.openmrs.module.smartcontainer;

import java.util.Collection;
import java.util.Set;

import org.openmrs.Privilege;
import org.openmrs.Role;
import org.openmrs.User;
import org.openmrs.module.smartcontainer.app.App;

/**
 * Represents the OpenMRS User with assigned selected SMART Apps
 */
public class SmartUser {

	private static final long serialVersionUID = 1L;

	private Integer sMARTAppUserId;

	private Set<App> apps;

	private User openMRSUser;

	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	/**
	 * @return the apps
	 */
	public Set<App> getApps() {
		return apps;
	}

	/**
	 * @param apps
	 *            the apps to set
	 */
	public void setApps(Set<App> apps) {
		this.apps = apps;
	}

	/**
 * 
 */
	public SmartUser() {
		super();

	}

	/**
	 * @return the sMARTAppUserId
	 */
	public Integer getsMARTAppUserId() {
		return sMARTAppUserId;
	}

	/**
	 * @return the openMRSUser
	 */
	public User getOpenMRSUser() {
		return openMRSUser;
	}

	/**
	 * @param sMARTAppUserId
	 *            the sMARTAppUserId to set
	 */
	public void setsMARTAppUserId(Integer sMARTAppUserId) {
		this.sMARTAppUserId = sMARTAppUserId;
	}

	/**
	 * @param openMRSUser
	 *            the openMRSUser to set
	 */
	public void setOpenMRSUser(User openMRSUser) {
		this.openMRSUser = openMRSUser;
	}

	public boolean isSuperUser() {
		return openMRSUser.isSuperUser();
	}

	/**
	 * This method shouldn't be used directly. Use
	 * org.openmrs.api.context.Context#hasPrivilege so that
	 * anonymous/authenticated/proxy privileges are all included Return true if
	 * this user has the specified privilege
	 * 
	 * @param privilege
	 * @return true/false depending on whether user has specified privilege
	 */
	public boolean hasPrivilege(String privilege) {

		return this.openMRSUser.hasPrivilege(privilege);
	}

	/**
	 * Check if this user has the given String role
	 * 
	 * @param r
	 *            String name of a role to check
	 * @return Returns true if this user has the specified role, false otherwise
	 */
	public boolean hasRole(String r) {
		return this.openMRSUser.hasRole(r);
	}

	/**
	 * Checks if this user has the given String role
	 * 
	 * @param r
	 *            String name of a role to check
	 * @param ignoreSuperUser
	 *            If this is false, then this method will always return true for
	 *            a superuser.
	 * @return Returns true if the user has the given role, or if
	 *         ignoreSuperUser is false and the user is a superUser
	 */
	public boolean hasRole(String r, boolean ignoreSuperUser) {
		return this.openMRSUser.hasRole(r, ignoreSuperUser);
	}

	/**
	 * Get <i>all</i> privileges this user has. This delves into all of the
	 * roles that a person has, appending unique privileges
	 * 
	 * @return Collection of complete Privileges this user has
	 */
	public Collection<Privilege> getPrivileges() {
		return this.openMRSUser.getPrivileges();
	}

	/**
	 * Compares two objects for similarity
	 * 
	 * @param obj
	 * @return boolean true/false whether or not they are the same objects
	 */
	@Override
	public boolean equals(Object obj) {
		return this.openMRSUser.equals(obj);
	}

	/**
	 * The hashcode for a user is used to index the objects in a tree
	 * 
	 * @see org.openmrs.Person#hashCode()
	 */
	@Override
	public int hashCode() {
		return this.openMRSUser.hashCode();
	}

	/**
	 * Returns all roles attributed to this user by expanding the role list to
	 * include the parents of the assigned roles
	 * 
	 * @return all roles (inherited from parents and given) for this user
	 */
	public Set<Role> getAllRoles() {
		return this.openMRSUser.getAllRoles();
	}

	/**
	 * @return Returns the roles.
	 */
	public Set<Role> getRoles() {
		return this.openMRSUser.getRoles();
	}

	/**
	 * @param roles
	 *            The roles to set.
	 */
	public void setRoles(Set<Role> roles) {
		this.openMRSUser.setRoles(roles);
	}

	/**
	 * Add the given Role to the list of roles for this User
	 * 
	 * @param role
	 * @return Returns this user with the given role attached
	 */
	public User addRole(Role role) {
		return this.openMRSUser.addRole(role);
	}

	/**
	 * Remove the given Role from the list of roles for this User
	 * 
	 * @param role
	 * @return this user with the given role removed
	 */
	public User removeRole(Role role) {
		return this.openMRSUser.removeRole(role);
	}

}
