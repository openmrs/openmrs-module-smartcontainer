package org.openmrs.module.smartcontainer.rdfsource;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.List;

import org.openmrs.Patient;
import org.openmrs.module.smartcontainer.RdfSource;
import org.openmrs.module.smartcontainer.smartData.SmartProblem;
import org.openmrs.module.smartcontainer.util.RdfUtil;
import org.openrdf.model.BNode;
import org.openrdf.model.Literal;
import org.openrdf.model.URI;
import org.openrdf.rio.rdfxml.RdfXmlWriter;

/**
 * Render a RDF/XML for SMART Problem
 * 
 */
public class ProblemRDFSource extends RdfSource {

	/**
	 * Primary method to render rdf
	 * 
	 * @param problems
	 * @return
	 * @throws IOException
	 */
	public String getRDF(List<SmartProblem> problems) throws IOException {

		Writer sWriter = new StringWriter();
		RdfXmlWriter graph = new RdfXmlWriter(sWriter);
		addHeader(graph);
		graph.startDocument();
		for (SmartProblem p : problems) {
			/*
			 * Add parent node <sp:Problem> ..child nodes </sp:Problem>
			 */
			BNode problemNode = factory.createBNode();
			URI problem = factory.createURI(sp, "Problem");
			URI type = factory.createURI(org.openrdf.vocabulary.RDF.TYPE);
			graph.writeStatement(problemNode, type, problem);
			/*
			 * Add child node <sp:problemName> ..Coded value node
			 * </sp:problemName>
			 */
			URI problemName = factory.createURI(sp, "problemName");
			graph.writeStatement(problemNode, problemName,
					RdfUtil.codedValue(factory, graph, p.getProblemName()));
			/*
			 * Add child node <sp:onset>2007-06-12</sp:onset>
			 */
			URI onset = factory.createURI(sp, "onset");
			Literal onsetVal = factory.createLiteral(p.getOnset());
			graph.writeStatement(problemNode, onset, onsetVal);
			/*
			 * Add child node <sp:resolution>2007-08-01</sp:resolution>
			 */
			if (p.getResolution() != null) {
				URI resolution = factory.createURI(sp, "resolution");
				Literal resolutionVal = factory
						.createLiteral(p.getResolution());
				graph.writeStatement(problemNode, resolution, resolutionVal);
			}

		}
		//

		graph.endDocument();

		return sWriter.toString();
	}

	public String getRDF(Patient patient) throws IOException {

		return null;
	}

}
