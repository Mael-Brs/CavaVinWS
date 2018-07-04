package com.mbras.cavavin.web.rest;

import com.mbras.cavavin.CavavinApp;
import com.mbras.cavavin.domain.*;
import com.mbras.cavavin.repository.WineInCellarRepository;
import com.mbras.cavavin.repository.search.WineInCellarSearchRepository;
import com.mbras.cavavin.service.WineInCellarQueryService;
import com.mbras.cavavin.service.WineInCellarService;
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

import static com.mbras.cavavin.web.rest.TestUtil.createFormattingConversionService;
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

    private static final String DEFAULT_LOCATION = "AAAAAAAAAA";
    private static final String UPDATED_LOCATION = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_CHILD_YEAR = 1;
    private static final Integer UPDATED_CHILD_YEAR = 2;

    private static final Integer DEFAULT_APOGEE_YEAR = 1;
    private static final Integer UPDATED_APOGEE_YEAR = 2;

    @Autowired
    private WineInCellarRepository wineInCellarRepository;

    @Autowired
    private WineInCellarService wineInCellarService;

    @Autowired
    private WineInCellarSearchRepository wineInCellarSearchRepository;

    @Autowired
    private WineInCellarQueryService wineInCellarQueryService;

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
        final WineInCellarResource wineInCellarResource = new WineInCellarResource(wineInCellarService, wineInCellarQueryService);
        this.restWineInCellarMockMvc = MockMvcBuilders.standaloneSetup(wineInCellarResource)
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
    public static WineInCellar createEntity(EntityManager em) {
        WineInCellar wineInCellar = new WineInCellar()
            .minKeep(DEFAULT_MIN_KEEP)
            .maxKeep(DEFAULT_MAX_KEEP)
            .price(DEFAULT_PRICE)
            .quantity(DEFAULT_QUANTITY)
            .comments(DEFAULT_COMMENTS)
            .location(DEFAULT_LOCATION)
            .childYear(DEFAULT_CHILD_YEAR)
            .apogeeYear(DEFAULT_APOGEE_YEAR);
        // Add required entity
        Vintage vintage = VintageResourceIntTest.createEntity(em);
        Cellar cellar = CellarResourceIntTest.createEntity(em);
        WineAgingData wineAgingData = new WineAgingData();
        wineAgingData.setColor(vintage.getWine().getColor());
        wineAgingData.setRegion(vintage.getWine().getRegion());
        wineAgingData.setMinKeep(DEFAULT_MIN_KEEP + 1);
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
        wineInCellar.setApogeeYear(null);
        wineInCellar.setChildYear(null);
        wineInCellar.setMinKeep(null);
        wineInCellar.setMaxKeep(null);
        // Create the WineInCellar
        restWineInCellarMockMvc.perform(post("/api/wine-in-cellars")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(wineInCellar)))
            .andExpect(status().isCreated());

        // Validate the WineInCellar in the database
        List<WineInCellar> wineInCellarList = wineInCellarRepository.findAll();
        assertThat(wineInCellarList).hasSize(databaseSizeBeforeCreate + 1);
        WineInCellar testWineInCellar = wineInCellarList.get(wineInCellarList.size() - 1);
        assertThat(testWineInCellar.getMinKeep()).isEqualTo(DEFAULT_MIN_KEEP + 1);
        assertThat(testWineInCellar.getMaxKeep()).isEqualTo(DEFAULT_MAX_KEEP + 1);
        assertThat(testWineInCellar.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testWineInCellar.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
        assertThat(testWineInCellar.getComments()).isEqualTo(DEFAULT_COMMENTS);
        assertThat(testWineInCellar.getLocation()).isEqualTo(DEFAULT_LOCATION);
        assertThat(testWineInCellar.getCellarId()).isEqualTo(wineInCellar.getCellarId().intValue());
        assertThat(testWineInCellar.getChildYear()).isEqualTo(DEFAULT_MIN_KEEP + 1 + wineInCellar.getVintage().getYear());
        assertThat(testWineInCellar.getApogeeYear()).isEqualTo(DEFAULT_MAX_KEEP + 1 +  wineInCellar.getVintage().getYear());

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
            .andExpect(jsonPath("$.[*].cellarId").value(hasItem(wineInCellar.getCellarId().intValue())))
            .andExpect(jsonPath("$.[*].childYear").value(hasItem(DEFAULT_CHILD_YEAR)))
            .andExpect(jsonPath("$.[*].apogeeYear").value(hasItem(DEFAULT_APOGEE_YEAR)));
    }

    @Test
    @Transactional
    @WithMockUser
    public void getAllWineInCellarsWithoutCellar() throws Exception {
        // Initialize the database
        wineInCellarRepository.saveAndFlush(wineInCellar);

        // Get all the wineInCellarList
        restWineInCellarMockMvc.perform(get("/api/wine-in-cellars?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isEmpty());
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
            .andExpect(jsonPath("$.comments").value(DEFAULT_COMMENTS.toString()))
            .andExpect(jsonPath("$.location").value(DEFAULT_LOCATION.toString()))
            .andExpect(jsonPath("$.cellarId").value(wineInCellar.getCellarId().intValue()))
            .andExpect(jsonPath("$.childYear").value(DEFAULT_CHILD_YEAR))
            .andExpect(jsonPath("$.apogeeYear").value(DEFAULT_APOGEE_YEAR));
    }

    @Test
    @Transactional
    @WithMockUser("system")
    public void getAllWineInCellarsByMinKeepIsEqualToSomething() throws Exception {
        // Initialize the database
        wineInCellarRepository.saveAndFlush(wineInCellar);

        // Get all the wineInCellarList where minKeep equals to DEFAULT_MIN_KEEP
        defaultWineInCellarShouldBeFound("minKeep.equals=" + DEFAULT_MIN_KEEP);

        // Get all the wineInCellarList where minKeep equals to UPDATED_MIN_KEEP
        defaultWineInCellarShouldNotBeFound("minKeep.equals=" + UPDATED_MIN_KEEP);
    }

    @Test
    @Transactional
    @WithMockUser("system")
    public void getAllWineInCellarsByMinKeepIsInShouldWork() throws Exception {
        // Initialize the database
        wineInCellarRepository.saveAndFlush(wineInCellar);

        // Get all the wineInCellarList where minKeep in DEFAULT_MIN_KEEP or UPDATED_MIN_KEEP
        defaultWineInCellarShouldBeFound("minKeep.in=" + DEFAULT_MIN_KEEP + "," + UPDATED_MIN_KEEP);

        // Get all the wineInCellarList where minKeep equals to UPDATED_MIN_KEEP
        defaultWineInCellarShouldNotBeFound("minKeep.in=" + UPDATED_MIN_KEEP);
    }

    @Test
    @Transactional
    @WithMockUser("system")
    public void getAllWineInCellarsByMinKeepIsNullOrNotNull() throws Exception {
        // Initialize the database
        wineInCellarRepository.saveAndFlush(wineInCellar);

        // Get all the wineInCellarList where minKeep is not null
        defaultWineInCellarShouldBeFound("minKeep.specified=true");

        // Get all the wineInCellarList where minKeep is null
        defaultWineInCellarShouldNotBeFound("minKeep.specified=false");
    }

    @Test
    @Transactional
    @WithMockUser("system")
    public void getAllWineInCellarsByMinKeepIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        wineInCellarRepository.saveAndFlush(wineInCellar);

        // Get all the wineInCellarList where minKeep greater than or equals to DEFAULT_MIN_KEEP
        defaultWineInCellarShouldBeFound("minKeep.greaterOrEqualThan=" + DEFAULT_MIN_KEEP);

        // Get all the wineInCellarList where minKeep greater than or equals to UPDATED_MIN_KEEP
        defaultWineInCellarShouldNotBeFound("minKeep.greaterOrEqualThan=" + UPDATED_MIN_KEEP);
    }

    @Test
    @Transactional
    @WithMockUser("system")
    public void getAllWineInCellarsByMinKeepIsLessThanSomething() throws Exception {
        // Initialize the database
        wineInCellarRepository.saveAndFlush(wineInCellar);

        // Get all the wineInCellarList where minKeep less than or equals to DEFAULT_MIN_KEEP
        defaultWineInCellarShouldNotBeFound("minKeep.lessThan=" + DEFAULT_MIN_KEEP);

        // Get all the wineInCellarList where minKeep less than or equals to UPDATED_MIN_KEEP
        defaultWineInCellarShouldBeFound("minKeep.lessThan=" + UPDATED_MIN_KEEP);
    }


    @Test
    @Transactional
    @WithMockUser("system")
    public void getAllWineInCellarsByMaxKeepIsEqualToSomething() throws Exception {
        // Initialize the database
        wineInCellarRepository.saveAndFlush(wineInCellar);

        // Get all the wineInCellarList where maxKeep equals to DEFAULT_MAX_KEEP
        defaultWineInCellarShouldBeFound("maxKeep.equals=" + DEFAULT_MAX_KEEP);

        // Get all the wineInCellarList where maxKeep equals to UPDATED_MAX_KEEP
        defaultWineInCellarShouldNotBeFound("maxKeep.equals=" + UPDATED_MAX_KEEP);
    }

    @Test
    @Transactional
    @WithMockUser("system")
    public void getAllWineInCellarsByMaxKeepIsInShouldWork() throws Exception {
        // Initialize the database
        wineInCellarRepository.saveAndFlush(wineInCellar);

        // Get all the wineInCellarList where maxKeep in DEFAULT_MAX_KEEP or UPDATED_MAX_KEEP
        defaultWineInCellarShouldBeFound("maxKeep.in=" + DEFAULT_MAX_KEEP + "," + UPDATED_MAX_KEEP);

        // Get all the wineInCellarList where maxKeep equals to UPDATED_MAX_KEEP
        defaultWineInCellarShouldNotBeFound("maxKeep.in=" + UPDATED_MAX_KEEP);
    }

    @Test
    @Transactional
    @WithMockUser("system")
    public void getAllWineInCellarsByMaxKeepIsNullOrNotNull() throws Exception {
        // Initialize the database
        wineInCellarRepository.saveAndFlush(wineInCellar);

        // Get all the wineInCellarList where maxKeep is not null
        defaultWineInCellarShouldBeFound("maxKeep.specified=true");

        // Get all the wineInCellarList where maxKeep is null
        defaultWineInCellarShouldNotBeFound("maxKeep.specified=false");
    }

    @Test
    @Transactional
    @WithMockUser("system")
    public void getAllWineInCellarsByMaxKeepIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        wineInCellarRepository.saveAndFlush(wineInCellar);

        // Get all the wineInCellarList where maxKeep greater than or equals to DEFAULT_MAX_KEEP
        defaultWineInCellarShouldBeFound("maxKeep.greaterOrEqualThan=" + DEFAULT_MAX_KEEP);

        // Get all the wineInCellarList where maxKeep greater than or equals to UPDATED_MAX_KEEP
        defaultWineInCellarShouldNotBeFound("maxKeep.greaterOrEqualThan=" + UPDATED_MAX_KEEP);
    }

    @Test
    @Transactional
    @WithMockUser("system")
    public void getAllWineInCellarsByMaxKeepIsLessThanSomething() throws Exception {
        // Initialize the database
        wineInCellarRepository.saveAndFlush(wineInCellar);

        // Get all the wineInCellarList where maxKeep less than or equals to DEFAULT_MAX_KEEP
        defaultWineInCellarShouldNotBeFound("maxKeep.lessThan=" + DEFAULT_MAX_KEEP);

        // Get all the wineInCellarList where maxKeep less than or equals to UPDATED_MAX_KEEP
        defaultWineInCellarShouldBeFound("maxKeep.lessThan=" + UPDATED_MAX_KEEP);
    }

    @Test
    @Transactional
    @WithMockUser("system")
    public void getAllWineInCellarsByPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        wineInCellarRepository.saveAndFlush(wineInCellar);

        // Get all the wineInCellarList where price equals to DEFAULT_PRICE
        defaultWineInCellarShouldBeFound("price.equals=" + DEFAULT_PRICE);

        // Get all the wineInCellarList where price equals to UPDATED_PRICE
        defaultWineInCellarShouldNotBeFound("price.equals=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    @WithMockUser("system")
    public void getAllWineInCellarsByPriceIsInShouldWork() throws Exception {
        // Initialize the database
        wineInCellarRepository.saveAndFlush(wineInCellar);

        // Get all the wineInCellarList where price in DEFAULT_PRICE or UPDATED_PRICE
        defaultWineInCellarShouldBeFound("price.in=" + DEFAULT_PRICE + "," + UPDATED_PRICE);

        // Get all the wineInCellarList where price equals to UPDATED_PRICE
        defaultWineInCellarShouldNotBeFound("price.in=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    @WithMockUser("system")
    public void getAllWineInCellarsByPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        wineInCellarRepository.saveAndFlush(wineInCellar);

        // Get all the wineInCellarList where price is not null
        defaultWineInCellarShouldBeFound("price.specified=true");

        // Get all the wineInCellarList where price is null
        defaultWineInCellarShouldNotBeFound("price.specified=false");
    }

    @Test
    @Transactional
    @WithMockUser("system")
    public void getAllWineInCellarsByQuantityIsEqualToSomething() throws Exception {
        // Initialize the database
        wineInCellarRepository.saveAndFlush(wineInCellar);

        // Get all the wineInCellarList where quantity equals to DEFAULT_QUANTITY
        defaultWineInCellarShouldBeFound("quantity.equals=" + DEFAULT_QUANTITY);

        // Get all the wineInCellarList where quantity equals to UPDATED_QUANTITY
        defaultWineInCellarShouldNotBeFound("quantity.equals=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    @WithMockUser("system")
    public void getAllWineInCellarsByQuantityIsInShouldWork() throws Exception {
        // Initialize the database
        wineInCellarRepository.saveAndFlush(wineInCellar);

        // Get all the wineInCellarList where quantity in DEFAULT_QUANTITY or UPDATED_QUANTITY
        defaultWineInCellarShouldBeFound("quantity.in=" + DEFAULT_QUANTITY + "," + UPDATED_QUANTITY);

        // Get all the wineInCellarList where quantity equals to UPDATED_QUANTITY
        defaultWineInCellarShouldNotBeFound("quantity.in=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    @WithMockUser("system")
    public void getAllWineInCellarsByQuantityIsNullOrNotNull() throws Exception {
        // Initialize the database
        wineInCellarRepository.saveAndFlush(wineInCellar);

        // Get all the wineInCellarList where quantity is not null
        defaultWineInCellarShouldBeFound("quantity.specified=true");

        // Get all the wineInCellarList where quantity is null
        defaultWineInCellarShouldNotBeFound("quantity.specified=false");
    }

    @Test
    @Transactional
    @WithMockUser("system")
    public void getAllWineInCellarsByQuantityIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        wineInCellarRepository.saveAndFlush(wineInCellar);

        // Get all the wineInCellarList where quantity greater than or equals to DEFAULT_QUANTITY
        defaultWineInCellarShouldBeFound("quantity.greaterOrEqualThan=" + DEFAULT_QUANTITY);

        // Get all the wineInCellarList where quantity greater than or equals to UPDATED_QUANTITY
        defaultWineInCellarShouldNotBeFound("quantity.greaterOrEqualThan=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    @WithMockUser("system")
    public void getAllWineInCellarsByQuantityIsLessThanSomething() throws Exception {
        // Initialize the database
        wineInCellarRepository.saveAndFlush(wineInCellar);

        // Get all the wineInCellarList where quantity less than or equals to DEFAULT_QUANTITY
        defaultWineInCellarShouldNotBeFound("quantity.lessThan=" + DEFAULT_QUANTITY);

        // Get all the wineInCellarList where quantity less than or equals to UPDATED_QUANTITY
        defaultWineInCellarShouldBeFound("quantity.lessThan=" + UPDATED_QUANTITY);
    }


    @Test
    @Transactional
    @WithMockUser("system")
    public void getAllWineInCellarsByCommentsIsEqualToSomething() throws Exception {
        // Initialize the database
        wineInCellarRepository.saveAndFlush(wineInCellar);

        // Get all the wineInCellarList where comments equals to DEFAULT_COMMENTS
        defaultWineInCellarShouldBeFound("comments.equals=" + DEFAULT_COMMENTS);

        // Get all the wineInCellarList where comments equals to UPDATED_COMMENTS
        defaultWineInCellarShouldNotBeFound("comments.equals=" + UPDATED_COMMENTS);
    }

    @Test
    @Transactional
    @WithMockUser("system")
    public void getAllWineInCellarsByCommentsIsInShouldWork() throws Exception {
        // Initialize the database
        wineInCellarRepository.saveAndFlush(wineInCellar);

        // Get all the wineInCellarList where comments in DEFAULT_COMMENTS or UPDATED_COMMENTS
        defaultWineInCellarShouldBeFound("comments.in=" + DEFAULT_COMMENTS + "," + UPDATED_COMMENTS);

        // Get all the wineInCellarList where comments equals to UPDATED_COMMENTS
        defaultWineInCellarShouldNotBeFound("comments.in=" + UPDATED_COMMENTS);
    }

    @Test
    @Transactional
    @WithMockUser("system")
    public void getAllWineInCellarsByCommentsIsNullOrNotNull() throws Exception {
        // Initialize the database
        wineInCellarRepository.saveAndFlush(wineInCellar);

        // Get all the wineInCellarList where comments is not null
        defaultWineInCellarShouldBeFound("comments.specified=true");

        // Get all the wineInCellarList where comments is null
        defaultWineInCellarShouldNotBeFound("comments.specified=false");
    }

    @Test
    @Transactional
    @WithMockUser("system")
    public void getAllWineInCellarsByLocationIsEqualToSomething() throws Exception {
        // Initialize the database
        wineInCellarRepository.saveAndFlush(wineInCellar);

        // Get all the wineInCellarList where location equals to DEFAULT_LOCATION
        defaultWineInCellarShouldBeFound("location.equals=" + DEFAULT_LOCATION);

        // Get all the wineInCellarList where location equals to UPDATED_LOCATION
        defaultWineInCellarShouldNotBeFound("location.equals=" + UPDATED_LOCATION);
    }

    @Test
    @Transactional
    @WithMockUser("system")
    public void getAllWineInCellarsByLocationIsInShouldWork() throws Exception {
        // Initialize the database
        wineInCellarRepository.saveAndFlush(wineInCellar);

        // Get all the wineInCellarList where location in DEFAULT_LOCATION or UPDATED_LOCATION
        defaultWineInCellarShouldBeFound("location.in=" + DEFAULT_LOCATION + "," + UPDATED_LOCATION);

        // Get all the wineInCellarList where location equals to UPDATED_LOCATION
        defaultWineInCellarShouldNotBeFound("location.in=" + UPDATED_LOCATION);
    }

    @Test
    @Transactional
    @WithMockUser("system")
    public void getAllWineInCellarsByLocationIsNullOrNotNull() throws Exception {
        // Initialize the database
        wineInCellarRepository.saveAndFlush(wineInCellar);

        // Get all the wineInCellarList where location is not null
        defaultWineInCellarShouldBeFound("location.specified=true");

        // Get all the wineInCellarList where location is null
        defaultWineInCellarShouldNotBeFound("location.specified=false");
    }

    @Test
    @Transactional
    @WithMockUser("system")
    public void getAllWineInCellarsByCellarIdIsEqualToSomething() throws Exception {
        // Initialize the database
        wineInCellarRepository.saveAndFlush(wineInCellar);

        // Get all the wineInCellarList where cellarId equals to wineInCellar.getCellarId()
        defaultWineInCellarShouldBeFound("cellarId.equals=" + wineInCellar.getCellarId());

        // Get all the wineInCellarList where cellarId equals to UPDATED_CELLAR_ID
        defaultWineInCellarShouldNotBeFound("cellarId.equals=" + (wineInCellar.getCellarId() + 1));
    }

    @Test
    @Transactional
    @WithMockUser("system")
    public void getAllWineInCellarsByCellarIdIsInShouldWork() throws Exception {
        // Initialize the database
        wineInCellarRepository.saveAndFlush(wineInCellar);

        // Get all the wineInCellarList where cellarId in wineInCellar.getCellarId() or UPDATED_CELLAR_ID
        defaultWineInCellarShouldBeFound("cellarId.in=" + wineInCellar.getCellarId() + "," + (wineInCellar.getCellarId() + 1));

        // Get all the wineInCellarList where cellarId equals to UPDATED_CELLAR_ID
        defaultWineInCellarShouldNotBeFound("cellarId.in=" + (wineInCellar.getCellarId() + 1));
    }

    @Test
    @Transactional
    @WithMockUser("system")
    public void getAllWineInCellarsByCellarIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        wineInCellarRepository.saveAndFlush(wineInCellar);

        // Get all the wineInCellarList where cellarId is not null
        defaultWineInCellarShouldBeFound("cellarId.specified=true");

        // Get all the wineInCellarList where cellarId is null
        defaultWineInCellarShouldNotBeFound("cellarId.specified=false");
    }

    @Test
    @Transactional
    @WithMockUser("system")
    public void getAllWineInCellarsByCellarIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        wineInCellarRepository.saveAndFlush(wineInCellar);

        // Get all the wineInCellarList where cellarId greater than or equals to wineInCellar.getCellarId()
        defaultWineInCellarShouldBeFound("cellarId.greaterOrEqualThan=" + wineInCellar.getCellarId());

        // Get all the wineInCellarList where cellarId greater than or equals to UPDATED_CELLAR_ID
        defaultWineInCellarShouldNotBeFound("cellarId.greaterOrEqualThan=" + wineInCellar.getCellarId() + 1);
    }

    @Test
    @Transactional
    @WithMockUser("system")
    public void getAllWineInCellarsByCellarIdIsLessThanSomething() throws Exception {
        // Initialize the database
        wineInCellarRepository.saveAndFlush(wineInCellar);

        // Get all the wineInCellarList where cellarId less than or equals to wineInCellar.getCellarId()
        defaultWineInCellarShouldNotBeFound("cellarId.lessThan=" + wineInCellar.getCellarId());

        // Get all the wineInCellarList where cellarId less than or equals to UPDATED_CELLAR_ID
        defaultWineInCellarShouldBeFound("cellarId.lessThan=" + wineInCellar.getCellarId() + 1);
    }


    @Test
    @Transactional
    @WithMockUser("system")
    public void getAllWineInCellarsByChildYearIsEqualToSomething() throws Exception {
        // Initialize the database
        wineInCellarRepository.saveAndFlush(wineInCellar);

        // Get all the wineInCellarList where childYear equals to DEFAULT_CHILD_YEAR
        defaultWineInCellarShouldBeFound("childYear.equals=" + DEFAULT_CHILD_YEAR);

        // Get all the wineInCellarList where childYear equals to UPDATED_CHILD_YEAR
        defaultWineInCellarShouldNotBeFound("childYear.equals=" + UPDATED_CHILD_YEAR);
    }

    @Test
    @Transactional
    @WithMockUser("system")
    public void getAllWineInCellarsByChildYearIsInShouldWork() throws Exception {
        // Initialize the database
        wineInCellarRepository.saveAndFlush(wineInCellar);

        // Get all the wineInCellarList where childYear in DEFAULT_CHILD_YEAR or UPDATED_CHILD_YEAR
        defaultWineInCellarShouldBeFound("childYear.in=" + DEFAULT_CHILD_YEAR + "," + UPDATED_CHILD_YEAR);

        // Get all the wineInCellarList where childYear equals to UPDATED_CHILD_YEAR
        defaultWineInCellarShouldNotBeFound("childYear.in=" + UPDATED_CHILD_YEAR);
    }

    @Test
    @Transactional
    @WithMockUser("system")
    public void getAllWineInCellarsByChildYearIsNullOrNotNull() throws Exception {
        // Initialize the database
        wineInCellarRepository.saveAndFlush(wineInCellar);

        // Get all the wineInCellarList where childYear is not null
        defaultWineInCellarShouldBeFound("childYear.specified=true");

        // Get all the wineInCellarList where childYear is null
        defaultWineInCellarShouldNotBeFound("childYear.specified=false");
    }

    @Test
    @Transactional
    @WithMockUser("system")
    public void getAllWineInCellarsByChildYearIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        wineInCellarRepository.saveAndFlush(wineInCellar);

        // Get all the wineInCellarList where childYear greater than or equals to DEFAULT_CHILD_YEAR
        defaultWineInCellarShouldBeFound("childYear.greaterOrEqualThan=" + DEFAULT_CHILD_YEAR);

        // Get all the wineInCellarList where childYear greater than or equals to UPDATED_CHILD_YEAR
        defaultWineInCellarShouldNotBeFound("childYear.greaterOrEqualThan=" + UPDATED_CHILD_YEAR);
    }

    @Test
    @Transactional
    @WithMockUser("system")
    public void getAllWineInCellarsByChildYearIsLessThanSomething() throws Exception {
        // Initialize the database
        wineInCellarRepository.saveAndFlush(wineInCellar);

        // Get all the wineInCellarList where childYear less than or equals to DEFAULT_CHILD_YEAR
        defaultWineInCellarShouldNotBeFound("childYear.lessThan=" + DEFAULT_CHILD_YEAR);

        // Get all the wineInCellarList where childYear less than or equals to UPDATED_CHILD_YEAR
        defaultWineInCellarShouldBeFound("childYear.lessThan=" + UPDATED_CHILD_YEAR);
    }


    @Test
    @Transactional
    @WithMockUser("system")
    public void getAllWineInCellarsByApogeeYearIsEqualToSomething() throws Exception {
        // Initialize the database
        wineInCellarRepository.saveAndFlush(wineInCellar);

        // Get all the wineInCellarList where apogeeYear equals to DEFAULT_APOGEE_YEAR
        defaultWineInCellarShouldBeFound("apogeeYear.equals=" + DEFAULT_APOGEE_YEAR);

        // Get all the wineInCellarList where apogeeYear equals to UPDATED_APOGEE_YEAR
        defaultWineInCellarShouldNotBeFound("apogeeYear.equals=" + UPDATED_APOGEE_YEAR);
    }

    @Test
    @Transactional
    @WithMockUser("system")
    public void getAllWineInCellarsByApogeeYearIsInShouldWork() throws Exception {
        // Initialize the database
        wineInCellarRepository.saveAndFlush(wineInCellar);

        // Get all the wineInCellarList where apogeeYear in DEFAULT_APOGEE_YEAR or UPDATED_APOGEE_YEAR
        defaultWineInCellarShouldBeFound("apogeeYear.in=" + DEFAULT_APOGEE_YEAR + "," + UPDATED_APOGEE_YEAR);

        // Get all the wineInCellarList where apogeeYear equals to UPDATED_APOGEE_YEAR
        defaultWineInCellarShouldNotBeFound("apogeeYear.in=" + UPDATED_APOGEE_YEAR);
    }

    @Test
    @Transactional
    @WithMockUser("system")
    public void getAllWineInCellarsByApogeeYearIsNullOrNotNull() throws Exception {
        // Initialize the database
        wineInCellarRepository.saveAndFlush(wineInCellar);

        // Get all the wineInCellarList where apogeeYear is not null
        defaultWineInCellarShouldBeFound("apogeeYear.specified=true");

        // Get all the wineInCellarList where apogeeYear is null
        defaultWineInCellarShouldNotBeFound("apogeeYear.specified=false");
    }

    @Test
    @Transactional
    @WithMockUser("system")
    public void getAllWineInCellarsByApogeeYearIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        wineInCellarRepository.saveAndFlush(wineInCellar);

        // Get all the wineInCellarList where apogeeYear greater than or equals to DEFAULT_APOGEE_YEAR
        defaultWineInCellarShouldBeFound("apogeeYear.greaterOrEqualThan=" + DEFAULT_APOGEE_YEAR);

        // Get all the wineInCellarList where apogeeYear greater than or equals to UPDATED_APOGEE_YEAR
        defaultWineInCellarShouldNotBeFound("apogeeYear.greaterOrEqualThan=" + UPDATED_APOGEE_YEAR);
    }

    @Test
    @Transactional
    @WithMockUser("system")
    public void getAllWineInCellarsByApogeeYearIsLessThanSomething() throws Exception {
        // Initialize the database
        wineInCellarRepository.saveAndFlush(wineInCellar);

        // Get all the wineInCellarList where apogeeYear less than or equals to DEFAULT_APOGEE_YEAR
        defaultWineInCellarShouldNotBeFound("apogeeYear.lessThan=" + DEFAULT_APOGEE_YEAR);

        // Get all the wineInCellarList where apogeeYear less than or equals to UPDATED_APOGEE_YEAR
        defaultWineInCellarShouldBeFound("apogeeYear.lessThan=" + UPDATED_APOGEE_YEAR);
    }


    @Test
    @Transactional
    @WithMockUser("system")
    public void getAllWineInCellarsByVintageIsEqualToSomething() throws Exception {
        // Initialize the database
        Vintage vintage = VintageResourceIntTest.createEntity(em);
        em.persist(vintage);
        em.flush();
        wineInCellar.setVintage(vintage);
        wineInCellarRepository.saveAndFlush(wineInCellar);
        Long vintageId = vintage.getId();

        // Get all the wineInCellarList where vintage equals to vintageId
        defaultWineInCellarShouldBeFound("vintageId.equals=" + vintageId);

        // Get all the wineInCellarList where vintage equals to vintageId + 1
        defaultWineInCellarShouldNotBeFound("vintageId.equals=" + (vintageId + 1));
    }

    @Test
    @Transactional
    @WithMockUser("system")
    public void getAllWineInCellarsByRegionIsEqualToSomething() throws Exception {
        wineInCellarRepository.saveAndFlush(wineInCellar);
        String regionName = wineInCellar.getVintage().getWine().getRegion().getRegionName();
        // Get all the wineInCellarList where vintage equals to vintageId
        defaultWineInCellarShouldBeFound("region.equals=" + regionName);

        // Get all the wineInCellarList where vintage equals to vintageId + 1
        defaultWineInCellarShouldNotBeFound("region.equals=" + "fakeName");
    }

    @Test
    @Transactional
    @WithMockUser("system")
    public void getAllWineInCellarsByKeywordsContainsSomething() throws Exception {
        // Initialize the database
        wineInCellarRepository.saveAndFlush(wineInCellar);
        String wineName = wineInCellar.getVintage().getWine().getName().substring(1);
        // Get all the wineInCellarList where comments equals to DEFAULT_COMMENTS
        defaultWineInCellarShouldBeFound("keywords=" + wineName);

        // Get all the wineInCellarList where comments equals to UPDATED_COMMENTS
        defaultWineInCellarShouldNotBeFound("keywords=fakeName");
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultWineInCellarShouldBeFound(String filter) throws Exception {
        restWineInCellarMockMvc.perform(get("/api/wine-in-cellars?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(wineInCellar.getId().intValue())))
            .andExpect(jsonPath("$.[*].minKeep").value(hasItem(DEFAULT_MIN_KEEP)))
            .andExpect(jsonPath("$.[*].maxKeep").value(hasItem(DEFAULT_MAX_KEEP)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].comments").value(hasItem(DEFAULT_COMMENTS.toString())))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION.toString())))
            .andExpect(jsonPath("$.[*].cellarId").value(hasItem(wineInCellar.getCellarId().intValue())))
            .andExpect(jsonPath("$.[*].childYear").value(hasItem(DEFAULT_CHILD_YEAR)))
            .andExpect(jsonPath("$.[*].apogeeYear").value(hasItem(DEFAULT_APOGEE_YEAR)));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultWineInCellarShouldNotBeFound(String filter) throws Exception {
        restWineInCellarMockMvc.perform(get("/api/wine-in-cellars?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
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
        wineInCellarService.save(wineInCellar);

        int databaseSizeBeforeUpdate = wineInCellarRepository.findAll().size();

        // Update the wineInCellar
        WineInCellar updatedWineInCellar = wineInCellarRepository.findOne(wineInCellar.getId());
        // Disconnect from session so that the updates on updatedWineInCellar are not directly saved in db
        em.detach(updatedWineInCellar);
        updatedWineInCellar
            .minKeep(UPDATED_MIN_KEEP)
            .maxKeep(UPDATED_MAX_KEEP)
            .price(UPDATED_PRICE)
            .quantity(UPDATED_QUANTITY)
            .comments(UPDATED_COMMENTS)
            .location(UPDATED_LOCATION)
            .childYear(UPDATED_CHILD_YEAR)
            .apogeeYear(UPDATED_APOGEE_YEAR);

        restWineInCellarMockMvc.perform(put("/api/wine-in-cellars")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedWineInCellar)))
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
        assertThat(testWineInCellar.getLocation()).isEqualTo(UPDATED_LOCATION);
        assertThat(testWineInCellar.getCellarId()).isEqualTo(wineInCellar.getCellarId().intValue());
        assertThat(testWineInCellar.getChildYear()).isEqualTo(UPDATED_CHILD_YEAR);
        assertThat(testWineInCellar.getApogeeYear()).isEqualTo(UPDATED_APOGEE_YEAR);

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
    @WithMockUser("system")
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
    @WithMockUser("system")
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
            .andExpect(jsonPath("$.[*].cellarId").value(hasItem(wineInCellar.getCellarId().intValue())))
            .andExpect(jsonPath("$.[*].childYear").value(hasItem(DEFAULT_CHILD_YEAR)))
            .andExpect(jsonPath("$.[*].apogeeYear").value(hasItem(DEFAULT_APOGEE_YEAR)));
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

        restWineInCellarMockMvc.perform(post("/api/wine-in-cellars/all")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(wineInCellar)))
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
        wineInCellarService.saveFromScratch(wineInCellar);

        int databaseSizeBeforeUpdate = wineInCellarRepository.findAll().size();

        // Update the wineInCellar
        WineInCellar updatedWineInCellar = wineInCellarRepository.findOne(wineInCellar.getId());
        // Clear entity association
        em.clear();
        updatedWineInCellar.getVintage().getWine().setName(UPDATED_NAME);
        updatedWineInCellar.setMaxKeep(UPDATED_MAX_KEEP);

        restWineInCellarMockMvc.perform(put("/api/wine-in-cellars/all")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedWineInCellar)))
            .andExpect(status().isOk());

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
