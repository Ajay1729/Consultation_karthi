package com.bytatech.ayoos.service.impl;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.alfresco.service.cmr.repository.NodeRef;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;


import com.bytatech.ayoos.client.activiti_rest_api.api.FormsApi;
import com.bytatech.ayoos.client.activiti_rest_api.api.HistoryApi;
import com.bytatech.ayoos.client.activiti_rest_api.api.ProcessInstancesApi;
import com.bytatech.ayoos.client.activiti_rest_api.api.TasksApi;
import com.bytatech.ayoos.client.activiti_rest_api.model.DataResponse;
import com.bytatech.ayoos.client.activiti_rest_api.model.FormDataResponse;
import com.bytatech.ayoos.client.activiti_rest_api.model.ProcessInstanceCreateRequest;
import com.bytatech.ayoos.client.activiti_rest_api.model.ProcessInstanceResponse;
import com.bytatech.ayoos.client.activiti_rest_api.model.RestFormProperty;
import com.bytatech.ayoos.client.activiti_rest_api.model.RestVariable;
import com.bytatech.ayoos.client.activiti_rest_api.model.SubmitFormRequest;
import com.bytatech.ayoos.client.activiti_rest_api.model.TaskActionRequest;
import com.bytatech.ayoos.client.activiti_rest_api.model.consultation.ConsultationDetails;
import com.bytatech.ayoos.client.activiti_rest_api.model.consultation.ConsultationRequest;
import com.bytatech.ayoos.client.activiti_rest_api.model.consultation.DefaultInfoRequest;
import com.bytatech.ayoos.client.activiti_rest_api.model.consultation.InitiateMedicalSummaryRequest;
import com.bytatech.ayoos.client.activiti_rest_api.model.consultation.ParamedicalExaminationRequest;
import com.bytatech.ayoos.client.activiti_rest_api.model.consultation.PatientInfo;
import com.bytatech.ayoos.client.activiti_rest_api.model.consultation.PrescriptionRequest;
import com.bytatech.ayoos.client.alfresco_rest_api.api.NodesApi;
import com.bytatech.ayoos.client.alfresco_rest_api.api.SitesApi;
import com.bytatech.ayoos.client.alfresco_rest_api.model.NodeBodyCreate;
import com.bytatech.ayoos.client.alfresco_rest_api.model.NodeEntry;
import com.bytatech.ayoos.client.alfresco_rest_api.model.SiteBodyCreate;
import com.bytatech.ayoos.client.digitalsigning.api.DigitalSigningApi;
import com.bytatech.ayoos.client.digitalsigning.api.DigitalSigningUploadApi;
import com.bytatech.ayoos.client.digitalsigning.model.SigningCredentials;
import com.bytatech.ayoos.client.patient_service.api.PatientResourceApi;
import com.bytatech.ayoos.client.patient_service.model.PatientDTO;
import com.bytatech.ayoos.domain.Prescription;
import com.bytatech.ayoos.repository.ConsultationRepository;
import com.bytatech.ayoos.repository.PrescriptionRepository;
import com.bytatech.ayoos.repository.search.ConsultationSearchRepository;
import com.bytatech.ayoos.security.SecurityUtils;
import com.bytatech.ayoos.service.ConsultationCommandService;
import com.bytatech.ayoos.service.UserService;
import com.bytatech.ayoos.service.dto.UserDTO;
import com.bytatech.ayoos.service.mapper.ConsultationMapper;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

/**
 * Service Implementation for managing MedicalSummary.
 */
@Service
@Transactional
@SuppressWarnings("unchecked")
public class ConsultationCommandServiceImpl implements ConsultationCommandService{

	private final Logger log = LoggerFactory.getLogger(ConsultationCommandServiceImpl.class);

	/*@Autowired
	private ConsultationCommandResourceAssembler assembler;*/
	
    private final ConsultationRepository consultationRepository;

    private final ConsultationMapper consultationMapper;

    //private final ConsultationSearchRepository consultationSearchRepository;
    
    @Autowired
	DigitalSigningApi digitalSigningApi;
    
	@Autowired
	DigitalSigningUploadApi digitalSigningUploadApi;
    
    @Autowired
	private UserService userService;
	
    @Autowired
    private ProcessInstancesApi processInstanceApi;
    
    @Autowired
    private FormsApi formsApi;

