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
import org.openmrs.module.smartcontainer.RdfSource;
import org.openmrs.module.smartcontainer.smartData.Attribution;
import org.openmrs.module.smartcontainer.smartData.QuantitativeResult;
import org.openmrs.module.smartcontainer.smartData.SmartLabResult;
import org.openmrs.module.smartcontainer.smartData.ValueRange;
import org.openmrs.module.smartcontainer.util.RdfUtil;
import org.openrdf.model.BNode;
import org.openrdf.model.URI;
import org.openrdf.model.Value;
import org.openrdf.model.ValueFactory;
import org.openrdf.rio.rdfxml.RdfXmlWriter;

/**
 * Render a RDF/XML for SMART LabResult
 * 
 */
public class LabResultRDFSource extends RdfSource {
	Log log = LogFactory.getLog(getClass());

	/**
	 * Primary method to render rdf
	 * 
	 * @param labs
	 * @return
	 * @throws IOException
	 */
	public String getRDF(List<SmartLabResult> labs) throws IOException {

		Writer sWriter = new StringWriter();
		RdfXmlWriter graph = new RdfXmlWriter(sWriter);
		addHeader(graph);

		graph.startDocument();
		for (SmartLabResult l : labs) {
			/*
			 * Add parent node <sp:LabResult> ...child nodes </sp:LabResult>
			 */
			BNode labResultNode = factory.createBNode();
			URI labResult = factory.createURI(sp, "LabResult");
			URI type = factory.createURI(org.openrdf.vocabulary.RDF.TYPE);
			graph.writeStatement(labResultNode, type, labResult);
			/*
			 * Add child node <sp:labName> ...coded value node </sp:labName>
			 */
			URI labName = factory.createURI(sp, "labName");
			graph.writeStatement(labResultNode, labName,
					RdfUtil.codedValue(factory, graph, l.getLabName()));
			/*
			 * Add child node <sp:quantitativeResult> ...QuantitativeResult node
			 * </sp:quantitativeResult>
			 */
			URI quantitativeResults = factory.createURI(sp,
					"quantitativeResult");
			graph.writeStatement(
					labResultNode,
					quantitativeResults,
					addQuantityResultNode(factory, graph,
							l.getQuantitativeResult()));
			/*
			 * Add quantitativeResult node if the concept is coded value
			 */

			if (l.getQuantitativeResult().getNormalRange() == null
					&& l.getQuantitativeResult().getNonCriticalRange() == null) {

				BNode quantitativeResultNode = factory.createBNode();
				URI quantitativeResult = factory.createURI(sp,
						"QuantitativeResult");
				graph.writeStatement(quantitativeResultNode, type,
						quantitativeResult);
				URI value = factory.createURI(sp, "value");
				Value valueVal = factory.createLiteral(l
						.getQuantitativeResult().getValueAndUnit().getValue());
				graph.writeStatement(quantitativeResultNode, value, valueVal);
			}
			/*
			 * Add child node <sp:specimenCollected> ..Attribution node
			 * </sp:specimenCollected>
			 */

			URI specimenCollected = factory.createURI(sp, "specimenCollected");
			graph.writeStatement(
					labResultNode,
					specimenCollected,
					addattributionNode(factory, graph, l.getSpecimenCollected()));
			/*
			 * Add child node <sp:externalID>AC09205823577</sp:externalID>
			 */
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
	 * Create following rdf: <sp:Attribution> ..child nodes </sp:Attribution>
	 * 
	 * @param factory
	 * @param graph
	 * @param specimenCollected
	 * @return
	 * @throws IOException
	 */
	private Value addattributionNode(ValueFactory factory, RdfXmlWriter graph,
			Attribution specimenCollected) throws IOException {
		/*
		 * Add parent node <sp:Attribution> ..child nodes </sp:Attribution>
		 */
		BNode attributionNode = factory.createBNode();
		URI attribution = factory.createURI(sp, "Attribution");
		graph.writeStatement(attributionNode, type, attribution);
		/*
		 * Add child node <sp:startTime>2010-12-26T16:33:01</sp:startTime>
		 */
		URI startTime = factory.createURI(sp, "startTime");
		Value startDateVal = factory.createLiteral(specimenCollected
				.getStartTime());
		graph.writeStatement(attributionNode, startTime, startDateVal);
		return attributionNode;
	}

	/**
	 * Create following rdf node
	 * 
	 * <sp:QuantitativeResult> ...child nodes </sp:QuantitativeResult>
	 * 
	 * @param factory
	 * @param graph
	 * @param q
	 * @return
	 * @throws IOException
	 */
	private Value addQuantityResultNode(ValueFactory factory,
			RdfXmlWriter graph, QuantitativeResult q) throws IOException {
		BNode quantitativeResultNode = null;

		if (q.getNormalRange() != null || q.getNonCriticalRange() != null) {
			/*
			 * Add parent node<sp:QuantitativeResult> ...child nodes
			 * </sp:QuantitativeResult>
			 */
			quantitativeResultNode = factory.createBNode();
			URI quantitativeResult = factory
					.createURI(sp, "QuantitativeResult");
			graph.writeStatement(quantitativeResultNode, type,
					quantitativeResult);
			/*
			 * Add child node <sp:valueAndUnit> ..Value and Unit node
			 * </sp:valueAndUnit>
			 */
			URI valueAndUnit = factory.createURI(sp, "valueAndUnit");
			graph.writeStatement(quantitativeResultNode, valueAndUnit,
					RdfUtil.valueAndUnit(factory, graph, q.getValueAndUnit()));
			/*
			 * Add child node <sp:normalRange> ..Value range node
			 * </sp:normalRange>
			 */
			URI normalRange = factory.createURI(sp, "normalRange");
			graph.writeStatement(quantitativeResultNode, normalRange,
					addResultRangeNode(factory, graph, q.getNormalRange()));
		}
		return quantitativeResultNode;
	}

	/**
	 * Create following rdf:
	 * 
	 * <sp:ValueRange> ...child nodes </sp:ValueRange>
	 * 
	 * @param factory
	 * @param graph
	 * @param normalRange
	 * @return
	 * @throws IOException
	 */
	private Value addResultRangeNode(ValueFactory factory, RdfXmlWriter graph,
			ValueRange normalRange) throws IOException {
		/*
		 * Add parent node <sp:ValueRange> ...child nodes </sp:ValueRange>
		 */
		BNode resultRangeNode = factory.createBNode();
		URI resultRange = factory.createURI(sp, "ResultRange");
		graph.writeStatement(resultRangeNode, type, resultRange);
		/*
		 * Add child node <sp:minimum> ..Value and unit node </sp:minimum>
		 */
		if (normalRange.getMinimum() != null) {
			URI minimum = factory.createURI(sp, "minimum");
			graph.writeStatement(
					resultRangeNode,
					minimum,
					RdfUtil.valueAndUnit(factory, graph,
							normalRange.getMinimum()));
		}
		/*
		 * Add child node <sp:maximum> ..Value and unit node </sp:maximum>
		 */
		if (normalRange.getMaximum() != null) {
			URI maximum = factory.createURI(sp, "maximum");
			graph.writeStatement(
					resultRangeNode,
					maximum,
					RdfUtil.valueAndUnit(factory, graph,
							normalRange.getMaximum()));
		}
		return resultRangeNode;
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
