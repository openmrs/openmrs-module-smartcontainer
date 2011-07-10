package org.openmrs.module.smartcontainer.web.controller.API;

import java.io.IOException;
import java.io.Writer;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Patient;
import org.openmrs.api.context.Context;
import org.openmrs.module.smartcontainer.RDFSource;
import org.openmrs.module.smartcontainer.rdfsource.DemographicsRDFSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "module/smartcontainer/demographics.form")
public class DemographicsController {
	
	Log log = LogFactory.getLog(getClass());
	
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView handle(@RequestParam("pid") Integer patientId, HttpServletResponse resp) {
		log.info("In the Demographics Controller");
		resp.setContentType("text/xml"); // actually I use a constant
		Writer writer;
		try {
			writer = resp.getWriter();
			Patient patient = Context.getPatientService().getPatient(patientId);
			RDFSource resource = new DemographicsRDFSource();
			writer.write(resource.getRDF(patient)); // get the
			                                        // object
			writer.close();
		}
		catch (IOException e) {
			
			e.printStackTrace();
		}
		catch (Exception e) {
			
			e.printStackTrace();
		}
		return null; // indicates this controller did all necessary processing
		
	}
}
