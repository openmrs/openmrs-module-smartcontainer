package org.openmrs.module.smartcontainer.rdfsource;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import java.util.ArrayList;
import java.util.List;

import org.openmrs.Concept;
import org.openmrs.ConceptNumeric;
import org.openmrs.Encounter;
import org.openmrs.Obs;
import org.openmrs.Patient;
import org.openmrs.api.context.Context;
import org.openmrs.module.smartcontainer.ConceptMappingNotFoundException;
import org.openmrs.module.smartcontainer.RDFSource;
import org.openmrs.module.smartcontainer.SMARTConceptMap;
import org.openmrs.module.smartcontainer.smartData.CodedValue;
import org.openmrs.module.smartcontainer.smartData.SmartVitalSigns;
import org.openmrs.module.smartcontainer.smartData.VitalSign;
import org.openrdf.model.BNode;
import org.openrdf.model.Literal;
import org.openrdf.model.URI;
import org.openrdf.model.Value;
import org.openrdf.rio.rdfxml.RdfXmlWriter;

public class VitalSignRDFSource extends RDFSource {

	public String getRDF(List<SmartVitalSigns> signs) throws IOException {

		Writer sWriter = new StringWriter();
		RdfXmlWriter graph = new RdfXmlWriter(sWriter);

		graph.setNamespace("rdf", rdf);
		graph.setNamespace("sp", sp);
		graph.setNamespace("foaf", foaf);
		graph.setNamespace("dc", dc);
		graph.setNamespace("dcterms", dcterms);
		graph.setNamespace("v", v);
		graph.startDocument();
		for (SmartVitalSigns s : signs) {
			//

			BNode vitalSignNode = factory.createBNode();

			URI vitalSigns = factory.createURI(sp, "VitalSigns");
			URI type = factory.createURI(org.openrdf.vocabulary.RDF.TYPE);
			graph.writeStatement(vitalSignNode, type, vitalSigns);
			//
			URI date = factory.createURI(dc, "date");
			Literal dateVal = factory.createLiteral(s.getDate());
			graph.writeStatement(vitalSignNode, date, dateVal);
			//
			BNode encounterNode = factory.createBNode();
			//
			BNode codedValueNode = factory.createBNode();
			//
			URI codedValue = factory.createURI(sp, "CodedValue");
			graph.writeStatement(codedValueNode, type, codedValue);
			//
			URI code = factory.createURI(sp, "code");
			URI codeURI = factory.createURI(s.getSmartEncounter()
					.getEncounterType().getCodeBaseURL()
					+ s.getSmartEncounter().getEncounterType().getCode());
			graph.writeStatement(codedValueNode, code, codeURI);
			//
			URI title = factory.createURI(dcterms, "title");
			Literal titleVal = factory.createLiteral(s.getSmartEncounter()
					.getEncounterType().getCode());
			graph.writeStatement(codedValueNode, title, titleVal);
			//
			URI encounter = factory.createURI(sp, "Encounter");
			graph.writeStatement(encounterNode, type, encounter);
			//
			if (s.getSmartEncounter().getStartDate() != null
					&& s.getSmartEncounter().getStartDate() != null) {
				URI startDate = factory.createURI(sp, "startDate");
				Value startDateVal = factory.createLiteral(s
						.getSmartEncounter().getStartDate());
				graph.writeStatement(encounterNode, startDate, startDateVal);
				//
				URI endDate = factory.createURI(sp, "endtDate");
				Value endDateVal = factory.createLiteral(s.getSmartEncounter()
						.getStartDate());
				graph.writeStatement(encounterNode, endDate, endDateVal);
				//
			}
			URI encounterType = factory.createURI(sp, "encounterType");
			graph.writeStatement(encounterNode, encounterType, codedValueNode);
			//
			URI encounterS = factory.createURI(sp, "encounter");
			graph.writeStatement(vitalSignNode, encounterS, encounterNode);
			//
			BNode bpNode = factory.createBNode();
			URI bp = factory.createURI(sp, "BloodPressure");
			graph.writeStatement(bpNode, type, bp);

			
			if (s.getBloodPressure() != null) {
				
				if (s.getBloodPressure().getDiastolic() != null) {
					BNode avitalSignNameNode = factory.createBNode();
					URI aVitalSign = factory.createURI(sp, "VitalSign");
					graph.writeStatement(avitalSignNameNode, type, aVitalSign);
					URI vitalName = factory.createURI(sp, "vitalName");
					graph.writeStatement(
							avitalSignNameNode,
							vitalName,
							codedValue(graph, s.getBloodPressure()
									.getDiastolic().getVitalName()));
					//
					URI value = factory.createURI(sp, "value");
					Value valueVal = factory.createLiteral(s.getBloodPressure().getDiastolic().getValue());
					graph.writeStatement(avitalSignNameNode, value, valueVal);
					//
					URI unit = factory.createURI(sp, "unit");
					Value unitVal = factory.createLiteral(s.getBloodPressure()
							.getDiastolic().getUnit());
					graph.writeStatement(avitalSignNameNode, unit, unitVal);
					URI unknown = factory.createURI(sp, "diastolic");
					graph.writeStatement(bpNode, unknown, avitalSignNameNode);
				}
				if (s.getBloodPressure().getSystolic() != null) {
					BNode avitalSignNameNode = factory.createBNode();
					URI aVitalSign = factory.createURI(sp, "VitalSign");
					graph.writeStatement(avitalSignNameNode, type, aVitalSign);
					URI vitalName = factory.createURI(sp, "vitalName");
					graph.writeStatement(
							avitalSignNameNode,
							vitalName,
							codedValue(graph, s.getBloodPressure()
									.getSystolic().getVitalName()));
					//
					URI value = factory.createURI(sp, "value");
					Value valueVal = factory.createLiteral(s.getBloodPressure()
							.getSystolic().getValue());
					graph.writeStatement(avitalSignNameNode, value, valueVal);
					//
					URI unit = factory.createURI(sp, "unit");
					Value unitVal = factory.createLiteral(s.getBloodPressure()
							.getSystolic().getUnit());
					graph.writeStatement(avitalSignNameNode, unit, unitVal);

					URI unknown = factory.createURI(sp, "systolic");
					graph.writeStatement(bpNode, unknown, avitalSignNameNode);
				}
			}

			if (s.getWeight() != null) {
				BNode avitalSignNameNode = factory.createBNode();
				URI aVitalSign = factory.createURI(sp, "VitalSign");
				graph.writeStatement(avitalSignNameNode, type, aVitalSign);
				URI vitalName = factory.createURI(sp, "vitalName");
				graph.writeStatement(avitalSignNameNode, vitalName,
						codedValue(graph, s.getWeight().getVitalName()));
				//
				URI value = factory.createURI(sp, "value");
				Value valueVal = factory
						.createLiteral(s.getWeight().getValue());
				graph.writeStatement(avitalSignNameNode, value, valueVal);
				//
				URI unit = factory.createURI(sp, "unit");
				Value unitVal = factory.createLiteral(s.getWeight().getUnit());
				graph.writeStatement(avitalSignNameNode, unit, unitVal);
				//
				URI unknown = factory.createURI(sp, "weight");
				graph.writeStatement(vitalSignNode, unknown, avitalSignNameNode);
			}
			if (s.getHeight() != null) {
				BNode avitalSignNameNode = factory.createBNode();
				URI aVitalSign = factory.createURI(sp, "VitalSign");
				graph.writeStatement(avitalSignNameNode, type, aVitalSign);
				URI vitalName = factory.createURI(sp, "vitalName");
				graph.writeStatement(avitalSignNameNode, vitalName,
						codedValue(graph, s.getHeight().getVitalName()));
				//
				URI value = factory.createURI(sp, "value");
				Value valueVal = factory
						.createLiteral(s.getHeight().getValue());
				graph.writeStatement(avitalSignNameNode, value, valueVal);
				//
				URI unit = factory.createURI(sp, "unit");
				Value unitVal = factory.createLiteral(s.getHeight().getUnit());
				graph.writeStatement(avitalSignNameNode, unit, unitVal);
				//
				URI unknown = factory.createURI(sp, "height");
				graph.writeStatement(vitalSignNode, unknown, avitalSignNameNode);
			}
			if (s.getBodyMassIndex() != null) {
				BNode avitalSignNameNode = factory.createBNode();
				URI aVitalSign = factory.createURI(sp, "VitalSign");
				graph.writeStatement(avitalSignNameNode, type, aVitalSign);
				URI vitalName = factory.createURI(sp, "vitalName");
				graph.writeStatement(avitalSignNameNode, vitalName,
						codedValue(graph, s.getBodyMassIndex().getVitalName()));
				//
				URI value = factory.createURI(sp, "value");
				Value valueVal = factory.createLiteral(s.getBodyMassIndex()
						.getValue());
				graph.writeStatement(avitalSignNameNode, value, valueVal);
				//
				URI unit = factory.createURI(sp, "unit");
				Value unitVal = factory.createLiteral(s.getBodyMassIndex()
						.getUnit());
				graph.writeStatement(avitalSignNameNode, unit, unitVal);
				//
				URI unknown = factory.createURI(sp, "bodyMassIndex");
				graph.writeStatement(vitalSignNode, unknown, avitalSignNameNode);
			}
			if (s.getRespiratoryRate() != null) {
				BNode avitalSignNameNode = factory.createBNode();
				URI aVitalSign = factory.createURI(sp, "VitalSign");
				graph.writeStatement(avitalSignNameNode, type, aVitalSign);
				URI vitalName = factory.createURI(sp, "vitalName");
				graph.writeStatement(
						avitalSignNameNode,
						vitalName,
						codedValue(graph, s.getRespiratoryRate().getVitalName()));
				//
				URI value = factory.createURI(sp, "value");
				Value valueVal = factory.createLiteral(s.getRespiratoryRate()
						.getValue());
				graph.writeStatement(avitalSignNameNode, value, valueVal);
				//
				URI unit = factory.createURI(sp, "unit");
				Value unitVal = factory.createLiteral(s.getRespiratoryRate()
						.getUnit());
				graph.writeStatement(avitalSignNameNode, unit, unitVal);
				//
				URI unknown = factory.createURI(sp, "respiratoryRate");
				graph.writeStatement(vitalSignNode, unknown, avitalSignNameNode);
			}
			if (s.getHeartRate() != null) {
				BNode avitalSignNameNode = factory.createBNode();
				URI aVitalSign = factory.createURI(sp, "VitalSign");
				graph.writeStatement(avitalSignNameNode, type, aVitalSign);
				URI vitalName = factory.createURI(sp, "vitalName");
				graph.writeStatement(avitalSignNameNode, vitalName,
						codedValue(graph, s.getHeartRate().getVitalName()));
				//
				URI value = factory.createURI(sp, "value");
				Value valueVal = factory.createLiteral(s.getHeartRate()
						.getValue());
				graph.writeStatement(avitalSignNameNode, value, valueVal);
				//
				URI unit = factory.createURI(sp, "unit");
				Value unitVal = factory.createLiteral(s.getHeartRate()
						.getUnit());
				graph.writeStatement(avitalSignNameNode, unit, unitVal);
				//
				URI unknown = factory.createURI(sp, "heartRate");
				graph.writeStatement(vitalSignNode, unknown, avitalSignNameNode);
			}
			if (s.getOxygenSaturation() != null) {
				BNode avitalSignNameNode = factory.createBNode();
				URI aVitalSign = factory.createURI(sp, "VitalSign");
				graph.writeStatement(avitalSignNameNode, type, aVitalSign);
				URI vitalName = factory.createURI(sp, "vitalName");
				graph.writeStatement(
						avitalSignNameNode,
						vitalName,
						codedValue(graph, s.getOxygenSaturation()
								.getVitalName()));
				//
				URI value = factory.createURI(sp, "value");
				Value valueVal = factory.createLiteral(s.getOxygenSaturation()
						.getValue());
				graph.writeStatement(avitalSignNameNode, value, valueVal);
				//
				URI unit = factory.createURI(sp, "unit");
				Value unitVal = factory.createLiteral(s.getOxygenSaturation()
						.getUnit());
				graph.writeStatement(avitalSignNameNode, unit, unitVal);
				//
				URI unknown = factory.createURI(sp, "oxygenSaturation");
				graph.writeStatement(vitalSignNode, unknown, avitalSignNameNode);
			}
			if (s.getTemperature() != null) {
				BNode avitalSignNameNode = factory.createBNode();
				URI aVitalSign = factory.createURI(sp, "VitalSign");
				graph.writeStatement(avitalSignNameNode, type, aVitalSign);
				URI vitalName = factory.createURI(sp, "vitalName");
				graph.writeStatement(avitalSignNameNode, vitalName,
						codedValue(graph, s.getTemperature().getVitalName()));
				//
				URI value = factory.createURI(sp, "value");
				Value valueVal = factory.createLiteral(s.getTemperature()
						.getValue());
				graph.writeStatement(avitalSignNameNode, value, valueVal);
				//
				URI unit = factory.createURI(sp, "unit");
				Value unitVal = factory.createLiteral(s.getTemperature()
						.getUnit());
				graph.writeStatement(avitalSignNameNode, unit, unitVal);
				//
				URI unknown = factory.createURI(sp, "temperature");
				graph.writeStatement(vitalSignNode, unknown, avitalSignNameNode);
			}
			URI bps = factory.createURI(sp, "bloodPressure");
			graph.writeStatement(vitalSignNode, bps, bpNode);
		}

		//

		graph.endDocument();

		return sWriter.toString();
	}

	private Value codedValue(RdfXmlWriter graph, CodedValue c)
			throws IOException {
		BNode codedValueNode = factory.createBNode();
		URI type = factory.createURI(org.openrdf.vocabulary.RDF.TYPE);
		URI codedValue = factory.createURI(sp, "CodedValue");
		graph.writeStatement(codedValueNode, type, codedValue);
		//

		URI code = factory.createURI(sp, "code");
		URI uri;

		uri = factory.createURI(c.getCodeBaseURL() + c.getCode());

		graph.writeStatement(codedValueNode, code, uri);
		//
		URI title = factory.createURI(dcterms, "title");
		Literal titleVal = factory.createLiteral(c.getCode());
		graph.writeStatement(codedValueNode, title, titleVal);
		//
		graph.writeStatement(uri, type, code);
		//
		return codedValueNode;
	}

	public String getRDF(Patient patient) throws IOException {
		return null;
	}

}
