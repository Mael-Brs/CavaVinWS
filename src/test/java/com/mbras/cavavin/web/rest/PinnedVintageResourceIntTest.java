package com.mbras.cavavin.web.rest;

import com.mbras.cavavin.CavavinApp;

import com.mbras.cavavin.domain.PinnedVintage;
import com.mbras.cavavin.domain.Vintage;
import com.mbras.cavavin.repository.PinnedVintageRepository;
import com.mbras.cavavin.repository.search.PinnedVintageSearchRepository;
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
import org.springframework.security.test.context.support.WithMockUser;
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
 * Test class for the PinnedVintageResource REST controller.
 *
 * @see PinnedVintageResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CavavinApp.class)
public class PinnedVintageResourceIntTest {

    private static final Long DEFAULT_USER_ID = 1L;
    private static final Long UPDATED_USER_ID = 2L;

    @Autowired
    private PinnedVintageRepository pinnedVintageRepository;

    @Autowired
    private PinnedVintageSearchRepository pinnedVintageSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPinnedVintageMockMvc;

    private PinnedVintage pinnedVintage;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PinnedVintageResource pinnedVintageResource = new PinnedVintageResource(pinnedVintageRepository, pinnedVintageSearchRepository);
        this.restPinnedVintageMockMvc = MockMvcBuilders.standaloneSetup(pinnedVintageResource)
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
    public static PinnedVintage createEntity(EntityManager em) {
        PinnedVintage pinnedVintage = new PinnedVintage()
            .userId(DEFAULT_USER_ID);
        // Add required entity
        Vintage vintage = VintageResourceIntTest.createEntity(em);
        em.persist(vintage);
        em.flush();
        pinnedVintage.setVintage(vintage);
        return pinnedVintage;
    }

    @Before
    public void initTest() {
        pinnedVintageSearchRepository.deleteAll();
        pinnedVintage = createEntity(em);
    }

