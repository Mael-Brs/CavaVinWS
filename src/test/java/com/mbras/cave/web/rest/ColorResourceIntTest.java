package com.mbras.cave.web.rest;

import com.mbras.cave.CavaVinApp;

import com.mbras.cave.domain.Color;
import com.mbras.cave.repository.ColorRepository;
import com.mbras.cave.repository.search.ColorSearchRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ColorResource REST controller.
 *
 * @see ColorResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CavaVinApp.class)
public class ColorResourceIntTest {

    private static final String DEFAULT_COLOR_NAME = "AAAAA";
    private static final String UPDATED_COLOR_NAME = "BBBBB";

    @Inject
    private ColorRepository colorRepository;

    @Inject
    private ColorSearchRepository colorSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restColorMockMvc;

    private Color color;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ColorResource colorResource = new ColorResource();
        ReflectionTestUtils.setField(colorResource, "colorSearchRepository", colorSearchRepository);
        ReflectionTestUtils.setField(colorResource, "colorRepository", colorRepository);
        this.restColorMockMvc = MockMvcBuilders.standaloneSetup(colorResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Color createEntity(EntityManager em) {
        Color color = new Color()
                .colorName(DEFAULT_COLOR_NAME);
        return color;
    }

    @Before
    public void initTest() {
        colorSearchRepository.deleteAll();
        color = createEntity(em);
    }

    @Test
    @Transactional
    public void createColor() throws Exception {
        int databaseSizeBeforeCreate = colorRepository.findAll().size();

        // Create the Color

        restColorMockMvc.perform(post("/api/colors")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(color)))
                .andExpect(status().isCreated());

        // Validate the Color in the database
        List<Color> colors = colorRepository.findAll();
        assertThat(colors).hasSize(databaseSizeBeforeCreate + 1);
        Color testColor = colors.get(colors.size() - 1);
        assertThat(testColor.getColorName()).isEqualTo(DEFAULT_COLOR_NAME);

        // Validate the Color in ElasticSearch
        Color colorEs = colorSearchRepository.findOne(testColor.getId());
        assertThat(colorEs).isEqualToComparingFieldByField(testColor);
    }

    @Test
    @Transactional
    public void checkColorNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = colorRepository.findAll().size();
        // set the field null
        color.setColorName(null);

        // Create the Color, which fails.

        restColorMockMvc.perform(post("/api/colors")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(color)))
                .andExpect(status().isBadRequest());

        List<Color> colors = colorRepository.findAll();
        assertThat(colors).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllColors() throws Exception {
        // Initialize the database
        colorRepository.saveAndFlush(color);

        // Get all the colors
        restColorMockMvc.perform(get("/api/colors?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(color.getId().intValue())))
                .andExpect(jsonPath("$.[*].colorName").value(hasItem(DEFAULT_COLOR_NAME.toString())));
    }

    @Test
    @Transactional
    public void getColor() throws Exception {
        // Initialize the database
        colorRepository.saveAndFlush(color);

        // Get the color
        restColorMockMvc.perform(get("/api/colors/{id}", color.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(color.getId().intValue()))
            .andExpect(jsonPath("$.colorName").value(DEFAULT_COLOR_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingColor() throws Exception {
        // Get the color
        restColorMockMvc.perform(get("/api/colors/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateColor() throws Exception {
        // Initialize the database
        colorRepository.saveAndFlush(color);
        colorSearchRepository.save(color);
        int databaseSizeBeforeUpdate = colorRepository.findAll().size();

        // Update the color
        Color updatedColor = colorRepository.findOne(color.getId());
        updatedColor
                .colorName(UPDATED_COLOR_NAME);

        restColorMockMvc.perform(put("/api/colors")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedColor)))
                .andExpect(status().isOk());

        // Validate the Color in the database
        List<Color> colors = colorRepository.findAll();
        assertThat(colors).hasSize(databaseSizeBeforeUpdate);
        Color testColor = colors.get(colors.size() - 1);
        assertThat(testColor.getColorName()).isEqualTo(UPDATED_COLOR_NAME);

        // Validate the Color in ElasticSearch
        Color colorEs = colorSearchRepository.findOne(testColor.getId());
        assertThat(colorEs).isEqualToComparingFieldByField(testColor);
    }

    @Test
    @Transactional
    public void deleteColor() throws Exception {
        // Initialize the database
        colorRepository.saveAndFlush(color);
        colorSearchRepository.save(color);
        int databaseSizeBeforeDelete = colorRepository.findAll().size();

        // Get the color
        restColorMockMvc.perform(delete("/api/colors/{id}", color.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean colorExistsInEs = colorSearchRepository.exists(color.getId());
        assertThat(colorExistsInEs).isFalse();

        // Validate the database is empty
        List<Color> colors = colorRepository.findAll();
        assertThat(colors).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchColor() throws Exception {
        // Initialize the database
        colorRepository.saveAndFlush(color);
        colorSearchRepository.save(color);

        // Search the color
        restColorMockMvc.perform(get("/api/_search/colors?query=id:" + color.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(color.getId().intValue())))
            .andExpect(jsonPath("$.[*].colorName").value(hasItem(DEFAULT_COLOR_NAME.toString())));
    }
}
