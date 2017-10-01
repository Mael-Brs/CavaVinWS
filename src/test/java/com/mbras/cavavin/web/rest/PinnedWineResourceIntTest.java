package com.mbras.cavavin.web.rest;

import com.mbras.cavavin.CavavinApp;
import com.mbras.cavavin.domain.PinnedWine;
import com.mbras.cavavin.domain.Wine;
import com.mbras.cavavin.repository.PinnedWineRepository;
import com.mbras.cavavin.repository.search.PinnedWineSearchRepository;
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
 * Test class for the PinnedWineResource REST controller.
 *
 * @see PinnedWineResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CavavinApp.class)
public class PinnedWineResourceIntTest {

    private static final Long DEFAULT_USER_ID = 1L;
    private static final Long UPDATED_USER_ID = 2L;

    @Autowired
    private PinnedWineRepository pinnedWineRepository;

    @Autowired
    private PinnedWineSearchRepository pinnedWineSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPinnedWineMockMvc;

    private PinnedWine pinnedWine;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PinnedWineResource pinnedWineResource = new PinnedWineResource(pinnedWineRepository, pinnedWineSearchRepository);
        this.restPinnedWineMockMvc = MockMvcBuilders.standaloneSetup(pinnedWineResource)
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
    public static PinnedWine createEntity(EntityManager em) {
        PinnedWine pinnedWine = new PinnedWine()
            .userId(DEFAULT_USER_ID);
        // Add required entity
        Wine wine = WineResourceIntTest.createEntity(em);
        em.persist(wine);
        em.flush();
        pinnedWine.setWine(wine);
        return pinnedWine;
    }

    @Before
    public void initTest() {
        pinnedWineSearchRepository.deleteAll();
        pinnedWine = createEntity(em);
    }

