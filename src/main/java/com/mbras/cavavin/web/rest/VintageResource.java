package com.mbras.cavavin.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mbras.cavavin.domain.Vintage;

import com.mbras.cavavin.repository.VintageRepository;
import com.mbras.cavavin.repository.search.VintageSearchRepository;
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

    private static final String ENTITY_NAME = "vintage";

    private final VintageRepository vintageRepository;

    private final VintageSearchRepository vintageSearchRepository;

    public VintageResource(VintageRepository vintageRepository, VintageSearchRepository vintageSearchRepository) {
        this.vintageRepository = vintageRepository;
        this.vintageSearchRepository = vintageSearchRepository;
    }

    /**
     * POST  /vintages : Create a new vintage.
     *
     * @param vintage the vintage to create
     * @return the ResponseEntity with status 201 (Created) and with body the new vintage, or with status 400 (Bad Request) if the vintage has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/vintages")
    @Timed
    public ResponseEntity<Vintage> createVintage(@Valid @RequestBody Vintage vintage) throws URISyntaxException {
        log.debug("REST request to save Vintage : {}", vintage);
        if (vintage.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new vintage cannot already have an ID")).body(null);
        }
        Vintage result = vintageRepository.save(vintage);
        vintageSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/vintages/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /vintages : Updates an existing vintage.
     *
     * @param vintage the vintage to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated vintage,
     * or with status 400 (Bad Request) if the vintage is not valid,
     * or with status 500 (Internal Server Error) if the vintage couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/vintages")
    @Timed
    public ResponseEntity<Vintage> updateVintage(@Valid @RequestBody Vintage vintage) throws URISyntaxException {
        log.debug("REST request to update Vintage : {}", vintage);
        if (vintage.getId() == null) {
            return createVintage(vintage);
        }
        Vintage result = vintageRepository.save(vintage);
        vintageSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, vintage.getId().toString()))
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
        return vintageRepository.findAll();
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
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(vintage));
    }

    /**
     * GET  /wines/:id/vintages : get vintages for the "id" wine.
     *
     * @param id the id of the wine to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the wine, or with status 404 (Not Found)
     */
    @GetMapping("/wines/{id}/vintages")
    @Timed
    public ResponseEntity<List<Vintage>> getVintageByWine(@PathVariable Long id) {
        log.debug("REST request to get Wine : {}", id);
        List<Vintage> vintages = vintageRepository.findByWine_Id(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(vintages));
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
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
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
