/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.module.smartcontainer.app;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;

import org.json.simple.parser.ParseException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author aja
 */
public class AppFactoryTest {
	
	public AppFactoryTest() {
	}
	
	@BeforeClass
	public static void setUpClass() throws Exception {
	}
	
	@AfterClass
	public static void tearDownClass() throws Exception {
	}
	
	@Before
	public void setUp() {
	}
	
	@After
	public void tearDown() {
	}
	
	/**
	 * Test of getApp method, of class AppFactory.
	 */
	@Test
	public void testgetAppFromLocalFile() throws Exception {
		InputStream manifest = getClass().getClassLoader().getResourceAsStream("smart_manifest.json");
		assertNotNull(manifest);
		App result = AppFactory.getAppFromLocalFile(manifest);
		assertNotNull(result.getName());
		assertNotNull(result.getAuthor());
		assertNotNull(result.getAuthor());
		assertNotNull(result.getIcon());
	}
	
	/**
	 * Test of getApp method, of class AppFactory.
	 * 
	 * @throws ParseException
	 * @throws IOException
	 * @throws MalformedURLException
	 */
	@Test
	public void testGetAppFromUrl() throws ParseException, MalformedURLException, IOException {
		App result = AppFactory
		        .getAppFromUrl("http://sample-apps.smartplatforms.org/framework/cardio_risk_viz/smart_manifest.json");
		assertNotNull(result.getName());
		assertNotNull(result.getAuthor());
		assertNotNull(result.getAuthor());
		assertNotNull(result.getIcon());
	}
	
	/**
	 * Test of getApp method, of class AppFactory.
	 * 
	 * @throws ParseException
	 * @throws IOException
	 * @throws MalformedURLException
	 */
	@Test(expected = MalformedURLException.class)
	public void testGetAppFromUrlShouldThrowExceptionIfURLisMalformed() throws ParseException, MalformedURLException,
	    IOException {
		AppFactory.getAppFromUrl("sample-apps.smartplatforms.org/framework/cardio_risk_viz/smart_manifest.json");
	}
}
