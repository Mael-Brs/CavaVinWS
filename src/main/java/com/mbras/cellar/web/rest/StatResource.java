package com.mbras.cellar.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mbras.cellar.domain.WineByColor;
import com.mbras.cellar.domain.WineByRegion;
import com.mbras.cellar.repository.WineInCellarRepository;
import com.mbras.cellar.service.WineInCellarService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.util.List;

/**
 * REST controller for managing stats of cellar.
 */
@RestController
@RequestMapping("/api")
public class StatResource {

    private final Logger log = LoggerFactory.getLogger(WineInCellarResource.class);

    @Inject
    private WineInCellarRepository wineInCellarRepository;

    /**
     * GET  /sum-of-wine : get total number of wine
     *
     * @return the ResponseEntity with status 200 (OK) and the total number of wine in body
     */
    @GetMapping("/sum-of-wine")
    @Timed
    public Long getWineSum() {
        log.debug("REST request to get total number of wine");
        return wineInCellarRepository.sumWine();
    }

    /**
     * GET  /wine-by-region : get number of wine group by region
     *
     * @return the ResponseEntity with status 200 (OK) and the list of wine by region in body
     */
    @GetMapping("/wine-by-region")
    @Timed
    public List<WineByRegion> getWineByRegion() {
        log.debug("REST request to get number of wine by region");
        return wineInCellarRepository.sumWineByRegion();
    }

    /**
     * GET  /wine-by-color : get number of wine group by color
     *
     * @return the ResponseEntity with status 200 (OK) and the list of wine by color in body
     */
    @GetMapping("/wine-by-color")
    @Timed
    public List<WineByColor> getWineByColor() {
        log.debug("REST request to get number of wine by color");
        return wineInCellarRepository.sumWineByColor();
    }

}
