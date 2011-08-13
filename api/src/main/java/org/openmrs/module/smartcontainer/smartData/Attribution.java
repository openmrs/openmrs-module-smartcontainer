package org.openmrs.module.smartcontainer.smartData;

/**
 * The class representing SMART datatype
 * 
 *  <a
 *      href="http://wiki.chip.org/smart-project/index.php/Developers_Documentation:_SMART_Data_Model#Attribution_RDF">
 *      Attribution</a>
 * 
 */
public class Attribution {
	private String startTime;
	private String endTime;
	private Participant participant;

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public Participant getParticipant() {
		return participant;
	}

	public void setParticipant(Participant participant) {
		this.participant = participant;
	}

}
