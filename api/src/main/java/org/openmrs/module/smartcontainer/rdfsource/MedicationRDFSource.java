/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.module.smartcontainer.rdfsource;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.List;

import org.openmrs.module.smartcontainer.RdfSource;
import org.openmrs.module.smartcontainer.smartData.SmartMedication;
import org.openmrs.module.smartcontainer.util.RdfUtil;
import org.openrdf.model.BNode;
import org.openrdf.model.URI;
import org.openrdf.model.Value;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.rdfxml.util.RDFXMLPrettyWriter;

/**
 * Render a RDF/XML for SMART Medication
 */
public class MedicationRDFSource extends RdfSource {
	
	/**
	 * Primary method to render
	 * 
	 * @param meds
	 * @return
	 * @throws IOException
	 */
	public String getRDF(List<SmartMedication> meds) throws IOException, RDFHandlerException {
		Writer sWriter = new StringWriter();
		RDFXMLPrettyWriter graph = new RDFXMLPrettyWriter(sWriter);
		
		addHeader(graph);
		graph.startRDF();
		for (SmartMedication med : meds) {
			/*
			    * Add parent node <sp:Medication> ..child nodes </sp:Medication>
			    */
			BNode medicationNode = factory.createBNode();
			URI medication = factory.createURI(sp, "Medication");
			
			graph.handleStatement(factory.createStatement(medicationNode, RdfSource.type, medication));
			/*
			    * Add child node <sp:drugName> ..coded value node </sp:drugName>
			    */
			URI drugName = factory.createURI(sp, "drugName");
			graph.handleStatement(factory.createStatement(medicationNode, drugName,
			    RdfUtil.codedValue(factory, graph, med.getDrugName())));
			/*
			    * Add child node <sp:startDate>2007-03-14</sp:startDate>
			    */
			if (med.getStartDate() != null) {
				URI startDate = factory.createURI(sp, "startDate");
				Value startDateVal = factory.createLiteral(med.getStartDate());
				graph.handleStatement(factory.createStatement(medicationNode, startDate, startDateVal));
			}
			/*
			 * Add child node <sp:endDate>2007-03-14</sp:endDate>
			 */
			if (med.getEndDate() != null) {
				URI endDate = factory.createURI(sp, "endDate");
				Value endDateVal = factory.createLiteral(med.getEndDate());
				graph.handleStatement(factory.createStatement(medicationNode, endDate, endDateVal));
			}
			/*
			    * Add child node <sp:instructions>Take two tablets twice daily as
			    * needed for pain</sp:instructions>
			    */
			if (med.getInstructions() != null) {
				URI instructions = factory.createURI(sp, "instructions");
				Value sig = factory.createLiteral(med.getInstructions());
				graph.handleStatement(factory.createStatement(medicationNode, instructions, sig));
			}
			/*
			    * Add child node <sp:quantity> ..Value and unit node </sp:quantity>
			    */
			if ((med.getQuantity() != null)) {
				URI quantity = factory.createURI(sp, "quantity");
				graph.handleStatement(factory.createStatement(medicationNode, quantity,
				    RdfUtil.valueAndUnit(factory, graph, med.getQuantity())));
			}
			/*
			    * Add child node <sp:frequency> ..Value and unit node
			    * </sp:frequency>
			    */
			if ((med.getFrequency() != null)) {
				URI frequency = factory.createURI(sp, "frequency");
				graph.handleStatement(factory.createStatement(medicationNode, frequency,
				    RdfUtil.valueAndUnit(factory, graph, med.getFrequency())));
				
			}//
			
		}
		graph.endRDF();
		return sWriter.toString();
	}
	
}
