package com.mbras.cavavin.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mbras.cavavin.domain.PinnedWine;

import com.mbras.cavavin.repository.PinnedWineRepository;
import com.mbras.cavavin.repository.search.PinnedWineSearchRepository;
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
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing PinnedWine.
 */
@RestController
@RequestMapping("/api")
public class PinnedWineResource {

    private final Logger log = LoggerFactory.getLogger(PinnedWineResource.class);

    private static final String ENTITY_NAME = "pinnedWine";

    private final PinnedWineRepository pinnedWineRepository;

    private final PinnedWineSearchRepository pinnedWineSearchRepository;

    public PinnedWineResource(PinnedWineRepository pinnedWineRepository, PinnedWineSearchRepository pinnedWineSearchRepository) {
        this.pinnedWineRepository = pinnedWineRepository;
        this.pinnedWineSearchRepository = pinnedWineSearchRepository;
    }

    /**
     * POST  /pinned-wines : Create a new pinnedWine.
     *
     * @param pinnedWine the pinnedWine to create
     * @return the ResponseEntity with status 201 (Created) and with body the new pinnedWine, or with status 400 (Bad Request) if the pinnedWine has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/pinned-wines")
    @Timed
    public ResponseEntity<PinnedWine> createPinnedWine(@Valid @RequestBody PinnedWine pinnedWine) throws URISyntaxException {
        log.debug("REST request to save PinnedWine : {}", pinnedWine);
        if (pinnedWine.getId() != null) {
            throw new BadRequestAlertException("A new pinnedWine cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PinnedWine result = pinnedWineRepository.save(pinnedWine);
        pinnedWineSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/pinned-wines/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /pinned-wines : Updates an existing pinnedWine.
     *
     * @param pinnedWine the pinnedWine to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated pinnedWine,
     * or with status 400 (Bad Request) if the pinnedWine is not valid,
     * or with status 500 (Internal Server Error) if the pinnedWine couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/pinned-wines")
    @Timed
    public ResponseEntity<PinnedWine> updatePinnedWine(@Valid @RequestBody PinnedWine pinnedWine) throws URISyntaxException {
        log.debug("REST request to update PinnedWine : {}", pinnedWine);
        if (pinnedWine.getId() == null) {
            return createPinnedWine(pinnedWine);
        }
        PinnedWine result = pinnedWineRepository.save(pinnedWine);
        pinnedWineSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, pinnedWine.getId().toString()))
            .body(result);
    }

    /**
     * GET  /pinned-wines : get all the pinnedWines.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of pinnedWines in body
     */
    @GetMapping("/pinned-wines")
    @Timed
    public List<PinnedWine> getAllPinnedWines() {
        log.debug("REST request to get all PinnedWines");
        return pinnedWineRepository.findAll();
        }

    /**
     * GET  /pinned-wines/:id : get the "id" pinnedWine.
     *
     * @param id the id of the pinnedWine to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the pinnedWine, or with status 404 (Not Found)
     */
    @GetMapping("/pinned-wines/{id}")
    @Timed
    public ResponseEntity<PinnedWine> getPinnedWine(@PathVariable Long id) {
        log.debug("REST request to get PinnedWine : {}", id);
        PinnedWine pinnedWine = pinnedWineRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(pinnedWine));
    }

    /**
     * DELETE  /pinned-wines/:id : delete the "id" pinnedWine.
     *
     * @param id the id of the pinnedWine to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/pinned-wines/{id}")
    @Timed
    public ResponseEntity<Void> deletePinnedWine(@PathVariable Long id) {
        log.debug("REST request to delete PinnedWine : {}", id);
        pinnedWineRepository.delete(id);
        pinnedWineSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/pinned-wines?query=:query : search for the pinnedWine corresponding
     * to the query.
     *
     * @param query the query of the pinnedWine search
     * @return the result of the search
     */
    @GetMapping("/_search/pinned-wines")
    @Timed
    public List<PinnedWine> searchPinnedWines(@RequestParam String query) {
        log.debug("REST request to search PinnedWines for query {}", query);
        return StreamSupport
            .stream(pinnedWineSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
