package com.mbras.cavavin.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mbras.cavavin.domain.WineAgingData;
import com.mbras.cavavin.repository.WineAgingDataRepository;
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

/**
 * REST controller for managing WineAgingData.
 */
@RestController
@RequestMapping("/api")
public class WineAgingDataResource {

    private final Logger log = LoggerFactory.getLogger(WineAgingDataResource.class);

    private static final String ENTITY_NAME = "wineAgingData";

    private final WineAgingDataRepository wineAgingDataRepository;

    public WineAgingDataResource(WineAgingDataRepository wineAgingDataRepository) {
        this.wineAgingDataRepository = wineAgingDataRepository;
    }

    /**
     * POST  /wine-aging-data : Create a new wineAgingData.
     *
     * @param wineAgingData the wineAgingData to create
     * @return the ResponseEntity with status 201 (Created) and with body the new wineAgingData, or with status 400 (Bad Request) if the wineAgingData has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/wine-aging-data")
    @Timed
    public ResponseEntity<WineAgingData> createWineAgingData(@Valid @RequestBody WineAgingData wineAgingData) throws URISyntaxException {
        log.debug("REST request to save WineAgingData : {}", wineAgingData);
        if (wineAgingData.getId() != null) {
            throw new BadRequestAlertException("A new wineAgingData cannot already have an ID", ENTITY_NAME, "idexists");
        }
        WineAgingData result = wineAgingDataRepository.save(wineAgingData);
        return ResponseEntity.created(new URI("/api/wine-aging-data/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /wine-aging-data : Updates an existing wineAgingData.
     *
     * @param wineAgingData the wineAgingData to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated wineAgingData,
     * or with status 400 (Bad Request) if the wineAgingData is not valid,
     * or with status 500 (Internal Server Error) if the wineAgingData couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/wine-aging-data")
    @Timed
    public ResponseEntity<WineAgingData> updateWineAgingData(@Valid @RequestBody WineAgingData wineAgingData) throws URISyntaxException {
        log.debug("REST request to update WineAgingData : {}", wineAgingData);
        if (wineAgingData.getId() == null) {
            return createWineAgingData(wineAgingData);
        }
        WineAgingData result = wineAgingDataRepository.save(wineAgingData);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, wineAgingData.getId().toString()))
            .body(result);
    }

    /**
     * GET  /wine-aging-data : get all the wineAgingData.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of wineAgingData in body
     */
    @GetMapping("/wine-aging-data")
    @Timed
    public List<WineAgingData> getAllWineAgingData() {
        log.debug("REST request to get all WineAgingData");
        return wineAgingDataRepository.findAll();
        }

    /**
     * GET  /wine-aging-data/:id : get the "id" wineAgingData.
     *
     * @param id the id of the wineAgingData to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the wineAgingData, or with status 404 (Not Found)
     */
    @GetMapping("/wine-aging-data/{id}")
    @Timed
    public ResponseEntity<WineAgingData> getWineAgingData(@PathVariable Long id) {
        log.debug("REST request to get WineAgingData : {}", id);
        WineAgingData wineAgingData = wineAgingDataRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(wineAgingData));
    }

    /**
     * DELETE  /wine-aging-data/:id : delete the "id" wineAgingData.
     *
     * @param id the id of the wineAgingData to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/wine-aging-data/{id}")
    @Timed
    public ResponseEntity<Void> deleteWineAgingData(@PathVariable Long id) {
        log.debug("REST request to delete WineAgingData : {}", id);
        wineAgingDataRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
