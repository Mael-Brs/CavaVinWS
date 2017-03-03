package com.mbras.cave.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mbras.cave.domain.WineInCellar;
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
     * @param wineInCellar the wineInCellar to create
     * @return the ResponseEntity with status 201 (Created) and with body the new wineInCellar, or with status 400 (Bad Request) if the wineInCellar has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/wine-in-cellars",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<WineInCellar> createWineInCellar(@RequestBody WineInCellar wineInCellar) throws URISyntaxException {
        log.debug("REST request to save WineInCellar : {}", wineInCellar);
        if (wineInCellar.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("wineInCellar", "idexists", "A new wineInCellar cannot already have an ID")).body(null);
        }
        WineInCellar result = wineInCellarService.save(wineInCellar);
        return ResponseEntity.created(new URI("/api/wine-in-cellars/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("wineInCellar", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /wine-in-cellars : Updates an existing wineInCellar.
     *
     * @param wineInCellar the wineInCellar to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated wineInCellar,
     * or with status 400 (Bad Request) if the wineInCellar is not valid,
     * or with status 500 (Internal Server Error) if the wineInCellar couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/wine-in-cellars",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<WineInCellar> updateWineInCellar(@RequestBody WineInCellar wineInCellar) throws URISyntaxException {
        log.debug("REST request to update WineInCellar : {}", wineInCellar);
        if (wineInCellar.getId() == null) {
            return createWineInCellar(wineInCellar);
        }
        WineInCellar result = wineInCellarService.save(wineInCellar);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("wineInCellar", wineInCellar.getId().toString()))
            .body(result);
    }

    /**
     * GET  /wine-in-cellars : get all the wineInCellars.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of wineInCellars in body
     */
    @RequestMapping(value = "/wine-in-cellars",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<WineInCellar> getAllWineInCellars() {
        log.debug("REST request to get all WineInCellars");
        return wineInCellarService.findAll();
    }

    /**
     * GET  /wine-in-cellars/:id : get the "id" wineInCellar.
     *
     * @param id the id of the wineInCellar to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the wineInCellar, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/wine-in-cellars/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<WineInCellar> getWineInCellar(@PathVariable Long id) {
        log.debug("REST request to get WineInCellar : {}", id);
        WineInCellar wineInCellar = wineInCellarService.findOne(id);
        return Optional.ofNullable(wineInCellar)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /wine-in-cellars/:id : delete the "id" wineInCellar.
     *
     * @param id the id of the wineInCellar to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/wine-in-cellars/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
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
    @RequestMapping(value = "/_search/wine-in-cellars",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<WineInCellar> searchWineInCellars(@RequestParam String query) {
        log.debug("REST request to search WineInCellars for query {}", query);
        return wineInCellarService.search(query);
    }


}
