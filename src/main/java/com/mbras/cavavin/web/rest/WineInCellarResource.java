package com.mbras.cavavin.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mbras.cavavin.service.WineInCellarQueryService;
import com.mbras.cavavin.service.WineInCellarService;
import com.mbras.cavavin.service.dto.WineInCellarCriteria;
import com.mbras.cavavin.service.dto.WineInCellarDTO;
import com.mbras.cavavin.web.rest.errors.BadRequestAlertException;
import com.mbras.cavavin.web.rest.util.HeaderUtil;
import com.mbras.cavavin.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing WineInCellar.
 */
@RestController
@RequestMapping("/api")
public class WineInCellarResource {

    private final Logger log = LoggerFactory.getLogger(WineInCellarResource.class);

    private static final String ENTITY_NAME = "wineInCellar";

    private final WineInCellarService wineInCellarService;

    private final WineInCellarQueryService wineInCellarQueryService;

    public WineInCellarResource(WineInCellarService wineInCellarService, WineInCellarQueryService wineInCellarQueryService) {
        this.wineInCellarService = wineInCellarService;
        this.wineInCellarQueryService = wineInCellarQueryService;
    }

    /**
     * POST  /wine-in-cellars : Create a new wineInCellar.
     *
     * @param wineInCellarDTO the wineInCellarDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new wineInCellarDTO, or with status 400 (Bad Request) if the wineInCellar has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/wine-in-cellars")
    @Timed
    public ResponseEntity<WineInCellarDTO> createWineInCellar(@Valid @RequestBody WineInCellarDTO wineInCellarDTO) throws URISyntaxException {
        log.debug("REST request to save WineInCellar : {}", wineInCellarDTO);
        if (wineInCellarDTO.getId() != null) {
            throw new BadRequestAlertException("A new wineInCellar cannot already have an ID", ENTITY_NAME, "idexists");
        }
        WineInCellarDTO result = wineInCellarService.save(wineInCellarDTO);
        return ResponseEntity.created(new URI("/api/wine-in-cellars/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * POST  /wine-in-cellars : Create a new wineInCellar and a new vintage and a new wine.
     *
     * @param wineInCellarDTO the wineInCellarDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new wineInCellarDTO, or with status 400 (Bad Request) if the wineInCellar has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/wine-in-cellars/all")
    @Timed
    public ResponseEntity<WineInCellarDTO> createWineInCellarFromScratch(@Valid @RequestBody WineInCellarDTO wineInCellarDTO) throws URISyntaxException {
        log.debug("REST request to save WineInCellar : {}", wineInCellarDTO);
        if (wineInCellarDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new wineInCellar cannot already have an ID")).body(null);
        }

        WineInCellarDTO result = wineInCellarService.saveFromScratch(wineInCellarDTO);
        return ResponseEntity.created(new URI("/api/wine-in-cellars/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /wine-in-cellars : Updates an existing wineInCellar.
     *
     * @param wineInCellarDTO the wineInCellarDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated wineInCellarDTO,
     * or with status 400 (Bad Request) if the wineInCellarDTO is not valid,
     * or with status 500 (Internal Server Error) if the wineInCellarDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/wine-in-cellars")
    @Timed
    public ResponseEntity<WineInCellarDTO> updateWineInCellar(@Valid @RequestBody WineInCellarDTO wineInCellarDTO) throws URISyntaxException {
        log.debug("REST request to update WineInCellar : {}", wineInCellarDTO);
        if (wineInCellarDTO.getId() == null) {
            return createWineInCellar(wineInCellarDTO);
        }
        WineInCellarDTO result = wineInCellarService.save(wineInCellarDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, wineInCellarDTO.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /wine-in-cellars/all : Updates an existing wineInCellar and vinrage and wine.
     *
     * @param wineInCellarDTO the wineInCellarDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated wineInCellarDTO,
     * or with status 400 (Bad Request) if the wineInCellarDTO is not valid,
     * or with status 500 (Internal Server Error) if the wineInCellarDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/wine-in-cellars/all")
    @Timed
    public ResponseEntity<WineInCellarDTO> updateWineInCellarFromScratch(@Valid @RequestBody WineInCellarDTO wineInCellarDTO) throws URISyntaxException {
        log.debug("REST request to update WineInCellar : {}", wineInCellarDTO);
        if (wineInCellarDTO.getId() == null) {
            return createWineInCellar(wineInCellarDTO);
        }
        WineInCellarDTO result = wineInCellarService.saveFromScratch(wineInCellarDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, wineInCellarDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /wine-in-cellars : get all the wineInCellars.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of wineInCellars in body
     */
    @GetMapping("/wine-in-cellars")
    @Timed
    public ResponseEntity<List<WineInCellarDTO>> getAllWineInCellars(WineInCellarCriteria criteria, Pageable pageable) {
        log.debug("REST request to get WineInCellars by criteria: {}", criteria);
        Page<WineInCellarDTO> page = wineInCellarQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/wine-in-cellars");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
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
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(wineInCellarDTO));
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
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/wine-in-cellars?query=:query : search for the wineInCellar corresponding
     * to the query.
     *
     * @param query the query of the wineInCellar search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/wine-in-cellars")
    @Timed
    public ResponseEntity<List<WineInCellarDTO>> searchWineInCellars(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of WineInCellars for query {}", query);
        Page<WineInCellarDTO> page = wineInCellarService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/wine-in-cellars");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
