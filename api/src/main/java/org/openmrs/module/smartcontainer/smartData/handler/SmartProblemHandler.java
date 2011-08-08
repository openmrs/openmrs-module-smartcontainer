package org.openmrs.module.smartcontainer.smartData.handler;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.openmrs.Patient;
import org.openmrs.activelist.Problem;
import org.openmrs.api.context.Context;
import org.openmrs.module.smartcontainer.ConceptMappingNotFoundException;
import org.openmrs.module.smartcontainer.SMARTConceptMap;
import org.openmrs.module.smartcontainer.smartData.CodedValue;
import org.openmrs.module.smartcontainer.smartData.SmartBaseData;
import org.openmrs.module.smartcontainer.smartData.SmartProblem;
import org.springframework.format.datetime.DateFormatter;

public class SmartProblemHandler implements SmartDataHandler {
	private SMARTConceptMap map;
	
	public SMARTConceptMap getMap() {
		return map;
	}

	public void setMap(SMARTConceptMap map) {
		this.map = map;
	}

	public SmartBaseData get(Patient patient) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<? extends SmartBaseData> getAllForPatient(Patient patient) {
		List<Problem> openmrsProblems=Context.getPatientService().getProblems(patient);
		List<SmartProblem> smartProblems=new ArrayList<SmartProblem>();
		SmartProblem problem=null;
		CodedValue name=null;
		for(Problem p:openmrsProblems){
		problem=new SmartProblem();
		name=new CodedValue();
		name.setTitle(p.getProblem().getName().getName());
		try {
			name.setCode(map.lookUp(p.getProblem()));
		} catch (ConceptMappingNotFoundException e) {
			
			e.printStackTrace();
		}
		name.setCodeBaseURL(map.getBaseURL());
		problem.setProblemName(name);//coded value
		problem.setOnset(date(p.getStartDate()));//
		if(p.getEndDate()!=null){//
			problem.setResolution(date(p.getEndDate()));//
			
		}
		smartProblems.add(problem);
		}
		return smartProblems;
	}
protected String date(Date date) {
		
		DateFormatter parser = new DateFormatter("yyyy-MM-dd");
		return parser.print(date, Context.getLocale());
		
	}

}
