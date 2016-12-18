package com.mbras.cellar.web.rest;

import com.mbras.cellar.CavaVinApp;

import com.mbras.cellar.domain.Wine;
import com.mbras.cellar.repository.WineRepository;
import com.mbras.cellar.repository.search.WineSearchRepository;

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
 * Test class for the WineResource REST controller.
 *
 * @see WineResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CavaVinApp.class)
public class WineResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_APPELLATION = "AAAAAAAAAA";
    private static final String UPDATED_APPELLATION = "BBBBBBBBBB";

    private static final String DEFAULT_PRODUCER = "AAAAAAAAAA";
    private static final String UPDATED_PRODUCER = "BBBBBBBBBB";

    @Inject
    private WineRepository wineRepository;

    @Inject
    private WineSearchRepository wineSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restWineMockMvc;

    private Wine wine;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        WineResource wineResource = new WineResource();
        ReflectionTestUtils.setField(wineResource, "wineSearchRepository", wineSearchRepository);
        ReflectionTestUtils.setField(wineResource, "wineRepository", wineRepository);
        this.restWineMockMvc = MockMvcBuilders.standaloneSetup(wineResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
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
        List<Wine> wines = wineRepository.findAll();
        assertThat(wines).hasSize(databaseSizeBeforeCreate + 1);
        Wine testWine = wines.get(wines.size() - 1);
        assertThat(testWine.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testWine.getAppellation()).isEqualTo(DEFAULT_APPELLATION);
        assertThat(testWine.getProducer()).isEqualTo(DEFAULT_PRODUCER);

        // Validate the Wine in ElasticSearch
        Wine wineEs = wineSearchRepository.findOne(testWine.getId());
        assertThat(wineEs).isEqualToComparingFieldByField(testWine);
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

        List<Wine> wines = wineRepository.findAll();
        assertThat(wines).hasSize(databaseSizeBeforeTest);
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

        List<Wine> wines = wineRepository.findAll();
        assertThat(wines).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllWines() throws Exception {
        // Initialize the database
        wineRepository.saveAndFlush(wine);

        // Get all the wines
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
        List<Wine> wines = wineRepository.findAll();
        assertThat(wines).hasSize(databaseSizeBeforeUpdate);
        Wine testWine = wines.get(wines.size() - 1);
        assertThat(testWine.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testWine.getAppellation()).isEqualTo(UPDATED_APPELLATION);
        assertThat(testWine.getProducer()).isEqualTo(UPDATED_PRODUCER);

        // Validate the Wine in ElasticSearch
        Wine wineEs = wineSearchRepository.findOne(testWine.getId());
        assertThat(wineEs).isEqualToComparingFieldByField(testWine);
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

        // Validate ElasticSearch is empty
        boolean wineExistsInEs = wineSearchRepository.exists(wine.getId());
        assertThat(wineExistsInEs).isFalse();

        // Validate the database is empty
        List<Wine> wines = wineRepository.findAll();
        assertThat(wines).hasSize(databaseSizeBeforeDelete - 1);
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
}
