/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.module.smartcontainer.impl;

import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.openmrs.module.smartcontainer.app.App;
import org.openmrs.module.smartcontainer.db.AppDAO;

/**
 *
 * @author aja
 */
public class AppServiceImplTest {
    
    public AppServiceImplTest() {
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
     * Test of setDao method, of class AppServiceImpl.
     */
    @Test
    public void testSetDao() {
        System.out.println("setDao");
        AppDAO dao = null;
        SmartAppServiceImpl instance = new SmartAppServiceImpl();
        instance.setDao(dao);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getAppByName method, of class AppServiceImpl.
     */
    @Test
    public void testGetAppByName() {
        System.out.println("getAppByName");
        String name = "";
        SmartAppServiceImpl instance = new SmartAppServiceImpl();
        App expResult = null;
        App result = instance.getAppByName(name);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getAllApps method, of class AppServiceImpl.
     */
    @Test
    public void testGetAllApps() {
        System.out.println("getAllApps");
        SmartAppServiceImpl instance = new SmartAppServiceImpl();
        List expResult = null;
        List result = instance.getAllApps();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of DeleteApp method, of class AppServiceImpl.
     */
    @Test
    public void testDeleteApp() {
        System.out.println("DeleteApp");
        App app = null;
        SmartAppServiceImpl instance = new SmartAppServiceImpl();
       // instance.DeleteApp(app);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getAppById method, of class AppServiceImpl.
     */
    @Test
    public void testGetAppById() {
        System.out.println("getAppById");
        Integer id = null;
        SmartAppServiceImpl instance = new SmartAppServiceImpl();
        App expResult = null;
        App result = instance.getAppById(id);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of saveApp method, of class AppServiceImpl.
     */
    @Test
    public void testSaveApp() {
        System.out.println("saveApp");
        App newApp = null;
        SmartAppServiceImpl instance = new SmartAppServiceImpl();
        instance.saveApp(newApp);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
