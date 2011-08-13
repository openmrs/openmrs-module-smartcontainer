package org.openmrs.module.smartcontainer.rdfsource;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.List;

import org.openmrs.Patient;
import org.openmrs.module.smartcontainer.RdfSource;
import org.openmrs.module.smartcontainer.smartData.SmartEncounter;
import org.openmrs.module.smartcontainer.smartData.SmartVitalSigns;
import org.openmrs.module.smartcontainer.smartData.VitalSign;
import org.openmrs.module.smartcontainer.util.RdfUtil;
import org.openrdf.model.BNode;
import org.openrdf.model.Literal;
import org.openrdf.model.URI;
import org.openrdf.model.Value;
import org.openrdf.rio.rdfxml.RdfXmlWriter;

/**
 * Render a RDF/XML for SMART Vital Signs
 *
 */
public class VitalSignRDFSource extends RdfSource {

	/**
	 * Primary method to render
	 * 
	 * @param signs
	 * @return
	 * @throws IOException
	 */
	public String getRDF(List<SmartVitalSigns> signs) throws IOException {
		Writer sWriter = new StringWriter();
		RdfXmlWriter graph = new RdfXmlWriter(sWriter);
		addHeader(graph);
		graph.startDocument();
		
		for (SmartVitalSigns s : signs) {
			/*Add parent node
			 * <sp:VitalSigns>
			 * ..child nodes
			 * </sp:VitalSigns>
			 * 
			 */
			BNode vitalSignNode = factory.createBNode();
			URI vitalSigns = factory.createURI(sp, "VitalSigns");
			graph.writeStatement(vitalSignNode, type, vitalSigns);
		    /*Add child node
		     * <dc:date>2010-05-12T04:00:00Z</dc:date>
		     * 
		     */
			URI date = factory.createURI(dc, "date");
			Literal dateVal = factory.createLiteral(s.getDate());
			graph.writeStatement(vitalSignNode, date, dateVal);
            /*Add child node
             * <sp:encounter>
             * ..Encounter node
             * </sp:encounter>
             * 
             */
			URI encounter = factory.createURI(sp, "encounter");
			graph.writeStatement(vitalSignNode, encounter,
					addEncounter(s.getSmartEncounter(), graph));
			/*Add child node
             * <sp:bloodPressure>
             * ..Blood Pressure node
             * </sp:bloodPressure>
             * 
             */
			BNode bpNode = factory.createBNode();
			URI bp = factory.createURI(sp, "BloodPressure");
			graph.writeStatement(bpNode, type, bp);
            
			if (s.getBloodPressure() != null) {
				/*Add child node to Blood Pressure node
				 *  <sp:diastolic>
				 *  ..Vital Sign node
				 *  </sp:diastolic>
				 */
				if (s.getBloodPressure().getDiastolic() != null) {
					URI diastolic = factory.createURI(sp, "diastolic");
					graph.writeStatement(
							bpNode,
							diastolic,
							addVitalsign(s.getBloodPressure().getDiastolic(),
									graph));
				}
				/*Add child node to Blood Pressure node
				 * <sp:systolic>
				 *  ..Vital Sign node
				 *  </sp:systolic>
				 */
				if (s.getBloodPressure().getSystolic() != null) {
					URI systolic = factory.createURI(sp, "systolic");
					graph.writeStatement(
							bpNode,
							systolic,
							addVitalsign(s.getBloodPressure().getSystolic(),
									graph));
				}
			}
            /*Add child node to parent node
             * <sp:weight>
			 *  ..Vital Sign node
			 *  </sp:weight>
             */
			if (s.getWeight() != null) {
				URI weight = factory.createURI(sp, "weight");
				graph.writeStatement(vitalSignNode, weight,
						addVitalsign(s.getWeight(), graph));
			}
			/*Add child node to parent node
             * <sp:height>
			 *  ..Vital Sign node
			 *  </sp:height>
             */
			if (s.getHeight() != null) {
				URI height = factory.createURI(sp, "height");
				graph.writeStatement(vitalSignNode, height,
						addVitalsign(s.getHeight(), graph));
			}
			/*Add child node to parent node
             * <sp:bodyMassIndex>
			 *  ..Vital Sign node
			 *  </sp:bodyMassIndex>
             */
			if (s.getBodyMassIndex() != null) {
				URI bodyMassIndex = factory.createURI(sp, "bodyMassIndex");
				graph.writeStatement(vitalSignNode, bodyMassIndex,
						addVitalsign(s.getBodyMassIndex(), graph));
			}
			/*Add child node to parent node
             * <sp:respiratoryRate>
			 *  ..Vital Sign node
			 *  </sp:respiratoryRate>
             */
			if (s.getRespiratoryRate() != null) {
				URI respiratoryRate = factory.createURI(sp, "respiratoryRate");
				graph.writeStatement(vitalSignNode, respiratoryRate,
						addVitalsign(s.getRespiratoryRate(), graph));
			}
			/*Add child node to parent node
             * <sp:heartRate>
			 *  ..Vital Sign node
			 *  </sp:heartRate>
             */
			if (s.getHeartRate() != null) {
				URI heartRate = factory.createURI(sp, "heartRate");
				graph.writeStatement(vitalSignNode, heartRate,
						addVitalsign(s.getHeartRate(), graph));
			}
			/*Add child node to parent node
             * <sp:oxygenSaturation>
			 *  ..Vital Sign node
			 *  </sp:oxygenSaturation>
             */
			if (s.getOxygenSaturation() != null) {
				URI oxygenSaturation = factory
						.createURI(sp, "oxygenSaturation");
				graph.writeStatement(vitalSignNode, oxygenSaturation,
						addVitalsign(s.getOxygenSaturation(), graph));
			}
			/*Add child node to parent node
             * <sp:temperature>
			 *  ..Vital Sign node
			 *  </sp:temperature>
             */
			if (s.getTemperature() != null) {
     			URI temperature = factory.createURI(sp, "temperature");
				graph.writeStatement(vitalSignNode, temperature,
						addVitalsign(s.getTemperature(), graph));
			}
			URI bps = factory.createURI(sp, "bloodPressure");
			graph.writeStatement(vitalSignNode, bps, bpNode);
		}
		graph.endDocument();

		return sWriter.toString();
	}

