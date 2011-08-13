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
import org.openrdf.rio.rdfxml.RdfXmlWriter;

/**
 * Render a RDF/XML for SMART Medication
 * 
 */
public class MedicationRDFSource extends RdfSource {

	/**
	 * Primary method to render
	 * 
	 * @param meds
	 * @return
	 * @throws IOException
	 */
	public String getRDF(List<SmartMedication> meds) throws IOException {
		Writer sWriter = new StringWriter();
		RdfXmlWriter graph = new RdfXmlWriter(sWriter);

		addHeader(graph);
		graph.startDocument();
		for (SmartMedication med : meds) {
			/*
			 * Add parent node <sp:Medication> ..child nodes </sp:Medication>
			 */
			BNode medicationNode = factory.createBNode();
			URI medication = factory.createURI(sp, "Medication");
			URI type = factory.createURI(org.openrdf.vocabulary.RDF.TYPE);
			graph.writeStatement(medicationNode, type, medication);
			/*
			 * Add child node <sp:drugName> ..coded value node </sp:drugName>
			 */
			URI drugName = factory.createURI(sp, "drugName");
			graph.writeStatement(medicationNode, drugName,
					RdfUtil.codedValue(factory, graph, med.getDrugName()));
			/*
			 * Add child node <sp:startDate>2007-03-14</sp:startDate>
			 */
			if (med.getStartDate() != null) {
				URI startDate = factory.createURI(sp, "startDate");
				Value startDateVal = factory.createLiteral(med.getStartDate());
				graph.writeStatement(medicationNode, startDate, startDateVal);
			}
			/*
			 * Add child node <sp:instructions>Take two tablets twice daily as
			 * needed for pain</sp:instructions>
			 */
			if (med.getInstructions() != null) {
				URI instructions = factory.createURI(sp, "instructions");
				Value sig = factory.createLiteral(med.getInstructions());
				graph.writeStatement(medicationNode, instructions, sig);
			}
			/*
			 * Add child node <sp:quantity> ..Value and unit node </sp:quantity>
			 */
			if ((med.getQuantity() != null)) {
				URI quantity = factory.createURI(sp, "quantity");
				graph.writeStatement(medicationNode, quantity,
						RdfUtil.valueAndUnit(factory, graph, med.getQuantity()));
			}
			/*
			 * Add child node <sp:frequency> ..Value and unit node
			 * </sp:frequency>
			 */
			if ((med.getFrequency() != null)) {
				URI frequency = factory.createURI(sp, "frequency");
				graph.writeStatement(medicationNode, frequency, RdfUtil
						.valueAndUnit(factory, graph, med.getFrequency()));

			}//

		}
		graph.endDocument();
		return sWriter.toString();
	}

}
