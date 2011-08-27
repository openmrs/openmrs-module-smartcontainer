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
package org.openmrs.module.smartcontainer.db.hibernate;

import java.util.Collection;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.openmrs.api.APIException;
import org.openmrs.api.db.DAOException;
import org.openmrs.module.smartcontainer.SmartUser;
import org.openmrs.module.smartcontainer.db.UserDAO;

/**
 * Implementation for User DAO
 */
public class HibernateUserDAO implements UserDAO {
	
	private static final Log log = LogFactory.getLog(HibernateUserDAO.class);
	
	private SessionFactory sessionFactory;
	
	/**
	 * @return the sessionFactory
	 */
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	
	/**
	 * @param sessionFactory the sessionFactory to set
	 */
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	/**
	 * @see org.openmrs.module.smartcontainer.db.UserDAO#getUserByName(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public SmartUser getUserByName(String name) throws DAOException {
		Query query = sessionFactory
		        .getCurrentSession()
		        .createQuery(
		            "from SmartUser u where u.openMRSUser.retired = 0 and (u.openMRSUser.username = ? or u.openMRSUser.systemId = ?)");
		query.setString(0, name);
		query.setString(1, name);
		List<SmartUser> users = query.list();
		
		if (users.size() > 0) {
			if (users.size() > 1)
				log.warn("Found multiple users matching the username or system id:" + name);
			
			return users.get(0);
		}
		
		return null;
	}
	
	/**
	 * @see org.openmrs.module.smartcontainer.db.UserDAO#getAllUsers()
	 */
	public Collection<SmartUser> getAllUsers() throws DAOException {
		
		return sessionFactory.getCurrentSession().createQuery("from SmartUser u order by u.openMRSUser.userId").list();
	}
	
	/**
	 * @see org.openmrs.module.smartcontainer.db.UserDAO#saveUser(org.openmrs.module.smartcontainer.SmartUser)
	 */
	public void saveUser(SmartUser user) throws APIException {
		
		sessionFactory.getCurrentSession().saveOrUpdate(user);
		
	}
	
}
