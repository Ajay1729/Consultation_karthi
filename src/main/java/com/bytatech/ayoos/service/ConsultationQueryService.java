package com.bytatech.ayoos.service;

import java.util.List;

import javax.validation.Valid;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

import com.bytatech.ayoos.client.activiti_rest_api.model.DataResponse;
import com.bytatech.ayoos.client.activiti_rest_api.model.HistoricProcessInstanceResponse;
import com.bytatech.ayoos.client.activiti_rest_api.model.HistoricTaskInstanceQueryRequest;
import com.bytatech.ayoos.client.activiti_rest_api.model.RestVariable;
import com.bytatech.ayoos.client.activiti_rest_api.model.TaskResponse;
import com.bytatech.ayoos.client.activiti_rest_api.model.consultation.ConsultationDetails;
import com.bytatech.ayoos.client.activiti_rest_api.model.consultation.PrescriptionRequest;

import net.sf.jasperreports.engine.JRException;

public interface ConsultationQueryService {

	ResponseEntity<DataResponse> getTasks(String name, String nameLike, String description, String priority,
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
			@Valid String candidateOrAssigned, @Valid String category);

	ResponseEntity<TaskResponse> getTask(String taskId);

	ResponseEntity<RestVariable> getTaskInstanceVariable(String taskId, String variableName, String scope);

	ResponseEntity<List<RestVariable>> getTaskVariables(String taskId);

	ResponseEntity<DataResponse> getHistoricTask(HistoricTaskInstanceQueryRequest request);

	ResponseEntity<DataResponse> getTaskFormProperties(String id, String processInstanceId, String executionId,
			String activityInstanceId, String taskId, Boolean selectOnlyFormProperties,
			Boolean selectOnlyVariableUpdates);

	

	ConsultationDetails getConsultationDetails(String processInstanceId);

	ResponseEntity<HistoricProcessInstanceResponse> getHistoricProcessInstances(String processInstanceId);

	//PrescriptionRequest getPrescriptionDetails(String processInstanceId);
		
	
	
	byte[] getPrescriptionAsPdfDowload()throws JRException;

	

}
