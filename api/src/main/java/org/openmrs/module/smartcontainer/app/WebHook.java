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
package org.openmrs.module.smartcontainer.app;

/**
 * Represents the Web Hook related to a SMART App
 */
public class WebHook {

    private Integer webHookId;

    private String URL;

    private String name;

    private String description;

    /**
     * @return the webHookId
     */
    public Integer getWebHookId() {
        return webHookId;
    }

    /**
     * @param webHookId the webHookId to set
     */
    public void setWebHookId(Integer webHookId) {
        this.webHookId = webHookId;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return the uRL
     */
    public String getURL() {
        return URL;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @param uRL the uRL to set
     */
    public void setURL(String uRL) {
        URL = uRL;
    }

    /**
     * @param name
     * @param description
     * @param uRL
     */
    public WebHook(String name, String description, String uRL) {
        super();
        this.name = name;
        this.description = description;
        URL = uRL;
    }

    /**
     * constructor
     */
    public WebHook() {

    }
}
