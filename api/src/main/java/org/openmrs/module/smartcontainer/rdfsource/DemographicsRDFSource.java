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
import org.openmrs.module.smartcontainer.RDFSource;
import org.openmrs.module.smartcontainer.smartData.SmartDemographics;
import org.openrdf.model.BNode;
import org.openrdf.model.URI;
import org.openrdf.model.Value;
import org.openrdf.rio.rdfxml.RdfXmlWriter;

/**
 * Render a RDF/XML for SMART Demographics
 * 
 * 
 */
public class DemographicsRDFSource extends RDFSource {

	/**
	 * Here the received SmartDemographics is a perfect object. no need to check
	 * for null
	 * 
	 * @param d SMART Demographics
	 * @return RDF/XML as a String
	 * @throws IOException
	 */
	public String getRDF(SmartDemographics d) throws IOException {
		// Create a writer
		Writer sWriter = new StringWriter();
		// Create a graph and add the writer
		RdfXmlWriter graph = new RdfXmlWriter(sWriter);
		// Add required name spaces
		graph.setNamespace("sp", sp);
		graph.setNamespace("rdf", rdf);
		graph.setNamespace("dcterms", dcterms);
		graph.setNamespace("foaf", foaf);
		graph.startDocument();
		// Blank root node to represent a Demographics
		BNode demographicsNode = factory.createBNode();
		// This node is a type of person
		URI person = factory.createURI(foaf, "Person");
		URI type = factory.createURI(org.openrdf.vocabulary.RDF.TYPE);
		graph.writeStatement(demographicsNode, type, person);
		// Add givenName node to root node
		URI givenName = factory.createURI(foaf, "givenName");
		Value gNameVal = factory.createLiteral(d.getGivenName());
		graph.writeStatement(demographicsNode, givenName, gNameVal);
		// Add familyName node to root node
		URI familyName = factory.createURI(foaf, "familyName");
		Value fNameVal = factory.createLiteral(d.getFamilyName());
		graph.writeStatement(demographicsNode, familyName, fNameVal);
		// Add gender node to root node
		URI gender = factory.createURI(foaf, "gender");
		Value genderVal = factory.createLiteral(d.getGender());
		graph.writeStatement(demographicsNode, gender, genderVal);
		// Add zip code node to root node
		URI zipcode = factory.createURI(sp, "zipcode");
		Value zipcodeVal = factory.createLiteral(d.getZipCode());
		graph.writeStatement(demographicsNode, zipcode, zipcodeVal);
		// Add birthday node to root node
		URI birthday = factory.createURI(sp, "birthday");
		Value birthdayVal = factory.createLiteral(d.getBirthDate());
		graph.writeStatement(demographicsNode, birthday, birthdayVal);
		// End the graph to root node
		graph.endDocument();
		// Return the graph as a String
		return sWriter.toString();
	}

}
