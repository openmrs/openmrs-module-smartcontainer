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
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.openmrs.api.db.DAOException;
import org.openmrs.module.smartcontainer.app.App;
import org.openmrs.module.smartcontainer.db.AppDAO;

/**
 * Implimentation for App DAO
 */
public class HibernateAppDAO implements AppDAO {
	
	protected Log log = LogFactory.getLog(this.getClass());
	
	private SessionFactory sessionFactory;
	
	/**
	 * getter for session factory
	 * 
	 * @return
	 */
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	
	/**
	 * setter for session factory
	 * 
	 * @param sessionFactory
	 */
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	/**
	 * @see org.openmrs.module.smartcontainer.db.AppDAO#getAppByName(java.lang.String)
	 */
	public App getAppByName(String name) throws DAOException {
		Query query = sessionFactory.getCurrentSession().createQuery("from App a where  a.name = ?");
		query.setString(0, name);
		@SuppressWarnings("unchecked")
		List<App> users = (List<App>) query.list();
		
		if (users == null || ((List<App>) users).size() == 0) {
			log.warn("request for username '" + name + "' not found");
			return null;
		}
		
		return (App) users.get(0);
	}
	
	/**
	 * @see org.openmrs.module.smartcontainer.db.AppDAO#getAllApps()
	 */
	@SuppressWarnings("unchecked")
	public List<App> getAllApps() throws DAOException {
		
		return sessionFactory.getCurrentSession().createQuery("from App u order by u.appId").list();
	}
	
	/**
	 * @see org.openmrs.module.smartcontainer.db.AppDAO#getAppById(java.lang.Integer)
	 */
	public App getAppById(Integer id) {
		Query query = sessionFactory.getCurrentSession().createQuery("from App a where  a.appId = ?");
		query.setInteger(0, id);
		// query.setString(1, name);
		@SuppressWarnings("unchecked")
		List<App> apps = (List) query.list();
		
		if (apps == null || ((List<App>) apps).size() == 0) {
			log.warn("request for username '" + id + "' not found");
			return null;
		}
		
		return (App) apps.get(0);
	}
	
	/**
	 * @see org.openmrs.module.smartcontainer.db.AppDAO#save(org.openmrs.module.smartcontainer.app.App)
	 */
	public void save(App newApp) {
		sessionFactory.getCurrentSession().saveOrUpdate(newApp);
		
	}
	
	/**
	 * @see org.openmrs.module.smartcontainer.db.AppDAO#delete(org.openmrs.module.smartcontainer.app.App)
	 */
	public void deleteApp(App app) {
		sessionFactory.getCurrentSession().delete(app);
	}
	
	/**
	 * @see org.openmrs.module.smartcontainer.db.AppDAO#getApps(Collection)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<App> getApps(Collection<Integer> exclude) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(App.class);
		criteria.add(Restrictions.not(Restrictions.in("appId", exclude)));
		criteria.add(Restrictions.eq("retired", false));
		return criteria.list();
	}
}
