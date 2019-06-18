package com.mbras.cavavin.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mbras.cavavin.service.CellarService;
import com.mbras.cavavin.service.WineInCellarService;
import com.mbras.cavavin.service.dto.CellarDTO;
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
 * REST controller for managing Cellar.
 */
@RestController
@RequestMapping("/api")
public class CellarResource {

    private final Logger log = LoggerFactory.getLogger(CellarResource.class);

    private static final String ENTITY_NAME = "cellar";

    private final CellarService cellarService;

    private WineInCellarService wineInCellarService;

    public CellarResource(CellarService cellarService, WineInCellarService wineInCellarService) {
        this.cellarService = cellarService;
        this.wineInCellarService = wineInCellarService;
    }

    /**
     * POST  /cellars : Create a new cellar.
     *
     * @param cellarDTO the cellarDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new cellarDTO, or with status 400 (Bad Request) if the cellar has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/cellars")
    @Timed
    public ResponseEntity<CellarDTO> createCellar(@Valid @RequestBody CellarDTO cellarDTO) throws URISyntaxException {
        log.debug("REST request to save Cellar : {}", cellarDTO);
        if (cellarDTO.getId() != null) {
            throw new BadRequestAlertException("A new cellar cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CellarDTO result = cellarService.save(cellarDTO);
        return ResponseEntity.created(new URI("/api/cellars/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /cellars : Updates an existing cellar.
     *
     * @param cellarDTO the cellarDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated cellarDTO,
     * or with status 400 (Bad Request) if the cellarDTO is not valid,
     * or with status 500 (Internal Server Error) if the cellarDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/cellars")
    @Timed
    public ResponseEntity<CellarDTO> updateCellar(@Valid @RequestBody CellarDTO cellarDTO) throws URISyntaxException {
        log.debug("REST request to update Cellar : {}", cellarDTO);
        if (cellarDTO.getId() == null) {
            return createCellar(cellarDTO);
        }
        CellarDTO result = cellarService.save(cellarDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, cellarDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /cellars : get all the cellars.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of cellars in body
     */
    @GetMapping("/cellars")
    @Timed
    public List<CellarDTO> getAllCellars() {
        log.debug("REST request to get all Cellars");
        List<CellarDTO> cellars = cellarService.findAll();
        for(CellarDTO cellar : cellars){
            cellar.setSumOfWine(wineInCellarService.getWineSum(cellar.getId()));
            cellar.setWineByRegion(wineInCellarService.getWineByRegion(cellar.getId()));
            cellar.setWineByColor(wineInCellarService.getWineByColor(cellar.getId()));
            cellar.setWineByYear(wineInCellarService.getWineByYear(cellar.getId()));
        }
        return cellars;
    }

    /**
     * GET  /cellars/:id : get the "id" cellar.
     *
     * @param id the id of the cellarDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the cellarDTO, or with status 404 (Not Found)
     */
    @GetMapping("/cellars/{id}")
    @Timed
    public ResponseEntity<CellarDTO> getCellar(@PathVariable Long id) {
        log.debug("REST request to get Cellar : {}", id);
        CellarDTO cellar = cellarService.findOne(id);
        if (cellar != null){
            cellar.setSumOfWine(wineInCellarService.getWineSum(id));
            cellar.setWineByRegion(wineInCellarService.getWineByRegion(id));
            cellar.setWineByColor(wineInCellarService.getWineByColor(id));
            cellar.setWineByYear(wineInCellarService.getWineByYear(id));
        }
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(cellar));
    }

    /**
     * DELETE  /cellars/:id : delete the "id" cellar.
     *
     * @param id the id of the cellarDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/cellars/{id}")
    @Timed
    public ResponseEntity<Void> deleteCellar(@PathVariable Long id) {
        log.debug("REST request to delete Cellar : {}", id);
        cellarService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
