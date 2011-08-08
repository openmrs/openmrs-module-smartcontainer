package org.openmrs.module.smartcontainer.smartData;

public class CodeProvenance {
	private String sourceCode;
	private String sourceCodeBaseURL;
	private String title;
	private String translationFidelity;
	private String translationFidelityBaseURL;
	public String getSourceCode() {
		return sourceCode;
	}
	public void setSourceCode(String sourceCode) {
		this.sourceCode = sourceCode;
	}
	public String getSourceCodeBaseURL() {
		return sourceCodeBaseURL;
	}
	public void setSourceCodeBaseURL(String sourceCodeBaseURL) {
		this.sourceCodeBaseURL = sourceCodeBaseURL;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getTranslationFidelity() {
		return translationFidelity;
	}
	public void setTranslationFidelity(String translationFidelity) {
		this.translationFidelity = translationFidelity;
	}
	public String getTranslationFidelityBaseURL() {
		return translationFidelityBaseURL;
	}
	public void setTranslationFidelityBaseURL(String translationFidelityBaseURL) {
		this.translationFidelityBaseURL = translationFidelityBaseURL;
	}
	
}
