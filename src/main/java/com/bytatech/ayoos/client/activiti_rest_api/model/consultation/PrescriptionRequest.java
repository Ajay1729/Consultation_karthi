package com.bytatech.ayoos.client.activiti_rest_api.model.consultation;

public class PrescriptionRequest {

	

	private String drug;
	
	private String dose;
	
	private String frequency;
	
	private String period;

	public String getDrug() {
		return drug;
	}

	public void setDrug(String drug) {
		this.drug = drug;
	}

	public String getDose() {
		return dose;
	}

	public void setDose(String dose) {
		this.dose = dose;
	}

	public String getFrequency() {
		return frequency;
	}

	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}

	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}
	
	@Override
	public String toString() {
		return "PrescriptionRequest [drug=" + drug + ", dose=" + dose + ", frequency=" + frequency + ", period="
				+ period + "]";
	}
	
}
