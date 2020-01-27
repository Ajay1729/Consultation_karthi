package com.bytatech.ayoos.repository;

import com.bytatech.ayoos.client.activiti_rest_api.model.consultation.PrescriptionRequest;
import com.bytatech.ayoos.domain.Prescription;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Prescription entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {

	void save(PrescriptionRequest prescriptionRequest);

}
