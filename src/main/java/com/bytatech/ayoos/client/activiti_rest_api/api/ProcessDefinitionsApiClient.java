package com.bytatech.ayoos.client.activiti_rest_api.api;

import org.springframework.cloud.openfeign.FeignClient;
import com.bytatech.ayoos.client.activiti_rest_api.ClientConfiguration;

@FeignClient(name="${activitiRestApi.name:activitiRestApi}", url="${activitiRestApi.url}", configuration = ClientConfiguration.class)
public interface ProcessDefinitionsApiClient extends ProcessDefinitionsApi {
}