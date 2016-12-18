package com.mbras.cellar.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mbras.cellar.domain.Vintage;

import com.mbras.cellar.repository.VintageRepository;
import com.mbras.cellar.repository.search.VintageSearchRepository;
import com.mbras.cellar.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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
 * REST controller for managing Vintage.
 */
@RestController
@RequestMapping("/api")
public class VintageResource {

    private final Logger log = LoggerFactory.getLogger(VintageResource.class);
        
    @Inject
    private VintageRepository vintageRepository;

    @Inject
    private VintageSearchRepository vintageSearchRepository;

    /**
     * POST  /vintages : Create a new vintage.
     *
     * @param vintage the vintage to create
     * @return the ResponseEntity with status 201 (Created) and with body the new vintage, or with status 400 (Bad Request) if the vintage has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/vintages")
    @Timed
    public ResponseEntity<Vintage> createVintage(@RequestBody Vintage vintage) throws URISyntaxException {
        log.debug("REST request to save Vintage : {}", vintage);
        if (vintage.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("vintage", "idexists", "A new vintage cannot already have an ID")).body(null);
        }
        Vintage result = vintageRepository.save(vintage);
        vintageSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/vintages/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("vintage", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /vintages : Updates an existing vintage.
     *
     * @param vintage the vintage to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated vintage,
     * or with status 400 (Bad Request) if the vintage is not valid,
     * or with status 500 (Internal Server Error) if the vintage couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/vintages")
    @Timed
    public ResponseEntity<Vintage> updateVintage(@RequestBody Vintage vintage) throws URISyntaxException {
        log.debug("REST request to update Vintage : {}", vintage);
        if (vintage.getId() == null) {
            return createVintage(vintage);
        }
        Vintage result = vintageRepository.save(vintage);
        vintageSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("vintage", vintage.getId().toString()))
            .body(result);
    }

    /**
     * GET  /vintages : get all the vintages.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of vintages in body
     */
    @GetMapping("/vintages")
    @Timed
    public List<Vintage> getAllVintages() {
        log.debug("REST request to get all Vintages");
        List<Vintage> vintages = vintageRepository.findAll();
        return vintages;
    }

    /**
     * GET  /vintages/:id : get the "id" vintage.
     *
     * @param id the id of the vintage to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the vintage, or with status 404 (Not Found)
     */
    @GetMapping("/vintages/{id}")
    @Timed
    public ResponseEntity<Vintage> getVintage(@PathVariable Long id) {
        log.debug("REST request to get Vintage : {}", id);
        Vintage vintage = vintageRepository.findOne(id);
        return Optional.ofNullable(vintage)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /vintages/:id : delete the "id" vintage.
     *
     * @param id the id of the vintage to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/vintages/{id}")
    @Timed
    public ResponseEntity<Void> deleteVintage(@PathVariable Long id) {
        log.debug("REST request to delete Vintage : {}", id);
        vintageRepository.delete(id);
        vintageSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("vintage", id.toString())).build();
    }

    /**
     * SEARCH  /_search/vintages?query=:query : search for the vintage corresponding
     * to the query.
     *
     * @param query the query of the vintage search 
     * @return the result of the search
     */
    @GetMapping("/_search/vintages")
    @Timed
    public List<Vintage> searchVintages(@RequestParam String query) {
        log.debug("REST request to search Vintages for query {}", query);
        return StreamSupport
            .stream(vintageSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
