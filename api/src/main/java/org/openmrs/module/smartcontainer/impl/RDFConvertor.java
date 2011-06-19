package org.openmrs.module.smartcontainer.impl;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import org.openrdf.model.BNode;
import org.openrdf.model.Graph;
import org.openrdf.model.Literal;
import org.openrdf.model.Resource;
import org.openrdf.model.Statement;
import org.openrdf.model.URI;
import org.openrdf.model.Value;
import org.openrdf.model.ValueFactory;



import org.openrdf.rio.rdfxml.RdfXmlWriter;



public class RDFConvertor {
	static String sp= "http://smartplatforms.org/terms#";
	static String dcterms= "http://purl.org/dc/terms/";
	static Graph myGraph = new org.openrdf.model.impl.GraphImpl();
	static ValueFactory myFactory = myGraph.getValueFactory();
	public static String RDF() throws IOException  {
		
		Writer sWriter=new StringWriter();
		RdfXmlWriter graph=new RdfXmlWriter(sWriter);
		
		graph.setNamespace("sp", "http://smartplatforms.org/terms#");
		graph.setNamespace("rdf", "http://www.w3.org/1999/02/22-rdf-syntax-ns#");
		graph.setNamespace("dcterms", "http://purl.org/dc/terms/");
		graph.startDocument();
		
		//
		BNode problemNode = myFactory.createBNode();
		
		URI problem=myFactory.createURI(sp,"Problem");
		URI type=myFactory.createURI(org.openrdf.vocabulary.RDF.TYPE);
		graph.writeStatement(problemNode,type,problem);
		//
		URI onset=myFactory.createURI(sp,"onset");
		Literal onsetVal=myFactory.createLiteral("24-04-2011");
		graph.writeStatement(problemNode, onset, onsetVal);
		//
		URI resolution=myFactory.createURI(sp,"resolution");
		Literal resolutionVal=myFactory.createLiteral("24-04-2011");
		graph.writeStatement(problemNode, resolution, resolutionVal);
		//
		URI problemName = myFactory.createURI(sp,"ProblemName");
		graph.writeStatement(problemNode, problemName, codedValue(graph));
		//
		
		 
		 
		graph.endDocument();
		

		return sWriter.toString();
	}
	private static Value codedValue(RdfXmlWriter graph) throws IOException {
		BNode codedValueNode=myFactory.createBNode();
		URI type=myFactory.createURI(org.openrdf.vocabulary.RDF.TYPE);
		URI codedValue = myFactory.createURI(sp,"CodedValue");
		graph.writeStatement(codedValueNode, type, codedValue);
		//
		URI code = myFactory.createURI(sp,"Code");
		URI uri=myFactory.createURI("http://www.ihtsdo.org/snomed-ct/concepts/161891005");
		graph.writeStatement(codedValueNode, code, uri);
		//
		URI title=myFactory.createURI(dcterms,"title");
		Literal titleVal=myFactory.createLiteral("Backache (finding)");
		graph.writeStatement(codedValueNode, title, titleVal);
		//
		graph.writeStatement(uri, type, code);
		//
		URI system=myFactory.createURI(sp, "system");
		URI systemVal=myFactory.createURI("http://www.ihtsdo.org/snomed-ct/concepts/");
		graph.writeStatement(uri, system, systemVal);
		//
		URI identifier=myFactory.createURI(dcterms, "identifier");
		Literal identifierVal=myFactory.createLiteral("161891005");
		graph.writeStatement(uri, identifier, identifierVal);
		 
		return codedValueNode;
	}
   

}
