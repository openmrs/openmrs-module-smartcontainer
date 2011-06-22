package org.openmrs.module.smartcontainer;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import org.openmrs.Patient;
import org.openrdf.model.Graph;
import org.openrdf.model.ValueFactory;

public abstract class RDFSource {
	public final static String sp = "http://smartplatforms.org/terms#";
	public final static String dcterms = "http://purl.org/dc/terms/";
	public final static String rdf = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
	public final static String foaf = "http://xmlns.com/foaf/0.1/";
	protected Graph modelGraph = new org.openrdf.model.impl.GraphImpl();
	protected ValueFactory factory = modelGraph.getValueFactory();

	public abstract String getRDF(Patient patient) throws IOException;

	protected String date(Date startDate) {
		StringBuffer date = new StringBuffer();
		Calendar cal = Calendar.getInstance();
		cal.setTime(startDate);

		date.append(cal.get(Calendar.YEAR) + "-");
		date.append(cal.get(Calendar.MONTH) + "-");
		date.append(cal.get(Calendar.DATE));
		return date.toString();

	}

}