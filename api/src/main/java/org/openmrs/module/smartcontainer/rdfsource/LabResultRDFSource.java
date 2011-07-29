package org.openmrs.module.smartcontainer.rdfsource;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.openmrs.Concept;
import org.openmrs.ConceptNumeric;
import org.openmrs.Obs;
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


public class LabResultRDFSource extends RDFSource {
      Log log=LogFactory.getLog(getClass());
	
	private SMARTConceptMap map;
	
	public SMARTConceptMap getMap() {
		return map;
	}
	
	public void setMap(SMARTConceptMap map) {
		this.map = map;
	}
	
	private String getRDF(List<Obs> obs) throws IOException {
		Writer sWriter = new StringWriter();
		RdfXmlWriter graph = new RdfXmlWriter(sWriter);
		
		graph.setNamespace("sp", sp);
		graph.setNamespace("rdf", rdf);
		graph.setNamespace("dcterms", dcterms);
		graph.setNamespace("foaf", foaf);
		graph.setNamespace("v", v);
		graph.startDocument();
		for (Obs o : obs) {
			Concept concept=Context.getConceptService().getConcept(String.valueOf(o.getConcept().getConceptId()));
			log.error("Concept :"+concept.getName().getName()+".");
			log.error("ConceptClass :"+concept.getConceptClass().getName()+".");
			log.error("ConceptDataType :"+concept.getDatatype().getName()+".");
			log.error("ConceptMapping :"+concept.getConceptMappings().iterator().next().getSource().getName()+".");
			if(concept.getConceptClass().getName().equals("Test") && !o.isObsGrouping()){
			//
			BNode labResultNode = factory.createBNode();
			URI labResult = factory.createURI(sp, "LabResult");
			URI type = factory.createURI(org.openrdf.vocabulary.RDF.TYPE);
			graph.writeStatement(labResultNode, type, labResult);
			//
			URI labName = factory.createURI(sp, "labName");
			graph.writeStatement(labResultNode, labName , codedValue(graph, concept));
			//
			
			
			ConceptNumeric cn;
			if (concept.isNumeric()) {
				cn=Context.getConceptService().getConceptNumeric( concept.getConceptId());
				if(cn!=null){
				log.info("inside value code");
				BNode quantitativeResultNode = factory.createBNode();
				URI quantitativeResult = factory.createURI(sp, "QuantitativeResult");
				graph.writeStatement(quantitativeResultNode, type,quantitativeResult);
				URI valueAndUnit = factory.createURI(sp, "valueAndUnit");
				graph.writeStatement(quantitativeResultNode,valueAndUnit,valueAndUnit(o.getValueNumeric().toString(),cn.getUnits(), graph));
				//
				BNode resultRangeNode = factory.createBNode();
				URI resultRange = factory.createURI(sp, "ResultRange");
				graph.writeStatement(resultRangeNode, type,resultRange);
				//
				if(cn.getLowAbsolute()!=null){
				URI minimum = factory.createURI(sp, "minimum");
				graph.writeStatement(resultRangeNode, minimum, valueAndUnit(cn.getLowAbsolute().toString(), cn.getUnits(), graph));
				}
				//
				if(cn.getHiAbsolute()!=null){
				URI maximum = factory.createURI(sp, "maximum");
				graph.writeStatement(resultRangeNode, maximum, valueAndUnit(cn.getHiAbsolute().toString(), cn.getUnits(), graph));
				}
				//
				URI normalRange = factory.createURI(sp, "normalRange");
				graph.writeStatement(quantitativeResultNode, normalRange, resultRangeNode);
				//
				URI quantitativeResults = factory.createURI(sp, "quantitativeResult");
				graph.writeStatement(labResultNode, quantitativeResults, quantitativeResultNode);
				//
				}
				
			}
			//
			if (concept.getDatatype().isCoded()) {
				
				BNode quantitativeResultNode = factory.createBNode();
				URI quantitativeResult = factory.createURI(sp, "QuantitativeResult");
				graph.writeStatement(quantitativeResultNode, type,quantitativeResult);
				//
				URI value = factory.createURI(sp, "value");
				Value valueVal=factory.createLiteral(o.getValueCodedName().getName());
				graph.writeStatement(quantitativeResultNode,value,valueVal);
				
			}
			BNode attributionNode = factory.createBNode();
			URI attribution = factory.createURI(sp, "Attribution");
			graph.writeStatement(attributionNode, type,attribution);
			//
			URI startTime = factory.createURI(sp, "startTime");
			Value startDateVal = factory.createLiteral(date(o.getObsDatetime()));
			graph.writeStatement(attributionNode, startTime, startDateVal);
			//
			URI specimenCollected = factory.createURI(sp, "specimenCollected");
			graph.writeStatement(labResultNode, specimenCollected, attributionNode);
			//
			if(o.getAccessionNumber()!=null){
			URI externalID = factory.createURI(sp, "externalID");
			Value externalIDVal = factory.createLiteral(o.getAccessionNumber());
			graph.writeStatement(labResultNode, externalID, externalIDVal);
			}
			}
			
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
	
	private Value codedValue(RdfXmlWriter graph, Concept concept) throws IOException {
		BNode codedValueNode = factory.createBNode();
		URI type = factory.createURI(org.openrdf.vocabulary.RDF.TYPE);
		URI codedValue = factory.createURI(sp, "CodedValue");
		graph.writeStatement(codedValueNode, type, codedValue);
		//
		String conceptCode = null;
		try {
			conceptCode = map.lookUp(concept);
		}
		catch (ConceptMappingNotFoundException e) {
			
			e.printStackTrace();
		}
		URI code = factory.createURI(sp, "code");
		URI uri = factory.createURI("http://loinc.org/codes/" + conceptCode);
		graph.writeStatement(codedValueNode, code, uri);
		//
		URI title = factory.createURI(dcterms, "title");
		Literal titleVal = factory.createLiteral(concept.getDisplayString());
		graph.writeStatement(codedValueNode, title, titleVal);
		//
		BNode codeProvenanceNode = factory.createBNode();
		URI codeProvenance = factory.createURI(sp, "CodeProvenance");
		graph.writeStatement(codeProvenanceNode, type, codeProvenance);
		//
		URI sourceCode = factory.createURI(sp, "sourceCode");
		URI localCodeuri = factory.createURI("http://openmrs.org/codes/" + concept.getConceptId());
		graph.writeStatement(codeProvenanceNode, sourceCode,localCodeuri);
		//
		Literal localSourceCodetitleVal = factory.createLiteral(concept.getDisplayString());
		graph.writeStatement(codeProvenanceNode, title,localSourceCodetitleVal);
		//
		URI translationFidelity = factory.createURI(sp, "translationFidelity");
		URI translationFidelityuri = factory.createURI("http://smartplatforms.org/terms/code/fidelity#verified");
		graph.writeStatement(codeProvenanceNode,translationFidelity,translationFidelityuri );
		//
		URI codeProvenances = factory.createURI(sp, "codeProvenance");
		graph.writeStatement(codedValueNode,codeProvenances,codeProvenanceNode);
		//
		graph.writeStatement(uri, type, code);
		//
		URI system = factory.createURI(sp, "system");
		URI systemVal = factory.createURI("http://loinc.org/codes/");
		graph.writeStatement(uri, system, systemVal);
		//
		URI identifier = factory.createURI(dcterms, "identifier");
		Literal identifierVal = factory.createLiteral(conceptCode);
		graph.writeStatement(uri, identifier, identifierVal);
		
		return codedValueNode;
	}
	
	@Override
	public String getRDF(Patient patient) throws IOException {
		List<Obs> obs = Context.getObsService().getObservationsByPerson(patient);
		return getRDF(obs);
	}
	
}
