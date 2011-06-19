package org.openmrs.module.smartcontainer.impl;

import java.io.StringWriter;
import java.io.Writer;

import org.openrdf.model.BNode;
import org.openrdf.model.Graph;
import org.openrdf.model.Literal;
import org.openrdf.model.Statement;
import org.openrdf.model.URI;
import org.openrdf.model.ValueFactory;


import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.rdfxml.util.RDFXMLPrettyWriter;


public class RDFConvertor {
	public static String RDF() throws RDFHandlerException {
		
		Writer sWriter=new StringWriter();
		RDFXMLPrettyWriter graph=new RDFXMLPrettyWriter(sWriter);
		String sp= "http://smartplatforms.org/terms#";
		String dcterms= "http://purl.org/dc/terms/";
		graph.handleNamespace("sp", "http://smartplatforms.org/terms#");
		graph.handleNamespace("rdf", "http://www.w3.org/1999/02/22-rdf-syntax-ns#");
		graph.handleNamespace("dcterms", "http://purl.org/dc/terms/");
		graph.startRDF();
		Graph myGraph = new org.openrdf.model.impl.GraphImpl();
		ValueFactory myFactory = myGraph.getValueFactory();
		//
		BNode mySubject = myFactory.createBNode();
		URI myPredicate = myFactory.createURI(org.openrdf.model.vocabulary.RDF.TYPE.toString());
		Literal myObject = myFactory.createLiteral(sp+ "Problem");
		Statement stmt=myFactory.createStatement(mySubject, myPredicate, myObject);
		 graph.handleStatement(stmt);
		//
		 myPredicate = myFactory.createURI(sp,"onset");
		 myObject = myFactory.createLiteral("23-04-2011");
		 graph.handleStatement(myFactory.createStatement(mySubject, myPredicate, myObject));
		 //
		 BNode mySubject1 = myFactory.createBNode();
		 myPredicate = myFactory.createURI(org.openrdf.model.vocabulary.RDF.TYPE.toString());
		 myObject = myFactory.createLiteral(sp,"CodedValue");
		 graph.handleStatement(myFactory.createStatement(mySubject1, myPredicate, myObject ));
		 //
		 myPredicate = myFactory.createURI(sp,"code");
		 URI uri=myFactory.createURI("http://www.ihtsdo.org/snomed-ct/concepts/161891005");
		 graph.handleStatement(myFactory.createStatement(mySubject1, myPredicate, uri ));
		 //
		 myPredicate = myFactory.createURI(dcterms,"title");
		 myObject = myFactory.createLiteral("Backache (finding)");
		 graph.handleStatement(myFactory.createStatement(mySubject1, myPredicate, myObject ));
		 //
		 myPredicate = myFactory.createURI(sp,"ProblemName");
		 graph.handleStatement(myFactory.createStatement(mySubject, myPredicate, mySubject1 ));
		 
		 
		 //
		 uri=myFactory.createURI("http://www.ihtsdo.org/snomed-ct/concepts/161891005");
		 myPredicate = myFactory.createURI(org.openrdf.model.vocabulary.RDF.TYPE.toString());
		 myObject = myFactory.createLiteral(sp,"code");
		 graph.handleStatement(myFactory.createStatement(uri, myPredicate, myObject ));
		 myPredicate = myFactory.createURI(sp,"system");
		 URI uri1=myFactory.createURI("http://www.ihtsdo.org/snomed-ct/concepts/");
		 graph.handleStatement(myFactory.createStatement(uri, myPredicate, uri1 ));
		 
		 myPredicate = myFactory.createURI(dcterms,"identifier");
		 myObject = myFactory.createLiteral("161891005");
		 graph.handleStatement(myFactory.createStatement(uri, myPredicate, myObject ));
		 
		 
		 
		 
		 
		graph.endRDF();
		

		return sWriter.toString();
	}
   

}
