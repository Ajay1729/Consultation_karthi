package com.bytatech.ayoos.service.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;
import javax.validation.Valid;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.lang.SerializationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;


import com.bytatech.ayoos.client.activiti_rest_api.api.FormsApi;
import com.bytatech.ayoos.client.activiti_rest_api.api.HistoryApi;
import com.bytatech.ayoos.client.activiti_rest_api.api.TasksApi;
import com.bytatech.ayoos.client.activiti_rest_api.model.DataResponse;
import com.bytatech.ayoos.client.activiti_rest_api.model.FormDataResponse;
import com.bytatech.ayoos.client.activiti_rest_api.model.HistoricProcessInstanceResponse;
import com.bytatech.ayoos.client.activiti_rest_api.model.HistoricTaskInstanceQueryRequest;
import com.bytatech.ayoos.client.activiti_rest_api.model.RestVariable;
import com.bytatech.ayoos.client.activiti_rest_api.model.TaskResponse;
import com.bytatech.ayoos.client.activiti_rest_api.model.consultation.ConsultationDetails;
import com.bytatech.ayoos.client.activiti_rest_api.model.consultation.PrescriptionRequest;
import com.bytatech.ayoos.repository.ConsultationRepository;
import com.bytatech.ayoos.repository.search.ConsultationSearchRepository;
import com.bytatech.ayoos.service.ConsultationQueryService;
import com.bytatech.ayoos.service.mapper.ConsultationMapper;
import com.bytatech.ayoos.web.rest.ConsultationCommandResource;

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
@SuppressWarnings("unchecked")
public class ConsultationQueryServiceImpl implements ConsultationQueryService{

	 private final Logger log = LoggerFactory.getLogger(ConsultationQueryServiceImpl.class);
	    
	    @Autowired
		private TasksApi tasksApi;
	    
	    @Autowired
	    private FormsApi formsApi;
	    
	    @Autowired
	    private HistoryApi historyApi;
	    @Autowired
	    private ConsultationCommandResource commandResource; 
	    
	    private final ConsultationRepository consultationRepository;

	    private final ConsultationMapper consultationMapper;

	    private final ConsultationSearchRepository consultationSearchRepository;
	    	
	
	public ConsultationQueryServiceImpl(ConsultationRepository consultationRepository,
				ConsultationMapper consultationMapper, ConsultationSearchRepository consultationSearchRepository) {
			
			this.consultationRepository = consultationRepository;
			this.consultationMapper = consultationMapper;
			this.consultationSearchRepository = consultationSearchRepository;
		}

	@Override
	public ResponseEntity<DataResponse> getTasks(String name, String nameLike, String description, String priority,
			String minimumPriority, String maximumPriority, String assignee, String assigneeLike, String owner,
			String ownerLike, String unassigned, String delegationState, String candidateUser, String candidateGroup,
			String candidateGroups, String involvedUser, String taskDefinitionKey, String taskDefinitionKeyLike,
			String processInstanceId, String processInstanceBusinessKey, String processInstanceBusinessKeyLike,
			@Valid String processDefinitionId, @Valid String processDefinitionKey,
			@Valid String processDefinitionKeyLike, @Valid String processDefinitionName,
			@Valid String processDefinitionNameLike, @Valid String executionId, @Valid String createdOn,
			@Valid String createdBefore, @Valid String createdAfter, @Valid String dueOn, @Valid String dueBefore,
			@Valid String dueAfter, @Valid Boolean withoutDueDate, @Valid Boolean excludeSubTasks,
			@Valid Boolean active, @Valid Boolean includeTaskLocalVariables, @Valid Boolean includeProcessVariables,
			@Valid String tenantId, @Valid String tenantIdLike, @Valid Boolean withoutTenantId,
			@Valid String candidateOrAssigned, @Valid String category) {
		System.out.println("******************in tasks impl///////////////////////////////////////////");
		return tasksApi.getTasks( name,nameLike, description, priority, minimumPriority, maximumPriority, assignee, assigneeLike, owner, ownerLike, unassigned, delegationState, candidateUser, candidateGroup, candidateGroups, involvedUser, taskDefinitionKey, taskDefinitionKeyLike, processInstanceId, processInstanceBusinessKey, processInstanceBusinessKeyLike, processDefinitionId, processDefinitionKey, processDefinitionKeyLike, processDefinitionName, processDefinitionNameLike, executionId, createdOn, createdBefore, createdAfter, dueOn, dueBefore, dueAfter, withoutDueDate, excludeSubTasks, active, includeTaskLocalVariables, includeProcessVariables, tenantId, tenantIdLike, withoutTenantId, candidateOrAssigned, category);
	}