    @Test
    @Transactional
    public void createPinnedWine() throws Exception {
        int databaseSizeBeforeCreate = pinnedWineRepository.findAll().size();

        // Create the PinnedWine
        restPinnedWineMockMvc.perform(post("/api/pinned-wines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pinnedWine)))
            .andExpect(status().isCreated());

        // Validate the PinnedWine in the database
        List<PinnedWine> pinnedWineList = pinnedWineRepository.findAll();
        assertThat(pinnedWineList).hasSize(databaseSizeBeforeCreate + 1);
        PinnedWine testPinnedWine = pinnedWineList.get(pinnedWineList.size() - 1);
        assertThat(testPinnedWine.getUserId()).isEqualTo(DEFAULT_USER_ID);

        // Validate the PinnedWine in Elasticsearch
        PinnedWine pinnedWineEs = pinnedWineSearchRepository.findOne(testPinnedWine.getId());
        assertThat(pinnedWineEs).isEqualToComparingFieldByField(testPinnedWine);
    }

    @Test
    @Transactional
    public void createPinnedWineWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = pinnedWineRepository.findAll().size();

        // Create the PinnedWine with an existing ID
        pinnedWine.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPinnedWineMockMvc.perform(post("/api/pinned-wines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pinnedWine)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<PinnedWine> pinnedWineList = pinnedWineRepository.findAll();
        assertThat(pinnedWineList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkUserIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = pinnedWineRepository.findAll().size();
        // set the field null
        pinnedWine.setUserId(null);

        // Create the PinnedWine, which fails.

        restPinnedWineMockMvc.perform(post("/api/pinned-wines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pinnedWine)))
            .andExpect(status().isBadRequest());

        List<PinnedWine> pinnedWineList = pinnedWineRepository.findAll();
        assertThat(pinnedWineList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    @WithMockUser("system")
    public void getAllPinnedWines() throws Exception {
        // Initialize the database
        pinnedWineRepository.saveAndFlush(pinnedWine);

        // Get all the pinnedWineList
        restPinnedWineMockMvc.perform(get("/api/pinned-wines?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pinnedWine.getId().intValue())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.intValue())));
    }

    @Test
    @Transactional
    public void getPinnedWine() throws Exception {
        // Initialize the database
        pinnedWineRepository.saveAndFlush(pinnedWine);

        // Get the pinnedWine
        restPinnedWineMockMvc.perform(get("/api/pinned-wines/{id}", pinnedWine.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(pinnedWine.getId().intValue()))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingPinnedWine() throws Exception {
        // Get the pinnedWine
        restPinnedWineMockMvc.perform(get("/api/pinned-wines/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePinnedWine() throws Exception {
        // Initialize the database
        pinnedWineRepository.saveAndFlush(pinnedWine);
        pinnedWineSearchRepository.save(pinnedWine);
        int databaseSizeBeforeUpdate = pinnedWineRepository.findAll().size();

        // Update the pinnedWine
        PinnedWine updatedPinnedWine = pinnedWineRepository.findOne(pinnedWine.getId());
        updatedPinnedWine
            .userId(UPDATED_USER_ID);

        restPinnedWineMockMvc.perform(put("/api/pinned-wines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPinnedWine)))
            .andExpect(status().isOk());

        // Validate the PinnedWine in the database
        List<PinnedWine> pinnedWineList = pinnedWineRepository.findAll();
        assertThat(pinnedWineList).hasSize(databaseSizeBeforeUpdate);
        PinnedWine testPinnedWine = pinnedWineList.get(pinnedWineList.size() - 1);
        assertThat(testPinnedWine.getUserId()).isEqualTo(UPDATED_USER_ID);

        // Validate the PinnedWine in Elasticsearch
        PinnedWine pinnedWineEs = pinnedWineSearchRepository.findOne(testPinnedWine.getId());
        assertThat(pinnedWineEs).isEqualToComparingFieldByField(testPinnedWine);
    }

    @Test
    @Transactional
    public void updateNonExistingPinnedWine() throws Exception {
        int databaseSizeBeforeUpdate = pinnedWineRepository.findAll().size();

        // Create the PinnedWine

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPinnedWineMockMvc.perform(put("/api/pinned-wines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pinnedWine)))
            .andExpect(status().isCreated());

        // Validate the PinnedWine in the database
        List<PinnedWine> pinnedWineList = pinnedWineRepository.findAll();
        assertThat(pinnedWineList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deletePinnedWine() throws Exception {
        // Initialize the database
        pinnedWineRepository.saveAndFlush(pinnedWine);
        pinnedWineSearchRepository.save(pinnedWine);
        int databaseSizeBeforeDelete = pinnedWineRepository.findAll().size();

        // Get the pinnedWine
        restPinnedWineMockMvc.perform(delete("/api/pinned-wines/{id}", pinnedWine.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean pinnedWineExistsInEs = pinnedWineSearchRepository.exists(pinnedWine.getId());
        assertThat(pinnedWineExistsInEs).isFalse();

        // Validate the database is empty
        List<PinnedWine> pinnedWineList = pinnedWineRepository.findAll();
        assertThat(pinnedWineList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchPinnedWine() throws Exception {
        // Initialize the database
        pinnedWineRepository.saveAndFlush(pinnedWine);
        pinnedWineSearchRepository.save(pinnedWine);

        // Search the pinnedWine
        restPinnedWineMockMvc.perform(get("/api/_search/pinned-wines?query=id:" + pinnedWine.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pinnedWine.getId().intValue())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.intValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PinnedWine.class);
        PinnedWine pinnedWine1 = new PinnedWine();
        pinnedWine1.setId(1L);
        PinnedWine pinnedWine2 = new PinnedWine();
        pinnedWine2.setId(pinnedWine1.getId());
        assertThat(pinnedWine1).isEqualTo(pinnedWine2);
        pinnedWine2.setId(2L);
        assertThat(pinnedWine1).isNotEqualTo(pinnedWine2);
        pinnedWine1.setId(null);
        assertThat(pinnedWine1).isNotEqualTo(pinnedWine2);
    }
}
