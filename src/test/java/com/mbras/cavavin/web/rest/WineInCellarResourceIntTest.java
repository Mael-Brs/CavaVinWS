package com.mbras.cavavin.web.rest;

import com.mbras.cavavin.CavavinApp;
import com.mbras.cavavin.domain.*;
import com.mbras.cavavin.repository.WineInCellarRepository;
import com.mbras.cavavin.repository.search.WineInCellarSearchRepository;
import com.mbras.cavavin.service.WineInCellarService;
import com.mbras.cavavin.web.rest.errors.ExceptionTranslator;
import org.hamcrest.Matcher;
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
 * Test class for the WineInCellarResource REST controller.
 *
 * @see WineInCellarResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CavavinApp.class)
public class WineInCellarResourceIntTest {

    private static final Integer DEFAULT_MIN_KEEP = 1;
    public static final int CONFIG_MIN_KEEP = DEFAULT_MIN_KEEP + 1;
    private static final Integer UPDATED_MIN_KEEP = 2;

    private static final Integer DEFAULT_MAX_KEEP = 1;
    private static final Integer UPDATED_MAX_KEEP = 2;

    private static final Double DEFAULT_PRICE = 1D;
    private static final Double UPDATED_PRICE = 2D;

    private static final Integer DEFAULT_QUANTITY = 1;
    private static final Integer UPDATED_QUANTITY = 2;

    private static final String DEFAULT_COMMENTS = "AAAAAAAAAA";
    private static final String UPDATED_COMMENTS = "BBBBBBBBBB";

    private static final String DEFAULT_LOCATION = "AAAAAAAAAA";
    private static final String UPDATED_LOCATION = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_APPELLATION = "AAAAAAAAAA";

    private static final String DEFAULT_PRODUCER = "AAAAAAAAAA";

    private static final Long DEFAULT_CREATOR_ID = 1L;

