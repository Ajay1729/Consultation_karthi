package com.bytatech.ayoos.client.patient_service.api;

import org.springframework.cloud.openfeign.FeignClient;

import com.bytatech.ayoos.client.patient_service.PatientClientConfiguration;

@FeignClient(name="${patientService.name:patientService}", url="${patientService.url:35.243.137.225:8081/}", configuration = PatientClientConfiguration.class)
public interface CommandResourceApiClient extends CommandResourceApi {
}