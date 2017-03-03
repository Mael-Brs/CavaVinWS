package com.mbras.cave.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mbras.cave.domain.Cellar;
import com.mbras.cave.domain.WineInCellar;
import com.mbras.cave.service.CellarService;
import com.mbras.cave.service.WineInCellarService;
import com.mbras.cave.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
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
     * @param cellar the cellar to create
     * @return the ResponseEntity with status 201 (Created) and with body the new cellar, or with status 400 (Bad Request) if the cellar has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/cellars",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Cellar> createCellar(@RequestBody Cellar cellar) throws URISyntaxException {
        log.debug("REST request to save Cellar : {}", cellar);
        if (cellar.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("cellar", "idexists", "A new cellar cannot already have an ID")).body(null);
        }
        Cellar result = cellarService.save(cellar);
        return ResponseEntity.created(new URI("/api/cellars/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("cellar", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /cellars : Updates an existing cellar.
     *
     * @param cellar the cellar to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated cellar,
     * or with status 400 (Bad Request) if the cellar is not valid,
     * or with status 500 (Internal Server Error) if the cellar couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/cellars",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Cellar> updateCellar(@RequestBody Cellar cellar) throws URISyntaxException {
        log.debug("REST request to update Cellar : {}", cellar);
        if (cellar.getId() == null) {
            return createCellar(cellar);
        }
        Cellar result = cellarService.save(cellar);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("cellar", cellar.getId().toString()))
            .body(result);
    }

    /**
     * GET  /cellars : get all the cellars.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of cellars in body
     */
    @RequestMapping(value = "/cellars",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
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
    @RequestMapping(value = "/cellars/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Cellar> getCellar(@PathVariable Long id) {
        log.debug("REST request to get Cellar : {}", id);
        Cellar cellar = cellarService.findOne(id);
        return Optional.ofNullable(cellar)
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
    @RequestMapping(value = "/cellars/{id}/wine-in-cellars",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<WineInCellar>> getWineInCellarForCellar(@PathVariable Long id) {
        log.debug("REST request to get WineInCellars for Cellar : {}", id);
        List<WineInCellar> wineInCellars = wineInCellarService.findByCellar(id);
        return Optional.ofNullable(wineInCellars)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /cellars/:id : delete the "id" cellar.
     *
     * @param id the id of the cellar to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/cellars/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
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
    @RequestMapping(value = "/_search/cellars",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Cellar> searchCellars(@RequestParam String query) {
        log.debug("REST request to search Cellars for query {}", query);
        return cellarService.search(query);
    }


}
