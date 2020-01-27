package com.bytatech.ayoos.web.rest;

import com.bytatech.ayoos.ConsultationApp;

import com.bytatech.ayoos.domain.ParamedicalExamination;
import com.bytatech.ayoos.repository.ParamedicalExaminationRepository;
import com.bytatech.ayoos.repository.search.ParamedicalExaminationSearchRepository;
import com.bytatech.ayoos.service.ParamedicalExaminationService;
import com.bytatech.ayoos.service.dto.ParamedicalExaminationDTO;
import com.bytatech.ayoos.service.mapper.ParamedicalExaminationMapper;
import com.bytatech.ayoos.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;


import static com.bytatech.ayoos.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ParamedicalExaminationResource REST controller.
 *
 * @see ParamedicalExaminationResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ConsultationApp.class)
public class ParamedicalExaminationResourceIntTest {

    private static final String DEFAULT_BP = "AAAAAAAAAA";
    private static final String UPDATED_BP = "BBBBBBBBBB";

    private static final Double DEFAULT_HEIGHT = 1D;
    private static final Double UPDATED_HEIGHT = 2D;

    private static final Double DEFAULT_WEIGHT = 1D;
    private static final Double UPDATED_WEIGHT = 2D;

    private static final Double DEFAULT_TEMPERATURE = 1D;
    private static final Double UPDATED_TEMPERATURE = 2D;

    @Autowired
    private ParamedicalExaminationRepository paramedicalExaminationRepository;

    @Autowired
    private ParamedicalExaminationMapper paramedicalExaminationMapper;

    @Autowired
    private ParamedicalExaminationService paramedicalExaminationService;

    /**
     * This repository is mocked in the com.bytatech.ayoos.repository.search test package.
     *
     * @see com.bytatech.ayoos.repository.search.ParamedicalExaminationSearchRepositoryMockConfiguration
     */
    @Autowired
    private ParamedicalExaminationSearchRepository mockParamedicalExaminationSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restParamedicalExaminationMockMvc;

