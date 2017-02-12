package com.mbras.cellar.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mbras.cellar.domain.WineByRegion;
import com.mbras.cellar.service.WineInCellarService;
import com.mbras.cellar.web.rest.util.HeaderUtil;
import com.mbras.cellar.service.dto.WineInCellarDTO;
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
 * REST controller for managing WineInCellar.
 */
@RestController
@RequestMapping("/api")
public class WineInCellarResource {

    private final Logger log = LoggerFactory.getLogger(WineInCellarResource.class);

    @Inject
    private WineInCellarService wineInCellarService;

    /**
     * POST  /wine-in-cellars : Create a new wineInCellar.
     *
     * @param wineInCellarDTO the wineInCellarDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new wineInCellarDTO, or with status 400 (Bad Request) if the wineInCellar has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/wine-in-cellars")
    @Timed
    public ResponseEntity<WineInCellarDTO> createWineInCellar(@RequestBody WineInCellarDTO wineInCellarDTO) throws URISyntaxException {
        log.debug("REST request to save WineInCellar : {}", wineInCellarDTO);
        if (wineInCellarDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("wineInCellar", "idexists", "A new wineInCellar cannot already have an ID")).body(null);
        }
        WineInCellarDTO result = wineInCellarService.save(wineInCellarDTO);
        return ResponseEntity.created(new URI("/api/wine-in-cellars/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("wineInCellar", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /wine-in-cellars : Updates an existing wineInCellar.
     *
     * @param wineInCellarDTO the wineInCellarDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated wineInCellarDTO,
     * or with status 400 (Bad Request) if the wineInCellarDTO is not valid,
     * or with status 500 (Internal Server Error) if the wineInCellarDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/wine-in-cellars")
    @Timed
    public ResponseEntity<WineInCellarDTO> updateWineInCellar(@RequestBody WineInCellarDTO wineInCellarDTO) throws URISyntaxException {
        log.debug("REST request to update WineInCellar : {}", wineInCellarDTO);
        if (wineInCellarDTO.getId() == null) {
            return createWineInCellar(wineInCellarDTO);
        }
        WineInCellarDTO result = wineInCellarService.save(wineInCellarDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("wineInCellar", wineInCellarDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /wine-in-cellars : get all the wineInCellars.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of wineInCellars in body
     */
    @GetMapping("/wine-in-cellars")
    @Timed
    public List<WineInCellarDTO> getAllWineInCellars() {
        log.debug("REST request to get all WineInCellars");
        return wineInCellarService.findAll();
    }

    /**
     * GET  /wine-in-cellars/:id : get the "id" wineInCellar.
     *
     * @param id the id of the wineInCellarDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the wineInCellarDTO, or with status 404 (Not Found)
     */
    @GetMapping("/wine-in-cellars/{id}")
    @Timed
    public ResponseEntity<WineInCellarDTO> getWineInCellar(@PathVariable Long id) {
        log.debug("REST request to get WineInCellar : {}", id);
        WineInCellarDTO wineInCellarDTO = wineInCellarService.findOne(id);
        return Optional.ofNullable(wineInCellarDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /wine-in-cellars/:id : delete the "id" wineInCellar.
     *
     * @param id the id of the wineInCellarDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/wine-in-cellars/{id}")
    @Timed
    public ResponseEntity<Void> deleteWineInCellar(@PathVariable Long id) {
        log.debug("REST request to delete WineInCellar : {}", id);
        wineInCellarService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("wineInCellar", id.toString())).build();
    }

    /**
     * SEARCH  /_search/wine-in-cellars?query=:query : search for the wineInCellar corresponding
     * to the query.
     *
     * @param query the query of the wineInCellar search
     * @return the result of the search
     */
    @GetMapping("/_search/wine-in-cellars")
    @Timed
    public List<WineInCellarDTO> searchWineInCellars(@RequestParam String query) {
        log.debug("REST request to search WineInCellars for query {}", query);
        return wineInCellarService.search(query);
    }


}
