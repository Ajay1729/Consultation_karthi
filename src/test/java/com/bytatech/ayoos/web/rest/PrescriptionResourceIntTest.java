package com.bytatech.ayoos.web.rest;

import com.bytatech.ayoos.ConsultationApp;

import com.bytatech.ayoos.domain.Prescription;
import com.bytatech.ayoos.repository.PrescriptionRepository;
import com.bytatech.ayoos.repository.search.PrescriptionSearchRepository;
import com.bytatech.ayoos.service.PrescriptionService;
import com.bytatech.ayoos.service.dto.PrescriptionDTO;
import com.bytatech.ayoos.service.mapper.PrescriptionMapper;
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
 * Test class for the PrescriptionResource REST controller.
 *
 * @see PrescriptionResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ConsultationApp.class)
public class PrescriptionResourceIntTest {

    private static final String DEFAULT_PRESCRIPTION_DMSURL = "AAAAAAAAAA";
    private static final String UPDATED_PRESCRIPTION_DMSURL = "BBBBBBBBBB";

    private static final String DEFAULT_DRUG = "AAAAAAAAAA";
    private static final String UPDATED_DRUG = "BBBBBBBBBB";

    private static final String DEFAULT_DOSE = "AAAAAAAAAA";
    private static final String UPDATED_DOSE = "BBBBBBBBBB";

    private static final String DEFAULT_FREQUENCY = "AAAAAAAAAA";
    private static final String UPDATED_FREQUENCY = "BBBBBBBBBB";

    private static final String DEFAULT_PERIOD = "AAAAAAAAAA";
    private static final String UPDATED_PERIOD = "BBBBBBBBBB";

    @Autowired
    private PrescriptionRepository prescriptionRepository;

    @Autowired
    private PrescriptionMapper prescriptionMapper;

    @Autowired
    private PrescriptionService prescriptionService;

    /**
     * This repository is mocked in the com.bytatech.ayoos.repository.search test package.
     *
     * @see com.bytatech.ayoos.repository.search.PrescriptionSearchRepositoryMockConfiguration
     */
    @Autowired
    private PrescriptionSearchRepository mockPrescriptionSearchRepository;

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

    private MockMvc restPrescriptionMockMvc;

    private Prescription prescription;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PrescriptionResource prescriptionResource = new PrescriptionResource(prescriptionService);
        this.restPrescriptionMockMvc = MockMvcBuilders.standaloneSetup(prescriptionResource)
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
    public static Prescription createEntity(EntityManager em) {
        Prescription prescription = new Prescription()
            .prescriptionDMSURL(DEFAULT_PRESCRIPTION_DMSURL)
            .drug(DEFAULT_DRUG)
            .dose(DEFAULT_DOSE)
            .frequency(DEFAULT_FREQUENCY)
            .period(DEFAULT_PERIOD);
        return prescription;
    }

    @Before
    public void initTest() {
        prescription = createEntity(em);
    }

