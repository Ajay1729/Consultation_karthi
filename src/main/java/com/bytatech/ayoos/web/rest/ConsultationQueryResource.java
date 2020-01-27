package com.bytatech.ayoos.web.rest;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import com.bytatech.ayoos.client.activiti_rest_api.model.DataResponse;
import com.bytatech.ayoos.client.activiti_rest_api.model.HistoricProcessInstanceResponse;
import com.bytatech.ayoos.client.activiti_rest_api.model.HistoricTaskInstanceQueryRequest;
import com.bytatech.ayoos.client.activiti_rest_api.model.ProcessInstanceResponse;
import com.bytatech.ayoos.client.activiti_rest_api.model.RestVariable;
import com.bytatech.ayoos.client.activiti_rest_api.model.TaskResponse;
import com.bytatech.ayoos.client.activiti_rest_api.model.consultation.ConsultationDetails;
import com.bytatech.ayoos.client.activiti_rest_api.model.consultation.PrescriptionRequest;
import com.bytatech.ayoos.service.ConsultationQueryService;

import io.swagger.annotations.ApiParam;
import net.sf.jasperreports.engine.JRException;

/**
 * REST controller for managing MedicalSummary.
 */
@RestController
@RequestMapping("/api/query")
public class ConsultationQueryResource {

	private final Logger log = LoggerFactory.getLogger(ConsultationQueryResource.class);

    private static final String ENTITY_NAME = "consultation";
    
    private final ConsultationQueryService consultationQueryService;

	public ConsultationQueryResource(ConsultationQueryService medicalSummaryQueryService) {
	
		this.consultationQueryService = medicalSummaryQueryService;
	}
	
