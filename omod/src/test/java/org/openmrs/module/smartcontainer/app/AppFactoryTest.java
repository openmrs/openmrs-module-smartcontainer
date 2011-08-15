/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.module.smartcontainer.app;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;
import java.io.InputStream;
import java.io.DataInputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openmrs.module.smartcontainer.app.App;
import org.openmrs.module.smartcontainer.app.AppFactory;
import org.openmrs.util.OpenmrsUtil;
import static org.junit.Assert.*;

/**
 *
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
    public void testgetAppFromLocalFile() {
      InputStream manifest=getClass().getClassLoader().getResourceAsStream("smart_manifest.json");
        assertNotNull(manifest);
        App result = AppFactory.getAppFromLocalFile(manifest);
        assertNotNull(result.getName());
        assertNotNull(result.getAuthor());
        assertNotNull(result.getAuthor());
        assertNotNull(result.getIcon());
    }
    /**
     * Test of getApp method, of class AppFactory.
     */
    @Test
    public void testGetAppFromUrl() {
        try {
            App result = AppFactory.getAppFromUrl("http://sample-apps.smartplatforms.org/framework/cardio_risk_viz/smart_manifest.json");
            assertNotNull(result.getName());
            assertNotNull(result.getAuthor());
            assertNotNull(result.getAuthor());
            assertNotNull(result.getIcon());
        } catch (MalformedURLException ex) {
            fail(ex.getMessage());
        } catch (IOException ex) {
            fail(ex.getMessage());
        }
    }
    /**
     * Test of getApp method, of class AppFactory.
     */
    @Test
    public void testGetAppFromUrlShouldThrowExceptionIfURLisMalformed() {
        try {
            App result = AppFactory.getAppFromUrl("sample-apps.smartplatforms.org/framework/cardio_risk_viz/smart_manifest.json");
            assertNotNull(result.getName());
            assertNotNull(result.getAuthor());
            assertNotNull(result.getAuthor());
            assertNotNull(result.getIcon());
        } catch (MalformedURLException ex) {
            assertTrue(true);
        } catch (IOException ex) {
            fail(ex.getMessage());
        }
    }
}
