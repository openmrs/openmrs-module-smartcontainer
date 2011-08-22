package org.openmrs.module.smartcontainer.web.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Concept;
import org.openmrs.ConceptMap;
import org.openmrs.api.context.Context;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Controller for Manage Concept mapping page of the smartcontainer module
 * User: aja
 * Date: 8/20/11
 * Time: 7:11 AM
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "module/smartcontainer/conceptMapping.form")
public class ConceptMappingController {
    private Log log = LogFactory.getLog(ConceptMappingController.class);
    private String SUCCESS_VIEW = "module/smartcontainer/conceptMapping";

    /**
     * Called when the request to page load come
     */
    @RequestMapping(method = RequestMethod.GET)
    public String showForm(ModelMap model) {
        Map vitalSignMapped = new HashMap();

        Concept concept = null;

        //List<Concept> vitalSign=Context.getConceptService().getConceptsByConceptSet(Context.getConceptService().getConceptByName("Vital Sign"));
        //check for Hight
        concept = Context.getConceptService().getConceptByMapping("8302-2", "LOINC");
        if (concept != null) {
            vitalSignMapped.put("Height", concept.getConceptId());
        } else {
            vitalSignMapped.put("Height", null);
        }
        concept = Context.getConceptService().getConceptByMapping("3141-9", "LOINC");
        if (concept != null) {
            vitalSignMapped.put("Weight", concept.getConceptId());
        } else {
            vitalSignMapped.put("Weight", null);
        }
        concept = Context.getConceptService().getConceptByMapping("39156-5", "LOINC");
        if (concept != null) {
            vitalSignMapped.put("Body Mass Index", concept.getConceptId());
        } else {
            vitalSignMapped.put("Body Mass Index", null);
        }
        concept = Context.getConceptService().getConceptByMapping("9279-1", "LOINC");
        if (concept != null) {
            vitalSignMapped.put("Respiratory Rate", concept.getConceptId());
        } else {
            vitalSignMapped.put("Respiratory Rate", null);
        }
        concept = Context.getConceptService().getConceptByMapping("8867-4", "LOINC");
        if (concept != null) {
            vitalSignMapped.put("Heart Rate", concept.getConceptId());
        } else {
            vitalSignMapped.put("Heart Rate", null);
        }
        concept = Context.getConceptService().getConceptByMapping("2710-2", "LOINC");
        if (concept != null) {
            vitalSignMapped.put("Oxygen Saturation", concept.getConceptId());
        } else {
            vitalSignMapped.put("Oxygen Saturation", null);
        }
        concept = Context.getConceptService().getConceptByMapping("8462-4", "LOINC");
        if (concept != null) {
            vitalSignMapped.put("Diastolic Blood Pressure", concept.getConceptId());
        } else {
            vitalSignMapped.put("Diastolic Blood Pressure", null);
        }
        concept = Context.getConceptService().getConceptByMapping("8480-6", "LOINC");
        if (concept != null) {
            vitalSignMapped.put("Systolic Blood Pressure", concept.getConceptId());
        } else {
            vitalSignMapped.put("Systolic Blood Pressure", null);
        }


        model.put("mappedVitals", vitalSignMapped);

        return SUCCESS_VIEW;
    }

    @RequestMapping(method = RequestMethod.POST)
    public String saveMappings(@RequestParam("Height") Concept height, @RequestParam("Weight") Concept weight, @RequestParam("BodyMassIndex") Concept bmi, @RequestParam("RespiratoryRate") Concept rr, @RequestParam("HeartRate") Concept hr, @RequestParam("OxygenSaturation") Concept os, @RequestParam("DiastolicBloodPressure") Concept dbp, @RequestParam("SystolicBloodPressure") Concept sbp, HttpSession session) {

        saveMapping(height, "LOINC", "8302-2");
        saveMapping(weight, "LOINC", "3141-9");
        saveMapping(bmi, "LOINC", "39156-5");
        saveMapping(hr, "LOINC", "8867-4");
        saveMapping(rr, "LOINC", "9279-1");
        saveMapping(os, "LOINC", "2710-2");
        saveMapping(dbp, "LOINC", "8462-4");
        saveMapping(sbp, "LOINC", "8480-6");

        return "redirect:conceptMapping.form";
    }

    private void saveMapping(Concept concept, String conceptSourceName, String conceptSourceCode) {
        if (concept != null) {
            List<ConceptMap> cmap = new ArrayList<ConceptMap>();
            Boolean found = false;
            for (ConceptMap cm : concept.getConceptMappings()) {
                if (cm.getSource().getName().equals(conceptSourceName)) {
                    cmap.add(cm);
                }
            }

            // if the mapping is not found,
            if (!cmap.isEmpty()) {
                for (ConceptMap map : cmap) {
                    found = map.getSourceCode().equals(conceptSourceCode);

                    if (found)
                        break;
                }
            } else {

                found = false;
            }

            if (!found) {

                ConceptMap map = new ConceptMap();
                map.setConcept(concept);
                map.setSource(Context.getConceptService().getConceptSourceByName(conceptSourceName));
                map.setSourceCode(conceptSourceCode);
                concept.addConceptMapping(map);
                Context.getConceptService().saveConcept(concept);
            }


        }
    }
}