    @Test
    @Transactional
    public void createPrescription() throws Exception {
        int databaseSizeBeforeCreate = prescriptionRepository.findAll().size();

        // Create the Prescription
        PrescriptionDTO prescriptionDTO = prescriptionMapper.toDto(prescription);
        restPrescriptionMockMvc.perform(post("/api/prescriptions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(prescriptionDTO)))
            .andExpect(status().isCreated());

        // Validate the Prescription in the database
        List<Prescription> prescriptionList = prescriptionRepository.findAll();
        assertThat(prescriptionList).hasSize(databaseSizeBeforeCreate + 1);
        Prescription testPrescription = prescriptionList.get(prescriptionList.size() - 1);
        assertThat(testPrescription.getPrescriptionDMSURL()).isEqualTo(DEFAULT_PRESCRIPTION_DMSURL);
        assertThat(testPrescription.getDrug()).isEqualTo(DEFAULT_DRUG);
        assertThat(testPrescription.getDose()).isEqualTo(DEFAULT_DOSE);
        assertThat(testPrescription.getFrequency()).isEqualTo(DEFAULT_FREQUENCY);
        assertThat(testPrescription.getPeriod()).isEqualTo(DEFAULT_PERIOD);

        // Validate the Prescription in Elasticsearch
        verify(mockPrescriptionSearchRepository, times(1)).save(testPrescription);
    }

    @Test
    @Transactional
    public void createPrescriptionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = prescriptionRepository.findAll().size();

        // Create the Prescription with an existing ID
        prescription.setId(1L);
        PrescriptionDTO prescriptionDTO = prescriptionMapper.toDto(prescription);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPrescriptionMockMvc.perform(post("/api/prescriptions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(prescriptionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Prescription in the database
        List<Prescription> prescriptionList = prescriptionRepository.findAll();
        assertThat(prescriptionList).hasSize(databaseSizeBeforeCreate);

        // Validate the Prescription in Elasticsearch
        verify(mockPrescriptionSearchRepository, times(0)).save(prescription);
    }

    @Test
    @Transactional
    public void getAllPrescriptions() throws Exception {
        // Initialize the database
        prescriptionRepository.saveAndFlush(prescription);

        // Get all the prescriptionList
        restPrescriptionMockMvc.perform(get("/api/prescriptions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(prescription.getId().intValue())))
            .andExpect(jsonPath("$.[*].prescriptionDMSURL").value(hasItem(DEFAULT_PRESCRIPTION_DMSURL.toString())))
            .andExpect(jsonPath("$.[*].drug").value(hasItem(DEFAULT_DRUG.toString())))
            .andExpect(jsonPath("$.[*].dose").value(hasItem(DEFAULT_DOSE.toString())))
            .andExpect(jsonPath("$.[*].frequency").value(hasItem(DEFAULT_FREQUENCY.toString())))
            .andExpect(jsonPath("$.[*].period").value(hasItem(DEFAULT_PERIOD.toString())));
    }
    
    @Test
    @Transactional
    public void getPrescription() throws Exception {
        // Initialize the database
        prescriptionRepository.saveAndFlush(prescription);

        // Get the prescription
        restPrescriptionMockMvc.perform(get("/api/prescriptions/{id}", prescription.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(prescription.getId().intValue()))
            .andExpect(jsonPath("$.prescriptionDMSURL").value(DEFAULT_PRESCRIPTION_DMSURL.toString()))
            .andExpect(jsonPath("$.drug").value(DEFAULT_DRUG.toString()))
            .andExpect(jsonPath("$.dose").value(DEFAULT_DOSE.toString()))
            .andExpect(jsonPath("$.frequency").value(DEFAULT_FREQUENCY.toString()))
            .andExpect(jsonPath("$.period").value(DEFAULT_PERIOD.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPrescription() throws Exception {
        // Get the prescription
        restPrescriptionMockMvc.perform(get("/api/prescriptions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePrescription() throws Exception {
        // Initialize the database
        prescriptionRepository.saveAndFlush(prescription);

        int databaseSizeBeforeUpdate = prescriptionRepository.findAll().size();

        // Update the prescription
        Prescription updatedPrescription = prescriptionRepository.findById(prescription.getId()).get();
        // Disconnect from session so that the updates on updatedPrescription are not directly saved in db
        em.detach(updatedPrescription);
        updatedPrescription
            .prescriptionDMSURL(UPDATED_PRESCRIPTION_DMSURL)
            .drug(UPDATED_DRUG)
            .dose(UPDATED_DOSE)
            .frequency(UPDATED_FREQUENCY)
            .period(UPDATED_PERIOD);
        PrescriptionDTO prescriptionDTO = prescriptionMapper.toDto(updatedPrescription);

        restPrescriptionMockMvc.perform(put("/api/prescriptions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(prescriptionDTO)))
            .andExpect(status().isOk());

        // Validate the Prescription in the database
        List<Prescription> prescriptionList = prescriptionRepository.findAll();
        assertThat(prescriptionList).hasSize(databaseSizeBeforeUpdate);
        Prescription testPrescription = prescriptionList.get(prescriptionList.size() - 1);
        assertThat(testPrescription.getPrescriptionDMSURL()).isEqualTo(UPDATED_PRESCRIPTION_DMSURL);
        assertThat(testPrescription.getDrug()).isEqualTo(UPDATED_DRUG);
        assertThat(testPrescription.getDose()).isEqualTo(UPDATED_DOSE);
        assertThat(testPrescription.getFrequency()).isEqualTo(UPDATED_FREQUENCY);
        assertThat(testPrescription.getPeriod()).isEqualTo(UPDATED_PERIOD);

        // Validate the Prescription in Elasticsearch
        verify(mockPrescriptionSearchRepository, times(1)).save(testPrescription);
    }

    @Test
    @Transactional
    public void updateNonExistingPrescription() throws Exception {
        int databaseSizeBeforeUpdate = prescriptionRepository.findAll().size();

        // Create the Prescription
        PrescriptionDTO prescriptionDTO = prescriptionMapper.toDto(prescription);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPrescriptionMockMvc.perform(put("/api/prescriptions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(prescriptionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Prescription in the database
        List<Prescription> prescriptionList = prescriptionRepository.findAll();
        assertThat(prescriptionList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Prescription in Elasticsearch
        verify(mockPrescriptionSearchRepository, times(0)).save(prescription);
    }

    @Test
    @Transactional
    public void deletePrescription() throws Exception {
        // Initialize the database
        prescriptionRepository.saveAndFlush(prescription);

        int databaseSizeBeforeDelete = prescriptionRepository.findAll().size();

        // Delete the prescription
        restPrescriptionMockMvc.perform(delete("/api/prescriptions/{id}", prescription.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Prescription> prescriptionList = prescriptionRepository.findAll();
        assertThat(prescriptionList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Prescription in Elasticsearch
        verify(mockPrescriptionSearchRepository, times(1)).deleteById(prescription.getId());
    }

    @Test
    @Transactional
    public void searchPrescription() throws Exception {
        // Initialize the database
        prescriptionRepository.saveAndFlush(prescription);
        when(mockPrescriptionSearchRepository.search(queryStringQuery("id:" + prescription.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(prescription), PageRequest.of(0, 1), 1));
        // Search the prescription
        restPrescriptionMockMvc.perform(get("/api/_search/prescriptions?query=id:" + prescription.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(prescription.getId().intValue())))
            .andExpect(jsonPath("$.[*].prescriptionDMSURL").value(hasItem(DEFAULT_PRESCRIPTION_DMSURL)))
            .andExpect(jsonPath("$.[*].drug").value(hasItem(DEFAULT_DRUG)))
            .andExpect(jsonPath("$.[*].dose").value(hasItem(DEFAULT_DOSE)))
            .andExpect(jsonPath("$.[*].frequency").value(hasItem(DEFAULT_FREQUENCY)))
            .andExpect(jsonPath("$.[*].period").value(hasItem(DEFAULT_PERIOD)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Prescription.class);
        Prescription prescription1 = new Prescription();
        prescription1.setId(1L);
        Prescription prescription2 = new Prescription();
        prescription2.setId(prescription1.getId());
        assertThat(prescription1).isEqualTo(prescription2);
        prescription2.setId(2L);
        assertThat(prescription1).isNotEqualTo(prescription2);
        prescription1.setId(null);
        assertThat(prescription1).isNotEqualTo(prescription2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PrescriptionDTO.class);
        PrescriptionDTO prescriptionDTO1 = new PrescriptionDTO();
        prescriptionDTO1.setId(1L);
        PrescriptionDTO prescriptionDTO2 = new PrescriptionDTO();
        assertThat(prescriptionDTO1).isNotEqualTo(prescriptionDTO2);
        prescriptionDTO2.setId(prescriptionDTO1.getId());
        assertThat(prescriptionDTO1).isEqualTo(prescriptionDTO2);
        prescriptionDTO2.setId(2L);
        assertThat(prescriptionDTO1).isNotEqualTo(prescriptionDTO2);
        prescriptionDTO1.setId(null);
        assertThat(prescriptionDTO1).isNotEqualTo(prescriptionDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(prescriptionMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(prescriptionMapper.fromId(null)).isNull();
    }
}