	@Override
	public ResponseEntity<TaskResponse> getTask(String taskId) {
		 return tasksApi.getTask(taskId);
	}

	@Override
	public ResponseEntity<RestVariable> getTaskInstanceVariable(String taskId, String variableName, String scope) {
		
		return tasksApi.getTaskInstanceVariable(taskId, variableName, scope);
	}

	@Override
	public ResponseEntity<List<RestVariable>> getTaskVariables(String taskId) {
		
		return tasksApi.listTaskVariables(taskId);
	}

	@Override
	public ResponseEntity<DataResponse> getHistoricTask(HistoricTaskInstanceQueryRequest request) {
		
		return historyApi.queryHistoricTaskInstance(request);
	}

	@Override
	public ResponseEntity<DataResponse> getTaskFormProperties(String id, String processInstanceId, String executionId,
			String activityInstanceId, String taskId, Boolean selectOnlyFormProperties,
			Boolean selectOnlyVariableUpdates) {
		
		return historyApi.getHistoricDetailInfo(id, processInstanceId, executionId, activityInstanceId, taskId, selectOnlyFormProperties, selectOnlyVariableUpdates);
	}

	@Override
	public ConsultationDetails getConsultationDetails(String processInstanceId) {
	
		ResponseEntity<List<byte[]>> response = historyApi.getHistoricProcessInstanceVariableData(processInstanceId,
				"collect informations");
		
		LinkedHashMap<Object, Object> consultationDetailsMap = (LinkedHashMap<Object, Object>) SerializationUtils
				.deserialize((InputStream) response.getBody());
		
		ConsultationDetails consultationDetails = new ConsultationDetails();
		consultationDetails.setPatientId((String) consultationDetailsMap.get("patientId"));
		consultationDetails.setToken((String) consultationDetailsMap.get("token"));
		consultationDetails.setEmail((String) consultationDetailsMap.get("email"));
		
		
		return consultationDetails;
	}

	@Override
	public  ResponseEntity<HistoricProcessInstanceResponse> getHistoricProcessInstances(String processInstanceId) {
		return historyApi.getHistoricProcessInstance(processInstanceId);
		
	}

