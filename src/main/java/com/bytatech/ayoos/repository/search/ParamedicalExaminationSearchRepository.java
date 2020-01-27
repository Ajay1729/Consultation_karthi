package com.bytatech.ayoos.repository.search;

import com.bytatech.ayoos.domain.ParamedicalExamination;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the ParamedicalExamination entity.
 */
public interface ParamedicalExaminationSearchRepository extends ElasticsearchRepository<ParamedicalExamination, Long> {
}
