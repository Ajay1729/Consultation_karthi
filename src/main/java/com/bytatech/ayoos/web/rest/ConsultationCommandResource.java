package com.bytatech.ayoos.web.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.bytatech.ayoos.client.activiti_rest_api.model.consultation.ConsultationRequest;
import com.bytatech.ayoos.client.activiti_rest_api.model.consultation.DefaultInfoRequest;
import com.bytatech.ayoos.client.activiti_rest_api.model.consultation.InitiateMedicalSummaryRequest;
import com.bytatech.ayoos.client.activiti_rest_api.model.consultation.ParamedicalExaminationRequest;
import com.bytatech.ayoos.client.activiti_rest_api.model.consultation.PrescriptionRequest;
import com.bytatech.ayoos.client.alfresco_rest_api.model.SiteBodyCreate;
import com.bytatech.ayoos.client.digitalsigning.model.SigningCredentials;
import com.bytatech.ayoos.domain.Prescription;
import com.bytatech.ayoos.service.ConsultationCommandService;
import com.bytatech.ayoos.service.impl.ConsultationCommandServiceImpl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * REST controller for managing MedicalSummary.
 */
@RestController
@RequestMapping("/api/command")
public class ConsultationCommandResource {

	private final Logger log = LoggerFactory.getLogger(ConsultationCommandResource.class);

    private static final String ENTITY_NAME = "consultation";
    
    @Autowired
    ConsultationCommandService consultationCommandService;
    
    
    @PostMapping("/setList")   
    public void setPrescriptionRequestList(@RequestBody PrescriptionRequest prescriptionRequest) {
    	
    List<PrescriptionRequest> prescriptionRequestList=new ArrayList<PrescriptionRequest>();
    
    prescriptionRequestList.add(prescriptionRequest);
    
    ConsultationCommandServiceImpl.setPrescriptionRequestList(prescriptionRequestList);;

    }

    
    @PostMapping("/initiateConsultation")
    public String initiateConsultationSummary(@RequestBody InitiateMedicalSummaryRequest medicalSummaryRequest) {
    	
    	return consultationCommandService.initiate(medicalSummaryRequest);
		 
    }
    
    @PostMapping("/collectDefaultInfo/{taskId}")
    public void collectInformations(@PathVariable String taskId, @RequestBody DefaultInfoRequest defaultInfoRequest){
    	
    	consultationCommandService.collectDefaultInfo(taskId,defaultInfoRequest);
    }
    
    @PostMapping("/collectConsultationInfo/{taskId}")
    public void collectConsultationInformations(@PathVariable String taskId, @RequestBody ConsultationRequest consultationRequest){
    	
    	consultationCommandService.collectConsultationInfo(taskId,consultationRequest);
    }
    
    @PostMapping("/collectParamedicalExaminationInfo/{taskId}")
    public void collectParamedicalExaminationInformations(@PathVariable String taskId, @RequestBody ParamedicalExaminationRequest paramedicalExxminationRequest){
    	
    	consultationCommandService.collectParamedicalExaminationInfo(taskId,paramedicalExxminationRequest);
    }
    
    /*@PostMapping("/collectExaminationInfo/{taskId}")
    public void collectExaminationInformations(@PathVariable String taskId,ExaminationRequest examinationRequest){
    	consultationCommandService.collectExaminationInfo(taskId, examinationRequest);
    }*/
    
   /* @PostMapping("/collectExaminationResultInfo/{taskId}")
    public void collectExaminationResultsInformations(@PathVariable String taskId, ExaminationResult examinationResult){
    	consultationCommandService.collectExaminationResultInfo(taskId, examinationResult);
    	
    }*/
    
    
    @PostMapping("/collectPrescriptionInfo/{taskId}")
    public void collectPrescriptionInformations(@PathVariable String taskId, @RequestBody List<PrescriptionRequest> prescriptionRequest ){
    	consultationCommandService.collectPrescriptionInfo(taskId,prescriptionRequest);
    }
    
    //create report for prescription with jsaper
	/*
	 * @PostMapping("/createPrescriptionReport") public void
	 * createPrescriptionReports( ){
	 * 
	 * consultationCommandService.createPrescriptionReport(); }
	 * 
	 * 
	 * @PostMapping("/createPrescriptionReportAndSave") public void
	 * createPrescriptionReportAndSave( ){
	 * 
	 * consultationCommandService.createPrescriptionReportAndSave(); }
	 */
   @PostMapping("/sign")
	public String sign(@RequestBody SigningCredentials signingCredentials) {
		
	   consultationCommandService.sign(signingCredentials);
		return "+++++++successfully signed+++++++";
   }
   
	
	@PostMapping("/uploadFile")
	public String uploadFile(@RequestBody MultipartFile file) {
		log.info("************************into upload file****************");
		consultationCommandService.uploadFile(file);
		return "success";
	}
	 
   
   @PostMapping("/createSite")
	public String createSite(@RequestBody SiteBodyCreate siteBodyCreate) {
		
		//System.out.println(sitesApi.createSite(siteBodyCreate, false, false, new ArrayList<>()).getBody());
		consultationCommandService.createSite(siteBodyCreate); 
		return "succes";
	}
   @GetMapping("/dmsid/{id}")
   public String getPatientDMSID(@PathVariable Long id) {
	   consultationCommandService.getPatientDMSID(id);
	   return "====success====";
   }
}
