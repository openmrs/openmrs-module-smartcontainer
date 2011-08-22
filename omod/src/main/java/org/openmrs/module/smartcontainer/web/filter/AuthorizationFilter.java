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
package org.openmrs.module.smartcontainer.web.filter;

import java.io.IOException;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;
import org.openmrs.module.smartcontainer.web.controller.ManageAppUserlevelController;
import org.openmrs.util.OpenmrsUtil;
import org.springframework.web.bind.ServletRequestUtils;

/**
 * Filter intended for all /ws/smartcontainer calls that allows the smart app deployed to OpenMRS to
 * make http requests to the smart rest API
 */
public class AuthorizationFilter implements Filter {
	
	private static final Log log = LogFactory.getLog(AuthorizationFilter.class);
	
	/**
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	@Override
	public void init(FilterConfig config) throws ServletException {
		if (log.isDebugEnabled())
			log.error("Initializing SMART REST Authorization filter");
	}
	
	/**
	 * @see javax.servlet.Filter#destroy()
	 */
	@Override
	public void destroy() {
		if (log.isDebugEnabled())
			log.debug("Destroying SMART REST Authorization filter");
	}
	
	/**
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest,
	 *      javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
	    ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		//only allow requests from  smart apps installed in this container instance
		if (Context.isAuthenticated() && request.getRemoteAddr().equals(request.getLocalAddr())) {
			try {
				String userAndAppId = ServletRequestUtils.getRequiredStringParameter(httpRequest, "userAndAppId");
				String accessToken = ServletRequestUtils.getRequiredStringParameter(httpRequest, "accessToken");
				//check if we know this combination
				Map<String, String> credentials = ManageAppUserlevelController.getUserAppAccessTokenMap();
				if (credentials.containsKey(userAndAppId)
				        && OpenmrsUtil.nullSafeEquals(accessToken, credentials.get(userAndAppId))) {
					chain.doFilter(request, response);
					return;
				}
			}
			catch (Exception ex) {
				log.error("Error occurred:" + ex.getMessage());
				//do nothing
			}
		}
		
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN);
	}
}
