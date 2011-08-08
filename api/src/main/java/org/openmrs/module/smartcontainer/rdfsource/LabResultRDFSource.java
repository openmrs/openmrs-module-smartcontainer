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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.openmrs.Patient;
import org.openmrs.module.smartcontainer.RDFSource;
import org.openmrs.module.smartcontainer.smartData.CodedValue;
import org.openmrs.module.smartcontainer.smartData.SmartLabResult;
import org.openmrs.module.smartcontainer.smartData.ValueAndUnit;
import org.openrdf.model.BNode;
import org.openrdf.model.Literal;
import org.openrdf.model.URI;
import org.openrdf.model.Value;
import org.openrdf.rio.rdfxml.RdfXmlWriter;

/**
 * Render a RDF/XML for SMART LabResult
 * 
 */
public class LabResultRDFSource extends RDFSource {
	Log log = LogFactory.getLog(getClass());

	/**
	 * @param labs
	 * @return
	 * @throws IOException
	 */
	public String getRDF(List<SmartLabResult> labs) throws IOException {
		// Create a writer
		Writer sWriter = new StringWriter();
		// Create a graph and add the writer
		RdfXmlWriter graph = new RdfXmlWriter(sWriter);
		// Add required name spaces
		graph.setNamespace("sp", sp);
		graph.setNamespace("rdf", rdf);
		graph.setNamespace("dcterms", dcterms);
		graph.setNamespace("foaf", foaf);
		graph.setNamespace("v", v);
		//Start the graph
		graph.startDocument();
		//Loop through each labResult
		for (SmartLabResult l : labs) {
            //Create a node with the type of LabResult and add it to root
			BNode labResultNode = factory.createBNode();
			URI labResult = factory.createURI(sp, "LabResult");
			URI type = factory.createURI(org.openrdf.vocabulary.RDF.TYPE);
			graph.writeStatement(labResultNode, type, labResult);
			//Create a coded value node with the type of lab name and add it to LabResult
			URI labName = factory.createURI(sp, "labName");
			graph.writeStatement(labResultNode, labName,
					codedValue(graph, l.getLabName()));
			//

			if (l.getQuantitativeResult().getNormalRange() != null
					|| l.getQuantitativeResult().getNonCriticalRange() != null) {

				BNode quantitativeResultNode = factory.createBNode();
				URI quantitativeResult = factory.createURI(sp,
						"QuantitativeResult");
				graph.writeStatement(quantitativeResultNode, type,
						quantitativeResult);
				URI valueAndUnit = factory.createURI(sp, "valueAndUnit");
				graph.writeStatement(
						quantitativeResultNode,
						valueAndUnit,
						valueAndUnit(l.getQuantitativeResult()
								.getValueAndUnit(), graph));
				//
				BNode resultRangeNode = factory.createBNode();
				URI resultRange = factory.createURI(sp, "ResultRange");
				graph.writeStatement(resultRangeNode, type, resultRange);
				//
				if (l.getQuantitativeResult().getNormalRange().getMinimum() != null) {
					URI minimum = factory.createURI(sp, "minimum");
					graph.writeStatement(
							resultRangeNode,
							minimum,
							valueAndUnit(l.getQuantitativeResult()
									.getNormalRange().getMinimum(), graph));
				}
				//
				if (l.getQuantitativeResult().getNormalRange().getMaximum() != null) {
					URI maximum = factory.createURI(sp, "maximum");
					graph.writeStatement(
							resultRangeNode,
							maximum,
							valueAndUnit(l.getQuantitativeResult()
									.getNormalRange().getMaximum(), graph));
				}
				//
				URI normalRange = factory.createURI(sp, "normalRange");
				graph.writeStatement(quantitativeResultNode, normalRange,
						resultRangeNode);
				//
				URI quantitativeResults = factory.createURI(sp,
						"quantitativeResult");
				graph.writeStatement(labResultNode, quantitativeResults,
						quantitativeResultNode);
				//
			}

			//
			if (l.getQuantitativeResult().getNormalRange() == null
					&& l.getQuantitativeResult().getNonCriticalRange() == null) {

				BNode quantitativeResultNode = factory.createBNode();
				URI quantitativeResult = factory.createURI(sp,
						"QuantitativeResult");
				graph.writeStatement(quantitativeResultNode, type,
						quantitativeResult);
				//
				URI value = factory.createURI(sp, "value");
				Value valueVal = factory.createLiteral(l
						.getQuantitativeResult().getValueAndUnit().getValue());
				graph.writeStatement(quantitativeResultNode, value, valueVal);

			}
			BNode attributionNode = factory.createBNode();
			URI attribution = factory.createURI(sp, "Attribution");
			graph.writeStatement(attributionNode, type, attribution);
			//
			URI startTime = factory.createURI(sp, "startTime");
			Value startDateVal = factory.createLiteral(l.getSpecimenCollected()
					.getStartTime());
			graph.writeStatement(attributionNode, startTime, startDateVal);
			//
			URI specimenCollected = factory.createURI(sp, "specimenCollected");
			graph.writeStatement(labResultNode, specimenCollected,
					attributionNode);
			//
			if (l.getExternalID() != null) {
				URI externalID = factory.createURI(sp, "externalID");
				Value externalIDVal = factory.createLiteral(l.getExternalID());
				graph.writeStatement(labResultNode, externalID, externalIDVal);

			}

		}
		graph.endDocument();
		return sWriter.toString();
	}

