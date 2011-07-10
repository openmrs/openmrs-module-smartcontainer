package org.openmrs.module.smartcontainer;

import java.io.IOException;
import java.util.Date;

import org.openmrs.Patient;
import org.openmrs.api.context.Context;
import org.openrdf.model.Graph;
import org.openrdf.model.ValueFactory;
import org.springframework.format.datetime.DateFormatter;

public abstract class RDFSource {
	
	public final static String sp = "http://smartplatforms.org/terms#";
	
	public final static String dcterms = "http://purl.org/dc/terms/";
	
	public final static String rdf = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
	
	public final static String foaf = "http://xmlns.com/foaf/0.1/";
	
	protected Graph modelGraph = new org.openrdf.model.impl.GraphImpl();
	
	protected ValueFactory factory = modelGraph.getValueFactory();
	
	public abstract String getRDF(Patient patient) throws IOException;
	
	protected String date(Date startDate) {
		
		DateFormatter parser = new DateFormatter("yyyy-MM-dd HH:mm");
		return parser.print(startDate, Context.getLocale());
		
	}
	
}