    @Autowired
    private WineInCellarRepository wineInCellarRepository;

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

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final WineInCellarResource wineInCellarResource = new WineInCellarResource(wineInCellarService);
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
            .comments(DEFAULT_COMMENTS)
            .location(DEFAULT_LOCATION);
        // Add required entity
        Vintage vintage = VintageResourceIntTest.createEntity(em);
        Cellar cellar = CellarResourceIntTest.createEntity(em);
        WineAgingData wineAgingData = new WineAgingData();
        wineAgingData.setColor(vintage.getWine().getColor());
        wineAgingData.setRegion(vintage.getWine().getRegion());
        wineAgingData.setMinKeep(CONFIG_MIN_KEEP);
        wineAgingData.setMaxKeep(DEFAULT_MAX_KEEP + 1);
        em.persist(vintage);
        em.persist(cellar);
        em.persist(wineAgingData);
        em.flush();
        wineInCellar.setVintage(vintage);
        wineInCellar.setCellarId(cellar.getId());
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
        int expectedApogee = DEFAULT_MAX_KEEP + wineInCellar.getVintage().getYear();
        wineInCellar.setMinKeep(null);
        // Create the WineInCellar
        restWineInCellarMockMvc.perform(post("/api/wine-in-cellars")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(wineInCellar)))
            .andExpect(status().isCreated())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.apogee").value(expectedApogee))
            .andExpect(jsonPath("$.maxKeep").value(DEFAULT_MAX_KEEP));

        // Validate the WineInCellar in the database
        List<WineInCellar> wineInCellarList = wineInCellarRepository.findAll();
        assertThat(wineInCellarList).hasSize(databaseSizeBeforeCreate + 1);
        WineInCellar testWineInCellar = wineInCellarList.get(wineInCellarList.size() - 1);
        assertThat(testWineInCellar.getMinKeep()).isEqualTo(CONFIG_MIN_KEEP);
        assertThat(testWineInCellar.getMaxKeep()).isEqualTo(DEFAULT_MAX_KEEP);
        assertThat(testWineInCellar.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testWineInCellar.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
        assertThat(testWineInCellar.getComments()).isEqualTo(DEFAULT_COMMENTS);
        assertThat(testWineInCellar.getLocation()).isEqualTo(DEFAULT_LOCATION);
        assertThat(testWineInCellar.getCellarId()).isEqualTo(wineInCellar.getCellarId().intValue());

        // Validate the WineInCellar in Elasticsearch
        WineInCellar wineInCellarEs = wineInCellarSearchRepository.findOne(testWineInCellar.getId());
        assertThat(wineInCellarEs).isEqualToComparingFieldByField(testWineInCellar);
    }

    @Test
    @Transactional
    public void createWineInCellarWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = wineInCellarRepository.findAll().size();

        // Create the WineInCellar with an existing ID
        wineInCellar.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restWineInCellarMockMvc.perform(post("/api/wine-in-cellars")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(wineInCellar)))
            .andExpect(status().isBadRequest());

        // Validate the WineInCellar in the database
        List<WineInCellar> wineInCellarList = wineInCellarRepository.findAll();
        assertThat(wineInCellarList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkQuantityIsRequired() throws Exception {
        int databaseSizeBeforeTest = wineInCellarRepository.findAll().size();
        // set the field null
        wineInCellar.setQuantity(null);

        // Create the WineInCellar, which fails.

        restWineInCellarMockMvc.perform(post("/api/wine-in-cellars")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(wineInCellar)))
            .andExpect(status().isBadRequest());

        List<WineInCellar> wineInCellarList = wineInCellarRepository.findAll();
        assertThat(wineInCellarList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCellarIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = wineInCellarRepository.findAll().size();
        // set the field null
        wineInCellar.setCellarId(null);

        // Create the WineInCellar, which fails.

        restWineInCellarMockMvc.perform(post("/api/wine-in-cellars")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(wineInCellar)))
            .andExpect(status().isBadRequest());

        List<WineInCellar> wineInCellarList = wineInCellarRepository.findAll();
        assertThat(wineInCellarList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    @WithMockUser("system")
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
            .andExpect(jsonPath("$.[*].comments").value(hasItem(DEFAULT_COMMENTS.toString())))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION.toString())))
            .andExpect(jsonPath("$.[*].apogee").exists())
            .andExpect(jsonPath("$.[*].cellarId").value(hasItem(wineInCellar.getCellarId().intValue())));
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
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY))
            .andExpect(jsonPath("$.comments").value(DEFAULT_COMMENTS))
            .andExpect(jsonPath("$.location").value(DEFAULT_LOCATION))
            .andExpect(jsonPath("$.cellarId").value(wineInCellar.getCellarId().intValue()));
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
    public void findByCellar() throws Exception {
        // Initialize the database
        wineInCellarRepository.saveAndFlush(wineInCellar);

        // Get the wineInCellar
        restWineInCellarMockMvc.perform(get("/api/cellars/{id}/wine-in-cellars", wineInCellar.getCellarId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(wineInCellar.getId().intValue())))
            .andExpect(jsonPath("$.[*].minKeep").value(hasItem(DEFAULT_MIN_KEEP)))
            .andExpect(jsonPath("$.[*].maxKeep").value(hasItem(DEFAULT_MAX_KEEP)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE)))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].comments").value(hasItem(DEFAULT_COMMENTS)))
            .andExpect(jsonPath("$.[*].apogee").exists())
            .andExpect(jsonPath("$.[*].cellarId").value(hasItem(wineInCellar.getCellarId().intValue())));
    }

    @Test
    @Transactional
    public void updateWineInCellar() throws Exception {
        // Initialize the database
        wineInCellarService.save(wineInCellar);

        int databaseSizeBeforeUpdate = wineInCellarRepository.findAll().size();

        // Update the wineInCellar
        WineInCellar updatedWineInCellar = wineInCellarRepository.findOne(wineInCellar.getId());
        em.clear();
        updatedWineInCellar
            .minKeep(UPDATED_MIN_KEEP)
            .maxKeep(UPDATED_MAX_KEEP)
            .price(UPDATED_PRICE)
            .quantity(UPDATED_QUANTITY)
            .comments(UPDATED_COMMENTS)
            .location(UPDATED_LOCATION);

        int expectedApogee = UPDATED_MAX_KEEP + updatedWineInCellar.getVintage().getYear();
        restWineInCellarMockMvc.perform(put("/api/wine-in-cellars")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedWineInCellar)))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.apogee").value(expectedApogee));

        // Validate the WineInCellar in the database
        List<WineInCellar> wineInCellarList = wineInCellarRepository.findAll();
        assertThat(wineInCellarList).hasSize(databaseSizeBeforeUpdate);
        WineInCellar testWineInCellar = wineInCellarList.get(wineInCellarList.size() - 1);
        assertThat(testWineInCellar.getMinKeep()).isEqualTo(UPDATED_MIN_KEEP);
        assertThat(testWineInCellar.getMaxKeep()).isEqualTo(UPDATED_MAX_KEEP);
        assertThat(testWineInCellar.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testWineInCellar.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testWineInCellar.getComments()).isEqualTo(UPDATED_COMMENTS);
        assertThat(testWineInCellar.getLocation()).isEqualTo(UPDATED_LOCATION);
        assertThat(testWineInCellar.getCellarId()).isEqualTo(wineInCellar.getCellarId().intValue());

        // Validate the WineInCellar in Elasticsearch
        WineInCellar wineInCellarEs = wineInCellarSearchRepository.findOne(testWineInCellar.getId());
        assertThat(wineInCellarEs).isEqualToComparingFieldByField(testWineInCellar);
    }

    @Test
    @Transactional
    public void updateNonExistingWineInCellar() throws Exception {
        int databaseSizeBeforeUpdate = wineInCellarRepository.findAll().size();

        // Create the WineInCellar

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restWineInCellarMockMvc.perform(put("/api/wine-in-cellars")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(wineInCellar)))
            .andExpect(status().isCreated());

        // Validate the WineInCellar in the database
        List<WineInCellar> wineInCellarList = wineInCellarRepository.findAll();
        assertThat(wineInCellarList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteWineInCellar() throws Exception {
        // Initialize the database
        wineInCellarService.save(wineInCellar);

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
        wineInCellarService.save(wineInCellar);

        // Search the wineInCellar
        restWineInCellarMockMvc.perform(get("/api/_search/wine-in-cellars?query=id:" + wineInCellar.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(wineInCellar.getId().intValue())))
            .andExpect(jsonPath("$.[*].minKeep").value(hasItem(DEFAULT_MIN_KEEP)))
            .andExpect(jsonPath("$.[*].maxKeep").value(hasItem(DEFAULT_MAX_KEEP)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].comments").value(hasItem(DEFAULT_COMMENTS.toString())))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION.toString())))
            .andExpect(jsonPath("$.[*].cellarId").value(hasItem(wineInCellar.getCellarId().intValue())));
    }

    @Test
    @Transactional
    public void createWineInCellarFromScratch() throws Exception {
        int databaseSizeBeforeCreate = wineInCellarRepository.findAll().size();
        Wine wine = WineResourceIntTest.createEntity(em);
        Vintage vintage = VintageResourceIntTest.createEntity(em);
        vintage.setWine(wine);
        wineInCellar.setVintage(vintage);
        // Create the WineInCellar

        int expectedApogee = DEFAULT_MAX_KEEP + wineInCellar.getVintage().getYear();
        restWineInCellarMockMvc.perform(post("/api/wine-in-cellars/all")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(wineInCellar)))
            .andExpect(status().isCreated())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.apogee").value(expectedApogee));

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
        wineInCellarService.saveFromScratch(wineInCellar);

        int databaseSizeBeforeUpdate = wineInCellarRepository.findAll().size();

        // Update the wineInCellar
        WineInCellar updatedWineInCellar = wineInCellarRepository.findOne(wineInCellar.getId());
        // Clear entity association
        em.clear();
        updatedWineInCellar.getVintage().getWine().setName(UPDATED_NAME);
        updatedWineInCellar.setMaxKeep(UPDATED_MAX_KEEP);

        int expectedApogee = UPDATED_MAX_KEEP + updatedWineInCellar.getVintage().getYear();
        restWineInCellarMockMvc.perform(put("/api/wine-in-cellars/all")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedWineInCellar)))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.apogee").value(expectedApogee));

        // Validate the WineInCellar in the database
        List<WineInCellar> wineInCellarList = wineInCellarRepository.findAll();
        assertThat(wineInCellarList).hasSize(databaseSizeBeforeUpdate);
        WineInCellar testWineInCellar = wineInCellarList.get(wineInCellarList.size() - 1);
        assertThat(testWineInCellar.getVintage().getWine().getName()).isEqualTo(UPDATED_NAME);
        assertThat(testWineInCellar.getMaxKeep()).isEqualTo(UPDATED_MAX_KEEP);

        // Validate the WineInCellar in Elasticsearch
        WineInCellar wineInCellarEs = wineInCellarSearchRepository.findOne(testWineInCellar.getId());
        assertThat(wineInCellarEs).isEqualToComparingFieldByField(testWineInCellar);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WineInCellar.class);
        WineInCellar wineInCellar1 = new WineInCellar();
        wineInCellar1.setId(1L);
        WineInCellar wineInCellar2 = new WineInCellar();
        wineInCellar2.setId(wineInCellar1.getId());
        assertThat(wineInCellar1).isEqualTo(wineInCellar2);
        wineInCellar2.setId(2L);
        assertThat(wineInCellar1).isNotEqualTo(wineInCellar2);
        wineInCellar1.setId(null);
        assertThat(wineInCellar1).isNotEqualTo(wineInCellar2);
    }
}
