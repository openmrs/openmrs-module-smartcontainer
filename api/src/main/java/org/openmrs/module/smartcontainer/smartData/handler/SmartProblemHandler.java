package org.openmrs.module.smartcontainer.smartData.handler;

import java.util.ArrayList;
import java.util.List;

import org.openmrs.Patient;
import org.openmrs.activelist.Problem;
import org.openmrs.api.context.Context;
import org.openmrs.module.smartcontainer.SmartConceptMap;
import org.openmrs.module.smartcontainer.smartData.SmartBaseData;
import org.openmrs.module.smartcontainer.smartData.SmartProblem;
import org.openmrs.module.smartcontainer.util.SmartDataHandlerUtil;

public class SmartProblemHandler implements SmartDataHandler {
	private SmartConceptMap map;

	public SmartConceptMap getMap() {
		return map;
	}

	public void setMap(SmartConceptMap map) {
		this.map = map;
	}

	public SmartBaseData getForPatient(Patient patient) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<SmartProblem> getAllForPatient(Patient patient) {
		List<Problem> openmrsProblems = Context.getPatientService()
				.getProblems(patient);
		List<SmartProblem> smartProblems = new ArrayList<SmartProblem>();
		for (Problem p : openmrsProblems) {
			SmartProblem problem = new SmartProblem();
			problem.setProblemName(SmartDataHandlerUtil.codedValueHelper(
					p.getProblem(), map));// coded value
			problem.setOnset(SmartDataHandlerUtil.date(p.getStartDate()));//
			if (p.getEndDate() != null) {//
				problem.setResolution(SmartDataHandlerUtil.date(p.getEndDate()));//

			}
			smartProblems.add(problem);
		}
		return smartProblems;
	}

}
