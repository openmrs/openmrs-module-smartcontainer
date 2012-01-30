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

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.openmrs.User;
import org.openmrs.api.db.DAOException;
import org.openmrs.module.smartcontainer.app.App;
import org.openmrs.module.smartcontainer.app.UserHiddenAppMap;
import org.openmrs.module.smartcontainer.db.AppDAO;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation for App DAO
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
	@Transactional(readOnly = true)
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
	 * @see org.openmrs.module.smartcontainer.db.AppDAO#getApps(boolean)
	 */
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public List<App> getApps(boolean includeRetired) throws DAOException {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(App.class);
		if (!includeRetired)
			criteria.add(Restrictions.eq("retired", false));
		return criteria.list();
	}
	
	/**
	 * @see org.openmrs.module.smartcontainer.db.AppDAO#getAppById(java.lang.Integer)
	 */
	@SuppressWarnings("rawtypes")
	@Transactional(readOnly = true)
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
	@Transactional
	public void save(App newApp) {
		sessionFactory.getCurrentSession().saveOrUpdate(newApp);
		
	}
	
	/**
	 * @see org.openmrs.module.smartcontainer.db.AppDAO#delete(org.openmrs.module.smartcontainer.app.App)
	 */
	@Transactional
	public void deleteApp(App app) {
		deleteAssociatedUserHiddenAppMaps(app);
		sessionFactory.getCurrentSession().delete(app);
	}
	
	/**
	 * @see org.openmrs.module.smartcontainer.db.AppDAO#getUserVisibleApps(User)
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<App> getUserVisibleApps(User user) {
		return createAppCriteria(user, true, null).list();
	}
	
	/**
	 * @see org.openmrs.module.smartcontainer.db.AppDAO#getUserHiddenApps(org.openmrs.User, boolean)
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<App> getUserHiddenApps(User user, boolean includeRetiredByOtherUsers) {
		return createAppCriteria(user, false, includeRetiredByOtherUsers).list();
	}
	
	/**
	 * Creates a criteria for fetching either visible or hidden Apps for the specified user
	 * 
	 * @param user the user to match against
	 * @param includeHiddenByOtherUsers
	 * @param getVisible specifies the apps to fetch i.e visible Vs hidden
	 * @return the {@link Criteria} object
	 */
	private Criteria createAppCriteria(User user, boolean getVisible, Boolean includeHiddenByOtherUsers) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(App.class, "app");
		criteria.add(Restrictions.eq("app.retired", false));
		
		DetachedCriteria dc = DetachedCriteria.forClass(UserHiddenAppMap.class, "map")
		        .add(Restrictions.eq("map.user", user)).setProjection(Projections.distinct(Projections.property("map.app")));
		if (!getVisible && !includeHiddenByOtherUsers)
			dc.add(Restrictions.eq("map.hiddenBy", user));
		
		Criterion criterion = (getVisible) ? Property.forName("app.appId").notIn(dc) : Property.forName("app.appId").in(dc);
		criteria.add(criterion);
		
		return criteria;
		
	}
	
	/**
	 * @see org.openmrs.module.smartcontainer.db.AppDAO#saveUserHiddenAppMap(org.openmrs.module.smartcontainer.app.UserHiddenAppMap)
	 */
	@Override
	@Transactional
	public UserHiddenAppMap saveUserHiddenAppMap(UserHiddenAppMap userHiddenAppMap) {
		sessionFactory.getCurrentSession().save(userHiddenAppMap);
		return userHiddenAppMap;
	}
	
	/**
	 * @see org.openmrs.module.smartcontainer.db.AppDAO#deleteUserHiddenAppMap(org.openmrs.User,
	 *      org.openmrs.module.smartcontainer.app.App)
	 */
	@Override
	@Transactional
	public void deleteUserHiddenAppMap(User user, App app) {
		sessionFactory.getCurrentSession().createQuery("DELETE FROM UserHiddenAppMap WHERE app = :app AND user = :user")
		        .setParameter("app", app).setParameter("user", user).executeUpdate();
	}
	
	/**
	 * Utility method that deletes all {@link UserHiddenAppMap}s referencing the specified
	 * {@link App}.
	 * 
	 * @param app the app to match against when deleting maps
	 */
	@Transactional
	private void deleteAssociatedUserHiddenAppMaps(App app) {
		sessionFactory.getCurrentSession().createQuery("DELETE FROM UserHiddenAppMap WHERE app = :app")
		        .setParameter("app", app).executeUpdate();
	}
}
