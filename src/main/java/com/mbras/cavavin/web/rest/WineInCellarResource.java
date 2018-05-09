package com.mbras.cavavin.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mbras.cavavin.domain.WineInCellar;
import com.mbras.cavavin.service.WineInCellarService;
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

    public WineInCellarResource(WineInCellarService wineInCellarService) {
        this.wineInCellarService = wineInCellarService;
    }

    /**
     * POST  /wine-in-cellars : Create a new wineInCellar.
     *
     * @param wineInCellar the wineInCellar to create
     * @return the ResponseEntity with status 201 (Created) and with body the new wineInCellar, or with status 400 (Bad Request) if the wineInCellar has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/wine-in-cellars")
    @Timed
    public ResponseEntity<WineInCellar> createWineInCellar(@Valid @RequestBody WineInCellar wineInCellar) throws URISyntaxException {
        log.debug("REST request to save WineInCellar : {}", wineInCellar);
        if (wineInCellar.getId() != null) {
            throw new BadRequestAlertException("A new wineInCellar cannot already have an ID", ENTITY_NAME, "idexists");
        }
        WineInCellar result = wineInCellarService.save(wineInCellar);
        return ResponseEntity.created(new URI("/api/wine-in-cellars/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * POST  /wine-in-cellars : Create a new wineInCellar and a new vintage and a new wine.
     *
     * @param wineInCellar the wineInCellarDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new wineInCellarDTO, or with status 400 (Bad Request) if the wineInCellar has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/wine-in-cellars/all")
    @Timed
    public ResponseEntity<WineInCellar> createWineInCellarFromScratch(@Valid @RequestBody WineInCellar wineInCellar) throws URISyntaxException {
        log.debug("REST request to save WineInCellar : {}", wineInCellar);
        if (wineInCellar.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new wineInCellar cannot already have an ID")).body(null);
        }

        WineInCellar result = wineInCellarService.saveFromScratch(wineInCellar);
        return ResponseEntity.created(new URI("/api/wine-in-cellars/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /wine-in-cellars : Updates an existing wineInCellar.
     *
     * @param wineInCellar the wineInCellar to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated wineInCellar,
     * or with status 400 (Bad Request) if the wineInCellar is not valid,
     * or with status 500 (Internal Server Error) if the wineInCellar couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/wine-in-cellars")
    @Timed
    public ResponseEntity<WineInCellar> updateWineInCellar(@Valid @RequestBody WineInCellar wineInCellar) throws URISyntaxException {
        log.debug("REST request to update WineInCellar : {}", wineInCellar);
        if (wineInCellar.getId() == null) {
            return createWineInCellar(wineInCellar);
        }
        WineInCellar result = wineInCellarService.save(wineInCellar);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, wineInCellar.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /wine-in-cellars/all : Updates an existing wineInCellar and vinrage and wine.
     *
     * @param wineInCellar the wineInCellarDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated wineInCellarDTO,
     * or with status 400 (Bad Request) if the wineInCellarDTO is not valid,
     * or with status 500 (Internal Server Error) if the wineInCellarDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/wine-in-cellars/all")
    @Timed
    public ResponseEntity<WineInCellar> updateWineInCellarFromScratch(@Valid @RequestBody WineInCellar wineInCellar) throws URISyntaxException {
        log.debug("REST request to update WineInCellar : {}", wineInCellar);
        if (wineInCellar.getId() == null) {
            return createWineInCellar(wineInCellar);
        }
        WineInCellar result = wineInCellarService.saveFromScratch(wineInCellar);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, wineInCellar.getId().toString()))
            .body(result);
    }

    /**
     * GET  /wine-in-cellars : get all the wineInCellars.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of wineInCellars in body
     */
    @GetMapping("/wine-in-cellars")
    @Timed
    public ResponseEntity<List<WineInCellar>> getAllWineInCellars(Pageable pageable) {
        log.debug("REST request to get a page of WineInCellars");
        Page<WineInCellar> page = wineInCellarService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/wine-in-cellars");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /wine-in-cellars/:id : get the "id" wineInCellar.
     *
     * @param id the id of the wineInCellar to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the wineInCellar, or with status 404 (Not Found)
     */
    @GetMapping("/wine-in-cellars/{id}")
    @Timed
    public ResponseEntity<WineInCellar> getWineInCellar(@PathVariable Long id) {
        log.debug("REST request to get WineInCellar : {}", id);
        WineInCellar wineInCellar = wineInCellarService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(wineInCellar));
    }

    /**
     * DELETE  /wine-in-cellars/:id : delete the "id" wineInCellar.
     *
     * @param id the id of the wineInCellar to delete
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
     * @param cellarId the id of the cellar search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/wine-in-cellars")
    @Timed
    public ResponseEntity<List<WineInCellar>> searchWineInCellars(@RequestParam String query, @RequestParam(required = false) Long cellarId, Pageable pageable) {
        log.debug("REST request to search for a page of WineInCellars for query {}", query);
        Page<WineInCellar> page = wineInCellarService.search(query, cellarId, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/wine-in-cellars");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
