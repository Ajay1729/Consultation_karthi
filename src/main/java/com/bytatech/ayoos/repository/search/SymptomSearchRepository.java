package com.bytatech.ayoos.repository.search;

import com.bytatech.ayoos.domain.Symptom;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Symptom entity.
 */
public interface SymptomSearchRepository extends ElasticsearchRepository<Symptom, Long> {
}
