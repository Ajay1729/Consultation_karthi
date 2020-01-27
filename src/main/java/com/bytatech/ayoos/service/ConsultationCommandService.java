package com.bytatech.ayoos.service;

import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.bytatech.ayoos.client.activiti_rest_api.model.RestFormProperty;
import com.bytatech.ayoos.client.activiti_rest_api.model.consultation.ConsultationRequest;
import com.bytatech.ayoos.client.activiti_rest_api.model.consultation.DefaultInfoRequest;
import com.bytatech.ayoos.client.activiti_rest_api.model.consultation.InitiateMedicalSummaryRequest;
import com.bytatech.ayoos.client.activiti_rest_api.model.consultation.ParamedicalExaminationRequest;
import com.bytatech.ayoos.client.activiti_rest_api.model.consultation.PrescriptionRequest;
import com.bytatech.ayoos.client.alfresco_rest_api.model.SiteBodyCreate;
import com.bytatech.ayoos.client.digitalsigning.model.SigningCredentials;
import com.bytatech.ayoos.domain.Prescription;

import net.sf.jasperreports.engine.JRException;

//import net.sf.jasperreports.engine.JRException;

/**
 * Service Interface for managing MedicalSummary.
 */

public interface ConsultationCommandService {

	String initiate(InitiateMedicalSummaryRequest medicalSummaryRequest);

	void collectDefaultInfo(String taskId, DefaultInfoRequest defaultInfoRequest);

	void collectConsultationInfo(String taskId, ConsultationRequest consultationRequest);

	void collectParamedicalExaminationInfo(String taskId, ParamedicalExaminationRequest paramedicalExxminationRequest);

	void collectPrescriptionInfo(String taskId, List<PrescriptionRequest> prescriptionRequest);

	//void createPrescriptionReport();

	//void createPrescriptionReportAndSave();
	
	String sign(@RequestBody SigningCredentials signingCredentials);

	String upload(Resource resource);

	void createSite(SiteBodyCreate siteBodyCreate);
	
	void createPrescriptionReport(List<RestFormProperty> formProperties) throws JRException;
	
	void getPatientDMSID(Long id);

	String uploadFile(@RequestBody MultipartFile file);
	

}
