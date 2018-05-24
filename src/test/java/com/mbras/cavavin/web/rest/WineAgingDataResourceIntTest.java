package com.mbras.cavavin.web.rest;

import com.mbras.cavavin.CavavinApp;

import com.mbras.cavavin.domain.WineAgingData;
import com.mbras.cavavin.domain.Color;
import com.mbras.cavavin.domain.Region;
import com.mbras.cavavin.repository.WineAgingDataRepository;
import com.mbras.cavavin.repository.search.WineAgingDataSearchRepository;
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

import static com.mbras.cavavin.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the WineAgingDataResource REST controller.
 *
 * @see WineAgingDataResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CavavinApp.class)
public class WineAgingDataResourceIntTest {

    private static final Integer DEFAULT_MIN_KEEP = 1;
    private static final Integer UPDATED_MIN_KEEP = 2;

    private static final Integer DEFAULT_MAX_KEEP = 1;
    private static final Integer UPDATED_MAX_KEEP = 2;

    @Autowired
    private WineAgingDataRepository wineAgingDataRepository;

    @Autowired
    private WineAgingDataSearchRepository wineAgingDataSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restWineAgingDataMockMvc;

    private WineAgingData wineAgingData;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final WineAgingDataResource wineAgingDataResource = new WineAgingDataResource(wineAgingDataRepository, wineAgingDataSearchRepository);
        this.restWineAgingDataMockMvc = MockMvcBuilders.standaloneSetup(wineAgingDataResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WineAgingData createEntity(EntityManager em) {
        WineAgingData wineAgingData = new WineAgingData()
            .minKeep(DEFAULT_MIN_KEEP)
            .maxKeep(DEFAULT_MAX_KEEP);
        // Add required entity
        Color color = ColorResourceIntTest.createEntity(em);
        em.persist(color);
        em.flush();
        wineAgingData.setColor(color);
        // Add required entity
        Region region = RegionResourceIntTest.createEntity(em);
        em.persist(region);
        em.flush();
        wineAgingData.setRegion(region);
        return wineAgingData;
    }

    @Before
    public void initTest() {
        wineAgingDataSearchRepository.deleteAll();
        wineAgingData = createEntity(em);
    }

    @Test
    @Transactional
    public void createWineAgingData() throws Exception {
        int databaseSizeBeforeCreate = wineAgingDataRepository.findAll().size();

        // Create the WineAgingData
        restWineAgingDataMockMvc.perform(post("/api/wine-aging-data")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(wineAgingData)))
            .andExpect(status().isCreated());

        // Validate the WineAgingData in the database
        List<WineAgingData> wineAgingDataList = wineAgingDataRepository.findAll();
        assertThat(wineAgingDataList).hasSize(databaseSizeBeforeCreate + 1);
        WineAgingData testWineAgingData = wineAgingDataList.get(wineAgingDataList.size() - 1);
        assertThat(testWineAgingData.getMinKeep()).isEqualTo(DEFAULT_MIN_KEEP);
        assertThat(testWineAgingData.getMaxKeep()).isEqualTo(DEFAULT_MAX_KEEP);

        // Validate the WineAgingData in Elasticsearch
        WineAgingData wineAgingDataEs = wineAgingDataSearchRepository.findOne(testWineAgingData.getId());
        assertThat(wineAgingDataEs).isEqualToComparingFieldByField(testWineAgingData);
    }

    @Test
    @Transactional
    public void createWineAgingDataWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = wineAgingDataRepository.findAll().size();

