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

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.simple.parser.ParseException;

/**
 * Used to construct a SMART App from its manifest file
 */
public class AppFactory {
	
	public static Log log = LogFactory.getLog(AppFactory.class);
	
	public static final String NAME = "name";
	
	public static final String DESCRIPTION = "description";
	
	public static final String AUTHOR = "author";
	
	public static final String VERSION = "version";
	
	public static final String BASEURL = "base_url";
	
	public static final String ICON = "icon";
	
	public static final String SMARTAPPID = "id";
	
	public static final String URL = "url";
	
	private static ManifestParser pa;
	
	static String temp = null;
	
	/**
	 * Method to construct the App
	 * 
	 * @param maniFile
	 * @return
	 * @throws IOException
	 * @throws ParseException
	 */
	public static App getAppFromLocalFile(InputStream maniFileAsStream) throws ParseException, IOException {
		String maniFile = new Scanner(maniFileAsStream).useDelimiter("\\A").next();
		return getApp(maniFile);
		
	}
	
	/**
	 * Method to construct the App
	 * 
	 * @param maniFile
	 * @return
	 * @throws ParseException
	 */
	public static App getAppFromUrl(String url) throws MalformedURLException, IOException, ParseException {
		URL appURL = new URL(url);
		String maniFile = null;
		maniFile = new Scanner((InputStream) appURL.getContent()).useDelimiter("\\A").next();
		return getApp(maniFile);
		
	}
	
	/**
	 * helper method
	 * 
	 * @param pa
	 * @param manifestJsonText
	 * @return
	 * @throws ParseException
	 * @throws IOException
	 * @should return an App
	 */
	private static App getApp(String manifestJsonText) throws IOException, ParseException {
		App app = new App();
		pa = new ManifestParser();
		pa.parse(manifestJsonText);
		app.setName((String) pa.get(NAME));
		
		app.setAuthor((String) pa.get(AUTHOR));
		app.setDescription((String) pa.get(DESCRIPTION));
		temp = (String) pa.get(ICON);
		temp = removeBaseURL(temp, (String) pa.get(BASEURL));
		app.setIcon(temp);
		app.setsMARTAppId((String) pa.get(SMARTAPPID));
		app.setVersion((String) pa.get(VERSION));
		app.setManifest(manifestJsonText);
		
		return app;
	}
	
	/**
	 * helper method to insert base url into all URL
	 * 
	 * @param url
	 * @param base
	 * @return
	 */
	private static String removeBaseURL(String url, String base) {
		
		url = url.replaceAll("\\{base_url\\}", base);
		log.info(url);
		return url;
	}
}
