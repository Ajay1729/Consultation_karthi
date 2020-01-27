package com.bytatech.ayoos.client.activiti_rest_api.model.consultation;

public class DefaultInfoRequest {

	private String patientId;
	
	private String doctorId;
	
	//private String token;
	
	private String paramedRequired;
	
	//private String email;
	
	public String getDoctorId() {
		return doctorId;
	}
	public void setDoctorId(String doctorId) {
		this.doctorId = doctorId;
	}
	
	public String getPatientId() {
		return patientId;
	}
	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}

	/*
	 * public String getToken() { return token; } public void setToken(String token)
	 * { this.token = token; }
	 */
	public String getParamedRequired() {
		return paramedRequired;
	}
	public void setParamedRequired(String paramedRequired) {
		this.paramedRequired = paramedRequired;
	}
	
}