	@GetMapping("/tasks")
	   public ResponseEntity<DataResponse> getTasks(@RequestParam(value = "name", required = false) String name,
			   @RequestParam(value = "nameLike", required = false) String nameLike, 
			   @RequestParam(value = "description", required = false) String description, 
			   @RequestParam(value = "priority", required = false) String priority,
			   @RequestParam(value = "minimumPriority", required = false) String minimumPriority, 
			   @RequestParam(value = "maximumPriority", required = false) String maximumPriority, 
			   @RequestParam(value = "assignee", required = false) String assignee,
			   @RequestParam(value = "assigneeLike", required = false) String assigneeLike,
			   @RequestParam(value = "owner", required = false) String owner,
			   @RequestParam(value = "ownerLike", required = false) String ownerLike,
			   @RequestParam(value = "unassigned", required = false) String unassigned,
			   @RequestParam(value = "delegationState", required = false) String delegationState,
			   @RequestParam(value = "candidateUser", required = false) String candidateUser, 
			   @RequestParam(value = "candidateGroup", required = false) String candidateGroup, 
			   @RequestParam(value = "candidateGroups", required = false) String candidateGroups,
			   @RequestParam(value = "involvedUser", required = false) String involvedUser, 
			   @RequestParam(value = "taskDefinitionKey", required = false) String taskDefinitionKey, 
			   @RequestParam(value = "taskDefinitionKeyLike", required = false) String taskDefinitionKeyLike, 
			   @RequestParam(value = "processInstanceId", required = false) String processInstanceId, 
			   @RequestParam(value = "processInstanceBusinessKey", required = false) String processInstanceBusinessKey, 
			   @RequestParam(value = "processInstanceBusinessKeyLike", required = false) String processInstanceBusinessKeyLike,
			   @ApiParam(value = "Only return tasks which are part of a process instance which has a process definition with the given id.") @Valid @RequestParam(value = "processDefinitionId", required = false) String processDefinitionId,
			   @ApiParam(value = "Only return tasks which are part of a process instance which has a process definition with the given key.") @Valid @RequestParam(value = "processDefinitionKey", required = false) String processDefinitionKey,
			   @ApiParam(value = "Only return tasks which are part of a process instance which has a process definition with a key like the given value.") @Valid @RequestParam(value = "processDefinitionKeyLike", required = false) String processDefinitionKeyLike,
			   @ApiParam(value = "Only return tasks which are part of a process instance which has a process definition with the given name.") @Valid @RequestParam(value = "processDefinitionName", required = false) String processDefinitionName,
			   @ApiParam(value = "Only return tasks which are part of a process instance which has a process definition with a name like the given value.") @Valid @RequestParam(value = "processDefinitionNameLike", required = false) String processDefinitionNameLike,
			   @ApiParam(value = "Only return tasks which are part of the execution with the given id.") @Valid @RequestParam(value = "executionId", required = false) String executionId,
			   @ApiParam(value = "Only return tasks which are created on the given date.") @Valid @RequestParam(value = "createdOn", required = false) String createdOn,
			   @ApiParam(value = "Only return tasks which are created before the given date.") @Valid @RequestParam(value = "createdBefore", required = false) String createdBefore,
			   @ApiParam(value = "Only return tasks which are created after the given date.") @Valid @RequestParam(value = "createdAfter", required = false) String createdAfter,
			   @ApiParam(value = "Only return tasks which are due on the given date.") @Valid @RequestParam(value = "dueOn", required = false) String dueOn,
			   @ApiParam(value = "Only return tasks which are due before the given date.") @Valid @RequestParam(value = "dueBefore", required = false) String dueBefore,
			   @ApiParam(value = "Only return tasks which are due after the given date.") @Valid @RequestParam(value = "dueAfter", required = false) String dueAfter,
			   @ApiParam(value = "Only return tasks which don�t have a due date. The property is ignored if the value is false.") @Valid @RequestParam(value = "withoutDueDate", required = false) Boolean withoutDueDate,
			   @ApiParam(value = "Only return tasks that are not a subtask of another task.") @Valid @RequestParam(value = "excludeSubTasks", required = false) Boolean excludeSubTasks,
			   @ApiParam(value = "If true, only return tasks that are not suspended (either part of a process that is not suspended or not part of a process at all). If false, only tasks that are part of suspended process instances are returned.") @Valid @RequestParam(value = "active", required = false) Boolean active,
			   @ApiParam(value = "Indication to include task local variables in the result.") @Valid @RequestParam(value = "includeTaskLocalVariables", required = false) Boolean includeTaskLocalVariables,
			   @ApiParam(value = "Indication to include process variables in the result.") @Valid @RequestParam(value = "includeProcessVariables", required = false) Boolean includeProcessVariables,
			   @ApiParam(value = "Only return tasks with the given tenantId.") @Valid @RequestParam(value = "tenantId", required = false) String tenantId,
			   @ApiParam(value = "Only return tasks with a tenantId like the given value.") @Valid @RequestParam(value = "tenantIdLike", required = false) String tenantIdLike,
			   @ApiParam(value = "If true, only returns tasks without a tenantId set. If false, the withoutTenantId parameter is ignored.") @Valid @RequestParam(value = "withoutTenantId", required = false) Boolean withoutTenantId,
			   @ApiParam(value = "Select tasks that has been claimed or assigned to user or waiting to claim by user (candidate user or groups).") @Valid @RequestParam(value = "candidateOrAssigned", required = false) String candidateOrAssigned,
			   @ApiParam(value = "Select tasks with the given category. Note that this is the task category, not the category of the process definition (namespace within the BPMN Xml). ") @Valid @RequestParam(value = "category", required = false) String category){
			
			return consultationQueryService.getTasks(name, nameLike, description, priority, minimumPriority, maximumPriority, assignee, assigneeLike, 
					owner, ownerLike, unassigned, delegationState, candidateUser, candidateGroup, candidateGroups, involvedUser, taskDefinitionKey, 
					taskDefinitionKeyLike, processInstanceId, processInstanceBusinessKey, processInstanceBusinessKeyLike, processDefinitionId, 
					processDefinitionKey, processDefinitionKeyLike, processDefinitionName, processDefinitionNameLike, executionId, createdOn, 
					createdBefore, createdAfter, dueOn, dueBefore, dueAfter, withoutDueDate, excludeSubTasks, active, includeTaskLocalVariables, 
					includeProcessVariables, tenantId, tenantIdLike, withoutTenantId, candidateOrAssigned, category);
		}
	
	

