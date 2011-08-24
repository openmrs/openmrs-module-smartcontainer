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

import org.openmrs.Patient;
import org.openmrs.module.smartcontainer.RdfSource;
import org.openmrs.module.smartcontainer.smartData.CodedValue;
import org.openmrs.module.smartcontainer.smartData.SmartAllergyException;
import org.openmrs.module.smartcontainer.util.RdfUtil;
import org.openrdf.model.BNode;
import org.openrdf.model.URI;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.rdfxml.RDFXMLWriter;
import org.openrdf.rio.rdfxml.util.RDFXMLPrettyWriter;

/**
 * Render a RDF/XML for @link {@link SmartAllergyException}
 */
public class AllergyExceptionRDFSource extends RdfSource {
	
	/**
	 * Primary method to render rdf
	 * 
	 * @param allergyExceptions
	 * @return the rdf string representation
	 * @throws IOException
	 * @throws RDFHandlerException
	 */
	public String getRDF(List<SmartAllergyException> allergyExceptions) throws IOException, RDFHandlerException {
		
		Writer sWriter = new StringWriter();
		RDFXMLWriter graph = new RDFXMLPrettyWriter(sWriter);
		addHeader(graph);
		graph.startRDF();
		for (SmartAllergyException allergyException : allergyExceptions) {
			//Add parent node <sp:AllergyException> ..child nodes </sp:AllergyException>
			BNode allergyExceptionNode = factory.createBNode();
			URI allergyExceptionURI = factory.createURI(sp, "AllergyException");
			graph.handleStatement(factory.createStatement(allergyExceptionNode, RdfSource.type, allergyExceptionURI));
			
			//Add child node <sp:exception> ..coded value node </sp:exception>
			addChildNode("exception", allergyExceptionNode, graph, allergyException.getException());
		}
		
		graph.endRDF();
		
		return sWriter.toString();
	}
	
	//TODO move methods like this to the RdfSource to make them reusable
	private URI addChildNode(String nodename, BNode parentNode, RDFXMLWriter graph, CodedValue codedValue)
	    throws RDFHandlerException, IOException {
		URI childnode = factory.createURI(sp, nodename);
		graph.handleStatement(factory.createStatement(parentNode, childnode, RdfUtil.codedValue(factory, graph, codedValue)));
		return childnode;
	}
	
	public String getRDF(Patient patient) throws IOException {
		return null;
	}
	
}