    @Autowired
    private TasksApi tasksApi;
    
    @Autowired
    private NodesApi nodesApi;
    
    @Autowired
	SitesApi sitesApi;
    
    @Autowired
    HistoryApi historyApi;
    
    @Autowired
    PatientResourceApi patientResourceApi;
    
    @Autowired
    PrescriptionRepository prescriptionRepo;
    
    
    String siteName = null;
    
    private static List<PrescriptionRequest> prescriptionRequestList = new ArrayList<PrescriptionRequest>();
    
	public ConsultationCommandServiceImpl(ConsultationRepository consultationRepository,
			ConsultationMapper consultationMapper) {
		this.consultationRepository = consultationRepository;
		this.consultationMapper = consultationMapper;
		
	}

	
	
	@Override
	public String initiate(InitiateMedicalSummaryRequest medicalSummaryRequest) {
		ProcessInstanceCreateRequest processInstanceCreateRequest=new ProcessInstanceCreateRequest();
   		List<RestVariable> variables=new ArrayList<RestVariable>();
   		//processInstanceCreateRequest.setProcessDefinitionId("consultation:1:43");
   		//processInstanceCreateRequest.setProcessDefinitionId("consultation:2:923");
   		//processInstanceCreateRequest.setProcessDefinitionId("consultation:5:2835");
   		processInstanceCreateRequest.setProcessDefinitionId("consultation:1:76397");
   		RestVariable tokenRestVariable=new RestVariable();
   		
   		tokenRestVariable.setName("token");
   		tokenRestVariable.setScope("global");
   		tokenRestVariable.setType("string");
   		tokenRestVariable.setValue(medicalSummaryRequest.getToken());
   		variables.add(tokenRestVariable);
   		
   		RestVariable doctorRestVariable=new RestVariable();
   		doctorRestVariable.setName("doctor");
   		doctorRestVariable.setScope("global");
   		doctorRestVariable.setType("string");
   		doctorRestVariable.setValue(medicalSummaryRequest.getDoctor());
   		variables.add(doctorRestVariable);
   		
   		RestVariable nurseRestVariable=new RestVariable();
   		nurseRestVariable.setName("nurse");
   		nurseRestVariable.setScope("global");
   		nurseRestVariable.setType("string");
   		nurseRestVariable.setValue(medicalSummaryRequest.getNurse());
   		variables.add(nurseRestVariable);
   		
   		
   		
   		log.info("*****************************************************"+variables.size());
   		processInstanceCreateRequest.setVariables(variables);
   		
   		
   		ResponseEntity<ProcessInstanceResponse> processInstanceResponse = processInstanceApi
				.createProcessInstance(processInstanceCreateRequest);
		String processInstanceId = processInstanceResponse.getBody().getId();
		String processDefinitionId = processInstanceCreateRequest.getProcessDefinitionId();
		log.info("++++++++++++++++processDefinitionId++++++++++++++++++"+ processDefinitionId);
		log.info("++++++++++++++++ProcessInstanceId is+++++++++++++ " + processInstanceId);
		
   		processInstanceApi.createProcessInstance(processInstanceCreateRequest);
   		
		/*ResponseEntity<DataResponse> taskResponseDefaultInfo = tasksApi.getTasks("Choose DefaultInfo", null, null, null, null,
				null, null, null, null, null, null, null, null, null, null, null, null, null, processInstanceId, null,
				null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null);
		System.out.println(taskResponseDefaultInfo.getBody());
		String chooseDefaultInfoTaskId = ((List<LinkedHashMap<String, String>>) taskResponseDefaultInfo.getBody().getData())
				.get(0).get("id");
		log.info("Task ID of Choose DefaultInfo task ID is " + chooseDefaultInfoTaskId);
		collectDefaultInfo(chooseDefaultInfoTaskId, testDefaultInfoRequest(medicalSummaryRequest.getToken()));
		
		CommandResource commandResource = assembler.toResource(processInstanceId);
		commandResource.setStatus(taskResponseDefaultInfo.getStatusCode().name());
		*/
		
		return processInstanceId;
   		
   	}
		
