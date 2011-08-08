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

import org.openrdf.model.Graph;
import org.openrdf.model.ValueFactory;

/**
 * Contains declarations common to all RDFSources
 *
 */
public abstract class RDFSource {

	/**
	 * Name space declaration for smartplatform terms
	 */
	public final static String sp = "http://smartplatforms.org/terms#";

	/**
	 * Name space declaration for Dublin core terms
	 */
	public final static String dcterms = "http://purl.org/dc/terms/";

	/**
	 * Name space declaration for Dublin core vocabulary
	 */
	public final static String dc = "http://purl.org/dc/elements/1.1/";

	/**
	 *Name space declaration for Resource description framework
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
	protected Graph modelGraph ;

	/**
	 * Factory for creating URI and Value of RDF
	 */
	protected ValueFactory factory ;

	/**
	 *No arg constructor for initializing modelGraph and factory 
	 */
	public RDFSource() {
		super();
		modelGraph = new org.openrdf.model.impl.GraphImpl();
		factory = modelGraph.getValueFactory();
	}
	

}
