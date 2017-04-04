package com.mbras.cavavin.web.rest;

import com.mbras.cavavin.CavavinApp;

import com.mbras.cavavin.domain.Vintage;
import com.mbras.cavavin.domain.Wine;
import com.mbras.cavavin.domain.WineInCellar;
import com.mbras.cavavin.repository.WineInCellarRepository;
import com.mbras.cavavin.service.WineInCellarService;
import com.mbras.cavavin.repository.search.WineInCellarSearchRepository;
import com.mbras.cavavin.service.dto.WineInCellarDTO;
import com.mbras.cavavin.service.mapper.WineInCellarMapper;
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
 * Test class for the WineInCellarResource REST controller.
 *
 * @see WineInCellarResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CavavinApp.class)
public class WineInCellarResourceIntTest {

    private static final Integer DEFAULT_MIN_KEEP = 1;
    private static final Integer UPDATED_MIN_KEEP = 2;

    private static final Integer DEFAULT_MAX_KEEP = 1;
    private static final Integer UPDATED_MAX_KEEP = 2;

    private static final Double DEFAULT_PRICE = 1D;
    private static final Double UPDATED_PRICE = 2D;

    private static final Integer DEFAULT_QUANTITY = 1;
    private static final Integer UPDATED_QUANTITY = 2;

    private static final String DEFAULT_COMMENTS = "AAAAAAAAAA";
    private static final String UPDATED_COMMENTS = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_APPELLATION = "AAAAAAAAAA";

    private static final String DEFAULT_PRODUCER = "AAAAAAAAAA";

    private static final Long DEFAULT_CREATOR_ID = 1L;


    @Autowired
    private WineInCellarRepository wineInCellarRepository;

    @Autowired
    private WineInCellarMapper wineInCellarMapper;

    @Autowired
    private WineInCellarService wineInCellarService;

    @Autowired
    private WineInCellarSearchRepository wineInCellarSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restWineInCellarMockMvc;

    private WineInCellar wineInCellar;

    private Wine wine;

    private Vintage vintage;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        WineInCellarResource wineInCellarResource = new WineInCellarResource(wineInCellarService);
        this.restWineInCellarMockMvc = MockMvcBuilders.standaloneSetup(wineInCellarResource)
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
    public static WineInCellar createEntity(EntityManager em) {
        WineInCellar wineInCellar = new WineInCellar()
                .minKeep(DEFAULT_MIN_KEEP)
                .maxKeep(DEFAULT_MAX_KEEP)
                .price(DEFAULT_PRICE)
                .quantity(DEFAULT_QUANTITY)
                .comments(DEFAULT_COMMENTS);
        return wineInCellar;
    }