	public ConsultationDetails getAppointmentDetails() {
		ConsultationDetails consultationDetails = new ConsultationDetails();
		//consultationDetails.setAppointmentDateAndTime(ZonedDateTime.now(ZoneId.systemDefault()).toString());
		//appointmentDetails.setTrackingID(generateTrackingID());
		consultationDetails.setToken(sampleToken());
		
		UserDTO currentUser = null;
		if (SecurityContextHolder.getContext().getAuthentication() instanceof OAuth2Authentication) {
			currentUser = userService.getUserFromAuthentication(
					(OAuth2Authentication) SecurityContextHolder.getContext().getAuthentication());
		}
		//appointmentDetails.setPatientInfo(getPatientDetails(currentUser));
		consultationDetails.setPatientId(currentUser.getFirstName());
		
		log.info("Consultation Details are " + consultationDetails);
		return consultationDetails;
	}

	
	public PatientInfo getPatientDetails(UserDTO userDTO) {
		// User principal call to get patient Details here
		if (userDTO == null) {

			return null;

		} else {
			PatientInfo patientInfo = new PatientInfo();
			patientInfo.setEmail(userDTO.getEmail());
			patientInfo.setPhoneNumber(Long.parseLong(SecurityUtils.getPhoneNumberFromAuthentication().get()));
			patientInfo.setFirstName(userDTO.getFirstName());
			patientInfo.setPatientId(userDTO.getLogin());
			log.info("Patient Details " + patientInfo);
			return patientInfo;
		}
	}
	
	public String sampleToken() {
		String tokenName =null;
		for (int i=0;i<1;i++) {
		tokenName = "Token"+i;
		}
		return tokenName;
	}

	public DefaultInfoRequest testDefaultInfoRequest(String token) {
		
		DefaultInfoRequest defaultInfoRequest = new DefaultInfoRequest();
		defaultInfoRequest.setPatientId("karthi");
		//defaultInfoRequest.setToken(token);
		
		
		return defaultInfoRequest;
	}



	@Override
	public void collectDefaultInfo(String taskId, DefaultInfoRequest defaultInfoRequest) {
		log .info("into ====================collectDefaultInfo()");
   		List<RestFormProperty>formProperties=new ArrayList<RestFormProperty>();
   		SubmitFormRequest submitFormRequest = new SubmitFormRequest();
   		submitFormRequest.setAction("completed");
   		submitFormRequest.setTaskId(taskId);
   		
   		RestFormProperty patientIdFormProperty = new RestFormProperty();
   		patientIdFormProperty.setId("patientId");
   		patientIdFormProperty.setName("patientId");
   		patientIdFormProperty.setType("String");
   		patientIdFormProperty.setReadable(true);
   		patientIdFormProperty.setValue(defaultInfoRequest.getPatientId());
   		formProperties.add(patientIdFormProperty);
   		
   		RestFormProperty tokenFormProperty = new RestFormProperty();
   		/*tokenFormProperty.setId("token");
   		tokenFormProperty.setName("token");
   		tokenFormProperty.setType("String");
   		tokenFormProperty.setReadable(true);
   		tokenFormProperty.setValue(defaultInfoRequest.getToken());
   		formProperties.add(tokenFormProperty);*/
   		
   		RestFormProperty doctorIdFormProperty = new RestFormProperty();
   		doctorIdFormProperty.setId("doctorId");
   		doctorIdFormProperty.setName("doctorId");
   		doctorIdFormProperty.setType("String");
   		doctorIdFormProperty.setReadable(true);
   		doctorIdFormProperty.setValue(defaultInfoRequest.getDoctorId());
   		formProperties.add(doctorIdFormProperty);
   		
   		RestFormProperty paramedRequiredFormProperty = new RestFormProperty();
   		paramedRequiredFormProperty.setId("paramedRequired");
   		paramedRequiredFormProperty.setName("paramedRequired");
   		paramedRequiredFormProperty.setType("String");
   		paramedRequiredFormProperty.setReadable(true);
   		paramedRequiredFormProperty.setValue(defaultInfoRequest.getParamedRequired());
   		formProperties.add(paramedRequiredFormProperty);
   		
   		submitFormRequest.setProperties(formProperties);
   		//String processInstanceId=tasksApi.getTask(taskId).getBody().getProcessInstanceId();
   		//CommandResource commandResource = assembler.toResource(processInstanceId);
   		//ResponseEntity<ProcessInstanceResponse> response = formsApi.submitForm(submitFormRequest);
   		//commandResource.setStatus(response.getStatusCode().name());
   		formsApi.submitForm(submitFormRequest);
		
	}

