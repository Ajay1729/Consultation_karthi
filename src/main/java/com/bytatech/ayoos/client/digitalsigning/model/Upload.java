package com.bytatech.ayoos.client.digitalsigning.model;

import org.springframework.web.multipart.MultipartFile;



public class Upload {

	public Upload() {
		//super();
	}
	private MultipartFile image;
	private MultipartFile key;
	private String keytype;
	private String password;
	private String alert;
	
	
	
	public MultipartFile getImage() {
		return image;
	}
	public void setImage(MultipartFile image) {
		this.image = image;
	}
	public MultipartFile getKey() {
		return key;
	}
	public void setKey(MultipartFile key) {
		this.key = key;
	}
	public String getKeytype() {
		return keytype;
	}
	public void setKeytype(String keytype) {
		this.keytype = keytype;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getAlert() {
		return alert;
	}
	public void setAlert(String alert) {
		this.alert = alert;
	}
	@Override
	public String toString() {
		return "Upload [image=" + image + ", key=" + key + ", keytype=" + keytype + ", password=" + password
				+ ", alert=" + alert + "]";
	}
	
	
	
	
}