    @Test
    @Transactional
    public void createPinnedVintage() throws Exception {
        int databaseSizeBeforeCreate = pinnedVintageRepository.findAll().size();

        // Create the PinnedVintage
        restPinnedVintageMockMvc.perform(post("/api/pinned-vintages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pinnedVintage)))
            .andExpect(status().isCreated());

        // Validate the PinnedVintage in the database
        List<PinnedVintage> pinnedVintageList = pinnedVintageRepository.findAll();
        assertThat(pinnedVintageList).hasSize(databaseSizeBeforeCreate + 1);
        PinnedVintage testPinnedVintage = pinnedVintageList.get(pinnedVintageList.size() - 1);
        assertThat(testPinnedVintage.getUserId()).isEqualTo(DEFAULT_USER_ID);

        // Validate the PinnedVintage in Elasticsearch
        PinnedVintage pinnedVintageEs = pinnedVintageSearchRepository.findOne(testPinnedVintage.getId());
        assertThat(pinnedVintageEs).isEqualToComparingFieldByField(testPinnedVintage);
    }

    @Test
    @Transactional
    public void createPinnedVintageWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = pinnedVintageRepository.findAll().size();

        // Create the PinnedVintage with an existing ID
        pinnedVintage.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPinnedVintageMockMvc.perform(post("/api/pinned-vintages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pinnedVintage)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<PinnedVintage> pinnedVintageList = pinnedVintageRepository.findAll();
        assertThat(pinnedVintageList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkUserIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = pinnedVintageRepository.findAll().size();
        // set the field null
        pinnedVintage.setUserId(null);

        // Create the PinnedVintage, which fails.

        restPinnedVintageMockMvc.perform(post("/api/pinned-vintages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pinnedVintage)))
            .andExpect(status().isBadRequest());

        List<PinnedVintage> pinnedVintageList = pinnedVintageRepository.findAll();
        assertThat(pinnedVintageList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    @WithMockUser("system")
    public void getAllPinnedVintages() throws Exception {
        // Initialize the database
        pinnedVintageRepository.saveAndFlush(pinnedVintage);

        // Get all the pinnedVintageList
        restPinnedVintageMockMvc.perform(get("/api/pinned-vintages?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pinnedVintage.getId().intValue())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.intValue())));
    }

    @Test
    @Transactional
    public void getPinnedVintage() throws Exception {
        // Initialize the database
        pinnedVintageRepository.saveAndFlush(pinnedVintage);

        // Get the pinnedVintage
        restPinnedVintageMockMvc.perform(get("/api/pinned-vintages/{id}", pinnedVintage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(pinnedVintage.getId().intValue()))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingPinnedVintage() throws Exception {
        // Get the pinnedVintage
        restPinnedVintageMockMvc.perform(get("/api/pinned-vintages/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePinnedVintage() throws Exception {
        // Initialize the database
        pinnedVintageRepository.saveAndFlush(pinnedVintage);
        pinnedVintageSearchRepository.save(pinnedVintage);
        int databaseSizeBeforeUpdate = pinnedVintageRepository.findAll().size();

        // Update the pinnedVintage
        PinnedVintage updatedPinnedVintage = pinnedVintageRepository.findOne(pinnedVintage.getId());
        updatedPinnedVintage
            .userId(UPDATED_USER_ID);

        restPinnedVintageMockMvc.perform(put("/api/pinned-vintages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPinnedVintage)))
            .andExpect(status().isOk());

        // Validate the PinnedVintage in the database
        List<PinnedVintage> pinnedVintageList = pinnedVintageRepository.findAll();
        assertThat(pinnedVintageList).hasSize(databaseSizeBeforeUpdate);
        PinnedVintage testPinnedVintage = pinnedVintageList.get(pinnedVintageList.size() - 1);
        assertThat(testPinnedVintage.getUserId()).isEqualTo(UPDATED_USER_ID);

        // Validate the PinnedVintage in Elasticsearch
        PinnedVintage pinnedVintageEs = pinnedVintageSearchRepository.findOne(testPinnedVintage.getId());
        assertThat(pinnedVintageEs).isEqualToComparingFieldByField(testPinnedVintage);
    }

    @Test
    @Transactional
    public void updateNonExistingPinnedVintage() throws Exception {
        int databaseSizeBeforeUpdate = pinnedVintageRepository.findAll().size();

        // Create the PinnedVintage

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPinnedVintageMockMvc.perform(put("/api/pinned-vintages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pinnedVintage)))
            .andExpect(status().isCreated());

        // Validate the PinnedVintage in the database
        List<PinnedVintage> pinnedVintageList = pinnedVintageRepository.findAll();
        assertThat(pinnedVintageList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deletePinnedVintage() throws Exception {
        // Initialize the database
        pinnedVintageRepository.saveAndFlush(pinnedVintage);
        pinnedVintageSearchRepository.save(pinnedVintage);
        int databaseSizeBeforeDelete = pinnedVintageRepository.findAll().size();

        // Get the pinnedVintage
        restPinnedVintageMockMvc.perform(delete("/api/pinned-vintages/{id}", pinnedVintage.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean pinnedVintageExistsInEs = pinnedVintageSearchRepository.exists(pinnedVintage.getId());
        assertThat(pinnedVintageExistsInEs).isFalse();

        // Validate the database is empty
        List<PinnedVintage> pinnedVintageList = pinnedVintageRepository.findAll();
        assertThat(pinnedVintageList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchPinnedVintage() throws Exception {
        // Initialize the database
        pinnedVintageRepository.saveAndFlush(pinnedVintage);
        pinnedVintageSearchRepository.save(pinnedVintage);

        // Search the pinnedVintage
        restPinnedVintageMockMvc.perform(get("/api/_search/pinned-vintages?query=id:" + pinnedVintage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pinnedVintage.getId().intValue())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.intValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PinnedVintage.class);
        PinnedVintage pinnedVintage1 = new PinnedVintage();
        pinnedVintage1.setId(1L);
        PinnedVintage pinnedVintage2 = new PinnedVintage();
        pinnedVintage2.setId(pinnedVintage1.getId());
        assertThat(pinnedVintage1).isEqualTo(pinnedVintage2);
        pinnedVintage2.setId(2L);
        assertThat(pinnedVintage1).isNotEqualTo(pinnedVintage2);
        pinnedVintage1.setId(null);
        assertThat(pinnedVintage1).isNotEqualTo(pinnedVintage2);
    }
}