	/**
	 * Create following rdf:
	 * 
	 * <sp:Encounter>
	 * ..child nodes
	 * </sp:Encounter>
	 * 
	 * @param smartEncounter
	 * @param graph
	 * @return
	 * @throws IOException
	 */
	private Value addEncounter(SmartEncounter smartEncounter, RdfXmlWriter graph)
			throws IOException {
	    /*Add parent node
	     * <sp:Encounter>
	     * ..child nodes
	     * </sp:Encounter>
	     * 
	     */
		BNode encounterNode = factory.createBNode();	
		URI encounter = factory.createURI(sp, "Encounter");
		graph.writeStatement(encounterNode, type, encounter);
		/*Add child node
	     * <sp:startDate>2010-05-12T04:00:00Z</sp:startDate>
	     * 
	     */
		if (smartEncounter.getStartDate() != null) {
			URI startDate = factory.createURI(sp, "startDate");
			Value startDateVal = factory.createLiteral(smartEncounter
					.getStartDate());
			graph.writeStatement(encounterNode, startDate, startDateVal);
		}
		/*Add child node
	     * <sp:endDate>2010-05-12T04:20:00Z</sp:endDate>
	     * 
	     */
		if(smartEncounter.getEndDate() != null){
			URI endDate = factory.createURI(sp, "endtDate");
			Value endDateVal = factory.createLiteral(smartEncounter
					.getEndDate());
			graph.writeStatement(encounterNode, endDate, endDateVal);
		
		}
		/*Add child node
	     * <sp:encounterType>
	     * ..Coded value
	     * </sp:encounterType>
	     * 
	     */
		URI encounterType = factory.createURI(sp, "encounterType");
		graph.writeStatement(encounterNode, encounterType, RdfUtil.codedValue(factory, graph, smartEncounter.getEncounterType()));
		return encounterNode;
	}

	/**
	 * Create following rdf:
	 * 
	 * <sp:VitalSign>
	 * ..child node
	 * </sp:VitalSign>
	 * 
	 * @param v
	 * @param graph
	 * @return
	 * @throws IOException
	 */
	private BNode addVitalsign(VitalSign v, RdfXmlWriter graph)
			throws IOException {
		/*
		 * Add parent node
		 * <sp:VitalSign>
	     * ..child node
	     * </sp:VitalSign>
		 */
		BNode avitalSignNameNode = factory.createBNode();
		URI aVitalSign = factory.createURI(sp, "VitalSign");
		graph.writeStatement(avitalSignNameNode, type, aVitalSign);
		/*
		 * Add child node
		 * <sp:vitalName>
	     * ..coded value node
	     * </sp:vitalName>
		 */
		URI vitalName = factory.createURI(sp, "vitalName");
		graph.writeStatement(avitalSignNameNode, vitalName,
				RdfUtil.codedValue(factory, graph, v.getVitalName()));
		/*Add child node
		 * <sp:value>1.80</sp:value>
		 */
		URI value = factory.createURI(sp, "value");
		Value valueVal = factory.createLiteral(v.getValue());
		graph.writeStatement(avitalSignNameNode, value, valueVal);
		/*Add child node
		 *<sp:unit>m</sp:unit> 
		 */
		URI unit = factory.createURI(sp, "unit");
		Value unitVal = factory.createLiteral(v.getUnit());
		graph.writeStatement(avitalSignNameNode, unit, unitVal);
		
		return avitalSignNameNode;
	}

	public String getRDF(Patient patient) throws IOException {
		return null;
	}

}
