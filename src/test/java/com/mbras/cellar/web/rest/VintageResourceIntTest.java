package com.mbras.cellar.web.rest;

import com.mbras.cellar.CavaVinApp;

import com.mbras.cellar.domain.Vintage;
import com.mbras.cellar.repository.VintageRepository;
import com.mbras.cellar.repository.search.VintageSearchRepository;

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
 * Test class for the VintageResource REST controller.
 *
 * @see VintageResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CavaVinApp.class)
public class VintageResourceIntTest {

    private static final Integer DEFAULT_BARE_CODE = 1;
    private static final Integer UPDATED_BARE_CODE = 2;

    @Inject
    private VintageRepository vintageRepository;

    @Inject
    private VintageSearchRepository vintageSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restVintageMockMvc;

    private Vintage vintage;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        VintageResource vintageResource = new VintageResource();
        ReflectionTestUtils.setField(vintageResource, "vintageSearchRepository", vintageSearchRepository);
        ReflectionTestUtils.setField(vintageResource, "vintageRepository", vintageRepository);
        this.restVintageMockMvc = MockMvcBuilders.standaloneSetup(vintageResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
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
                .bareCode(DEFAULT_BARE_CODE);
        return vintage;
    }

    @Before
    public void initTest() {
        vintageSearchRepository.deleteAll();
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
        List<Vintage> vintages = vintageRepository.findAll();
        assertThat(vintages).hasSize(databaseSizeBeforeCreate + 1);
        Vintage testVintage = vintages.get(vintages.size() - 1);
        assertThat(testVintage.getBareCode()).isEqualTo(DEFAULT_BARE_CODE);

        // Validate the Vintage in ElasticSearch
        Vintage vintageEs = vintageSearchRepository.findOne(testVintage.getId());
        assertThat(vintageEs).isEqualToComparingFieldByField(testVintage);
    }

    @Test
    @Transactional
    public void getAllVintages() throws Exception {
        // Initialize the database
        vintageRepository.saveAndFlush(vintage);

        // Get all the vintages
        restVintageMockMvc.perform(get("/api/vintages?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vintage.getId().intValue())))
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
        vintageSearchRepository.save(vintage);
        int databaseSizeBeforeUpdate = vintageRepository.findAll().size();

        // Update the vintage
        Vintage updatedVintage = vintageRepository.findOne(vintage.getId());
        updatedVintage
                .bareCode(UPDATED_BARE_CODE);

        restVintageMockMvc.perform(put("/api/vintages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedVintage)))
            .andExpect(status().isOk());

        // Validate the Vintage in the database
        List<Vintage> vintages = vintageRepository.findAll();
        assertThat(vintages).hasSize(databaseSizeBeforeUpdate);
        Vintage testVintage = vintages.get(vintages.size() - 1);
        assertThat(testVintage.getBareCode()).isEqualTo(UPDATED_BARE_CODE);

        // Validate the Vintage in ElasticSearch
        Vintage vintageEs = vintageSearchRepository.findOne(testVintage.getId());
        assertThat(vintageEs).isEqualToComparingFieldByField(testVintage);
    }

    @Test
    @Transactional
    public void deleteVintage() throws Exception {
        // Initialize the database
        vintageRepository.saveAndFlush(vintage);
        vintageSearchRepository.save(vintage);
        int databaseSizeBeforeDelete = vintageRepository.findAll().size();

        // Get the vintage
        restVintageMockMvc.perform(delete("/api/vintages/{id}", vintage.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean vintageExistsInEs = vintageSearchRepository.exists(vintage.getId());
        assertThat(vintageExistsInEs).isFalse();

        // Validate the database is empty
        List<Vintage> vintages = vintageRepository.findAll();
        assertThat(vintages).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchVintage() throws Exception {
        // Initialize the database
        vintageRepository.saveAndFlush(vintage);
        vintageSearchRepository.save(vintage);

        // Search the vintage
        restVintageMockMvc.perform(get("/api/_search/vintages?query=id:" + vintage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vintage.getId().intValue())))
            .andExpect(jsonPath("$.[*].bareCode").value(hasItem(DEFAULT_BARE_CODE)));
    }
}
