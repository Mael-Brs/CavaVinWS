package com.mbras.cavavin.web.rest;

import com.mbras.cavavin.CavavinApp;

import com.mbras.cavavin.domain.Year;
import com.mbras.cavavin.repository.YearRepository;
import com.mbras.cavavin.repository.search.YearSearchRepository;
import com.mbras.cavavin.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the YearResource REST controller.
 *
 * @see YearResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CavavinApp.class)
public class YearResourceIntTest {

    private static final Integer DEFAULT_NUMBER = 1;
    private static final Integer UPDATED_NUMBER = 2;

    @Autowired
    private YearRepository yearRepository;

    @Autowired
    private YearSearchRepository yearSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restYearMockMvc;

    private Year year;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        YearResource yearResource = new YearResource(yearRepository, yearSearchRepository);
        this.restYearMockMvc = MockMvcBuilders.standaloneSetup(yearResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Year createEntity(EntityManager em) {
        Year year = new Year()
            .number(DEFAULT_NUMBER);
        return year;
    }

    @Before
    public void initTest() {
        yearSearchRepository.deleteAll();
        year = createEntity(em);
    }

    @Test
    @Transactional
    public void createYear() throws Exception {
        int databaseSizeBeforeCreate = yearRepository.findAll().size();

        // Create the Year
        restYearMockMvc.perform(post("/api/years")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(year)))
            .andExpect(status().isCreated());

        // Validate the Year in the database
        List<Year> yearList = yearRepository.findAll();
        assertThat(yearList).hasSize(databaseSizeBeforeCreate + 1);
        Year testYear = yearList.get(yearList.size() - 1);
        assertThat(testYear.getNumber()).isEqualTo(DEFAULT_NUMBER);

        // Validate the Year in Elasticsearch
        Year yearEs = yearSearchRepository.findOne(testYear.getId());
        assertThat(yearEs).isEqualToComparingFieldByField(testYear);
    }

    @Test
    @Transactional
    public void createYearWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = yearRepository.findAll().size();

        // Create the Year with an existing ID
        year.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restYearMockMvc.perform(post("/api/years")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(year)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Year> yearList = yearRepository.findAll();
        assertThat(yearList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = yearRepository.findAll().size();
        // set the field null
        year.setNumber(null);

        // Create the Year, which fails.

        restYearMockMvc.perform(post("/api/years")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(year)))
            .andExpect(status().isBadRequest());

        List<Year> yearList = yearRepository.findAll();
        assertThat(yearList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllYears() throws Exception {
        // Initialize the database
        yearRepository.saveAndFlush(year);

        // Get all the yearList
        restYearMockMvc.perform(get("/api/years?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(year.getId().intValue())))
            .andExpect(jsonPath("$.[*].number").value(hasItem(DEFAULT_NUMBER)));
    }

    @Test
    @Transactional
    public void getYear() throws Exception {
        // Initialize the database
        yearRepository.saveAndFlush(year);

        // Get the year
        restYearMockMvc.perform(get("/api/years/{id}", year.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(year.getId().intValue()))
            .andExpect(jsonPath("$.number").value(DEFAULT_NUMBER));
    }

    @Test
    @Transactional
    public void getNonExistingYear() throws Exception {
        // Get the year
        restYearMockMvc.perform(get("/api/years/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateYear() throws Exception {
        // Initialize the database
        yearRepository.saveAndFlush(year);
        yearSearchRepository.save(year);
        int databaseSizeBeforeUpdate = yearRepository.findAll().size();

        // Update the year
        Year updatedYear = yearRepository.findOne(year.getId());
        updatedYear
            .number(UPDATED_NUMBER);

        restYearMockMvc.perform(put("/api/years")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedYear)))
            .andExpect(status().isOk());

        // Validate the Year in the database
        List<Year> yearList = yearRepository.findAll();
        assertThat(yearList).hasSize(databaseSizeBeforeUpdate);
        Year testYear = yearList.get(yearList.size() - 1);
        assertThat(testYear.getNumber()).isEqualTo(UPDATED_NUMBER);

        // Validate the Year in Elasticsearch
        Year yearEs = yearSearchRepository.findOne(testYear.getId());
        assertThat(yearEs).isEqualToComparingFieldByField(testYear);
    }

    @Test
    @Transactional
    public void updateNonExistingYear() throws Exception {
        int databaseSizeBeforeUpdate = yearRepository.findAll().size();

        // Create the Year

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restYearMockMvc.perform(put("/api/years")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(year)))
            .andExpect(status().isCreated());

        // Validate the Year in the database
        List<Year> yearList = yearRepository.findAll();
        assertThat(yearList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteYear() throws Exception {
        // Initialize the database
        yearRepository.saveAndFlush(year);
        yearSearchRepository.save(year);
        int databaseSizeBeforeDelete = yearRepository.findAll().size();

        // Get the year
        restYearMockMvc.perform(delete("/api/years/{id}", year.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean yearExistsInEs = yearSearchRepository.exists(year.getId());
        assertThat(yearExistsInEs).isFalse();

        // Validate the database is empty
        List<Year> yearList = yearRepository.findAll();
        assertThat(yearList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchYear() throws Exception {
        // Initialize the database
        yearRepository.saveAndFlush(year);
        yearSearchRepository.save(year);

        // Search the year
        restYearMockMvc.perform(get("/api/_search/years?query=id:" + year.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(year.getId().intValue())))
            .andExpect(jsonPath("$.[*].number").value(hasItem(DEFAULT_NUMBER)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Year.class);
        Year year1 = new Year();
        year1.setId(1L);
        Year year2 = new Year();
        year2.setId(year1.getId());
        assertThat(year1).isEqualTo(year2);
        year2.setId(2L);
        assertThat(year1).isNotEqualTo(year2);
        year1.setId(null);
        assertThat(year1).isNotEqualTo(year2);
    }
}