	/**
	 * @param val
	 * @param graph
	 * @return
	 * @throws IOException
	 */
	private Value valueAndUnit(ValueAndUnit val, RdfXmlWriter graph)
			throws IOException {
		BNode valueAndUnitNode = factory.createBNode();
		URI type = factory.createURI(org.openrdf.vocabulary.RDF.TYPE);
		URI valueAndUnit = factory.createURI(sp, "ValueAndUnit");
		graph.writeStatement(valueAndUnitNode, type, valueAndUnit);
		//
		URI value = factory.createURI(sp, "value");
		Value valueVal = factory.createLiteral(val.getValue());
		graph.writeStatement(valueAndUnitNode, value, valueVal);
		//
		URI unit = factory.createURI(sp, "unit");
		Value unitVal = factory.createLiteral(val.getUnit());
		graph.writeStatement(valueAndUnitNode, unit, unitVal);

		return valueAndUnitNode;
	}

	/**
	 * @param graph
	 * @param c
	 * @return
	 * @throws IOException
	 */
	private Value codedValue(RdfXmlWriter graph, CodedValue c)
			throws IOException {
		BNode codedValueNode = factory.createBNode();
		URI type = factory.createURI(org.openrdf.vocabulary.RDF.TYPE);
		URI codedValue = factory.createURI(sp, "CodedValue");
		graph.writeStatement(codedValueNode, type, codedValue);
		//

		URI code = factory.createURI(sp, "code");
		URI uri = factory.createURI(c.getCodeBaseURL() + c.getCode());
		graph.writeStatement(codedValueNode, code, uri);
		//
		URI title = factory.createURI(dcterms, "title");
		Literal titleVal = factory.createLiteral(c.getTitle());
		graph.writeStatement(codedValueNode, title, titleVal);
		//
		BNode codeProvenanceNode = factory.createBNode();
		URI codeProvenance = factory.createURI(sp, "CodeProvenance");
		graph.writeStatement(codeProvenanceNode, type, codeProvenance);
		//
		URI sourceCode = factory.createURI(sp, "sourceCode");
		URI localCodeuri = factory
				.createURI(c.getCodeProvenance().getSourceCodeBaseURL()
						+ c.getCodeProvenance().getSourceCode());
		graph.writeStatement(codeProvenanceNode, sourceCode, localCodeuri);
		//
		Literal localSourceCodetitleVal = factory.createLiteral(c
				.getCodeProvenance().getTitle());
		graph.writeStatement(codeProvenanceNode, title, localSourceCodetitleVal);
		//
		URI translationFidelity = factory.createURI(sp, "translationFidelity");
		URI translationFidelityuri = factory.createURI(c.getCodeProvenance()
				.getTranslationFidelityBaseURL()
				+ c.getCodeProvenance().getTranslationFidelity());
		graph.writeStatement(codeProvenanceNode, translationFidelity,
				translationFidelityuri);
		//
		URI codeProvenances = factory.createURI(sp, "codeProvenance");
		graph.writeStatement(codedValueNode, codeProvenances,
				codeProvenanceNode);
		//
		graph.writeStatement(uri, type, code);
		//
		URI system = factory.createURI(sp, "system");
		URI systemVal = factory.createURI(c.getCodeBaseURL());
		graph.writeStatement(uri, system, systemVal);
		//
		URI identifier = factory.createURI(dcterms, "identifier");
		Literal identifierVal = factory.createLiteral(c.getCode());
		graph.writeStatement(uri, identifier, identifierVal);

		return codedValueNode;
	}

	/**
	 * @param patient
	 * @return
	 * @throws IOException
	 */
	public String getRDF(Patient patient) throws IOException {

		return null;
	}

}
