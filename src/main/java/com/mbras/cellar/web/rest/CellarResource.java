package com.mbras.cellar.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mbras.cellar.domain.WineByColor;
import com.mbras.cellar.domain.WineByRegion;
import com.mbras.cellar.service.CellarService;
import com.mbras.cellar.service.WineInCellarService;
import com.mbras.cellar.service.dto.WineInCellarDTO;
import com.mbras.cellar.web.rest.util.HeaderUtil;
import com.mbras.cellar.service.dto.CellarDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
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

    @Inject
    private CellarService cellarService;

    @Inject
    private WineInCellarService wineInCellarService;

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
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("cellar", "idexists", "A new cellar cannot already have an ID")).body(null);
        }
        CellarDTO result = cellarService.save(cellarDTO);
        return ResponseEntity.created(new URI("/api/cellars/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("cellar", result.getId().toString()))
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
            .headers(HeaderUtil.createEntityUpdateAlert("cellar", cellarDTO.getId().toString()))
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
        return Optional.ofNullable(cellarDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
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
        return Optional.ofNullable(wineInCellars)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * GET  /sum-of-wine : get total number of wine
     * @param id the id of cellar
     * @return the ResponseEntity with status 200 (OK) and the total number of wine in body
     */
    @GetMapping("/cellars/{id}/sum-of-wine")
    @Timed
    public ResponseEntity<Long> getWineSum(@PathVariable Long id) {
        log.debug("REST request to get total number of wine");
        Long sum = wineInCellarService.getWineSum(id);
        return Optional.ofNullable(sum)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(0L,HttpStatus.OK));
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
        List<WineByRegion> wineByRegions = wineInCellarService.getWineByRegion(id);
        return wineInCellarService.getWineByRegion(id);
    }

    /**
     * GET  /wine-by-color : get number of wine group by color
     * @param id the id of cellar
     * @return the ResponseEntity with status 200 (OK) and the list of wine by color in body
     */
    @GetMapping("/wine-by-color")
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
    @DeleteMapping("/cellars/{id}//cellars/{id}")
    @Timed
    public ResponseEntity<Void> deleteCellar(@PathVariable Long id) {
        log.debug("REST request to delete Cellar : {}", id);
        cellarService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("cellar", id.toString())).build();
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
