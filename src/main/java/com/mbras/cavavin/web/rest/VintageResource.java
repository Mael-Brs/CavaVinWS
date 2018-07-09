package com.mbras.cavavin.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mbras.cavavin.domain.Vintage;
import com.mbras.cavavin.domain.Wine;
import com.mbras.cavavin.domain.WineAgingData;
import com.mbras.cavavin.repository.VintageRepository;
import com.mbras.cavavin.repository.WineAgingDataRepository;
import com.mbras.cavavin.security.AuthoritiesConstants;
import com.mbras.cavavin.web.rest.errors.BadRequestAlertException;
import com.mbras.cavavin.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Vintage.
 */
@RestController
@RequestMapping("/api")
public class VintageResource {

    private final Logger log = LoggerFactory.getLogger(VintageResource.class);

    private static final String ENTITY_NAME = "vintage";

    private final VintageRepository vintageRepository;

    private final WineAgingDataRepository wineAgingDataRepository;

    public VintageResource(VintageRepository vintageRepository, WineAgingDataRepository wineAgingDataRepository) {
        this.vintageRepository = vintageRepository;
        this.wineAgingDataRepository = wineAgingDataRepository;
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
            throw new BadRequestAlertException("A new vintage cannot already have an ID", ENTITY_NAME, "idexists");
        }

        if (vintage.getApogeeYear() == null || vintage.getChildYear() == null) {
            Wine wine = vintage.getWine();

            WineAgingData wineAgingData = wineAgingDataRepository.findByColorAndRegion(wine.getColor(), wine.getRegion());
            if(wineAgingData != null) {
                Integer childYear = vintage.getChildYear() != null ? vintage.getChildYear() : wineAgingData.getMinKeep() + vintage.getYear();
                vintage.setChildYear(childYear);
                Integer apogeeYear = vintage.getApogeeYear() != null ? vintage.getApogeeYear() : wineAgingData.getMaxKeep() + vintage.getYear();
                vintage.setApogeeYear(apogeeYear);
            }
        }

        Vintage result = vintageRepository.save(vintage);
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
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<Void> deleteVintage(@PathVariable Long id) {
        log.debug("REST request to delete Vintage : {}", id);
        vintageRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
