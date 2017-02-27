package com.mbras.cellar.web.rest;

import com.mbras.cellar.CavaVinApp;

import com.mbras.cellar.domain.Cellar;
import com.mbras.cellar.repository.CellarRepository;
import com.mbras.cellar.service.CellarService;
import com.mbras.cellar.repository.search.CellarSearchRepository;
import com.mbras.cellar.service.dto.CellarDTO;
import com.mbras.cellar.service.mapper.CellarMapper;

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
import org.springframework.web.util.NestedServletException;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the CellarResource REST controller.
 *
 * @see CellarResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CavaVinApp.class)
public class CellarResourceIntTest {

    private static final Integer DEFAULT_CAPACITY = 1;
    private static final Integer UPDATED_CAPACITY = 2;

    @Inject
    private CellarRepository cellarRepository;

    @Inject
    private CellarMapper cellarMapper;

    @Inject
    private CellarService cellarService;

    @Inject
    private CellarSearchRepository cellarSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restCellarMockMvc;

    private Cellar cellar;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CellarResource cellarResource = new CellarResource();
        ReflectionTestUtils.setField(cellarResource, "cellarService", cellarService);
        this.restCellarMockMvc = MockMvcBuilders.standaloneSetup(cellarResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cellar createEntity(EntityManager em) {
        Cellar cellar = new Cellar()
                .capacity(DEFAULT_CAPACITY);
        return cellar;
    }

    @Before
    public void initTest() {
        cellarSearchRepository.deleteAll();
        cellar = createEntity(em);
    }

    @Test
    @Transactional
    public void createCellar() throws Exception {
        int databaseSizeBeforeCreate = cellarRepository.findAll().size();

        // Create the Cellar
        CellarDTO cellarDTO = cellarMapper.cellarToCellarDTO(cellar);

        restCellarMockMvc.perform(post("/api/cellars")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cellarDTO)))
            .andExpect(status().isCreated());

        // Validate the Cellar in the database
        List<Cellar> cellars = cellarRepository.findAll();
        assertThat(cellars).hasSize(databaseSizeBeforeCreate + 1);
        Cellar testCellar = cellars.get(cellars.size() - 1);
        assertThat(testCellar.getCapacity()).isEqualTo(DEFAULT_CAPACITY);

        // Validate the Cellar in ElasticSearch
        Cellar cellarEs = cellarSearchRepository.findOne(testCellar.getId());
        assertThat(cellarEs).isEqualToComparingFieldByField(testCellar);
    }

    @Test
    @Transactional
    public void getAllCellars() throws Exception {
        // Initialize the database
        cellarRepository.saveAndFlush(cellar);

        // Get all the cellars
        restCellarMockMvc.perform(get("/api/cellars?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cellar.getId().intValue())))
            .andExpect(jsonPath("$.[*].capacity").value(hasItem(DEFAULT_CAPACITY)));
    }

    @Test(expected = NestedServletException.class)
    @Transactional
    public void getCellar() throws Exception {
        // Initialize the database
        cellarRepository.saveAndFlush(cellar);

        // Get the cellar
        restCellarMockMvc.perform(get("/api/cellars/{id}", cellar.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(cellar.getId().intValue()))
            .andExpect(jsonPath("$.capacity").value(DEFAULT_CAPACITY));
    }

    @Test(expected = NestedServletException.class)
    @Transactional
    public void getNonExistingCellar() throws Exception {
        // Get the cellar
        restCellarMockMvc.perform(get("/api/cellars/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCellar() throws Exception {
        // Initialize the database
        cellarRepository.saveAndFlush(cellar);
        cellarSearchRepository.save(cellar);
        int databaseSizeBeforeUpdate = cellarRepository.findAll().size();

        // Update the cellar
        Cellar updatedCellar = cellarRepository.findOne(cellar.getId());
        updatedCellar
                .capacity(UPDATED_CAPACITY);
        CellarDTO cellarDTO = cellarMapper.cellarToCellarDTO(updatedCellar);

        restCellarMockMvc.perform(put("/api/cellars")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cellarDTO)))
            .andExpect(status().isOk());

        // Validate the Cellar in the database
        List<Cellar> cellars = cellarRepository.findAll();
        assertThat(cellars).hasSize(databaseSizeBeforeUpdate);
        Cellar testCellar = cellars.get(cellars.size() - 1);
        assertThat(testCellar.getCapacity()).isEqualTo(UPDATED_CAPACITY);

        // Validate the Cellar in ElasticSearch
        Cellar cellarEs = cellarSearchRepository.findOne(testCellar.getId());
        assertThat(cellarEs).isEqualToComparingFieldByField(testCellar);
    }

    @Test
    @Transactional
    public void deleteCellar() throws Exception {
        // Initialize the database
        cellarRepository.saveAndFlush(cellar);
        cellarSearchRepository.save(cellar);
        int databaseSizeBeforeDelete = cellarRepository.findAll().size();

        // Get the cellar
        restCellarMockMvc.perform(delete("/api/cellars/{id}", cellar.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean cellarExistsInEs = cellarSearchRepository.exists(cellar.getId());
        assertThat(cellarExistsInEs).isFalse();

        // Validate the database is empty
        List<Cellar> cellars = cellarRepository.findAll();
        assertThat(cellars).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchCellar() throws Exception {
        // Initialize the database
        cellarRepository.saveAndFlush(cellar);
        cellarSearchRepository.save(cellar);

        // Search the cellar
        restCellarMockMvc.perform(get("/api/_search/cellars?query=id:" + cellar.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cellar.getId().intValue())))
            .andExpect(jsonPath("$.[*].capacity").value(hasItem(DEFAULT_CAPACITY)));
    }
}
