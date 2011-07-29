package org.openmrs.module.smartcontainer.rdfsource;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import org.openmrs.Patient;
import org.openmrs.PersonAddress;
import org.openmrs.module.smartcontainer.RDFSource;
import org.openrdf.model.BNode;
import org.openrdf.model.URI;
import org.openrdf.model.Value;
import org.openrdf.rio.rdfxml.RdfXmlWriter;

public class DemographicsRDFSource extends RDFSource {
	
	public String getRDF(Patient patient) throws IOException {
		
		Writer sWriter = new StringWriter();
		RdfXmlWriter graph = new RdfXmlWriter(sWriter);
		
		graph.setNamespace("sp", sp);
		graph.setNamespace("rdf", rdf);
		graph.setNamespace("dcterms", dcterms);
		graph.setNamespace("foaf", foaf);
		graph.startDocument();
		//
		BNode demographicsNode = factory.createBNode();
		//
		URI person = factory.createURI(foaf, "Person");
		URI type = factory.createURI(org.openrdf.vocabulary.RDF.TYPE);
		graph.writeStatement(demographicsNode, type, person);
		//
		URI givenName = factory.createURI(foaf, "givenName");
		Value gNameVal = factory.createLiteral(patient.getGivenName());
		graph.writeStatement(demographicsNode, givenName, gNameVal);
		//
		URI familyName = factory.createURI(foaf, "familyName");
		Value fNameVal = factory.createLiteral(patient.getFamilyName());
		graph.writeStatement(demographicsNode, familyName, fNameVal);
		//
		URI gender = factory.createURI(foaf, "gender");
		Value genderVal = factory.createLiteral((patient.getGender().equals("M")?"male":"female"));
		graph.writeStatement(demographicsNode, gender, genderVal);
		//
		if (!patient.getAddresses().isEmpty()) {
			URI zipcode = factory.createURI(sp, "zipcode");
			Value zipcodeVal = factory.createLiteral(((PersonAddress) patient.getAddresses().toArray()[0]).getPostalCode());
			graph.writeStatement(demographicsNode, zipcode, zipcodeVal);
		}
		//
		URI birthday = factory.createURI(sp, "birthday");
		Value birthdayVal = factory.createLiteral(date(patient.getBirthdate()));
		graph.writeStatement(demographicsNode, birthday, birthdayVal);
		
		graph.endDocument();
		return sWriter.toString();
	}
	
}