	@Override
	public void collectConsultationInfo(String taskId, ConsultationRequest consultationRequest) {
		log .info("into ====================collectConsultationInfo()");
		/*
		 * List<RestFormProperty>formProperties=new ArrayList<RestFormProperty>();
		 * SubmitFormRequest submitFormRequest = new SubmitFormRequest();
		 * submitFormRequest.setAction("completed");
		 * submitFormRequest.setTaskId(taskId);
		 * 
		 * RestFormProperty symptomFormProperty = new RestFormProperty();
		 * symptomFormProperty.setId("symptom"); symptomFormProperty.setName("symptom");
		 * symptomFormProperty.setType("String"); symptomFormProperty.setReadable(true);
		 * symptomFormProperty.setValue(consultationRequest.getSymptom());
		 * formProperties.add(symptomFormProperty);
		 * 
		 * RestFormProperty dateFormProperty = new RestFormProperty();
		 * dateFormProperty.setId("date"); dateFormProperty.setName("date");
		 * dateFormProperty.setType("Date"); dateFormProperty.setReadable(true);
		 * dateFormProperty.setWritable(true); SimpleDateFormat formatter=new
		 * SimpleDateFormat("MM-dd-yyyy"); String
		 * date=formatter.format(consultationRequest.getDate());
		 * dateFormProperty.setValue(date); formProperties.add(dateFormProperty);
		 * 
		 * RestFormProperty evaluationFormProperty = new RestFormProperty();
		 * evaluationFormProperty.setId("evaluation");
		 * evaluationFormProperty.setName("evaluation");
		 * evaluationFormProperty.setType("String");
		 * evaluationFormProperty.setReadable(true);
		 * evaluationFormProperty.setValue(consultationRequest.getEvaluation());
		 * formProperties.add(evaluationFormProperty);
		 * 
		 * RestFormProperty labResultDMSURLFormProperty = new RestFormProperty();
		 * labResultDMSURLFormProperty.setId("labResultDMSURL");
		 * labResultDMSURLFormProperty.setName("labResultDMSURL");
		 * labResultDMSURLFormProperty.setType("String");
		 * labResultDMSURLFormProperty.setReadable(true);
		 * labResultDMSURLFormProperty.setValue(consultationRequest.getLabResultDMSURL()
		 * ); formProperties.add(labResultDMSURLFormProperty);
		 * 
		 * 
		 * RestFormProperty examinationRequiredFormProperty = new RestFormProperty();
		 * examinationRequiredFormProperty.setId("examinationRequired");
		 * examinationRequiredFormProperty.setName("examinationRequired");
		 * examinationRequiredFormProperty.setType("String");
		 * examinationRequiredFormProperty.setReadable(true);
		 * examinationRequiredFormProperty.setValue(consultationRequest.
		 * getExaminationRequired());
		 * formProperties.add(examinationRequiredFormProperty);
		 * 
		 * submitFormRequest.setProperties(formProperties);
		 * formsApi.submitForm(submitFormRequest);
		 */
   		
		
		//String processInstanceId=tasksApi.getTask(taskId).getBody().getProcessInstanceId();

		TaskActionRequest taskActionRequest = new TaskActionRequest();
		taskActionRequest.setAction("complete");
		List<RestVariable> variables = new ArrayList<RestVariable>();

		RestVariable consultationRequestVariable = new RestVariable();
		consultationRequestVariable.setName("consultationRequest");
		consultationRequestVariable.setScope("global");
		consultationRequestVariable.setValue(consultationRequest);
		variables.add(consultationRequestVariable);
		
		/*
		 * RestVariable evaluationVariable = new RestVariable();
		 * evaluationVariable.setName("evaluation");
		 * evaluationVariable.setScope("global"); evaluationVariable.setValue("String");
		 * variables.add(evaluationVariable);
		 * 
		 * RestVariable examinationRequiredVariable = new RestVariable();
		 * examinationRequiredVariable.setName("examinationRequired");
		 * examinationRequiredVariable.setScope("global");
		 * examinationRequiredVariable.setValue("String");
		 * variables.add(examinationRequiredVariable);
		 * 
		 * RestVariable dateVariable = new RestVariable(); dateVariable.setName("date");
		 * dateVariable.setScope("global"); dateVariable.setValue("Date");
		 * variables.add(dateVariable);
		 */		
		taskActionRequest.setVariables(variables);
		log.info("consultationRequestVariable " + consultationRequestVariable);
//		log.info("examinationRequiredVariable " + examinationRequiredVariable);
		
		
		
		/*
		 * List<RestFormProperty>formProperties=new ArrayList<RestFormProperty>();
		 * SubmitFormRequest submitFormRequest = new SubmitFormRequest();
		 * submitFormRequest.setAction("completed");
		 * submitFormRequest.setTaskId(taskId);
		 * 
		 * RestFormProperty examinationRequiredFormProperty = new RestFormProperty();
		 * examinationRequiredFormProperty.setId("examinationRequired");
		 * examinationRequiredFormProperty.setName("examinationRequired");
		 * examinationRequiredFormProperty.setType("String");
		 * examinationRequiredFormProperty.setReadable(true);
		 * examinationRequiredFormProperty.setValue(consultationRequest.
		 * getExaminationRequired());
		 * formProperties.add(examinationRequiredFormProperty);
		 */
		
		
		
		//submitFormRequest.setProperties(formProperties);
		//formsApi.submitForm(submitFormRequest);
		tasksApi.executeTaskAction(taskId, taskActionRequest);
		 
		/*
		 * ResponseEntity<DataResponse> taskReponse =
		 * historyApi.listHistoricTaskInstances(taskId, null, null, null, null, null,
		 * null, null, null, null, null, "Collect Informations", null, null, null, null,
		 * null, null, null, null, null, null, null, null, null, null, null, null, null,
		 * null, null, null, null, null, null, null, null, null, null, null, null,
		 * null); List<LinkedHashMap<String, String>> map = (List<LinkedHashMap<String,
		 * String>>) taskReponse.getBody().getData();
		 */
		
		
	}

