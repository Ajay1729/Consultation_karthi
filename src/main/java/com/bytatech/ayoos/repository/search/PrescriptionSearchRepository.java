package com.bytatech.ayoos.repository.search;

import com.bytatech.ayoos.domain.Prescription;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Prescription entity.
 */
public interface PrescriptionSearchRepository extends ElasticsearchRepository<Prescription, Long> {
}
