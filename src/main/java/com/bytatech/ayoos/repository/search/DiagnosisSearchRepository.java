package com.bytatech.ayoos.repository.search;

import com.bytatech.ayoos.domain.Diagnosis;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Diagnosis entity.
 */
public interface DiagnosisSearchRepository extends ElasticsearchRepository<Diagnosis, Long> {
}
