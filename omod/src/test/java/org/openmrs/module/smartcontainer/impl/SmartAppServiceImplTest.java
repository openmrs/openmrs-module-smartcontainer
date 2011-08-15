/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.module.smartcontainer.impl;

import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.openmrs.User;
import org.openmrs.api.context.Context;
import org.openmrs.module.smartcontainer.app.App;
import org.openmrs.module.smartcontainer.db.AppDAO;
import org.openmrs.module.smartcontainer.SmartAppService;
import org.openmrs.test.BaseModuleContextSensitiveTest;

/**
 *
 * @author aja
 */
public class SmartAppServiceImplTest extends BaseModuleContextSensitiveTest{
    private  SmartAppService appService;
    
    public SmartAppServiceImplTest() {
    }

   
    @Before
    public void setUp() {
        try {
            executeDataSet("smartdataset.xml");
        } catch (Exception ex) {
            Logger.getLogger(SmartAppServiceImplTest.class.getName()).log(Level.SEVERE, null, ex);
        }
         appService = Context.getService(SmartAppService.class);
    }
    
    @After
    public void tearDown() {
    }

   
    /**
     * Test of getAppByName method, of class SmartAppServiceImpl.
     */
    @Test
    public void testGetAppByName() {
       
      
        
        App expResult = appService.getAppByName("PediBPC");
        assertEquals(expResult.getName(), "PediBPC");
       
    }

    /**
     * Test of getAllApps method, of class SmartAppServiceImpl.
     */
    @Test
    public void testGetAllApps() {
       
       Collection <App>result = appService.getAllApps();
        assertFalse(result.isEmpty());
    }

    /**
     * Test of deleteApp method, of class SmartAppServiceImpl.
     */
    @Test
    public void testDeleteApp() {
        
        App app = appService.getAppByName("PediBPC");
        appService.deleteApp(app);
        app=appService.getAppByName("PediBPC");
        assertTrue(app.getRetire());
    }

    /**
     * Test of getAppById method, of class SmartAppServiceImpl.
     */
    @Test
    public void testGetAppById() {
        App result = appService.getAppById(1);
        assertNotNull(result);
    }

    /**
     * Test of saveApp method, of class SmartAppServiceImpl.
     */
    @Test
    public void testSaveApp() {
       
        App newApp =new App();
        newApp.setName("BpGraph");
        appService.saveApp(newApp);
        assertNotNull(appService.getAppByName("BpGraph"));
    }

    /**
     * Test of getAppsByUserName method, of class SmartAppServiceImpl.
     */
    @Test
    public void testGetAppsByUserName() {
        
       Collection result = appService.getAppsByUserName(Context.getUserService().getUserByUsername("admin"));
       assertFalse(result.isEmpty());
    }
}