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
import org.openmrs.module.smartcontainer.RdfSource;
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
public class DemographicsRDFSource extends RdfSource {

	/** 
	 * @param d SMART Demographics
	 * @return RDF/XML as a String
	 * @throws IOException
	 */
	public String getRDF(SmartDemographics d) throws IOException {
		
		Writer sWriter = new StringWriter();
		RdfXmlWriter graph = new RdfXmlWriter(sWriter);
		addHeader(graph);
		graph.startDocument();
		/*
		 * <sp:Demographics>
		 * ....child nodes
		 * </sp:Demographics>
		 */
		BNode demographicsNode = factory.createBNode();
		URI person = factory.createURI(foaf, "Person");
		URI type = factory.createURI(org.openrdf.vocabulary.RDF.TYPE);
		graph.writeStatement(demographicsNode, type, person);
		/*Add child node
		 * <foaf:givenName>Bob</foaf:givenName>
		 */
		URI givenName = factory.createURI(foaf, "givenName");
		Value gNameVal = factory.createLiteral(d.getGivenName());
		graph.writeStatement(demographicsNode, givenName, gNameVal);
		/*Add child node
		 * <foaf:familyName>Odenkirk</foaf:familyName>
		 */
		URI familyName = factory.createURI(foaf, "familyName");
		Value fNameVal = factory.createLiteral(d.getFamilyName());
		graph.writeStatement(demographicsNode, familyName, fNameVal);
		/*Add child node
		 * <foaf:gender>male</foaf:gender>
		 */
		URI gender = factory.createURI(foaf, "gender");
		Value genderVal = factory.createLiteral(d.getGender());
		graph.writeStatement(demographicsNode, gender, genderVal);
		/*Add child node
		 *  <sp:zipcode>90001</sp:zipcode>
		 */
		URI zipcode = factory.createURI(sp, "zipcode");
		Value zipcodeVal = factory.createLiteral(d.getZipCode());
		graph.writeStatement(demographicsNode, zipcode, zipcodeVal);
		/*Add child node
		 *  <sp:birthday>1959-12-25</sp:birthday>
		 */
		URI birthday = factory.createURI(sp, "birthday");
		Value birthdayVal = factory.createLiteral(d.getBirthDate());
		graph.writeStatement(demographicsNode, birthday, birthdayVal);
		
		graph.endDocument();
		
		return sWriter.toString();
	}

}
