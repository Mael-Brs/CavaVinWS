package com.mbras.cellar.web.rest;

import com.mbras.cellar.CavaVinApp;

import com.mbras.cellar.domain.WineInCellar;
import com.mbras.cellar.repository.WineInCellarRepository;
import com.mbras.cellar.service.WineInCellarService;
import com.mbras.cellar.repository.search.WineInCellarSearchRepository;
import com.mbras.cellar.service.dto.WineInCellarDTO;
import com.mbras.cellar.service.mapper.WineInCellarMapper;

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
 * Test class for the WineInCellarResource REST controller.
 *
 * @see WineInCellarResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CavaVinApp.class)
public class WineInCellarResourceIntTest {

    private static final Double DEFAULT_PRICE = 1D;
    private static final Double UPDATED_PRICE = 2D;

    private static final Integer DEFAULT_QUANTITY = 1;
    private static final Integer UPDATED_QUANTITY = 2;

    @Inject
    private WineInCellarRepository wineInCellarRepository;

    @Inject
    private WineInCellarMapper wineInCellarMapper;

    @Inject
    private WineInCellarService wineInCellarService;

    @Inject
    private WineInCellarSearchRepository wineInCellarSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restWineInCellarMockMvc;

    private WineInCellar wineInCellar;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        WineInCellarResource wineInCellarResource = new WineInCellarResource();
        ReflectionTestUtils.setField(wineInCellarResource, "wineInCellarService", wineInCellarService);
        this.restWineInCellarMockMvc = MockMvcBuilders.standaloneSetup(wineInCellarResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
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
                .price(DEFAULT_PRICE)
                .quantity(DEFAULT_QUANTITY);
        return wineInCellar;
    }

    @Before
    public void initTest() {
        wineInCellarSearchRepository.deleteAll();
        wineInCellar = createEntity(em);
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
        List<WineInCellar> wineInCellars = wineInCellarRepository.findAll();
        assertThat(wineInCellars).hasSize(databaseSizeBeforeCreate + 1);
        WineInCellar testWineInCellar = wineInCellars.get(wineInCellars.size() - 1);
        assertThat(testWineInCellar.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testWineInCellar.getQuantity()).isEqualTo(DEFAULT_QUANTITY);

        // Validate the WineInCellar in ElasticSearch
        WineInCellar wineInCellarEs = wineInCellarSearchRepository.findOne(testWineInCellar.getId());
        assertThat(wineInCellarEs).isEqualToComparingFieldByField(testWineInCellar);
    }

    @Test
    @Transactional
    public void getAllWineInCellars() throws Exception {
        // Initialize the database
        wineInCellarRepository.saveAndFlush(wineInCellar);

        // Get all the wineInCellars
        restWineInCellarMockMvc.perform(get("/api/wine-in-cellars?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(wineInCellar.getId().intValue())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)));
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
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.doubleValue()))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY));
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
                .price(UPDATED_PRICE)
                .quantity(UPDATED_QUANTITY);
        WineInCellarDTO wineInCellarDTO = wineInCellarMapper.wineInCellarToWineInCellarDTO(updatedWineInCellar);

        restWineInCellarMockMvc.perform(put("/api/wine-in-cellars")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(wineInCellarDTO)))
            .andExpect(status().isOk());

        // Validate the WineInCellar in the database
        List<WineInCellar> wineInCellars = wineInCellarRepository.findAll();
        assertThat(wineInCellars).hasSize(databaseSizeBeforeUpdate);
        WineInCellar testWineInCellar = wineInCellars.get(wineInCellars.size() - 1);
        assertThat(testWineInCellar.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testWineInCellar.getQuantity()).isEqualTo(UPDATED_QUANTITY);

        // Validate the WineInCellar in ElasticSearch
        WineInCellar wineInCellarEs = wineInCellarSearchRepository.findOne(testWineInCellar.getId());
        assertThat(wineInCellarEs).isEqualToComparingFieldByField(testWineInCellar);
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

        // Validate ElasticSearch is empty
        boolean wineInCellarExistsInEs = wineInCellarSearchRepository.exists(wineInCellar.getId());
        assertThat(wineInCellarExistsInEs).isFalse();

        // Validate the database is empty
        List<WineInCellar> wineInCellars = wineInCellarRepository.findAll();
        assertThat(wineInCellars).hasSize(databaseSizeBeforeDelete - 1);
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
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)));
    }
}
