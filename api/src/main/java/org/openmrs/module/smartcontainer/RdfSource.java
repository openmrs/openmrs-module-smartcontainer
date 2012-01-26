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
package org.openmrs.module.smartcontainer;

import java.io.IOException;

import org.openmrs.module.smartcontainer.smartData.CodedValue;
import org.openmrs.module.smartcontainer.util.RdfUtil;
import org.openrdf.model.BNode;
import org.openrdf.model.Graph;
import org.openrdf.model.URI;
import org.openrdf.model.Value;
import org.openrdf.model.ValueFactory;
import org.openrdf.model.impl.GraphImpl;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.rdfxml.RDFXMLWriter;

/**
 * Contains declarations common to all RDFSources
 */

public abstract class RdfSource {
	
	/**
	 * Name space declaration for smartplatform terms
	 */
	public final static String sp = "http://smartplatforms.org/terms#";
	
	/**
	 * Name space declaration for Dublin core terms
	 */
	public final static String dcterms = "http://purl.org/dc/terms/";
	
	/**
	 * Name space declaration for Resource description framework
	 */
	public final static String rdf = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
	
	/**
	 * Name space declaration for Friend of a friend
	 */
	public final static String foaf = "http://xmlns.com/foaf/0.1/";
	
	/**
	 * Name space declaration for vCard
	 */
	public final static String v = "http://www.w3.org/2006/vcard/ns#";
	
	/**
	 * Model graph for getting factory
	 */
	protected Graph modelGraph;
	
	/**
	 * Factory for creating URI and Value of RDF
	 */
	protected ValueFactory factory;
	
	/**
     *
     */
	public static URI type = org.openrdf.model.vocabulary.RDF.TYPE;
	
	/**
	 * No arg constructor for initializing modelGraph and factory
	 */
	public RdfSource() {
		super();
		modelGraph = new GraphImpl();
		factory = modelGraph.getValueFactory();
	}
	
	/**
	 * This method adds following to the rdf output. <rdf:RDF
	 * xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#"
	 * xmlns:sp="http://smartplatforms.org/terms#" xmlns:dcterms="http://purl.org/dc/terms/"
	 * xmlns:foaf="http://xmlns.com/foaf/0.1/" xmlns:dc="http://purl.org/dc/elements/1.1/"
	 * xmlns:v="http://www.w3.org/2006/vcard/ns#"> </rdf:RDF>
	 * 
	 * @param graph
	 */
	protected void addHeader(RDFXMLWriter graph) {
		
		graph.handleNamespace("rdf", rdf);
		graph.handleNamespace("sp", sp);
		graph.handleNamespace("foaf", foaf);
		graph.handleNamespace("dcterms", dcterms);
		graph.handleNamespace("v", v);
		
	}
	
	protected URI addChildNode(String namespace, String nodename, BNode parentNode, RDFXMLWriter graph, CodedValue codedValue)
	    throws RDFHandlerException, IOException {
		URI childnode = factory.createURI(namespace, nodename);
		graph.handleStatement(factory.createStatement(parentNode, childnode, RdfUtil.codedValue(factory, graph, codedValue)));
		return childnode;
	}
	
	protected URI addChildNode(String namespace, String nodename, BNode parentNode, RDFXMLWriter graph, String textContent)
	    throws RDFHandlerException, IOException {
		URI childnode = factory.createURI(namespace, nodename);
		Value value = factory.createLiteral(textContent);
		graph.handleStatement(factory.createStatement(parentNode, childnode, value));
		return childnode;
	}
	
	protected void addChildBNode(String namespace, String nodename, BNode parentNode, RDFXMLWriter graph)
	    throws RDFHandlerException, IOException {
		URI name = factory.createURI(namespace, nodename);
		graph.handleStatement(factory.createStatement(parentNode, type, name));
	}
}
