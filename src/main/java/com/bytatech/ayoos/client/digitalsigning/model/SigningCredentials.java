package com.bytatech.ayoos.client.digitalsigning.model;

public class SigningCredentials {

	private String filesToSign;
	private String keyPassword;
	
	
	public String getDocument() {
		return filesToSign;
	}
	public void setDocument(String document) {
		this.filesToSign = document;
	}
	public String getPassword() {
		return keyPassword;
	}
	public void setPassword(String password) {
		this.keyPassword = password;
	}
}
