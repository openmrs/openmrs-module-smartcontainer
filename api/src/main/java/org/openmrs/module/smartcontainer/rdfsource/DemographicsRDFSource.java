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

import org.apache.commons.lang.StringUtils;
import org.openmrs.module.smartcontainer.RdfSource;
import org.openmrs.module.smartcontainer.smartData.Address;
import org.openmrs.module.smartcontainer.smartData.Name;
import org.openmrs.module.smartcontainer.smartData.SmartDemographics;
import org.openrdf.model.BNode;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.rdfxml.util.RDFXMLPrettyWriter;

/**
 * Render a RDF/XML for SMART Demographics
 */
public class DemographicsRDFSource extends RdfSource {
	
	/**
	 * @param d SMART Demographics
	 * @return RDF/XML as a String
	 * @throws IOException
	 */
	public String getRDF(SmartDemographics d) throws IOException, RDFHandlerException {
		
		Writer sWriter = new StringWriter();
		RDFXMLPrettyWriter graph = new RDFXMLPrettyWriter(sWriter);
		
		addHeader(graph);
		graph.startRDF();
		/*
		 * <sp:Demographics>
		 * ....child nodes
		 * </sp:Demographics>
		 */
		BNode demographicsNode = factory.createBNode();
		addChildBNode(sp, "Demographics", demographicsNode, graph);
		
		/*
		 * <v:n>
		 * ....child nodes
		 * </v:n>
		 */
		BNode nNode = factory.createBNode();
		graph.handleStatement(factory.createStatement(demographicsNode, factory.createURI(v, "n"), nNode));
		
		/*
		 * <v:Name>
		 * ....child nodes
		 * </v:Name>
		 */
		addChildBNode(v, "Name", nNode, graph);
		Name name = d.getName();
		
		/*
		 * Add child nodes
		 * <v:given-name>Some given name</v:given-name>
		 */
		addChildNode(v, "given-name", nNode, graph, name.getGivenName());
		addChildNode(v, "family-name", nNode, graph, name.getFamilyName());
		if (StringUtils.isNotBlank(d.getName().getMiddleName()))
			addChildNode(v, "additional-name", nNode, graph, name.getMiddleName());
		if (StringUtils.isNotBlank(name.getNamePrefix()))
			addChildNode(v, "honorific-prefix", nNode, graph, name.getNamePrefix());
		if (StringUtils.isNotBlank(name.getNameSuffix()))
			addChildNode(v, "honorific-suffix", nNode, graph, name.getNameSuffix());
		
		/*
		 * <v:Adr>
		 * ....child nodes
		 * </v:Adr>
		 */
		BNode adrNode = factory.createBNode();
		graph.handleStatement(factory.createStatement(demographicsNode, factory.createURI(v, "adr"), adrNode));
		
		/*
		 * <v:Address>
		 * ....child nodes
		 * </v:Address>
		 */
		addChildBNode(v, "Address", adrNode, graph);
		Address address = d.getAddress();
		//Mark the address as preferred
		if (address.isPreferred())
			graph.handleStatement(factory.createStatement(adrNode, type, factory.createURI(v, "pref")));
		
		/*
		 * Add child nodes
		 * <v:street-address>Some given name</v:street-address>
		 */
		if (StringUtils.isNotBlank(address.getStreetAddress()))
			addChildNode(v, "street-address", adrNode, graph, address.getStreetAddress());
		if (StringUtils.isNotBlank(address.getLocality()))
			addChildNode(v, "locality", adrNode, graph, address.getLocality());
		if (StringUtils.isNotBlank(address.getRegion()))
			addChildNode(v, "region", adrNode, graph, address.getRegion());
		if (StringUtils.isNotBlank(address.getPostalCode()))
			addChildNode(v, "postal-code", adrNode, graph, address.getPostalCode());
		if (StringUtils.isNotBlank(address.getCountryName()))
			addChildNode(v, "country", adrNode, graph, address.getCountryName());
		
		addChildNode(foaf, "gender", demographicsNode, graph, d.getGender());
		addChildNode(v, "bday", demographicsNode, graph, d.getBirthDate());
		
		/*
		 * <sp:medicalRecordNumber>
		 * ....child nodes
		 * </sp:medicalRecordNumber>
		 */
		BNode mrnNode = factory.createBNode();
		graph.handleStatement(factory.createStatement(demographicsNode, factory.createURI(sp, "medicalRecordNumber"),
		    mrnNode));
		
		/*
		 * <v:Code>
		 * ....child nodes
		 * </v:Code>
		 */
		addChildBNode(sp, "Code", mrnNode, graph);
		
		/*
		 * Add child nodes
		 * <v:dcterms>Some given name</v:title>
		 */
		addChildNode(dcterms, "title", mrnNode, graph, d.getIdentifierType() + " " + d.getIdentifier());
		addChildNode(dcterms, "identifier", mrnNode, graph, d.getIdentifier());
		addChildNode(sp, "system", mrnNode, graph, d.getIdentifierType());
		
		graph.endRDF();
		
		return sWriter.toString();
	}
}