	/*
	 * @Override public PrescriptionRequest getPrescriptionDetails(String
	 * processInstanceId) {
	 * 
	 * List<LinkedHashMap<String, String>> prescriptionInfoResponse =
	 * (List<LinkedHashMap<String, String>>)
	 * getHistoricTaskusingProcessInstanceIdAndName( processInstanceId,
	 * "Prescription").getBody().getData(); String taskId =
	 * prescriptionInfoResponse.get(0).get("id");
	 * System.out.println("Task id for Choose Doctor Is "+taskId);
	 * 
	 * ResponseEntity<List<byte[]>> response =
	 * historyApi.getHistoricTaskInstanceVariableData(taskId,
	 * "prescription","global"); LinkedHashMap<Object, Object>
	 * prescriptionDetailsMap = (LinkedHashMap<Object, Object>) SerializationUtils
	 * .deserialize((InputStream) response.getBody());
	 * 
	 * PrescriptionRequest prescriptionRequest = new PrescriptionRequest();
	 * prescriptionRequest.setDose((String) prescriptionDetailsMap.get("dose"));
	 * prescriptionRequest.setDrug((String) prescriptionDetailsMap.get("drug"));
	 * prescriptionRequest.setFrequency((String)
	 * prescriptionDetailsMap.get("frequency"));
	 * prescriptionRequest.setPeriod((String) prescriptionDetailsMap.get("period"));
	 * 
	 * return prescriptionRequest; }
	 */
	/*
	 * @Override public PrescriptionRequest getPrescriptionDetails(String
	 * processInstanceId) {
	 * 
	 * PrescriptionRequest prescriptionRequest = new PrescriptionRequest();
	 * 
	 * List<LinkedHashMap<String, String>> taskResponsePrescription =
	 * (List<LinkedHashMap<String, String>>)
	 * getHistoricTaskusingProcessInstanceIdAndName( processInstanceId,
	 * "Prescription").getBody().getData();
	 * 
	 * Long taskId=taskResponsePrescription.stream().mapToLong(obj ->
	 * Long.parseLong(obj.get("id"))).max().getAsLong();
	 * 
	 * ResponseEntity<DataResponse> prescriptionDetails =
	 * historyApi.getHistoricDetailInfo(null, processInstanceId, null, null,
	 * taskId.toString(), true, false);
	 * 
	 * List<LinkedHashMap<String, String>> prescriptionFormProperties =
	 * (List<LinkedHashMap<String, String>>) prescriptionDetails
	 * .getBody().getData();
	 * 
	 * System.out.println("******************************"+taskResponsePrescription.
	 * size()); System.out.println("******************************"+taskId);
	 * 
	 * for(LinkedHashMap<String, String> prescriptionMap :
	 * prescriptionFormProperties) { String drug = null; String dose = null; String
	 * frequency = null; String period = null;
	 * 
	 * String propertyId = prescriptionMap.get("propertyId");
	 * //System.out.println("***********"+propertyId); if
	 * (propertyId.equals("drug")) { drug = prescriptionMap.get("propertyValue");
	 * System.out.println("***********"+drug); prescriptionRequest.setDrug(drug); }
	 * //System.out.println("***---------------*************--------"+drug); }
	 * 
	 * return prescriptionRequest;
	 * 
	 * 
	 * }
	 */
	public ResponseEntity<DataResponse> getHistoricTaskusingProcessInstanceIdAndName(String processInstanceId,
			String name) {
		return historyApi.listHistoricTaskInstances(null, processInstanceId, null, null, null, null, null, null, null,
				null, null, name, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);

	}
	


	   
	   
	   //download as pdf
	@Override
	public byte[] getPrescriptionAsPdfDowload() throws JRException {
	
		   
		      log.debug("Request to pdf of all products");

		      JasperReport jr = JasperCompileManager.compileReport("prescription_2.jrxml");

		      JRBeanCollectionDataSource datasource = new JRBeanCollectionDataSource(ConsultationCommandServiceImpl.getPrescriptionRequestList());
		      
		     
		      //Preparing parameters
		     Map<String, Object> parameters = new HashMap<String, Object>();
		     parameters.put("prescription", jr);

		     
		     JasperPrint jp = JasperFillManager.fillReport(jr, parameters, datasource);

		     //JasperExportManager.exportReportToPdfFile(jp, "UserNeeds.pdf");
		     //byte[] file;
		     
		   
			/*
			 * BASE64DecodedMultipartFile file = new
			 * BASE64DecodedMultipartFile(JasperExportManager.exportReportToPdf(jp));
			 * 
			 * File f =new File("/home/karthi/Desktop/Doc6.pdf");
			 * 
			 * System.out.println("*******************************************************"+
			 * file.getSize()); try {
			 * 
			 * file.transferTo(f); System.out.println(f.length()); } catch
			 * (IllegalStateException e) { // TODO Auto-generated catch block
			 * e.printStackTrace(); } catch (IOException e) { // TODO Auto-generated catch
			 * block e.printStackTrace(); }
			 */ //file= JasperExportManager.exportReportToPdf(jp);
		     
			
			/*
			 * DiskFileItem fileItem = new DiskFileItem("f", "application/pdf", true,
			 * f.getName(), (int) f.length() , f.getParentFile());
			 * System.out.println("///////////////////////////////////////////////"+fileItem
			 * .getSize()); try { fileItem.getOutputStream(); } catch (IOException e) {
			 * e.printStackTrace(); } MultipartFile multipartFile = new
			 * CommonsMultipartFile(fileItem);
			 * System.out.println("-------------------------------------------------------"+
			 * multipartFile.getSize());
			 *///commandResource.upload(multipartFile);
		     

		    return JasperExportManager.exportReportToPdf(jp);
	         
	}

	


}