	@GetMapping("/tasks/{taskId}")
	public ResponseEntity<TaskResponse> getTask(@PathVariable String taskId){
		return consultationQueryService.getTask(taskId);
	}

	
	
	@GetMapping("/tasks/{taskId}/{variableName}")
	public ResponseEntity<RestVariable> getTaskInstanceVariable(@PathVariable String taskId,@PathVariable String variableName,
			@RequestParam(required=false) String scope){
		
		return consultationQueryService.getTaskInstanceVariable(taskId,variableName,scope);
		
	}
	
	@GetMapping("/tasks/{taskId}/variables")
	public ResponseEntity<List<RestVariable>> getTaskVariables(@PathVariable String taskId){
		
		return consultationQueryService.getTaskVariables(taskId);
		
	}
	
	

	@PostMapping("/getHistoricTask")
	public ResponseEntity<DataResponse> getHistoricTask(@RequestBody HistoricTaskInstanceQueryRequest request) {
		System.out.println("++++++++++++++++++++++++++++##########" + request.getProcessInstanceId()
				+ "^^^^^^^^^^^^^^^^^^^^^^^^^" + request.getTaskName());
		return consultationQueryService.getHistoricTask(request);
	}
	
	
	
	@GetMapping("/history")
	public ResponseEntity<DataResponse> getTaskFormProperties(@RequestParam(required = false) String id,
			@RequestParam(required = false)String processInstanceId, 
			@RequestParam(required = false)String executionId,@RequestParam(required = false) String activityInstanceId,
			@RequestParam(required = false)String taskId,@RequestParam(required = false)Boolean selectOnlyFormProperties,
			@RequestParam(required = false)Boolean selectOnlyVariableUpdates){ 	
		return  consultationQueryService.getTaskFormProperties(id, processInstanceId, executionId, activityInstanceId, taskId, 
				selectOnlyFormProperties, selectOnlyVariableUpdates);
	}
	

	@GetMapping("/consultation-details/{processInstanceId}")
	public ConsultationDetails getConsultationDetails(@PathVariable String processInstanceId) {

		return consultationQueryService.getConsultationDetails(processInstanceId);
	}
	
	/*
	 * @GetMapping("/prescription-details/{processInstanceId}") public
	 * PrescriptionRequest getPrescriptionDetails(@PathVariable String
	 * processInstanceId) {
	 * 
	 * return
	 * consultationSummaryQueryService.getPrescriptionDetails(processInstanceId); }
	 */
	
	
	@GetMapping("/historic-process-instances/{processInstanceId}")
	public ResponseEntity<HistoricProcessInstanceResponse> getHistoricProcessInstances(String processInstanceId) {
	
		return consultationQueryService.getHistoricProcessInstances(processInstanceId);
	}
	
	
	 /**
	    * GET  /pdf/ProdutsReport : get the pdf of all the products.
	    *  
	    * @return the byte[]
	    * @return the ResponseEntity with status 200 (OK) and the pdf of products in body
	    */
	   
	
	@GetMapping("/pdf/downloadPrescriptionReport")
	public ResponseEntity<byte[]> getPrescriptionAsPdf() {

		log.debug("REST request to get a pdf of products");

		byte[] pdfContents = null;

		try {
			pdfContents = consultationQueryService.getPrescriptionAsPdfDowload();
		} catch (JRException e) {
			e.printStackTrace();
		}
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.parseMediaType("application/pdf"));
		String fileName = "prescriptionReport.pdf";
		headers.add("content-disposition", "attachment; filename=" + fileName);
		ResponseEntity<byte[]> response = new ResponseEntity<byte[]>(pdfContents, headers, HttpStatus.OK);
		return response;

	}
	 

}
