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
package org.openmrs.module.smartcontainer.web.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;
import org.openmrs.module.smartcontainer.SmartAppService;
import org.openmrs.module.smartcontainer.SmartUser;
import org.openmrs.module.smartcontainer.SmartUserService;
import org.openmrs.module.smartcontainer.app.App;
import org.openmrs.module.smartcontainer.app.AppFactory;
import org.openmrs.web.WebConstants;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 *
 */
@Controller
@RequestMapping(value = "module/smartcontainer/smartcontainerLink.form")
public class SmartAppListController {

    /**
     * Logger for this class and subclasses
     */
    protected final Log log = LogFactory.getLog(getClass());

    /**
     * Success form view name
     */
    private final String SUCCESS_FORM_VIEW = "/module/smartcontainer/smartcontainerForm";

    /**
     * Initially called after the formBackingObject method to get the landing form name
     *
     * @return String form view name
     */
    @RequestMapping(method = RequestMethod.GET)
    public String showForm() {
        return SUCCESS_FORM_VIEW;

    }

    /**
     * All the parameters are optional based on the necessity
     *
     * @param action
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView deleteApp(@RequestParam("action") String action, HttpServletRequest request) {

        ModelAndView modelAndView = new ModelAndView();
        SmartAppService appService = Context.getService(SmartAppService.class);
        SmartUserService userService = Context.getService(SmartUserService.class);
        Boolean isUploadFromURL = ServletRequestUtils.getBooleanParameter(request, "updateFromURL", false);
        HttpSession httpSession = request.getSession();
        //
        if ("removeApp".equals(action)) {
            Integer appId = null;
            try {
                appId = ServletRequestUtils.getIntParameter(request, "appId");
            } catch (ServletRequestBindingException e) {

                log.error("Unable to get appId parameter from request", e);
            }


            App app = appService.getAppById(appId);
            Set<App> userApps = null;
            for (SmartUser user : userService.getAllUsers()) {

                userApps = user.getApps();
                userApps.remove(app);
                user.setApps(null);
                user.setApps(userApps);
                userService.saveUser(user);

            }
            appService.deleteApp(app);

        } else if ("upload".equals(action)) {
            App newApp = null;
            if (isUploadFromURL) {
                String app = null;
                String url = ServletRequestUtils.getStringParameter(request, "manifestURL", "");
                log.info("URL stirng :" + url);

                try {
                    newApp = AppFactory.getAppFromUrl(url);
                    log.info("APP  :" + newApp);
                    List<App> apps = (List<App>) appService.getAllApps();
                    if (!apps.contains(newApp)) {
                        newApp.setRetire(false);
                        appService.saveApp(newApp);
                    }
                } catch (MalformedURLException e) {
                    httpSession.setAttribute(WebConstants.OPENMRS_ERROR_ATTR, "Malformed URL");
                    log.error("Malformed URL", e);
                } catch (IOException e) {
                    httpSession.setAttribute(WebConstants.OPENMRS_ERROR_ATTR, "Malformed Manifest file");
                    log.error("Malformed Manifest file", e);
                }


            } else {
                MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
                MultipartFile multipartFile = multipartRequest.getFile("moduleFile");

                String text = null;
                try {
                    newApp = AppFactory.getAppFromLocalFile(multipartFile.getInputStream());
                    List<App> apps = (List<App>) appService.getAllApps();
                    if (!apps.contains(newApp)) {
                        newApp.setRetire(false);
                        appService.saveApp(newApp);
                    } else {

                    }
                } catch (IOException e1) {
                    httpSession.setAttribute(WebConstants.OPENMRS_ERROR_ATTR, "Malformed Manifest file");
                    log.error("Malformed Manifest file", e1);
                }


            }
        }
        //
        Collection<App> apps = Context.getService(SmartAppService.class).getAllApps();
        modelAndView.setViewName(SUCCESS_FORM_VIEW);
        modelAndView.addObject("appList", apps);
        return modelAndView;
    }

    /**
     * This class returns the form backing object. This can be a string, a boolean, or a normal java
     * pojo. The bean name defined in the ModelAttribute annotation and the type can be just defined
     * by the return type of this method
     */
    @ModelAttribute("appList")
    protected Collection<App> formBackingObject(HttpServletRequest request) throws Exception {

        Collection<App> apps = Context.getService(SmartAppService.class).getAllApps();
        // Context.getService(UserService.class).saveUser(new SMARTAppUser());

        return apps;
    }

}
