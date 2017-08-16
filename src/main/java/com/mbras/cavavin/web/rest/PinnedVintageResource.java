package com.mbras.cavavin.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mbras.cavavin.domain.PinnedVintage;
import com.mbras.cavavin.repository.PinnedVintageRepository;
import com.mbras.cavavin.repository.search.PinnedVintageSearchRepository;
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

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * REST controller for managing PinnedVintage.
 */
@RestController
@RequestMapping("/api")
public class PinnedVintageResource {

    private final Logger log = LoggerFactory.getLogger(PinnedVintageResource.class);

    private static final String ENTITY_NAME = "pinnedVintage";

    private final PinnedVintageRepository pinnedVintageRepository;

    private final PinnedVintageSearchRepository pinnedVintageSearchRepository;

    public PinnedVintageResource(PinnedVintageRepository pinnedVintageRepository, PinnedVintageSearchRepository pinnedVintageSearchRepository) {
        this.pinnedVintageRepository = pinnedVintageRepository;
        this.pinnedVintageSearchRepository = pinnedVintageSearchRepository;
    }

    /**
     * POST  /pinned-vintages : Create a new pinnedVintage.
     *
     * @param pinnedVintage the pinnedVintage to create
     * @return the ResponseEntity with status 201 (Created) and with body the new pinnedVintage, or with status 400 (Bad Request) if the pinnedVintage has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/pinned-vintages")
    @Timed
    public ResponseEntity<PinnedVintage> createPinnedVintage(@Valid @RequestBody PinnedVintage pinnedVintage) throws URISyntaxException {
        log.debug("REST request to save PinnedVintage : {}", pinnedVintage);
        if (pinnedVintage.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new pinnedVintage cannot already have an ID")).body(null);
        }
        PinnedVintage result = pinnedVintageRepository.save(pinnedVintage);
        pinnedVintageSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/pinned-vintages/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /pinned-vintages : Updates an existing pinnedVintage.
     *
     * @param pinnedVintage the pinnedVintage to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated pinnedVintage,
     * or with status 400 (Bad Request) if the pinnedVintage is not valid,
     * or with status 500 (Internal Server Error) if the pinnedVintage couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/pinned-vintages")
    @Timed
    public ResponseEntity<PinnedVintage> updatePinnedVintage(@Valid @RequestBody PinnedVintage pinnedVintage) throws URISyntaxException {
        log.debug("REST request to update PinnedVintage : {}", pinnedVintage);
        if (pinnedVintage.getId() == null) {
            return createPinnedVintage(pinnedVintage);
        }
        PinnedVintage result = pinnedVintageRepository.save(pinnedVintage);
        pinnedVintageSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, pinnedVintage.getId().toString()))
            .body(result);
    }

    /**
     * GET  /pinned-vintages : get all the pinnedVintages.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of pinnedVintages in body
     */
    @GetMapping("/pinned-vintages")
    @Timed
    public List<PinnedVintage> getAllPinnedVintages() {
        log.debug("REST request to get all PinnedVintages");
        return pinnedVintageRepository.findByUserIsCurrentUser();
    }

    /**
     * GET  /pinned-vintages : get all the pinnedVintages.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of pinnedVintages in body
     */
    @GetMapping("/users/{id}/pinned-vintages")
    @Timed
    public List<PinnedVintage> getAllPinnedVintagesForUser(@PathVariable Long id) {
        log.debug("REST request to get all PinnedVintages for user {}", id);
        return pinnedVintageRepository.findByUserId(id);
    }

    /**
     * GET  /pinned-vintages/:id : get the "id" pinnedVintage.
     *
     * @param id the id of the pinnedVintage to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the pinnedVintage, or with status 404 (Not Found)
     */
    @GetMapping("/pinned-vintages/{id}")
    @Timed
    public ResponseEntity<PinnedVintage> getPinnedVintage(@PathVariable Long id) {
        log.debug("REST request to get PinnedVintage : {}", id);
        PinnedVintage pinnedVintage = pinnedVintageRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(pinnedVintage));
    }

    /**
     * DELETE  /pinned-vintages/:id : delete the "id" pinnedVintage.
     *
     * @param id the id of the pinnedVintage to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/pinned-vintages/{id}")
    @Timed
    public ResponseEntity<Void> deletePinnedVintage(@PathVariable Long id) {
        log.debug("REST request to delete PinnedVintage : {}", id);
        pinnedVintageRepository.delete(id);
        pinnedVintageSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/pinned-vintages?query=:query : search for the pinnedVintage corresponding
     * to the query.
     *
     * @param query the query of the pinnedVintage search
     * @return the result of the search
     */
    @GetMapping("/_search/pinned-vintages")
    @Timed
    public List<PinnedVintage> searchPinnedVintages(@RequestParam String query) {
        log.debug("REST request to search PinnedVintages for query {}", query);
        return StreamSupport
            .stream(pinnedVintageSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
