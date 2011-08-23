package org.openmrs.module.smartcontainer.web.controller.api;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Patient;
import org.openmrs.api.context.Context;
import org.openmrs.module.smartcontainer.SmartDataService;
import org.openmrs.module.smartcontainer.rdfsource.LabResultRDFSource;
import org.openmrs.module.smartcontainer.smartData.SmartLabResult;
import org.openrdf.rio.RDFHandlerException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.util.List;

@Controller
@RequestMapping(value = "/smartcontainer/api/")
public class LabResultController {
    Log log = LogFactory.getLog(getClass());
    private LabResultRDFSource resource;

    public LabResultRDFSource getResource() {
        return resource;
    }

    public void setResource(LabResultRDFSource resource) {
        this.resource = resource;
    }

    @RequestMapping(method = RequestMethod.GET, value = "records/{pid}/lab_results")
    public ModelAndView handle(@PathVariable("pid") Patient patient,
                               HttpServletResponse resp) {
        log.info("In the LabResult Controller");
        resp.setContentType("text/xml"); // actually I use a constant
        Writer writer;
        try {
            writer = resp.getWriter();
            List<SmartLabResult> labs = Context.getService(
                    SmartDataService.class).getAllForPatient(patient,
                    SmartLabResult.class);
            writer.write(resource.getRDF(labs));
            writer.close();
        } catch (IOException e) {

            log.error("Unable to write out LabResult for pid: " + patient.getId(), e);
        } catch (RDFHandlerException e) {
            log.error("Unable to write out LabResult for pid: " + patient.getId(), e);
        }
        return null; // indicates this controller did all necessary processing

    }
}
