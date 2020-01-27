package com.bytatech.ayoos.client.activiti_rest_api.model.consultation;

public class ParamedicalExaminationRequest {

	private String paramedRef;
	
	private String bp;
	
	private String weight;
	
	private String height;
	
	private String temperature;

	public String getParamedRef() {
		return paramedRef;
	}

	public void setParamedRef(String paramedRef) {
		this.paramedRef = paramedRef;
	}

	public String getBp() {
		return bp;
	}

	public void setBp(String bp) {
		this.bp = bp;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public String getTemperature() {
		return temperature;
	}

	public void setTemperature(String temperature) {
		this.temperature = temperature;
	}
}
