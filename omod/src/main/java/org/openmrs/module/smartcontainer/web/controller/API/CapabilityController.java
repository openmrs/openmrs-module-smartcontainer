package org.openmrs.module.smartcontainer.web.controller.API;

import java.io.IOException;
import java.io.Writer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.module.smartcontainer.impl.RDFConvertor;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "module/smartcontainer/problems.form")
public class CapabilityController {
	Log log=LogFactory.getLog(getClass());
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView handle(HttpServletResponse resp) {
		log.info("capabilities");
		resp.setContentType("text/xml");  // actually I use a constant
		  Writer writer;
		try {
			writer = resp.getWriter();
		
		  writer.write(RDFConvertor.RDF());  // get the object however you like
		  
		  writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  return null;  // indicates this controller did all necessary processing

		
	}
}
