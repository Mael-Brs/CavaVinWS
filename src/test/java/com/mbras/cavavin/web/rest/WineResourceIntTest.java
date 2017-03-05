package com.mbras.cavavin.web.rest;

import com.mbras.cavavin.CavavinApp;

import com.mbras.cavavin.domain.Wine;
import com.mbras.cavavin.repository.VintageRepository;
import com.mbras.cavavin.repository.WineRepository;
import com.mbras.cavavin.repository.search.WineSearchRepository;
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
 * Test class for the WineResource REST controller.
 *
 * @see WineResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CavavinApp.class)
public class WineResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_APPELLATION = "AAAAAAAAAA";
    private static final String UPDATED_APPELLATION = "BBBBBBBBBB";

    private static final String DEFAULT_PRODUCER = "AAAAAAAAAA";
    private static final String UPDATED_PRODUCER = "BBBBBBBBBB";

    @Autowired
    private WineRepository wineRepository;

    @Autowired
    private WineSearchRepository wineSearchRepository;

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

    private MockMvc restWineMockMvc;

    private Wine wine;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
            WineResource wineResource = new WineResource(wineRepository, wineSearchRepository, vintageRepository);
        this.restWineMockMvc = MockMvcBuilders.standaloneSetup(wineResource)
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
    public static Wine createEntity(EntityManager em) {
        Wine wine = new Wine()
                .name(DEFAULT_NAME)
                .appellation(DEFAULT_APPELLATION)
                .producer(DEFAULT_PRODUCER);
        return wine;
    }

    @Before
    public void initTest() {
        wineSearchRepository.deleteAll();
        wine = createEntity(em);
    }

    @Test
    @Transactional
    public void createWine() throws Exception {
        int databaseSizeBeforeCreate = wineRepository.findAll().size();

        // Create the Wine

        restWineMockMvc.perform(post("/api/wines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(wine)))
            .andExpect(status().isCreated());

        // Validate the Wine in the database
        List<Wine> wineList = wineRepository.findAll();
        assertThat(wineList).hasSize(databaseSizeBeforeCreate + 1);
        Wine testWine = wineList.get(wineList.size() - 1);
        assertThat(testWine.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testWine.getAppellation()).isEqualTo(DEFAULT_APPELLATION);
        assertThat(testWine.getProducer()).isEqualTo(DEFAULT_PRODUCER);

        // Validate the Wine in Elasticsearch
        Wine wineEs = wineSearchRepository.findOne(testWine.getId());
        assertThat(wineEs).isEqualToComparingFieldByField(testWine);
    }

    @Test
    @Transactional
    public void createWineWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = wineRepository.findAll().size();

        // Create the Wine with an existing ID
        Wine existingWine = new Wine();
        existingWine.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restWineMockMvc.perform(post("/api/wines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingWine)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Wine> wineList = wineRepository.findAll();
        assertThat(wineList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = wineRepository.findAll().size();
        // set the field null
        wine.setName(null);

        // Create the Wine, which fails.

        restWineMockMvc.perform(post("/api/wines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(wine)))
            .andExpect(status().isBadRequest());

        List<Wine> wineList = wineRepository.findAll();
        assertThat(wineList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkProducerIsRequired() throws Exception {
        int databaseSizeBeforeTest = wineRepository.findAll().size();
        // set the field null
        wine.setProducer(null);

        // Create the Wine, which fails.

        restWineMockMvc.perform(post("/api/wines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(wine)))
            .andExpect(status().isBadRequest());

        List<Wine> wineList = wineRepository.findAll();
        assertThat(wineList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllWines() throws Exception {
        // Initialize the database
        wineRepository.saveAndFlush(wine);

        // Get all the wineList
        restWineMockMvc.perform(get("/api/wines?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(wine.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].appellation").value(hasItem(DEFAULT_APPELLATION.toString())))
            .andExpect(jsonPath("$.[*].producer").value(hasItem(DEFAULT_PRODUCER.toString())));
    }

    @Test
    @Transactional
    public void getWine() throws Exception {
        // Initialize the database
        wineRepository.saveAndFlush(wine);

        // Get the wine
        restWineMockMvc.perform(get("/api/wines/{id}", wine.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(wine.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.appellation").value(DEFAULT_APPELLATION.toString()))
            .andExpect(jsonPath("$.producer").value(DEFAULT_PRODUCER.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingWine() throws Exception {
        // Get the wine
        restWineMockMvc.perform(get("/api/wines/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateWine() throws Exception {
        // Initialize the database
        wineRepository.saveAndFlush(wine);
        wineSearchRepository.save(wine);
        int databaseSizeBeforeUpdate = wineRepository.findAll().size();

        // Update the wine
        Wine updatedWine = wineRepository.findOne(wine.getId());
        updatedWine
                .name(UPDATED_NAME)
                .appellation(UPDATED_APPELLATION)
                .producer(UPDATED_PRODUCER);

        restWineMockMvc.perform(put("/api/wines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedWine)))
            .andExpect(status().isOk());

        // Validate the Wine in the database
        List<Wine> wineList = wineRepository.findAll();
        assertThat(wineList).hasSize(databaseSizeBeforeUpdate);
        Wine testWine = wineList.get(wineList.size() - 1);
        assertThat(testWine.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testWine.getAppellation()).isEqualTo(UPDATED_APPELLATION);
        assertThat(testWine.getProducer()).isEqualTo(UPDATED_PRODUCER);

        // Validate the Wine in Elasticsearch
        Wine wineEs = wineSearchRepository.findOne(testWine.getId());
        assertThat(wineEs).isEqualToComparingFieldByField(testWine);
    }

    @Test
    @Transactional
    public void updateNonExistingWine() throws Exception {
        int databaseSizeBeforeUpdate = wineRepository.findAll().size();

        // Create the Wine

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restWineMockMvc.perform(put("/api/wines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(wine)))
            .andExpect(status().isCreated());

        // Validate the Wine in the database
        List<Wine> wineList = wineRepository.findAll();
        assertThat(wineList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteWine() throws Exception {
        // Initialize the database
        wineRepository.saveAndFlush(wine);
        wineSearchRepository.save(wine);
        int databaseSizeBeforeDelete = wineRepository.findAll().size();

        // Get the wine
        restWineMockMvc.perform(delete("/api/wines/{id}", wine.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean wineExistsInEs = wineSearchRepository.exists(wine.getId());
        assertThat(wineExistsInEs).isFalse();

        // Validate the database is empty
        List<Wine> wineList = wineRepository.findAll();
        assertThat(wineList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchWine() throws Exception {
        // Initialize the database
        wineRepository.saveAndFlush(wine);
        wineSearchRepository.save(wine);

        // Search the wine
        restWineMockMvc.perform(get("/api/_search/wines?query=id:" + wine.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(wine.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].appellation").value(hasItem(DEFAULT_APPELLATION.toString())))
            .andExpect(jsonPath("$.[*].producer").value(hasItem(DEFAULT_PRODUCER.toString())));
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Wine.class);
    }
}