    private ParamedicalExamination paramedicalExamination;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ParamedicalExaminationResource paramedicalExaminationResource = new ParamedicalExaminationResource(paramedicalExaminationService);
        this.restParamedicalExaminationMockMvc = MockMvcBuilders.standaloneSetup(paramedicalExaminationResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ParamedicalExamination createEntity(EntityManager em) {
        ParamedicalExamination paramedicalExamination = new ParamedicalExamination()
            .bp(DEFAULT_BP)
            .height(DEFAULT_HEIGHT)
            .weight(DEFAULT_WEIGHT)
            .temperature(DEFAULT_TEMPERATURE);
        return paramedicalExamination;
    }

    @Before
    public void initTest() {
        paramedicalExamination = createEntity(em);
    }

    @Test
    @Transactional
    public void createParamedicalExamination() throws Exception {
        int databaseSizeBeforeCreate = paramedicalExaminationRepository.findAll().size();

        // Create the ParamedicalExamination
        ParamedicalExaminationDTO paramedicalExaminationDTO = paramedicalExaminationMapper.toDto(paramedicalExamination);
        restParamedicalExaminationMockMvc.perform(post("/api/paramedical-examinations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(paramedicalExaminationDTO)))
            .andExpect(status().isCreated());

        // Validate the ParamedicalExamination in the database
        List<ParamedicalExamination> paramedicalExaminationList = paramedicalExaminationRepository.findAll();
        assertThat(paramedicalExaminationList).hasSize(databaseSizeBeforeCreate + 1);
        ParamedicalExamination testParamedicalExamination = paramedicalExaminationList.get(paramedicalExaminationList.size() - 1);
        assertThat(testParamedicalExamination.getBp()).isEqualTo(DEFAULT_BP);
        assertThat(testParamedicalExamination.getHeight()).isEqualTo(DEFAULT_HEIGHT);
        assertThat(testParamedicalExamination.getWeight()).isEqualTo(DEFAULT_WEIGHT);
        assertThat(testParamedicalExamination.getTemperature()).isEqualTo(DEFAULT_TEMPERATURE);

        // Validate the ParamedicalExamination in Elasticsearch
        verify(mockParamedicalExaminationSearchRepository, times(1)).save(testParamedicalExamination);
    }

    @Test
    @Transactional
    public void createParamedicalExaminationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = paramedicalExaminationRepository.findAll().size();

        // Create the ParamedicalExamination with an existing ID
        paramedicalExamination.setId(1L);
        ParamedicalExaminationDTO paramedicalExaminationDTO = paramedicalExaminationMapper.toDto(paramedicalExamination);

        // An entity with an existing ID cannot be created, so this API call must fail
        restParamedicalExaminationMockMvc.perform(post("/api/paramedical-examinations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(paramedicalExaminationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ParamedicalExamination in the database
        List<ParamedicalExamination> paramedicalExaminationList = paramedicalExaminationRepository.findAll();
        assertThat(paramedicalExaminationList).hasSize(databaseSizeBeforeCreate);

        // Validate the ParamedicalExamination in Elasticsearch
        verify(mockParamedicalExaminationSearchRepository, times(0)).save(paramedicalExamination);
    }

    @Test
    @Transactional
    public void getAllParamedicalExaminations() throws Exception {
        // Initialize the database
        paramedicalExaminationRepository.saveAndFlush(paramedicalExamination);

        // Get all the paramedicalExaminationList
        restParamedicalExaminationMockMvc.perform(get("/api/paramedical-examinations?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(paramedicalExamination.getId().intValue())))
            .andExpect(jsonPath("$.[*].bp").value(hasItem(DEFAULT_BP.toString())))
            .andExpect(jsonPath("$.[*].height").value(hasItem(DEFAULT_HEIGHT.doubleValue())))
            .andExpect(jsonPath("$.[*].weight").value(hasItem(DEFAULT_WEIGHT.doubleValue())))
            .andExpect(jsonPath("$.[*].temperature").value(hasItem(DEFAULT_TEMPERATURE.doubleValue())));
    }
    
    @Test
    @Transactional
    public void getParamedicalExamination() throws Exception {
        // Initialize the database
        paramedicalExaminationRepository.saveAndFlush(paramedicalExamination);

        // Get the paramedicalExamination
        restParamedicalExaminationMockMvc.perform(get("/api/paramedical-examinations/{id}", paramedicalExamination.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(paramedicalExamination.getId().intValue()))
            .andExpect(jsonPath("$.bp").value(DEFAULT_BP.toString()))
            .andExpect(jsonPath("$.height").value(DEFAULT_HEIGHT.doubleValue()))
            .andExpect(jsonPath("$.weight").value(DEFAULT_WEIGHT.doubleValue()))
            .andExpect(jsonPath("$.temperature").value(DEFAULT_TEMPERATURE.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingParamedicalExamination() throws Exception {
        // Get the paramedicalExamination
        restParamedicalExaminationMockMvc.perform(get("/api/paramedical-examinations/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateParamedicalExamination() throws Exception {
        // Initialize the database
        paramedicalExaminationRepository.saveAndFlush(paramedicalExamination);

        int databaseSizeBeforeUpdate = paramedicalExaminationRepository.findAll().size();

        // Update the paramedicalExamination
        ParamedicalExamination updatedParamedicalExamination = paramedicalExaminationRepository.findById(paramedicalExamination.getId()).get();
        // Disconnect from session so that the updates on updatedParamedicalExamination are not directly saved in db
        em.detach(updatedParamedicalExamination);
        updatedParamedicalExamination
            .bp(UPDATED_BP)
            .height(UPDATED_HEIGHT)
            .weight(UPDATED_WEIGHT)
            .temperature(UPDATED_TEMPERATURE);
        ParamedicalExaminationDTO paramedicalExaminationDTO = paramedicalExaminationMapper.toDto(updatedParamedicalExamination);

        restParamedicalExaminationMockMvc.perform(put("/api/paramedical-examinations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(paramedicalExaminationDTO)))
            .andExpect(status().isOk());

        // Validate the ParamedicalExamination in the database
        List<ParamedicalExamination> paramedicalExaminationList = paramedicalExaminationRepository.findAll();
        assertThat(paramedicalExaminationList).hasSize(databaseSizeBeforeUpdate);
        ParamedicalExamination testParamedicalExamination = paramedicalExaminationList.get(paramedicalExaminationList.size() - 1);
        assertThat(testParamedicalExamination.getBp()).isEqualTo(UPDATED_BP);
        assertThat(testParamedicalExamination.getHeight()).isEqualTo(UPDATED_HEIGHT);
        assertThat(testParamedicalExamination.getWeight()).isEqualTo(UPDATED_WEIGHT);
        assertThat(testParamedicalExamination.getTemperature()).isEqualTo(UPDATED_TEMPERATURE);

        // Validate the ParamedicalExamination in Elasticsearch
        verify(mockParamedicalExaminationSearchRepository, times(1)).save(testParamedicalExamination);
    }

    @Test
    @Transactional
    public void updateNonExistingParamedicalExamination() throws Exception {
        int databaseSizeBeforeUpdate = paramedicalExaminationRepository.findAll().size();

        // Create the ParamedicalExamination
        ParamedicalExaminationDTO paramedicalExaminationDTO = paramedicalExaminationMapper.toDto(paramedicalExamination);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restParamedicalExaminationMockMvc.perform(put("/api/paramedical-examinations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(paramedicalExaminationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ParamedicalExamination in the database
        List<ParamedicalExamination> paramedicalExaminationList = paramedicalExaminationRepository.findAll();
        assertThat(paramedicalExaminationList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ParamedicalExamination in Elasticsearch
        verify(mockParamedicalExaminationSearchRepository, times(0)).save(paramedicalExamination);
    }

    @Test
    @Transactional
    public void deleteParamedicalExamination() throws Exception {
        // Initialize the database
        paramedicalExaminationRepository.saveAndFlush(paramedicalExamination);

        int databaseSizeBeforeDelete = paramedicalExaminationRepository.findAll().size();

        // Delete the paramedicalExamination
        restParamedicalExaminationMockMvc.perform(delete("/api/paramedical-examinations/{id}", paramedicalExamination.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ParamedicalExamination> paramedicalExaminationList = paramedicalExaminationRepository.findAll();
        assertThat(paramedicalExaminationList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the ParamedicalExamination in Elasticsearch
        verify(mockParamedicalExaminationSearchRepository, times(1)).deleteById(paramedicalExamination.getId());
    }

    @Test
    @Transactional
    public void searchParamedicalExamination() throws Exception {
        // Initialize the database
        paramedicalExaminationRepository.saveAndFlush(paramedicalExamination);
        when(mockParamedicalExaminationSearchRepository.search(queryStringQuery("id:" + paramedicalExamination.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(paramedicalExamination), PageRequest.of(0, 1), 1));
        // Search the paramedicalExamination
        restParamedicalExaminationMockMvc.perform(get("/api/_search/paramedical-examinations?query=id:" + paramedicalExamination.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(paramedicalExamination.getId().intValue())))
            .andExpect(jsonPath("$.[*].bp").value(hasItem(DEFAULT_BP)))
            .andExpect(jsonPath("$.[*].height").value(hasItem(DEFAULT_HEIGHT.doubleValue())))
            .andExpect(jsonPath("$.[*].weight").value(hasItem(DEFAULT_WEIGHT.doubleValue())))
            .andExpect(jsonPath("$.[*].temperature").value(hasItem(DEFAULT_TEMPERATURE.doubleValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ParamedicalExamination.class);
        ParamedicalExamination paramedicalExamination1 = new ParamedicalExamination();
        paramedicalExamination1.setId(1L);
        ParamedicalExamination paramedicalExamination2 = new ParamedicalExamination();
        paramedicalExamination2.setId(paramedicalExamination1.getId());
        assertThat(paramedicalExamination1).isEqualTo(paramedicalExamination2);
        paramedicalExamination2.setId(2L);
        assertThat(paramedicalExamination1).isNotEqualTo(paramedicalExamination2);
        paramedicalExamination1.setId(null);
        assertThat(paramedicalExamination1).isNotEqualTo(paramedicalExamination2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ParamedicalExaminationDTO.class);
        ParamedicalExaminationDTO paramedicalExaminationDTO1 = new ParamedicalExaminationDTO();
        paramedicalExaminationDTO1.setId(1L);
        ParamedicalExaminationDTO paramedicalExaminationDTO2 = new ParamedicalExaminationDTO();
        assertThat(paramedicalExaminationDTO1).isNotEqualTo(paramedicalExaminationDTO2);
        paramedicalExaminationDTO2.setId(paramedicalExaminationDTO1.getId());
        assertThat(paramedicalExaminationDTO1).isEqualTo(paramedicalExaminationDTO2);
        paramedicalExaminationDTO2.setId(2L);
        assertThat(paramedicalExaminationDTO1).isNotEqualTo(paramedicalExaminationDTO2);
        paramedicalExaminationDTO1.setId(null);
        assertThat(paramedicalExaminationDTO1).isNotEqualTo(paramedicalExaminationDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(paramedicalExaminationMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(paramedicalExaminationMapper.fromId(null)).isNull();
    }
}