	@Override
	public void collectParamedicalExaminationInfo(String taskId,
			ParamedicalExaminationRequest paramedicalExaminationRequest) {
		log .info("into ====================collectConsultationInfo()");
   		List<RestFormProperty>formProperties=new ArrayList<RestFormProperty>();
   		SubmitFormRequest submitFormRequest = new SubmitFormRequest();
   		submitFormRequest.setAction("completed");
   		submitFormRequest.setTaskId(taskId);
   		
   		RestFormProperty paramedRefFormProperty = new RestFormProperty();
   		paramedRefFormProperty.setId("paramedRef");
   		paramedRefFormProperty.setName("paramedRef");
   		paramedRefFormProperty.setType("String");
   		paramedRefFormProperty.setReadable(true);
   		paramedRefFormProperty.setValue(paramedicalExaminationRequest.getParamedRef());
   		formProperties.add(paramedRefFormProperty);
   		
   		RestFormProperty bpFormProperty = new RestFormProperty();
   		bpFormProperty.setId("bp");
   		bpFormProperty.setName("bp");
   		bpFormProperty.setType("Date");
   		bpFormProperty.setReadable(true);
   		bpFormProperty.setValue(paramedicalExaminationRequest.getBp());
   		formProperties.add(bpFormProperty);
   		
   		RestFormProperty weightFormProperty = new RestFormProperty();
   		weightFormProperty.setId("weight");
   		weightFormProperty.setName("weight");
   		weightFormProperty.setType("String");
   		weightFormProperty.setReadable(true);
   		weightFormProperty.setValue(paramedicalExaminationRequest.getWeight());
   		formProperties.add(weightFormProperty);
   		
   		RestFormProperty heightFormProperty = new RestFormProperty();
   		heightFormProperty.setId("height");
   		heightFormProperty.setName("height");
   		heightFormProperty.setType("String");
   		heightFormProperty.setReadable(true);
   		heightFormProperty.setValue(paramedicalExaminationRequest.getHeight());
   		formProperties.add(heightFormProperty);
   		
   		
   		RestFormProperty temperatureFormProperty = new RestFormProperty();
   		temperatureFormProperty.setId("temperature");
   		temperatureFormProperty.setName("temperature");
   		temperatureFormProperty.setType("String");
   		temperatureFormProperty.setReadable(true);
   		temperatureFormProperty.setValue(paramedicalExaminationRequest.getTemperature());
   		formProperties.add(temperatureFormProperty);
   		
   		submitFormRequest.setProperties(formProperties);
   		formsApi.submitForm(submitFormRequest);
		
	}

