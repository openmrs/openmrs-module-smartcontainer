package org.openmrs.module.smartcontainer.smartData.handler;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.api.context.Context;
import org.openmrs.module.smartcontainer.smartData.SmartDemographics;
import org.openmrs.test.BaseModuleContextSensitiveTest;


/**
 * Created by IntelliJ IDEA.
 * User: aja
 * Date: 8/15/11
 * Time: 4:06 PM
 * To change this template use File | Settings | File Templates.
 */
public class SmartDemographicsHandlerTest extends BaseModuleContextSensitiveTest {
    @Before
    public void setUp() {
        try {
            executeDataSet("patient.xml");
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }

    /**
     * @verifies return SmartDemographics
     * @see SmartDemographicsHandler#getForPatient(org.openmrs.Patient)
     */
    @Test
    public void getForPatient_shouldReturnSmartDemographics() throws Exception {
        SmartDemographicsHandler smartDataHandler = new SmartDemographicsHandler();
        SmartDemographics smartDemographics = smartDataHandler.getForPatient(Context.getPatientService().getPatient(2));
        Assert.assertNotNull(smartDemographics);
        Assert.assertNotNull(smartDemographics.getFamilyName());
        Assert.assertNotNull(smartDemographics.getBirthDate());
        Assert.assertNotNull(smartDemographics.getGender());
        Assert.assertNotNull(smartDemographics.getZipCode());
    }
}
