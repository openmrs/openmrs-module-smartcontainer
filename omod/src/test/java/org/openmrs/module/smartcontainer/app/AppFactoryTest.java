/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.module.smartcontainer.app;

import org.junit.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;

import static org.junit.Assert.*;

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
