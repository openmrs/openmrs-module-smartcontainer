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

import org.openmrs.module.smartcontainer.RdfSource;
import org.openmrs.module.smartcontainer.smartData.SmartDemographics;
import org.openrdf.model.BNode;
import org.openrdf.model.URI;
import org.openrdf.model.Value;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.rdfxml.util.RDFXMLPrettyWriter;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

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
        URI person = factory.createURI(foaf, "Person");

        graph.handleStatement(factory.createStatement(demographicsNode, type, person));
        /*Add child node
           * <foaf:givenName>Bob</foaf:givenName>
           */
        URI givenName = factory.createURI(foaf, "givenName");
        Value gNameVal = factory.createLiteral(d.getGivenName());
        graph.handleStatement(factory.createStatement(demographicsNode, givenName, gNameVal));
        /*Add child node
           * <foaf:familyName>Odenkirk</foaf:familyName>
           */
        URI familyName = factory.createURI(foaf, "familyName");
        Value fNameVal = factory.createLiteral(d.getFamilyName());
        graph.handleStatement(factory.createStatement(demographicsNode, familyName, fNameVal));
        /*Add child node
           * <foaf:gender>male</foaf:gender>
           */
        URI gender = factory.createURI(foaf, "gender");
        Value genderVal = factory.createLiteral(d.getGender());
        graph.handleStatement(factory.createStatement(demographicsNode, gender, genderVal));
        /*Add child node
           *  <sp:zipcode>90001</sp:zipcode>
           */
        URI zipcode = factory.createURI(sp, "zipcode");
        Value zipcodeVal = factory.createLiteral(d.getZipCode());
        graph.handleStatement(factory.createStatement(demographicsNode, zipcode, zipcodeVal));
        /*Add child node
           *  <sp:birthday>1959-12-25</sp:birthday>
           */
        URI birthday = factory.createURI(sp, "birthday");
        Value birthdayVal = factory.createLiteral(d.getBirthDate());
        graph.handleStatement(factory.createStatement(demographicsNode, birthday, birthdayVal));

        graph.endRDF();

        return sWriter.toString();
    }

}
