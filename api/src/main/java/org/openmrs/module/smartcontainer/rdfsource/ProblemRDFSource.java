package org.openmrs.module.smartcontainer.rdfsource;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.List;

import org.openmrs.Concept;
import org.openmrs.ConceptMap;
import org.openmrs.Patient;
import org.openmrs.activelist.Problem;
import org.openmrs.api.context.Context;
import org.openmrs.module.smartcontainer.RDFSource;
import org.openrdf.model.BNode;
import org.openrdf.model.Literal;
import org.openrdf.model.URI;
import org.openrdf.model.Value;
import org.openrdf.rio.rdfxml.RdfXmlWriter;

public class ProblemRDFSource extends RDFSource {

	public String getRDF(List<Problem> problems) throws IOException {

		Writer sWriter = new StringWriter();
		RdfXmlWriter graph = new RdfXmlWriter(sWriter);

		graph.setNamespace("sp", sp);
		graph.setNamespace("rdf", rdf);
		graph.setNamespace("dcterms", dcterms);
		graph.startDocument();
		for (Problem p : problems) {
			//
			BNode problemNode = factory.createBNode();

			URI problem = factory.createURI(sp, "Problem");
			URI type = factory.createURI(org.openrdf.vocabulary.RDF.TYPE);
			graph.writeStatement(problemNode, type, problem);
			//
			URI onset = factory.createURI(sp, "onset");
			Literal onsetVal = factory.createLiteral(date(p.getStartDate()));
			graph.writeStatement(problemNode, onset, onsetVal);
			//
			if (p.getEndDate() != null) {
				URI resolution = factory.createURI(sp, "resolution");
				Literal resolutionVal = factory.createLiteral(date(p
						.getEndDate()));
				graph.writeStatement(problemNode, resolution, resolutionVal);
			}
			//
			URI problemName = factory.createURI(sp, "problemName");
			graph.writeStatement(problemNode, problemName, codedValue(graph, p));
		}
		//

		graph.endDocument();

		return sWriter.toString();
	}

	private Value codedValue(RdfXmlWriter graph, Problem p) throws IOException {
		BNode codedValueNode = factory.createBNode();
		URI type = factory.createURI(org.openrdf.vocabulary.RDF.TYPE);
		URI codedValue = factory.createURI(sp, "CodedValue");
		graph.writeStatement(codedValueNode, type, codedValue);
		//
		Concept concept = p.getProblem();
		ConceptMap map = null;
		for (ConceptMap cm : concept.getConceptMappings()) {
			if (cm.getSource().getName().equals("SnoMed Source")) {
				map = cm;
			}
		}
		URI code = factory.createURI(sp, "code");
		URI uri = factory.createURI("http://www.ihtsdo.org/snomed-ct/concepts/"
				+ map.getSourceCode());
		graph.writeStatement(codedValueNode, code, uri);
		//
		URI title = factory.createURI(dcterms, "title");
		Literal titleVal = factory.createLiteral(concept.getDisplayString());
		graph.writeStatement(codedValueNode, title, titleVal);
		//
		graph.writeStatement(uri, type, code);
		//
		URI system = factory.createURI(sp, "system");
		URI systemVal = factory
				.createURI("http://www.ihtsdo.org/snomed-ct/concepts/");
		graph.writeStatement(uri, system, systemVal);
		//
		URI identifier = factory.createURI(dcterms, "identifier");
		Literal identifierVal = factory.createLiteral(map.getSourceCode()
				.toString());
		graph.writeStatement(uri, identifier, identifierVal);

		return codedValueNode;
	}

	@Override
	public String getRDF(Patient patient) throws IOException {
		List<Problem> problems = Context.getPatientService().getProblems(
				patient);
		return getRDF(problems);
	}

}
