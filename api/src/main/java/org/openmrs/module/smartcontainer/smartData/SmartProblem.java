package org.openmrs.module.smartcontainer.smartData;



public class SmartProblem implements SmartBaseData {
private CodedValue problemName;
private String onset;
private String resolution;
public CodedValue getProblemName() {
	return problemName;
}
public void setProblemName(CodedValue problemName) {
	this.problemName = problemName;
}
public String getOnset() {
	return onset;
}
public void setOnset(String onset) {
	this.onset = onset;
}
public String getResolution() {
	return resolution;
}
public void setResolution(String resolution) {
	this.resolution = resolution;
}
@Override
public String toString() {
	return "SmartProblem [problemName=" + problemName + ", onset=" + onset
			+ ", resolution=" + resolution + "]";
}

}
