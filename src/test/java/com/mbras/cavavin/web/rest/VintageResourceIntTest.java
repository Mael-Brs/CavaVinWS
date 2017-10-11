package com.mbras.cavavin.web.rest;

import com.mbras.cavavin.CavavinApp;

import com.mbras.cavavin.domain.Vintage;
import com.mbras.cavavin.domain.Wine;
import com.mbras.cavavin.repository.VintageRepository;
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
 * Test class for the VintageResource REST controller.
 *
 * @see VintageResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CavavinApp.class)
public class VintageResourceIntTest {

    private static final Integer DEFAULT_YEAR = 1;
    private static final Integer UPDATED_YEAR = 2;

    private static final Integer DEFAULT_BARE_CODE = 1;
    private static final Integer UPDATED_BARE_CODE = 2;

    @Autowired
    private VintageRepository vintageRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restVintageMockMvc;

    private Vintage vintage;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        VintageResource vintageResource = new VintageResource(vintageRepository);
        this.restVintageMockMvc = MockMvcBuilders.standaloneSetup(vintageResource)
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
    public static Vintage createEntity(EntityManager em) {
        Vintage vintage = new Vintage()
            .year(DEFAULT_YEAR)
            .bareCode(DEFAULT_BARE_CODE);
        // Add required entity
        Wine wine = WineResourceIntTest.createEntity(em);
        em.persist(wine);
        em.flush();
        vintage.setWine(wine);
        return vintage;
    }

    @Before
    public void initTest() {
        vintage = createEntity(em);
    }

    @Test
    @Transactional
    public void createVintage() throws Exception {
        int databaseSizeBeforeCreate = vintageRepository.findAll().size();

        // Create the Vintage
        restVintageMockMvc.perform(post("/api/vintages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(vintage)))
            .andExpect(status().isCreated());

        // Validate the Vintage in the database
        List<Vintage> vintageList = vintageRepository.findAll();
        assertThat(vintageList).hasSize(databaseSizeBeforeCreate + 1);
        Vintage testVintage = vintageList.get(vintageList.size() - 1);
        assertThat(testVintage.getYear()).isEqualTo(DEFAULT_YEAR);
        assertThat(testVintage.getBareCode()).isEqualTo(DEFAULT_BARE_CODE);
    }

    @Test
    @Transactional
    public void createVintageWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = vintageRepository.findAll().size();

        // Create the Vintage with an existing ID
        vintage.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restVintageMockMvc.perform(post("/api/vintages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(vintage)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Vintage> vintageList = vintageRepository.findAll();
        assertThat(vintageList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkYearIsRequired() throws Exception {
        int databaseSizeBeforeTest = vintageRepository.findAll().size();
        // set the field null
        vintage.setYear(null);

        // Create the Vintage, which fails.

        restVintageMockMvc.perform(post("/api/vintages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(vintage)))
            .andExpect(status().isBadRequest());

        List<Vintage> vintageList = vintageRepository.findAll();
        assertThat(vintageList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllVintages() throws Exception {
        // Initialize the database
        vintageRepository.saveAndFlush(vintage);

        // Get all the vintageList
        restVintageMockMvc.perform(get("/api/vintages?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vintage.getId().intValue())))
            .andExpect(jsonPath("$.[*].year").value(hasItem(DEFAULT_YEAR)))
            .andExpect(jsonPath("$.[*].bareCode").value(hasItem(DEFAULT_BARE_CODE)));
    }

    @Test
    @Transactional
    public void getVintage() throws Exception {
        // Initialize the database
        vintageRepository.saveAndFlush(vintage);

        // Get the vintage
        restVintageMockMvc.perform(get("/api/vintages/{id}", vintage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(vintage.getId().intValue()))
            .andExpect(jsonPath("$.year").value(DEFAULT_YEAR))
            .andExpect(jsonPath("$.bareCode").value(DEFAULT_BARE_CODE));
    }

    @Test
    @Transactional
    public void getNonExistingVintage() throws Exception {
        // Get the vintage
        restVintageMockMvc.perform(get("/api/vintages/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateVintage() throws Exception {
        // Initialize the database
        vintageRepository.saveAndFlush(vintage);
        int databaseSizeBeforeUpdate = vintageRepository.findAll().size();

        // Update the vintage
        Vintage updatedVintage = vintageRepository.findOne(vintage.getId());
        updatedVintage
            .year(UPDATED_YEAR)
            .bareCode(UPDATED_BARE_CODE);

        restVintageMockMvc.perform(put("/api/vintages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedVintage)))
            .andExpect(status().isOk());

        // Validate the Vintage in the database
        List<Vintage> vintageList = vintageRepository.findAll();
        assertThat(vintageList).hasSize(databaseSizeBeforeUpdate);
        Vintage testVintage = vintageList.get(vintageList.size() - 1);
        assertThat(testVintage.getYear()).isEqualTo(UPDATED_YEAR);
        assertThat(testVintage.getBareCode()).isEqualTo(UPDATED_BARE_CODE);
    }

    @Test
    @Transactional
    public void updateNonExistingVintage() throws Exception {
        int databaseSizeBeforeUpdate = vintageRepository.findAll().size();

        // Create the Vintage

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restVintageMockMvc.perform(put("/api/vintages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(vintage)))
            .andExpect(status().isCreated());

        // Validate the Vintage in the database
        List<Vintage> vintageList = vintageRepository.findAll();
        assertThat(vintageList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteVintage() throws Exception {
        // Initialize the database
        vintageRepository.saveAndFlush(vintage);
        int databaseSizeBeforeDelete = vintageRepository.findAll().size();

        // Get the vintage
        restVintageMockMvc.perform(delete("/api/vintages/{id}", vintage.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Vintage> vintageList = vintageRepository.findAll();
        assertThat(vintageList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Vintage.class);
        Vintage vintage1 = new Vintage();
        vintage1.setId(1L);
        Vintage vintage2 = new Vintage();
        vintage2.setId(vintage1.getId());
        assertThat(vintage1).isEqualTo(vintage2);
        vintage2.setId(2L);
        assertThat(vintage1).isNotEqualTo(vintage2);
        vintage1.setId(null);
        assertThat(vintage1).isNotEqualTo(vintage2);
    }
}