        // Create the WineAgingData with an existing ID
        wineAgingData.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restWineAgingDataMockMvc.perform(post("/api/wine-aging-data")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(wineAgingData)))
            .andExpect(status().isBadRequest());

        // Validate the WineAgingData in the database
        List<WineAgingData> wineAgingDataList = wineAgingDataRepository.findAll();
        assertThat(wineAgingDataList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllWineAgingData() throws Exception {
        // Initialize the database
        wineAgingDataRepository.saveAndFlush(wineAgingData);

        // Get all the wineAgingDataList
        restWineAgingDataMockMvc.perform(get("/api/wine-aging-data?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(wineAgingData.getId().intValue())))
            .andExpect(jsonPath("$.[*].minKeep").value(hasItem(DEFAULT_MIN_KEEP)))
            .andExpect(jsonPath("$.[*].maxKeep").value(hasItem(DEFAULT_MAX_KEEP)));
    }

    @Test
    @Transactional
    public void getWineAgingData() throws Exception {
        // Initialize the database
        wineAgingDataRepository.saveAndFlush(wineAgingData);

        // Get the wineAgingData
        restWineAgingDataMockMvc.perform(get("/api/wine-aging-data/{id}", wineAgingData.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(wineAgingData.getId().intValue()))
            .andExpect(jsonPath("$.minKeep").value(DEFAULT_MIN_KEEP))
            .andExpect(jsonPath("$.maxKeep").value(DEFAULT_MAX_KEEP));
    }

    @Test
    @Transactional
    public void getNonExistingWineAgingData() throws Exception {
        // Get the wineAgingData
        restWineAgingDataMockMvc.perform(get("/api/wine-aging-data/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateWineAgingData() throws Exception {
        // Initialize the database
        wineAgingDataRepository.saveAndFlush(wineAgingData);
        wineAgingDataSearchRepository.save(wineAgingData);
        int databaseSizeBeforeUpdate = wineAgingDataRepository.findAll().size();

        // Update the wineAgingData
        WineAgingData updatedWineAgingData = wineAgingDataRepository.findOne(wineAgingData.getId());
        // Disconnect from session so that the updates on updatedWineAgingData are not directly saved in db
        em.detach(updatedWineAgingData);
        updatedWineAgingData
            .minKeep(UPDATED_MIN_KEEP)
            .maxKeep(UPDATED_MAX_KEEP);

        restWineAgingDataMockMvc.perform(put("/api/wine-aging-data")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedWineAgingData)))
            .andExpect(status().isOk());

        // Validate the WineAgingData in the database
        List<WineAgingData> wineAgingDataList = wineAgingDataRepository.findAll();
        assertThat(wineAgingDataList).hasSize(databaseSizeBeforeUpdate);
        WineAgingData testWineAgingData = wineAgingDataList.get(wineAgingDataList.size() - 1);
        assertThat(testWineAgingData.getMinKeep()).isEqualTo(UPDATED_MIN_KEEP);
        assertThat(testWineAgingData.getMaxKeep()).isEqualTo(UPDATED_MAX_KEEP);

        // Validate the WineAgingData in Elasticsearch
        WineAgingData wineAgingDataEs = wineAgingDataSearchRepository.findOne(testWineAgingData.getId());
        assertThat(wineAgingDataEs).isEqualToComparingFieldByField(testWineAgingData);
    }

    @Test
    @Transactional
    public void updateNonExistingWineAgingData() throws Exception {
        int databaseSizeBeforeUpdate = wineAgingDataRepository.findAll().size();

        // Create the WineAgingData

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restWineAgingDataMockMvc.perform(put("/api/wine-aging-data")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(wineAgingData)))
            .andExpect(status().isCreated());

        // Validate the WineAgingData in the database
        List<WineAgingData> wineAgingDataList = wineAgingDataRepository.findAll();
        assertThat(wineAgingDataList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteWineAgingData() throws Exception {
        // Initialize the database
        wineAgingDataRepository.saveAndFlush(wineAgingData);
        wineAgingDataSearchRepository.save(wineAgingData);
        int databaseSizeBeforeDelete = wineAgingDataRepository.findAll().size();

        // Get the wineAgingData
        restWineAgingDataMockMvc.perform(delete("/api/wine-aging-data/{id}", wineAgingData.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean wineAgingDataExistsInEs = wineAgingDataSearchRepository.exists(wineAgingData.getId());
        assertThat(wineAgingDataExistsInEs).isFalse();

        // Validate the database is empty
        List<WineAgingData> wineAgingDataList = wineAgingDataRepository.findAll();
        assertThat(wineAgingDataList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchWineAgingData() throws Exception {
        // Initialize the database
        wineAgingDataRepository.saveAndFlush(wineAgingData);
        wineAgingDataSearchRepository.save(wineAgingData);

        // Search the wineAgingData
        restWineAgingDataMockMvc.perform(get("/api/_search/wine-aging-data?query=id:" + wineAgingData.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(wineAgingData.getId().intValue())))
            .andExpect(jsonPath("$.[*].minKeep").value(hasItem(DEFAULT_MIN_KEEP)))
            .andExpect(jsonPath("$.[*].maxKeep").value(hasItem(DEFAULT_MAX_KEEP)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WineAgingData.class);
        WineAgingData wineAgingData1 = new WineAgingData();
        wineAgingData1.setId(1L);
        WineAgingData wineAgingData2 = new WineAgingData();
        wineAgingData2.setId(wineAgingData1.getId());
        assertThat(wineAgingData1).isEqualTo(wineAgingData2);
        wineAgingData2.setId(2L);
        assertThat(wineAgingData1).isNotEqualTo(wineAgingData2);
        wineAgingData1.setId(null);
        assertThat(wineAgingData1).isNotEqualTo(wineAgingData2);
    }
}