    /**
     * Create a wine for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Wine createWine(EntityManager em) {
        Wine wine = new Wine()
            .name(DEFAULT_NAME)
            .appellation(DEFAULT_APPELLATION)
            .producer(DEFAULT_PRODUCER)
            .creatorId(DEFAULT_CREATOR_ID);
        return wine;
    }

    @Before
    public void initTest() {
        wineInCellarSearchRepository.deleteAll();
        wineInCellar = createEntity(em);
        wine = createWine(em);
        vintage = new Vintage();
        vintage.setWine(wine);
    }

    @Test
    @Transactional
    public void createWineInCellar() throws Exception {
        int databaseSizeBeforeCreate = wineInCellarRepository.findAll().size();

        // Create the WineInCellar
        WineInCellarDTO wineInCellarDTO = wineInCellarMapper.wineInCellarToWineInCellarDTO(wineInCellar);

        restWineInCellarMockMvc.perform(post("/api/wine-in-cellars")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(wineInCellarDTO)))
            .andExpect(status().isCreated());

        // Validate the WineInCellar in the database
        List<WineInCellar> wineInCellarList = wineInCellarRepository.findAll();
        assertThat(wineInCellarList).hasSize(databaseSizeBeforeCreate + 1);
        WineInCellar testWineInCellar = wineInCellarList.get(wineInCellarList.size() - 1);
        assertThat(testWineInCellar.getMinKeep()).isEqualTo(DEFAULT_MIN_KEEP);
        assertThat(testWineInCellar.getMaxKeep()).isEqualTo(DEFAULT_MAX_KEEP);
        assertThat(testWineInCellar.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testWineInCellar.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
        assertThat(testWineInCellar.getComments()).isEqualTo(DEFAULT_COMMENTS);

        // Validate the WineInCellar in Elasticsearch
        WineInCellar wineInCellarEs = wineInCellarSearchRepository.findOne(testWineInCellar.getId());
        assertThat(wineInCellarEs).isEqualToComparingFieldByField(testWineInCellar);
    }

    @Test
    @Transactional
    public void createWineInCellarWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = wineInCellarRepository.findAll().size();

        // Create the WineInCellar with an existing ID
        WineInCellar existingWineInCellar = new WineInCellar();
        existingWineInCellar.setId(1L);
        WineInCellarDTO existingWineInCellarDTO = wineInCellarMapper.wineInCellarToWineInCellarDTO(existingWineInCellar);

        // An entity with an existing ID cannot be created, so this API call must fail
        restWineInCellarMockMvc.perform(post("/api/wine-in-cellars")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingWineInCellarDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<WineInCellar> wineInCellarList = wineInCellarRepository.findAll();
        assertThat(wineInCellarList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllWineInCellars() throws Exception {
        // Initialize the database
        wineInCellarRepository.saveAndFlush(wineInCellar);

        // Get all the wineInCellarList
        restWineInCellarMockMvc.perform(get("/api/wine-in-cellars?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(wineInCellar.getId().intValue())))
            .andExpect(jsonPath("$.[*].minKeep").value(hasItem(DEFAULT_MIN_KEEP)))
            .andExpect(jsonPath("$.[*].maxKeep").value(hasItem(DEFAULT_MAX_KEEP)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].comments").value(hasItem(DEFAULT_COMMENTS.toString())));
    }

    @Test
    @Transactional
    public void getWineInCellar() throws Exception {
        // Initialize the database
        wineInCellarRepository.saveAndFlush(wineInCellar);

        // Get the wineInCellar
        restWineInCellarMockMvc.perform(get("/api/wine-in-cellars/{id}", wineInCellar.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(wineInCellar.getId().intValue()))
            .andExpect(jsonPath("$.minKeep").value(DEFAULT_MIN_KEEP))
            .andExpect(jsonPath("$.maxKeep").value(DEFAULT_MAX_KEEP))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.doubleValue()))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY))
            .andExpect(jsonPath("$.comments").value(DEFAULT_COMMENTS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingWineInCellar() throws Exception {
        // Get the wineInCellar
        restWineInCellarMockMvc.perform(get("/api/wine-in-cellars/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateWineInCellar() throws Exception {
        // Initialize the database
        wineInCellarRepository.saveAndFlush(wineInCellar);
        wineInCellarSearchRepository.save(wineInCellar);
        int databaseSizeBeforeUpdate = wineInCellarRepository.findAll().size();

        // Update the wineInCellar
        WineInCellar updatedWineInCellar = wineInCellarRepository.findOne(wineInCellar.getId());
        updatedWineInCellar
                .minKeep(UPDATED_MIN_KEEP)
                .maxKeep(UPDATED_MAX_KEEP)
                .price(UPDATED_PRICE)
                .quantity(UPDATED_QUANTITY)
                .comments(UPDATED_COMMENTS);
        WineInCellarDTO wineInCellarDTO = wineInCellarMapper.wineInCellarToWineInCellarDTO(updatedWineInCellar);

        restWineInCellarMockMvc.perform(put("/api/wine-in-cellars")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(wineInCellarDTO)))
            .andExpect(status().isOk());

        // Validate the WineInCellar in the database
        List<WineInCellar> wineInCellarList = wineInCellarRepository.findAll();
        assertThat(wineInCellarList).hasSize(databaseSizeBeforeUpdate);
        WineInCellar testWineInCellar = wineInCellarList.get(wineInCellarList.size() - 1);
        assertThat(testWineInCellar.getMinKeep()).isEqualTo(UPDATED_MIN_KEEP);
        assertThat(testWineInCellar.getMaxKeep()).isEqualTo(UPDATED_MAX_KEEP);
        assertThat(testWineInCellar.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testWineInCellar.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testWineInCellar.getComments()).isEqualTo(UPDATED_COMMENTS);

        // Validate the WineInCellar in Elasticsearch
        WineInCellar wineInCellarEs = wineInCellarSearchRepository.findOne(testWineInCellar.getId());
        assertThat(wineInCellarEs).isEqualToComparingFieldByField(testWineInCellar);
    }

    @Test
    @Transactional
    public void updateNonExistingWineInCellar() throws Exception {
        int databaseSizeBeforeUpdate = wineInCellarRepository.findAll().size();

        // Create the WineInCellar
        WineInCellarDTO wineInCellarDTO = wineInCellarMapper.wineInCellarToWineInCellarDTO(wineInCellar);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restWineInCellarMockMvc.perform(put("/api/wine-in-cellars")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(wineInCellarDTO)))
            .andExpect(status().isCreated());

        // Validate the WineInCellar in the database
        List<WineInCellar> wineInCellarList = wineInCellarRepository.findAll();
        assertThat(wineInCellarList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteWineInCellar() throws Exception {
        // Initialize the database
        wineInCellarRepository.saveAndFlush(wineInCellar);
        wineInCellarSearchRepository.save(wineInCellar);
        int databaseSizeBeforeDelete = wineInCellarRepository.findAll().size();

        // Get the wineInCellar
        restWineInCellarMockMvc.perform(delete("/api/wine-in-cellars/{id}", wineInCellar.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean wineInCellarExistsInEs = wineInCellarSearchRepository.exists(wineInCellar.getId());
        assertThat(wineInCellarExistsInEs).isFalse();

        // Validate the database is empty
        List<WineInCellar> wineInCellarList = wineInCellarRepository.findAll();
        assertThat(wineInCellarList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchWineInCellar() throws Exception {
        // Initialize the database
        wineInCellarRepository.saveAndFlush(wineInCellar);
        wineInCellarSearchRepository.save(wineInCellar);

        // Search the wineInCellar
        restWineInCellarMockMvc.perform(get("/api/_search/wine-in-cellars?query=id:" + wineInCellar.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(wineInCellar.getId().intValue())))
            .andExpect(jsonPath("$.[*].minKeep").value(hasItem(DEFAULT_MIN_KEEP)))
            .andExpect(jsonPath("$.[*].maxKeep").value(hasItem(DEFAULT_MAX_KEEP)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].comments").value(hasItem(DEFAULT_COMMENTS.toString())));
    }

    @Test
    @Transactional
    public void createWineInCellarFromScratch() throws Exception {
        int databaseSizeBeforeCreate = wineInCellarRepository.findAll().size();

        wineInCellar.setVintage(vintage);
        // Create the WineInCellar
        WineInCellarDTO wineInCellarDTO = wineInCellarMapper.wineInCellarToWineInCellarDTO(wineInCellar);

        restWineInCellarMockMvc.perform(post("/api/wine-in-cellars/all")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(wineInCellarDTO)))
            .andExpect(status().isCreated());

        // Validate the WineInCellar in the database
        List<WineInCellar> wineInCellarList = wineInCellarRepository.findAll();
        assertThat(wineInCellarList).hasSize(databaseSizeBeforeCreate + 1);
        WineInCellar testWineInCellar = wineInCellarList.get(wineInCellarList.size() - 1);
        assertThat(testWineInCellar.getMinKeep()).isEqualTo(DEFAULT_MIN_KEEP);
        assertThat(testWineInCellar.getMaxKeep()).isEqualTo(DEFAULT_MAX_KEEP);
        assertThat(testWineInCellar.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testWineInCellar.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
        assertThat(testWineInCellar.getComments()).isEqualTo(DEFAULT_COMMENTS);
        assertThat(testWineInCellar.getVintage().getWine().getName()).isEqualTo(DEFAULT_NAME);

        // Validate the WineInCellar in Elasticsearch
        WineInCellar wineInCellarEs = wineInCellarSearchRepository.findOne(testWineInCellar.getId());
        assertThat(wineInCellarEs).isEqualToComparingFieldByField(testWineInCellar);
    }

    @Test
    @Transactional
    public void updateWineInCellarFromScratch() throws Exception {
        // Initialize the database
        vintage.setWine(wine);
        wineInCellar.setVintage(vintage);
        WineInCellarDTO wineInCellarDTO = wineInCellarMapper.wineInCellarToWineInCellarDTO(wineInCellar);
        wineInCellarDTO = wineInCellarService.saveFromScratch(wineInCellarDTO);

        int databaseSizeBeforeUpdate = wineInCellarRepository.findAll().size();

        // Update the wineInCellar
        WineInCellar updatedWineInCellar = wineInCellarRepository.findOne(wineInCellarDTO.getId());
        updatedWineInCellar
            .minKeep(UPDATED_MIN_KEEP)
            .maxKeep(UPDATED_MAX_KEEP)
            .price(UPDATED_PRICE)
            .quantity(UPDATED_QUANTITY)
            .comments(UPDATED_COMMENTS);

        wine.name(UPDATED_NAME);
        vintage.setWine(wine);
        updatedWineInCellar.setVintage(vintage);
        WineInCellarDTO updatedWineInCellarDTO = wineInCellarMapper.wineInCellarToWineInCellarDTO(updatedWineInCellar);

        restWineInCellarMockMvc.perform(put("/api/wine-in-cellars")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedWineInCellarDTO)))
            .andExpect(status().isOk());

        // Validate the WineInCellar in the database
        List<WineInCellar> wineInCellarList = wineInCellarRepository.findAll();
        assertThat(wineInCellarList).hasSize(databaseSizeBeforeUpdate);
        WineInCellar testWineInCellar = wineInCellarList.get(wineInCellarList.size() - 1);
        assertThat(testWineInCellar.getMinKeep()).isEqualTo(UPDATED_MIN_KEEP);
        assertThat(testWineInCellar.getMaxKeep()).isEqualTo(UPDATED_MAX_KEEP);
        assertThat(testWineInCellar.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testWineInCellar.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testWineInCellar.getComments()).isEqualTo(UPDATED_COMMENTS);
        assertThat(testWineInCellar.getVintage().getWine().getName()).isEqualTo(UPDATED_NAME);

        // Validate the WineInCellar in Elasticsearch
        WineInCellar wineInCellarEs = wineInCellarSearchRepository.findOne(testWineInCellar.getId());
        assertThat(wineInCellarEs).isEqualToComparingFieldByField(testWineInCellar);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WineInCellar.class);
    }
}
