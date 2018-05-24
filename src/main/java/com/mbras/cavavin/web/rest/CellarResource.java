package com.mbras.cavavin.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mbras.cavavin.domain.Cellar;
import com.mbras.cavavin.service.CellarService;
import com.mbras.cavavin.web.rest.errors.BadRequestAlertException;
import com.mbras.cavavin.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
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

    public CellarResource(CellarService cellarService) {
        this.cellarService = cellarService;
    }

    /**
     * POST  /cellars : Create a new cellar.
     *
     * @param cellar the cellar to create
     * @return the ResponseEntity with status 201 (Created) and with body the new cellar, or with status 400 (Bad Request) if the cellar has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/cellars")
    @Timed
    public ResponseEntity<Cellar> createCellar(@Valid @RequestBody Cellar cellar) throws URISyntaxException {
        log.debug("REST request to save Cellar : {}", cellar);
        if (cellar.getId() != null) {
            throw new BadRequestAlertException("A new cellar cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Cellar result = cellarService.save(cellar);
        return ResponseEntity.created(new URI("/api/cellars/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /cellars : Updates an existing cellar.
     *
     * @param cellar the cellar to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated cellar,
     * or with status 400 (Bad Request) if the cellar is not valid,
     * or with status 500 (Internal Server Error) if the cellar couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/cellars")
    @Timed
    public ResponseEntity<Cellar> updateCellar(@Valid @RequestBody Cellar cellar) throws URISyntaxException {
        log.debug("REST request to update Cellar : {}", cellar);
        if (cellar.getId() == null) {
            return createCellar(cellar);
        }
        Cellar result = cellarService.save(cellar);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, cellar.getId().toString()))
            .body(result);
    }

    /**
     * GET  /cellars : get all the cellars.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of cellars in body
     */
    @GetMapping("/cellars")
    @Timed
    public List<Cellar> getAllCellars() {
        log.debug("REST request to get all Cellars");
        return cellarService.findAll();
        }

    /**
     * GET  /cellars/:id : get the "id" cellar.
     *
     * @param id the id of the cellar to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the cellar, or with status 404 (Not Found)
     */
    @GetMapping("/cellars/{id}")
    @Timed
    public ResponseEntity<Cellar> getCellar(@PathVariable Long id) {
        log.debug("REST request to get Cellar : {}", id);
        Cellar cellar = cellarService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(cellar));
    }

    /**
     * DELETE  /cellars/:id : delete the "id" cellar.
     *
     * @param id the id of the cellar to delete
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
    public List<Cellar> searchCellars(@RequestParam String query) {
        log.debug("REST request to search Cellars for query {}", query);
        return cellarService.search(query);
    }

}
