package org.openmrs.module.smartcontainer.util;

import java.io.IOException;

import org.openmrs.module.smartcontainer.RdfSource;
import org.openmrs.module.smartcontainer.smartData.CodedValue;
import org.openmrs.module.smartcontainer.smartData.ValueAndUnit;
import org.openrdf.model.BNode;
import org.openrdf.model.Literal;
import org.openrdf.model.URI;
import org.openrdf.model.Value;
import org.openrdf.model.ValueFactory;
import org.openrdf.rio.rdfxml.RdfXmlWriter;

/**
 * Some utility methods for assisting in creating RDF
 * 
 */
public class RdfUtil {

	/**
	 * Utility method for adding valueAndUnit to the RDF graph. The output is
	 * looks like following: 
         * <sp:ValueAndUnit> 
         * <sp:value>2</sp:value>
	 * <sp:unit>{tablet}</sp:unit>
         * </sp:ValueAndUnit>
	 * 
	 * 
	 * @param factory
	 *            ValueFactory
	 * @param val
	 *            ValueAndUnit object
	 * @param graph
	 *            graph writer
	 * @return Value
	 * @throws IOException
	 */
	public static Value valueAndUnit(ValueFactory factory, RdfXmlWriter graph,
			ValueAndUnit val) throws IOException {
		BNode valueAndUnitNode = factory.createBNode();
		URI type = factory.createURI(org.openrdf.vocabulary.RDF.TYPE);
		URI valueAndUnit = factory.createURI(RdfSource.sp, "ValueAndUnit");
		graph.writeStatement(valueAndUnitNode, type, valueAndUnit);
		//
		URI value = factory.createURI(RdfSource.sp, "value");
		Value valueVal = factory.createLiteral(val.getValue());
		graph.writeStatement(valueAndUnitNode, value, valueVal);
		//
		URI unit = factory.createURI(RdfSource.sp, "unit");
		Value unitVal = factory.createLiteral(val.getUnit());
		graph.writeStatement(valueAndUnitNode, unit, unitVal);

		return valueAndUnitNode;
	}

	/**
	 * Utility method for adding codedValue to the RDF graph The output is looks
	 * like following:
         * <sp:CodedValue> 
         * <sp:code rdf:resource="http://loinc.org/codes/2951-2"/>
	 * <dcterms:title>Serum sodium</dcterms:title>
	 * <sp:codeProvenance>
         * <sp:CodeProvenance>
	 * <sp:sourceCode rdf:resource="http://my.local.coding.system/01234" />
	 * <dcterms:title>Random blood sodium level</dcterms:title>
	 * <sp:translationFidelity
	 * rdf:resource="http://smartplatforms.org/terms/code/fidelity#verified" />
	 * </sp:CodeProvenance>
         * </sp:codeProvenance> 
         * </sp:CodedValue> 
         * <sp:Code rdf:about="http://loinc.org/codes/2951-2"> 
	 * <sp:system rdf:resource="http://loinc.org/codes/" />
	 * <dcterms:identifier>2951-2</dcterms:identifier>
	 * </sp:Code>
	 * 
	 * 
	 * @param factory
	 *            ValueFactoryoded
	 * @param graph
	 *            graph writer
	 * @param c
	 *            CodedValue
	 * @return Value
	 * @throws IOException
	 */
	public static Value codedValue(ValueFactory factory, RdfXmlWriter graph,
			CodedValue c) throws IOException {
		BNode codedValueNode = factory.createBNode();
		URI type = factory.createURI(org.openrdf.vocabulary.RDF.TYPE);
		URI codedValue = factory.createURI(RdfSource.sp, "CodedValue");
		graph.writeStatement(codedValueNode, type, codedValue);
		//

		URI code = factory.createURI(RdfSource.sp, "code");
		URI uri = factory.createURI(c.getCodeBaseURL() + c.getCode());
		graph.writeStatement(codedValueNode, code, uri);
		//
		URI title = factory.createURI(RdfSource.dcterms, "title");
		Literal titleVal = factory.createLiteral(c.getTitle());
		graph.writeStatement(codedValueNode, title, titleVal);
		//
		if (c.getCodeProvenance() != null) {
			BNode codeProvenanceNode = factory.createBNode();
			URI codeProvenance = factory.createURI(RdfSource.sp,
					"CodeProvenance");
			graph.writeStatement(codeProvenanceNode, type, codeProvenance);
			//
			URI sourceCode = factory.createURI(RdfSource.sp, "sourceCode");
			URI localCodeuri = factory.createURI(c.getCodeProvenance()
					.getSourceCodeBaseURL()
					+ c.getCodeProvenance().getSourceCode());
			graph.writeStatement(codeProvenanceNode, sourceCode, localCodeuri);
			//
			Literal localSourceCodetitleVal = factory.createLiteral(c
					.getCodeProvenance().getTitle());
			graph.writeStatement(codeProvenanceNode, title,
					localSourceCodetitleVal);
			//
			URI translationFidelity = factory.createURI(RdfSource.sp,
					"translationFidelity");
			URI translationFidelityuri = factory.createURI(c
					.getCodeProvenance().getTranslationFidelityBaseURL()
					+ c.getCodeProvenance().getTranslationFidelity());
			graph.writeStatement(codeProvenanceNode, translationFidelity,
					translationFidelityuri);
			//
			URI codeProvenances = factory.createURI(RdfSource.sp,
					"codeProvenance");
			graph.writeStatement(codedValueNode, codeProvenances,
					codeProvenanceNode);
		}
		//
		graph.writeStatement(uri, type, code);
		//
		URI system = factory.createURI(RdfSource.sp, "system");
		URI systemVal = factory.createURI(c.getCodeBaseURL());
		graph.writeStatement(uri, system, systemVal);
		//
		URI identifier = factory.createURI(RdfSource.dcterms, "identifier");
		Literal identifierVal = factory.createLiteral(c.getCode());
		graph.writeStatement(uri, identifier, identifierVal);

		return codedValueNode;
	}

}
