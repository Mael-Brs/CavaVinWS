package com.mbras.cellar.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mbras.cellar.domain.Year;

import com.mbras.cellar.repository.YearRepository;
import com.mbras.cellar.repository.search.YearSearchRepository;
import com.mbras.cellar.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Year.
 */
@RestController
@RequestMapping("/api")
public class YearResource {

    private final Logger log = LoggerFactory.getLogger(YearResource.class);
        
    @Inject
    private YearRepository yearRepository;

    @Inject
    private YearSearchRepository yearSearchRepository;

    /**
     * POST  /years : Create a new year.
     *
     * @param year the year to create
     * @return the ResponseEntity with status 201 (Created) and with body the new year, or with status 400 (Bad Request) if the year has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/years")
    @Timed
    public ResponseEntity<Year> createYear(@Valid @RequestBody Year year) throws URISyntaxException {
        log.debug("REST request to save Year : {}", year);
        if (year.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("year", "idexists", "A new year cannot already have an ID")).body(null);
        }
        Year result = yearRepository.save(year);
        yearSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/years/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("year", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /years : Updates an existing year.
     *
     * @param year the year to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated year,
     * or with status 400 (Bad Request) if the year is not valid,
     * or with status 500 (Internal Server Error) if the year couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/years")
    @Timed
    public ResponseEntity<Year> updateYear(@Valid @RequestBody Year year) throws URISyntaxException {
        log.debug("REST request to update Year : {}", year);
        if (year.getId() == null) {
            return createYear(year);
        }
        Year result = yearRepository.save(year);
        yearSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("year", year.getId().toString()))
            .body(result);
    }

    /**
     * GET  /years : get all the years.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of years in body
     */
    @GetMapping("/years")
    @Timed
    public List<Year> getAllYears() {
        log.debug("REST request to get all Years");
        List<Year> years = yearRepository.findAll();
        return years;
    }

    /**
     * GET  /years/:id : get the "id" year.
     *
     * @param id the id of the year to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the year, or with status 404 (Not Found)
     */
    @GetMapping("/years/{id}")
    @Timed
    public ResponseEntity<Year> getYear(@PathVariable Long id) {
        log.debug("REST request to get Year : {}", id);
        Year year = yearRepository.findOne(id);
        return Optional.ofNullable(year)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /years/:id : delete the "id" year.
     *
     * @param id the id of the year to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/years/{id}")
    @Timed
    public ResponseEntity<Void> deleteYear(@PathVariable Long id) {
        log.debug("REST request to delete Year : {}", id);
        yearRepository.delete(id);
        yearSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("year", id.toString())).build();
    }

    /**
     * SEARCH  /_search/years?query=:query : search for the year corresponding
     * to the query.
     *
     * @param query the query of the year search 
     * @return the result of the search
     */
    @GetMapping("/_search/years")
    @Timed
    public List<Year> searchYears(@RequestParam String query) {
        log.debug("REST request to search Years for query {}", query);
        return StreamSupport
            .stream(yearSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
