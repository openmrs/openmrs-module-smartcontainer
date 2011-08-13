package org.openmrs.module.smartcontainer.web.controller.API;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Patient;
import org.openmrs.api.context.Context;
import org.openmrs.module.smartcontainer.SmartDataService;
import org.openmrs.module.smartcontainer.rdfsource.ProblemRDFSource;
import org.openmrs.module.smartcontainer.smartData.SmartProblem;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(method = RequestMethod.GET, value = "/smartcontainer/api/")
public class ProblemController {

	Log log = LogFactory.getLog(getClass());

	private ProblemRDFSource resource;

	public ProblemRDFSource getResource() {
		return resource;
	}

	public void setResource(ProblemRDFSource resource) {
		this.resource = resource;
	}

	@RequestMapping(method = RequestMethod.GET, value = "records/{pid}/problems/")
	public ModelAndView handle(@PathVariable("pid") Patient patient,
			HttpServletResponse resp) {
		log.info("In Problem Controller");
		resp.setContentType("text/xml"); // actually I use a constant
		Writer writer;
		try {
			writer = resp.getWriter();
			List<SmartProblem> props = Context.getService(
					SmartDataService.class).getAllForPatient(patient,
					SmartProblem.class);
			writer.write(resource.getRDF(props)); // get the object

			writer.close();
		} catch (IOException e) {

			throw new RuntimeException(e);
		} catch (Exception e) {

			throw new RuntimeException(e);
		}
		return null; // indicates this controller did all necessary processing

	}
}
