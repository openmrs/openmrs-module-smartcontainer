package org.openmrs.module.smartcontainer.web.controller.api;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Patient;
import org.openmrs.api.context.Context;
import org.openmrs.module.smartcontainer.SmartDataService;
import org.openmrs.module.smartcontainer.rdfsource.MedicationRDFSource;
import org.openmrs.module.smartcontainer.smartData.SmartMedication;
import org.openrdf.rio.RDFHandlerException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/smartcontainer/api/")
public class MedicationController {
	
	Log log = LogFactory.getLog(getClass());
	
	private MedicationRDFSource resource;
	
	public MedicationRDFSource getResource() {
		return resource;
	}
	
	public void setResource(MedicationRDFSource resource) {
		this.resource = resource;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "records/{pid}/medications/")
	public ModelAndView handle(@PathVariable("pid") Patient patient, HttpServletResponse resp) {
		log.info("In the Medication Controller");
		resp.setContentType("text/xml"); // actually I use a constant
		Writer writer;
		try {
			writer = resp.getWriter();
			List<SmartMedication> meds = Context.getService(SmartDataService.class).getAllForPatient(patient,
			    SmartMedication.class);
			writer.write(resource.getRDF(meds)); // get the object
			writer.close();
		}
		catch (IOException e) {
			
			log.error("Unable to write out Medication for pid: " + patient.getId(), e);
		}
		catch (RDFHandlerException e) {
			log.error("Unable to write out Medication for pid: " + patient.getId(), e);
		}
		return null; // indicates this controller did all necessary processing
		
	}
}
