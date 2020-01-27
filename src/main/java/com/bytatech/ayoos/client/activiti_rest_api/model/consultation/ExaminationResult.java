package com.bytatech.ayoos.client.activiti_rest_api.model.consultation;

public class ExaminationResult {

	private String labTestRef;
	
	private String labResultDMSURL;

	public String getLabTestRef() {
		return labTestRef;
	}

	public void setLabTestRef(String labTestRef) {
		this.labTestRef = labTestRef;
	}

	public String getLabResultDMSURL() {
		return labResultDMSURL;
	}

	public void setLabResultDMSURL(String labResultDMSURL) {
		this.labResultDMSURL = labResultDMSURL;
	}
}
