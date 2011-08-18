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

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.simple.parser.ContainerFactory;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Contains methods to parse the manifest file of the SMART App
 */
public class ManifestParser {
	
	private Log log = LogFactory.getLog(this.getClass());
	
	private Map<String, String> json;
	
	/**
	 * @return the json
	 */
	public Map<String, String> getJson() {
		return json;
	}
	
	/**
	 * @param json the json to set
	 */
	public void setJson(Map json) {
		this.json = json;
	}
	
	/**
	 * Parse a manifest file
	 * 
	 * @param file
	 * @return
	 * @should parse a manifest file given as a String
	 */
	public Boolean parseFile(String file) {
		String jsonText = null;
		
		jsonText = (file);
		
		return parse(jsonText);
	}
	
	/**
	 * parse a json object in a String
	 * 
	 * @param jsonText
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Boolean parse(String jsonText) {
		try {
			
			JSONParser parser = new JSONParser();
			ContainerFactory containerFactory = new ContainerFactory() {
				
				public List creatArrayContainer() {
					return new LinkedList();
				}
				
				public Map createObjectContainer() {
					return new LinkedHashMap();
				}
				
			};
			
			try {
				json = (Map) parser.parse(jsonText, containerFactory);
				
			}
			catch (ParseException pe) {
				log.info(pe.getCause());
			}
		}
		catch (Exception e) {
			log.info(e.getCause());
		}
		Iterator iter = json.entrySet().iterator();
		log.info("==iterate result==");
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			log.info(entry.getKey() + "=>" + entry.getValue());
		}
		return !json.isEmpty();
	}
	
	public Object get(String key) {
		return json.get(key);
	}
}
