package com.mbras.cavavin.web.rest;

import com.mbras.cavavin.CavavinApp;

import com.mbras.cavavin.domain.Color;
import com.mbras.cavavin.repository.ColorRepository;
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
 * Test class for the ColorResource REST controller.
 *
 * @see ColorResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CavavinApp.class)
public class ColorResourceIntTest {

    private static final String DEFAULT_COLOR_NAME = "AAAAAAAAAA";
    private static final String UPDATED_COLOR_NAME = "BBBBBBBBBB";

    @Autowired
    private ColorRepository colorRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restColorMockMvc;

    private Color color;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ColorResource colorResource = new ColorResource(colorRepository);
        this.restColorMockMvc = MockMvcBuilders.standaloneSetup(colorResource)
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
    public static Color createEntity(EntityManager em) {
        Color color = new Color()
            .colorName(DEFAULT_COLOR_NAME);
        return color;
    }

    @Before
    public void initTest() {
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
        List<Color> colorList = colorRepository.findAll();
        assertThat(colorList).hasSize(databaseSizeBeforeCreate + 1);
        Color testColor = colorList.get(colorList.size() - 1);
        assertThat(testColor.getColorName()).isEqualTo(DEFAULT_COLOR_NAME);
    }

    @Test
    @Transactional
    public void createColorWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = colorRepository.findAll().size();

        // Create the Color with an existing ID
        color.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restColorMockMvc.perform(post("/api/colors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(color)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Color> colorList = colorRepository.findAll();
        assertThat(colorList).hasSize(databaseSizeBeforeCreate);
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

        List<Color> colorList = colorRepository.findAll();
        assertThat(colorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllColors() throws Exception {
        // Initialize the database
        colorRepository.saveAndFlush(color);

        // Get all the colorList
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
        List<Color> colorList = colorRepository.findAll();
        assertThat(colorList).hasSize(databaseSizeBeforeUpdate);
        Color testColor = colorList.get(colorList.size() - 1);
        assertThat(testColor.getColorName()).isEqualTo(UPDATED_COLOR_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingColor() throws Exception {
        int databaseSizeBeforeUpdate = colorRepository.findAll().size();

        // Create the Color

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restColorMockMvc.perform(put("/api/colors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(color)))
            .andExpect(status().isCreated());

        // Validate the Color in the database
        List<Color> colorList = colorRepository.findAll();
        assertThat(colorList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteColor() throws Exception {
        // Initialize the database
        colorRepository.saveAndFlush(color);
        int databaseSizeBeforeDelete = colorRepository.findAll().size();

        // Get the color
        restColorMockMvc.perform(delete("/api/colors/{id}", color.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Color> colorList = colorRepository.findAll();
        assertThat(colorList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Color.class);
        Color color1 = new Color();
        color1.setId(1L);
        Color color2 = new Color();
        color2.setId(color1.getId());
        assertThat(color1).isEqualTo(color2);
        color2.setId(2L);
        assertThat(color1).isNotEqualTo(color2);
        color1.setId(null);
        assertThat(color1).isNotEqualTo(color2);
    }
}
