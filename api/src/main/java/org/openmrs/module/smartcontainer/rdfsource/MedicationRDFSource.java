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
import org.openrdf.model.BNode;
import org.openrdf.model.Literal;
import org.openrdf.model.URI;
import org.openrdf.model.Value;
import org.openrdf.rio.rdfxml.RdfXmlWriter;

public class MedicationRDFSource extends RDFSource {
	
	private SMARTConceptMap map;
	
	public SMARTConceptMap getMap() {
		return map;
	}
	
	public void setMap(SMARTConceptMap map) {
		this.map = map;
	}
	
	private String getRDF(List<DrugOrder> drugs) throws IOException {
		Writer sWriter = new StringWriter();
		RdfXmlWriter graph = new RdfXmlWriter(sWriter);
		
		graph.setNamespace("sp", sp);
		graph.setNamespace("rdf", rdf);
		graph.setNamespace("dcterms", dcterms);
		graph.startDocument();
		for (DrugOrder oder : drugs) {
			//
			BNode medicationNode = factory.createBNode();
			URI medication = factory.createURI(sp, "Medication");
			URI type = factory.createURI(org.openrdf.vocabulary.RDF.TYPE);
			graph.writeStatement(medicationNode, type, medication);
			//
			URI drugName = factory.createURI(sp, "drugName");
			graph.writeStatement(medicationNode, drugName, codedValue(graph, oder.getDrug()));
			//
			if (oder.getStartDate() != null) {
				URI startDate = factory.createURI(sp, "startDate");
				Value startDateVal = factory.createLiteral(date(oder.getStartDate()));
				graph.writeStatement(medicationNode, startDate, startDateVal);
			}
			//
			URI instructions = factory.createURI(sp, "instructions");
			Value sig=null;
			if(oder.getInstructions()!=null){
			
		       sig = factory.createLiteral(oder.getInstructions());
			
			}
			graph.writeStatement(medicationNode, instructions, sig);
			//
			if ((oder.getQuantity() != null) && (oder.getUnits() != null)) {
				URI quantity = factory.createURI(sp, "quantity");
				graph.writeStatement(medicationNode, quantity,
				    valueAndUnit(oder.getQuantity().toString(), oder.getUnits(), graph));
			}
			//
			if ((oder.getFrequency() != null) && (oder.getUnits() != null)) {
				URI frequency = factory.createURI(sp, "frequency");
				graph.writeStatement(medicationNode, frequency,
				    valueAndUnit(oder.getFrequency().toString(), oder.getUnits(), graph));
				
			}//
			
		}
		graph.endDocument();
		return sWriter.toString();
	}
	
	private Value valueAndUnit(String quantity, String units, RdfXmlWriter graph) throws IOException {
		BNode valueAndUnitNode = factory.createBNode();
		URI type = factory.createURI(org.openrdf.vocabulary.RDF.TYPE);
		URI valueAndUnit = factory.createURI(sp, "ValueAndUnit");
		graph.writeStatement(valueAndUnitNode, type, valueAndUnit);
		//
		URI value = factory.createURI(sp, "value");
		Value valueVal = factory.createLiteral(quantity);
		graph.writeStatement(valueAndUnitNode, value, valueVal);
		//
		URI unit = factory.createURI(sp, "unit");
		Value unitVal = factory.createLiteral(units);
		graph.writeStatement(valueAndUnitNode, unit, unitVal);
		
		return valueAndUnitNode;
	}
	
	private Value codedValue(RdfXmlWriter graph, Drug drug) throws IOException {
		BNode codedValueNode = factory.createBNode();
		URI type = factory.createURI(org.openrdf.vocabulary.RDF.TYPE);
		URI codedValue = factory.createURI(sp, "CodedValue");
		graph.writeStatement(codedValueNode, type, codedValue);
		//
		Concept concept = drug.getConcept();
		String conceptCode = null;
		try {
			conceptCode = map.lookUp(concept);
		}
		catch (ConceptMappingNotFoundException e) {
			
			e.printStackTrace();
		}
		URI code = factory.createURI(sp, "code");
		URI uri = factory.createURI("http://rxnav.nlm.nih.gov/REST/rxcui/" + conceptCode);
		graph.writeStatement(codedValueNode, code, uri);
		//
		URI title = factory.createURI(dcterms, "title");
		Literal titleVal = factory.createLiteral(concept.getDisplayString());
		graph.writeStatement(codedValueNode, title, titleVal);
		//
		graph.writeStatement(uri, type, code);
		//
		URI system = factory.createURI(sp, "system");
		URI systemVal = factory.createURI("http://www.ihtsdo.org/snomed-ct/concepts/");
		graph.writeStatement(uri, system, systemVal);
		//
		URI identifier = factory.createURI(dcterms, "identifier");
		Literal identifierVal = factory.createLiteral(conceptCode);
		graph.writeStatement(uri, identifier, identifierVal);
		
		return codedValueNode;
	}
	
	@Override
	public String getRDF(Patient patient) throws IOException {
		List<DrugOrder> drugs = Context.getOrderService().getDrugOrdersByPatient(patient);
		return getRDF(drugs);
	}
}
