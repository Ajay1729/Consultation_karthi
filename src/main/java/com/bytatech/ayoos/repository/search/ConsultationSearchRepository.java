package com.bytatech.ayoos.repository.search;

import com.bytatech.ayoos.domain.Consultation;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Consultation entity.
 */
public interface ConsultationSearchRepository extends ElasticsearchRepository<Consultation, Long> {
}
