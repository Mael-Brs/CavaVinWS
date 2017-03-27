package com.mbras.cavavin.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mbras.cavavin.domain.WineByColor;
import com.mbras.cavavin.domain.WineByRegion;
import com.mbras.cavavin.service.CellarService;
import com.mbras.cavavin.service.WineInCellarService;
import com.mbras.cavavin.service.dto.WineInCellarDTO;
import com.mbras.cavavin.web.rest.util.HeaderUtil;
import com.mbras.cavavin.service.dto.CellarDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Cellar.
 */
@RestController
@RequestMapping("/api")
public class CellarResource {

    private final Logger log = LoggerFactory.getLogger(CellarResource.class);

    private static final String ENTITY_NAME = "cellar";

    private final CellarService cellarService;

    private WineInCellarService wineInCellarService;

    public CellarResource(CellarService cellarService, WineInCellarService wineInCellarService) {
        this.cellarService = cellarService;
        this.wineInCellarService = wineInCellarService;
    }


    /**
     * POST  /cellars : Create a new cellar.
     *
     * @param cellarDTO the cellarDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new cellarDTO, or with status 400 (Bad Request) if the cellar has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/cellars")
    @Timed
    public ResponseEntity<CellarDTO> createCellar(@RequestBody CellarDTO cellarDTO) throws URISyntaxException {
        log.debug("REST request to save Cellar : {}", cellarDTO);
        if (cellarDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new cellar cannot already have an ID")).body(null);
        }
        CellarDTO result = cellarService.save(cellarDTO);
        return ResponseEntity.created(new URI("/api/cellars/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /cellars : Updates an existing cellar.
     *
     * @param cellarDTO the cellarDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated cellarDTO,
     * or with status 400 (Bad Request) if the cellarDTO is not valid,
     * or with status 500 (Internal Server Error) if the cellarDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/cellars")
    @Timed
    public ResponseEntity<CellarDTO> updateCellar(@RequestBody CellarDTO cellarDTO) throws URISyntaxException {
        log.debug("REST request to update Cellar : {}", cellarDTO);
        if (cellarDTO.getId() == null) {
            return createCellar(cellarDTO);
        }
        CellarDTO result = cellarService.save(cellarDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, cellarDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /cellars : get all the cellars.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of cellars in body
     */
    @GetMapping("/cellars")
    @Timed
    public List<CellarDTO> getAllCellars() {
        log.debug("REST request to get all Cellars");
        return cellarService.findAll();
    }

    /**
     * GET  /cellars/:id : get the "id" cellar.
     *
     * @param id the id of the cellarDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the cellarDTO, or with status 404 (Not Found)
     */
    @GetMapping("/cellars/{id}")
    @Timed
    public ResponseEntity<CellarDTO> getCellar(@PathVariable Long id) {
        log.debug("REST request to get Cellar : {}", id);
        CellarDTO cellarDTO = cellarService.findOne(id);
        if (cellarDTO != null){
            cellarDTO.setSumOfWine(wineInCellarService.getWineSum(id));
            cellarDTO.setWineByRegion(wineInCellarService.getWineByRegion(id));
            cellarDTO.setWineByColor(wineInCellarService.getWineByColor(id));
            cellarDTO.setWineByYear(wineInCellarService.getWineByYear(id));
        }
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(cellarDTO));
    }

    /**
     * GET  /cellars/:id/wine-in-cellars : get the "id" cellar.
     *
     * @param id the id of the cellar for wineInCellars to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the wineInCellars, or with status 404 (Not Found)
     */
    @GetMapping("/cellars/{id}/wine-in-cellars")
    @Timed
    public ResponseEntity<List<WineInCellarDTO>> getWineInCellarForCellar(@PathVariable Long id) {
        log.debug("REST request to get WineInCellars for Cellar : {}", id);
        List<WineInCellarDTO> wineInCellars = wineInCellarService.findByCellar(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(wineInCellars));
    }

    /**
     * GET  /sum-of-wine : get total number of wine
     * @param id the id of cellar
     * @return the ResponseEntity with status 200 (OK) and the total number of wine in body
     */
    @GetMapping("/cellars/{id}/sum-of-wine")
    @Timed
    public String getWineSum(@PathVariable Long id) {
        log.debug("REST request to get total number of wine");
        Long sum = wineInCellarService.getWineSum(id);
        return "{'sum':" + sum + "}";
    }

    /**
     * GET  /wine-by-region : get number of wine group by region
     * @param id the id of cellar
     * @return the ResponseEntity with status 200 (OK) and the list of wine by region in body
     */
    @GetMapping("/cellars/{id}/wine-by-region")
    @Timed
    public List<WineByRegion> getWineByRegion(@PathVariable Long id) {
        log.debug("REST request to get number of wine by region");

        return wineInCellarService.getWineByRegion(id);
    }

    /**
     * GET  /wine-by-color : get number of wine group by color
     * @param id the id of cellar
     * @return the ResponseEntity with status 200 (OK) and the list of wine by color in body
     */
    @GetMapping("/cellars/{id}/wine-by-color")
    @Timed
    public List<WineByColor> getWineByColor(@PathVariable Long id) {
        log.debug("REST request to get number of wine by color");
        return wineInCellarService.getWineByColor(id);
    }

    /**
     * DELETE  /cellars/:id : delete the "id" cellar.
     *
     * @param id the id of the cellarDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/cellars/{id}")
    @Timed
    public ResponseEntity<Void> deleteCellar(@PathVariable Long id) {
        log.debug("REST request to delete Cellar : {}", id);
        cellarService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/cellars?query=:query : search for the cellar corresponding
     * to the query.
     *
     * @param query the query of the cellar search
     * @return the result of the search
     */
    @GetMapping("/_search/cellars")
    @Timed
    public List<CellarDTO> searchCellars(@RequestParam String query) {
        log.debug("REST request to search Cellars for query {}", query);
        return cellarService.search(query);
    }


}
