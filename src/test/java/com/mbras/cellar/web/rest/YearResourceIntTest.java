package com.mbras.cellar.web.rest;

import com.mbras.cellar.CavaVinApp;

import com.mbras.cellar.domain.Year;
import com.mbras.cellar.repository.YearRepository;
import com.mbras.cellar.repository.search.YearSearchRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
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
@SpringBootTest(classes = CavaVinApp.class)
public class YearResourceIntTest {

    private static final Integer DEFAULT_NUMBER = 1;
    private static final Integer UPDATED_NUMBER = 2;

    @Inject
    private YearRepository yearRepository;

    @Inject
    private YearSearchRepository yearSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restYearMockMvc;

    private Year year;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        YearResource yearResource = new YearResource();
        ReflectionTestUtils.setField(yearResource, "yearSearchRepository", yearSearchRepository);
        ReflectionTestUtils.setField(yearResource, "yearRepository", yearRepository);
        this.restYearMockMvc = MockMvcBuilders.standaloneSetup(yearResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
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
        List<Year> years = yearRepository.findAll();
        assertThat(years).hasSize(databaseSizeBeforeCreate + 1);
        Year testYear = years.get(years.size() - 1);
        assertThat(testYear.getNumber()).isEqualTo(DEFAULT_NUMBER);

        // Validate the Year in ElasticSearch
        Year yearEs = yearSearchRepository.findOne(testYear.getId());
        assertThat(yearEs).isEqualToComparingFieldByField(testYear);
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

        List<Year> years = yearRepository.findAll();
        assertThat(years).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllYears() throws Exception {
        // Initialize the database
        yearRepository.saveAndFlush(year);

        // Get all the years
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
        List<Year> years = yearRepository.findAll();
        assertThat(years).hasSize(databaseSizeBeforeUpdate);
        Year testYear = years.get(years.size() - 1);
        assertThat(testYear.getNumber()).isEqualTo(UPDATED_NUMBER);

        // Validate the Year in ElasticSearch
        Year yearEs = yearSearchRepository.findOne(testYear.getId());
        assertThat(yearEs).isEqualToComparingFieldByField(testYear);
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

        // Validate ElasticSearch is empty
        boolean yearExistsInEs = yearSearchRepository.exists(year.getId());
        assertThat(yearExistsInEs).isFalse();

        // Validate the database is empty
        List<Year> years = yearRepository.findAll();
        assertThat(years).hasSize(databaseSizeBeforeDelete - 1);
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
}
