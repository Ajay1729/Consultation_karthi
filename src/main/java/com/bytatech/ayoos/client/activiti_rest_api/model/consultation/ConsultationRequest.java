package com.bytatech.ayoos.client.activiti_rest_api.model.consultation;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

public class ConsultationRequest {

	private List<String> symptom;
	
	@JsonFormat(pattern = "MM-dd-yyyy hh:mm")
	private Date date;
	
	private List<String> evaluation;
	
	private String examinationRequired;
	
	private String labResultDMSURL;

	/*
	 * public String getSymptom() { return symptom; }
	 * 
	 * public void setSymptom(String symptom) { this.symptom = symptom; }
	 */

	public Date getDate() {
		return date;
	}

	public List<String> getSymptom() {
		return symptom;
	}

	public void setSymptom(List<String> symptom) {
		this.symptom = symptom;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	/*
	 * public String getEvaluation() { return evaluation; }
	 * 
	 * public void setEvaluation(String evaluation) { this.evaluation = evaluation;
	 * }
	 */
	public String getExaminationRequired() {
		return examinationRequired;
	}

	public List<String> getEvaluation() {
		return evaluation;
	}

	public void setEvaluation(List<String> evaluation) {
		this.evaluation = evaluation;
	}

	public void setExaminationRequired(String examinationRequired) {
		this.examinationRequired = examinationRequired;
	}

	public String getLabResultDMSURL() {
		return labResultDMSURL;
	}

	public void setLabResultDMSURL(String labResultDMSURL) {
		this.labResultDMSURL = labResultDMSURL;
	}
	
}