	@Override
	public void collectPrescriptionInfo(String taskId, List<PrescriptionRequest> prescriptionRequest) {
		log .info("into ====================collectPrescriptionInfo()");
		/*
		 * List<RestFormProperty>formProperties=new ArrayList<RestFormProperty>();
		 * SubmitFormRequest submitFormRequest = new SubmitFormRequest();
		 * submitFormRequest.setAction("completed");
		 * submitFormRequest.setTaskId(taskId);
		 * 
		 * RestFormProperty drugFormProperty = new RestFormProperty();
		 * drugFormProperty.setId("drug"); drugFormProperty.setName("drug");
		 * drugFormProperty.setType("String"); drugFormProperty.setReadable(true);
		 * drugFormProperty.setWritable(true);
		 * drugFormProperty.setValue(prescriptionRequest.getDrug());
		 * formProperties.add(drugFormProperty);
		 * System.out.println("================================"+drugFormProperty.
		 * getValue()+"====================================");
		 * 
		 * 
		 * RestFormProperty doseFormProperty = new RestFormProperty();
		 * doseFormProperty.setId("dose"); doseFormProperty.setName("dose");
		 * doseFormProperty.setType("String"); doseFormProperty.setReadable(true);
		 * drugFormProperty.setWritable(true);
		 * doseFormProperty.setValue(prescriptionRequest.getDose());
		 * formProperties.add(doseFormProperty);
		 * 
		 * RestFormProperty frequencyFormProperty = new RestFormProperty();
		 * frequencyFormProperty.setId("frequency");
		 * frequencyFormProperty.setName("frequency");
		 * frequencyFormProperty.setType("String");
		 * frequencyFormProperty.setReadable(true); drugFormProperty.setWritable(true);
		 * frequencyFormProperty.setValue(prescriptionRequest.getFrequency());
		 * formProperties.add(frequencyFormProperty);
		 * 
		 * RestFormProperty periodFormProperty = new RestFormProperty();
		 * periodFormProperty.setId("period"); periodFormProperty.setName("period");
		 * periodFormProperty.setType("String"); periodFormProperty.setReadable(true);
		 * drugFormProperty.setWritable(true);
		 * periodFormProperty.setValue(prescriptionRequest.getPeriod());
		 * formProperties.add(periodFormProperty);
		 */
   		
   		//System.out.println("/////////////////////////"+periodFormProperty.getValue()+"////////////////////////////////////////");
   		//System.out.println("***********************"+prescriptionRequest.toString()+"*****************");
		
		TaskActionRequest taskActionRequest = new TaskActionRequest();
		taskActionRequest.setAction("complete");
		List<RestVariable> variables = new ArrayList<RestVariable>();

		RestVariable prescriptionRequestVariable = new RestVariable();
		prescriptionRequestVariable.setName("PrescriptionRequest");
		prescriptionRequestVariable.setScope("global");
		prescriptionRequestVariable.setValue(prescriptionRequest);
		variables.add(prescriptionRequestVariable);
		
		System.out.println("////////////////////////////////////////////////"+variables.size());
		System.out.println("///////////////////////////////////////////////"+variables.get(0).getValue());
		
		
		  List<PrescriptionRequest>request=new ArrayList<PrescriptionRequest>();
		//  request.add(prescriptionRequest); 
		  
		  request=(List<PrescriptionRequest>) variables.get(0).getValue();
		  setPrescriptionRequestList(request);
		  try {
			getPrescriptionAndUploadToAlfresco() ;
		} catch (JRException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  
		
		/*
		 * try { createPrescriptionReport(prescriptionRequest); } catch (JRException e)
		 * { e.printStackTrace();
		 * 
		 * }
		 */
		 
		 
   		//saveToRepo(formProperties);
   		//prescriptionRepo.save(prescriptionRequest);
   		//submitFormRequest.setProperties(formProperties);
   		//formsApi.submitForm(submitFormRequest);
   		
   		tasksApi.executeTaskAction(taskId, taskActionRequest);
	}


	
	  /**
	    * Get Report.
	    *    save to alfresco
	    * @return resource
	    * @throws JRException
	    */
	
	 public Resource getPrescriptionAndUploadToAlfresco() throws JRException {
		   
	      log.debug("Request to pdf of all products");

	      JasperReport jr = JasperCompileManager.compileReport("prescription_2.jrxml");

	      JRBeanCollectionDataSource datasource = new JRBeanCollectionDataSource(ConsultationCommandServiceImpl.getPrescriptionRequestList());
	      
	     
	      //Preparing parameters
	     Map<String, Object> parameters = new HashMap<String, Object>();
	     parameters.put("prescription", jr);

	     
	     JasperPrint jp = JasperFillManager.fillReport(jr, parameters, datasource);

 Resource resource=new ByteArrayResource(JasperExportManager.exportReportToPdf(jp));
	     
 			upload(resource);
	     return resource;
	    
       }    
	
	
	
	
	/*
	 * @Override public void createPrescriptionReport() { // TODO Auto-generated
	 * method stub
	 * 
	 * }
	 * 
	 * 
	 * 
	 * @Override public void createPrescriptionReportAndSave() { // TODO
	 * Auto-generated method stub //upload to alfesco after creating report }
	 */
	
	public void getPatientDMSID(Long id) {
		
		String siteName = patientResourceApi.getPatientUsingGET(id).getBody().getDmsId();
		
		System.out.println(siteName);
	}
	
	
	@Override
	public String upload(Resource resource) {
		System.out.println("+++++++++++++success+++++++++++++");

		

		
		/*
		 * try { resource = new ByteArrayResource(file.getBytes()); } catch (IOException
		 * e) { e.printStackTrace(); }
		 */

		/*
		 * PatientDTO patient = null;; String siteName= patient.getDmsId();
		 * System.out.println(
		 * "----------------------------------------------------------"+siteName);
		 */
		String name = "doc"+".pdf";
		NodeBodyCreate nodeBodyCreate = new NodeBodyCreate();
		nodeBodyCreate.setName(name);
		nodeBodyCreate.setNodeType("cm:content");
		//http://127.0.0.1:8013/share/page/site/dsite/documentlibrary
		nodeBodyCreate.setRelativePath("Sites/pat/documentlibrary");
		//nodeBodyCreate.setRelativePath("Sites/"+siteName+"/documentlibrary");
		//nodeBodyCreate.setRelativePath("Signed Documents");
		NodeEntry nodeEntry = nodesApi.createNode("-my-", nodeBodyCreate, true, null, null).getBody();
		nodesApi.updateNodeContent(nodeEntry.getEntry().getId(), resource, true, null, null, null, null);
		System.out.println("*****************************"+nodeEntry.getEntry().getId());
		nodeBodyCreate.getRelativePath();
		System.out.println("*********************************"+nodeBodyCreate.getRelativePath());
		//digitalSigningUploadApi.signDocument(upload);
		
		String docName = "workspace://SpacesStore/"+nodeEntry.getEntry().getId();
		
		SigningCredentials s= new SigningCredentials();
		s.setDocument(docName);
		s.setPassword("123456");
		
		sign(s);
	
		
		return "successfully uploaded";
	}
	
	
	
	
	public String sign(@RequestBody SigningCredentials signingCredentials) {
		
		
		System.out.println("/////////////////////"+signingCredentials.getDocument()+"/////////////////////////////");
		System.out.println("/////////////////////"+signingCredentials.getPassword()+"/////////////////////////////");
		
		digitalSigningApi.signDocument(signingCredentials);
		System.out.println(signingCredentials.getDocument());
		System.out.println(signingCredentials.getPassword());
		System.out.println("+++++++++++++success+++++++++++++");
		
		return "successfully signed";
		
	}



	@Override
	public void createSite(SiteBodyCreate siteBodyCreate) {
		System.out.println("+++++++++++++success++++++++++");
		sitesApi.createSite(siteBodyCreate, false, false, new ArrayList<>()).getBody();
		
	}
	
	

/*public void load(List<RestFormProperty> formProperties){
	sysout
}*/

	
		
/*List<PrescriptionRequest> list = repo.findAll();
		
		JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(list);

		JasperReport jasperReport = JasperCompileManager.compileReport("E:\\JASPER\\JaspersoftWorkspace\\MyReports\\Invoice.jrxml");
		System.out.println("************invoice template found********");
		Map<String, Object> parameters = new HashMap<String, Object>();

		JasperPrint jasperPrint = null;
		try {
			jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, beanColDataSource);

		} catch (JRException e1) {

			e1.printStackTrace();
		}

		File outDir = new File("E:\\JASPER\\jasperOutput\\test");
		outDir.mkdirs();

		try {
			JasperExportManager.exportReportToPdfFile(jasperPrint, "E:\\JASPER\\jasperOutput\\test\\Product.pdf");
			
			//Resource resource=new ByteArrayResource(JasperExportManager.exportReportToPdf(jasperPrint));
			
			//System.out.println("************"+resource+"**********");
			
		} catch (JRException e) {

			e.printStackTrace();
		}

		System.out.println("Done!");
		*/
		
	//@Override
	
	

	public static List<PrescriptionRequest> getPrescriptionRequestList() {
		return prescriptionRequestList;
	}

	public static void setPrescriptionRequestList(List<PrescriptionRequest> prescriptionRequestList) {
		ConsultationCommandServiceImpl.prescriptionRequestList = prescriptionRequestList;
	}
	
	
	
	/*
	 * public void createPrescriptionReport(PrescriptionRequest
	 * prescriptionRequest)throws JRException {
	 * 
	 * log.info("REST request to create pdf**********");
	 * 
	 * 
	 * List<PrescriptionRequest> list = new ArrayList<PrescriptionRequest>();
	 * 
	 * //list=formProperties;
	 * log.info("*****"+prescriptionRequest.toString()+"***********");
	 * 
	 * // System.out.println(formProperties.get(1).getValue()); JasperReport jr =
	 * JasperCompileManager.compileReport("prescription_2.jrxml");
	 * 
	 * JRBeanCollectionDataSource customerJRBean = new
	 * JRBeanCollectionDataSource(list); Map<String, Object> parameters = new
	 * HashMap<String, Object>(); parameters.put("CustomerData", customerJRBean);
	 * 
	 * JasperPrint jp = JasperFillManager.fillReport(jr, parameters,
	 * customerJRBean); JasperExportManager.exportReportToPdfFile(jp, "Blank1.pdf");
	 * 
	 * 
	 * 
	 * }
	 */
	  public void saveToRepo(List<RestFormProperty> formProperties) {
		  System.out.println("----------------into sateToRepo");
		  List<RestFormProperty> list = new ArrayList<RestFormProperty>();
		  for(RestFormProperty properties : list) {
			  System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++"+properties.getValue());
		  }
		  
	  }



	@Override
	public void createPrescriptionReport(List<RestFormProperty> formProperties) throws JRException {
		// TODO Auto-generated method stub
		
	}



	@Override
	public String uploadFile(@RequestBody MultipartFile file) {
		
		log.info("************************into upload file in impl****************");

		

Resource resource =null;
		
		try {
			resource = new ByteArrayResource(file.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
		 

		/*
		 * PatientDTO patient = null;; String siteName= patient.getDmsId();
		 * System.out.println(
		 * "----------------------------------------------------------"+siteName);
		 */
		String name = "document"+".pdf";
		NodeBodyCreate nodeBodyCreate = new NodeBodyCreate();
		nodeBodyCreate.setName(name);
		nodeBodyCreate.setNodeType("cm:content");
		//http://127.0.0.1:8013/share/page/site/dsite/documentlibrary
		nodeBodyCreate.setRelativePath("Sites/pat/documentlibrary");
		//nodeBodyCreate.setRelativePath("Sites/"+siteName+"/documentlibrary");
		//nodeBodyCreate.setRelativePath("Signed Documents");
		NodeEntry nodeEntry = nodesApi.createNode("-my-", nodeBodyCreate, true, null, null).getBody();
		nodesApi.updateNodeContent(nodeEntry.getEntry().getId(), resource, true, null, null, null, null);
		System.out.println("*****************************"+nodeEntry.getEntry().getId());
		nodeBodyCreate.getRelativePath();
		System.out.println("*********************************"+nodeBodyCreate.getRelativePath());
		//digitalSigningUploadApi.signDocument(upload);
		
		String docName = "workspace://SpacesStore/"+nodeEntry.getEntry().getId();
		
		SigningCredentials s= new SigningCredentials();
		s.setDocument(docName);
		s.setPassword("qwertyui");
		
		sign(s);
	
		
		return "successfully uploaded";
		
	}

	  
	  

}
