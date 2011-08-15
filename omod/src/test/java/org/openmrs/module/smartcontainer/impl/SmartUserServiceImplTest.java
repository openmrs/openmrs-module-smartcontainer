/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.module.smartcontainer.impl;

import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.api.context.Context;
import static org.junit.Assert.*;
import org.openmrs.module.smartcontainer.SmartUser;
import org.openmrs.module.smartcontainer.SmartUserService;
import org.openmrs.test.BaseModuleContextSensitiveTest;

/**
 *
 * @author aja
 */
public class SmartUserServiceImplTest extends BaseModuleContextSensitiveTest {
    private  SmartUserService userService;
    public SmartUserServiceImplTest() {
    }

    @Before
    public void setUp() {
        try {
            executeDataSet("smartdataset.xml");
        } catch (Exception ex) {
            Logger.getLogger(SmartUserServiceImplTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        userService=Context.getService(SmartUserService.class);
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getUserByName method, of class SmartUserServiceImpl.
     */
    @Test
    public void testGetUserByName() {
       
        String name = "admin";
        String expResult = name;
        SmartUser result = userService.getUserByName(name);
        assertEquals(expResult, result.getOpenMRSUser().getSystemId());
       
    }

    /**
     * Test of getAllUsers method, of class SmartUserServiceImpl.
     */
    @Test
    public void testGetAllUsers() {
        Collection result = userService.getAllUsers();
        assertFalse(result.isEmpty());
    }

    /**
     * Test of saveUser method, of class SmartUserServiceImpl.
     */
    @Test
    public void testSaveUser() {
        SmartUser savedUser=null;
        SmartUser user = userService.getUserByName("admin");
        user.getOpenMRSUser().setSystemId("root");
        userService.saveUser(user);
        
        savedUser=userService.getUserByName("root");
        assertEquals(user.getsMARTAppUserId(), savedUser.getsMARTAppUserId());
        
    }
}
