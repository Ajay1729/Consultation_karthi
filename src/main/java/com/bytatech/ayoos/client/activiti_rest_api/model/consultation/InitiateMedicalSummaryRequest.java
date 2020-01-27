package com.bytatech.ayoos.client.activiti_rest_api.model.consultation;

public class InitiateMedicalSummaryRequest {

	
	private String token;
	private String doctor;
	private String nurse;
	

	public String getDoctor() {
		return doctor;
	}

	public void setDoctor(String doctor) {
		this.doctor = doctor;
	}

	public String getNurse() {
		return nurse;
	}

	public void setNurse(String nurse) {
		this.nurse = nurse;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	
}
