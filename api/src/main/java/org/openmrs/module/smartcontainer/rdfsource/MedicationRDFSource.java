package org.openmrs.module.smartcontainer.rdfsource;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.List;

import org.openmrs.Concept;
import org.openmrs.Drug;
import org.openmrs.DrugOrder;
import org.openmrs.Patient;
import org.openmrs.api.context.Context;
import org.openmrs.module.smartcontainer.ConceptMappingNotFoundException;
import org.openmrs.module.smartcontainer.RDFSource;
import org.openmrs.module.smartcontainer.SMARTConceptMap;
import org.openmrs.module.smartcontainer.smartData.CodedValue;
import org.openmrs.module.smartcontainer.smartData.SmartMedication;
import org.openmrs.module.smartcontainer.smartData.ValueAndUnit;
import org.openrdf.model.BNode;
import org.openrdf.model.Literal;
import org.openrdf.model.URI;
import org.openrdf.model.Value;
import org.openrdf.rio.rdfxml.RdfXmlWriter;

public class MedicationRDFSource extends RDFSource {
	
	
	public String getRDF(List<SmartMedication> meds) throws IOException {
		Writer sWriter = new StringWriter();
		RdfXmlWriter graph = new RdfXmlWriter(sWriter);
		
		graph.setNamespace("sp", sp);
		graph.setNamespace("rdf", rdf);
		graph.setNamespace("dcterms", dcterms);
		graph.startDocument();
		for (SmartMedication med :meds ) {
			//
			BNode medicationNode = factory.createBNode();
			URI medication = factory.createURI(sp, "Medication");
			URI type = factory.createURI(org.openrdf.vocabulary.RDF.TYPE);
			graph.writeStatement(medicationNode, type, medication);
			//
			URI drugName = factory.createURI(sp, "drugName");
			graph.writeStatement(medicationNode, drugName, codedValue(graph, med.getDrugName()));
			//
			if (med.getStartDate() != null) {
				URI startDate = factory.createURI(sp, "startDate");
				Value startDateVal = factory.createLiteral(med.getStartDate());
				graph.writeStatement(medicationNode, startDate, startDateVal);
			}
			//
			URI instructions = factory.createURI(sp, "instructions");
			Value sig=null;
			if(med.getInstructions()!=null){
			
		       sig = factory.createLiteral(med.getInstructions());
			
			}
			graph.writeStatement(medicationNode, instructions, sig);
			//
			if ((med.getQuantity() != null)) {
				URI quantity = factory.createURI(sp, "quantity");
				graph.writeStatement(medicationNode, quantity,
				    valueAndUnit(med.getQuantity(), graph));
			}
			//
			if ((med.getFrequency() != null)) {
				URI frequency = factory.createURI(sp, "frequency");
				graph.writeStatement(medicationNode, frequency,
				    valueAndUnit(med.getFrequency(), graph));
				
			}//
			
		}
		graph.endDocument();
		return sWriter.toString();
	}
	
	private Value valueAndUnit(ValueAndUnit valAndUnit, RdfXmlWriter graph) throws IOException {
		BNode valueAndUnitNode = factory.createBNode();
		URI type = factory.createURI(org.openrdf.vocabulary.RDF.TYPE);
		URI valueAndUnit = factory.createURI(sp, "ValueAndUnit");
		graph.writeStatement(valueAndUnitNode, type, valueAndUnit);
		//
		URI value = factory.createURI(sp, "value");
		Value valueVal = factory.createLiteral(valAndUnit.getValue());
		graph.writeStatement(valueAndUnitNode, value, valueVal);
		//
		URI unit = factory.createURI(sp, "unit");
		Value unitVal = factory.createLiteral(valAndUnit.getUnit());
		graph.writeStatement(valueAndUnitNode, unit, unitVal);
		
		return valueAndUnitNode;
	}
	
	private Value codedValue(RdfXmlWriter graph, CodedValue codedVal) throws IOException {
		BNode codedValueNode = factory.createBNode();
		URI type = factory.createURI(org.openrdf.vocabulary.RDF.TYPE);
		URI codedValue = factory.createURI(sp, "CodedValue");
		graph.writeStatement(codedValueNode, type, codedValue);
		//
		
		URI code = factory.createURI(sp, "code");
		URI uri = factory.createURI("http://rxnav.nlm.nih.gov/REST/rxcui/" + codedVal.getCode());
		graph.writeStatement(codedValueNode, code, uri);
		//
		URI title = factory.createURI(dcterms, "title");
		Literal titleVal = factory.createLiteral(codedVal.getTitle());
		graph.writeStatement(codedValueNode, title, titleVal);
		//
		graph.writeStatement(uri, type, code);
		//
		URI system = factory.createURI(sp, "system");
		URI systemVal = factory.createURI(codedVal.getCodeBaseURL());
		graph.writeStatement(uri, system, systemVal);
		//
		URI identifier = factory.createURI(dcterms, "identifier");
		Literal identifierVal = factory.createLiteral(codedVal.getCode());
		graph.writeStatement(uri, identifier, identifierVal);
		
		return codedValueNode;
	}
	
}
