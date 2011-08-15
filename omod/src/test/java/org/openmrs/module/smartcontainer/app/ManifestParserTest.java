/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.module.smartcontainer.app;

import java.util.Scanner;
import java.io.InputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author aja
 */
public class ManifestParserTest {
    
    public ManifestParserTest() {
    }

    
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    
    
    /**
     * Test of parse method, of class ManifestParser.
     */
    @Test
    public void testParse() {
        InputStream manifest=getClass().getClassLoader().getResourceAsStream("smart_manifest.json");
        assertNotNull(manifest);
        String maniFile = new Scanner(manifest).useDelimiter("\\A").next();
        ManifestParser instance = new ManifestParser();
        Boolean result = instance.parse(maniFile);
        assertTrue(result);
    }

    
}
