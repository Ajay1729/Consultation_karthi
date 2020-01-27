package com.bytatech.ayoos.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of DiagnosisSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class DiagnosisSearchRepositoryMockConfiguration {

    @MockBean
    private DiagnosisSearchRepository mockDiagnosisSearchRepository;

}
