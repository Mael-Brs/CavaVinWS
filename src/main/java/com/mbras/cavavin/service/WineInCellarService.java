package com.mbras.cavavin.service;

import com.mbras.cavavin.domain.*;
import com.mbras.cavavin.repository.*;
import com.mbras.cavavin.repository.search.WineInCellarSearchRepository;
import com.mbras.cavavin.repository.search.WineSearchRepository;
import com.mbras.cavavin.security.SecurityUtils;
import com.mbras.cavavin.web.rest.errors.BadRequestAlertException;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing WineInCellar.
 */
@Service
@Transactional
public class WineInCellarService {

    private final Logger log = LoggerFactory.getLogger(WineInCellarService.class);

    private final WineInCellarRepository wineInCellarRepository;

    private final WineInCellarSearchRepository wineInCellarSearchRepository;

    private final WineRepository wineRepository;

    private final WineSearchRepository wineSearchRepository;

    private final VintageRepository vintageRepository;

    private final WineAgingDataRepository wineAgingDataRepository;

    private final CellarRepository cellarRepository;

    public WineInCellarService(WineInCellarRepository wineInCellarRepository, WineInCellarSearchRepository wineInCellarSearchRepository, WineRepository wineRepository, WineSearchRepository wineSearchRepository, VintageRepository vintageRepository, WineAgingDataRepository wineAgingDataRepository, CellarRepository cellarRepository) {
        this.wineInCellarRepository = wineInCellarRepository;
        this.wineInCellarSearchRepository = wineInCellarSearchRepository;
        this.wineRepository = wineRepository;
        this.wineSearchRepository = wineSearchRepository;
        this.vintageRepository = vintageRepository;
        this.wineAgingDataRepository = wineAgingDataRepository;
        this.cellarRepository = cellarRepository;
    }

    /**
     * Save a wineInCellar.
     *
     * @param wineInCellar the entity to save
     * @return the persisted entity
     */
    public WineInCellar save(WineInCellar wineInCellar) {
        log.debug("Request to save WineInCellar : {}", wineInCellar);
        if(wineInCellar.getMaxKeep() == null || wineInCellar.getMinKeep() == null) {
            setWineAgingData(wineInCellar);
        }
        wineInCellar.setApogee();
        WineInCellar result = wineInCellarRepository.save(wineInCellar);
        asyncIndexing(result);
        return result;
    }

    /**
     * Save a wineInCellar.
     *
     * @param wineInCellar the entity to save
     * @return the persisted entity
     */
    public WineInCellar saveFromScratch(WineInCellar wineInCellar) {
        log.debug("Request to save WineInCellar : {}", wineInCellar);
        Vintage newVintage = wineInCellar.getVintage();
        Wine newWine = newVintage.getWine();

        newWine = wineRepository.save(newWine);
        wineSearchRepository.save(newWine);
        newVintage.setWine(newWine);

        newVintage = vintageRepository.save(newVintage);
        wineInCellar.setVintage(newVintage);

        if(wineInCellar.getMaxKeep() == null || wineInCellar.getMinKeep() == null) {
            setWineAgingData(wineInCellar);
        }
        wineInCellar.setApogee();
        WineInCellar result = wineInCellarRepository.save(wineInCellar);
        asyncIndexing(result);
        return result;
    }

    /**
     * Crée l'index elestciSearch en mode asynchrone
     * @param result le vin à indexer
     */
    @Async
    public void asyncIndexing(WineInCellar result) {
        wineInCellarSearchRepository.save(result);
    }

    /**
     * Get all the wineInCellars.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<WineInCellar> findAll(Pageable pageable) {
        log.debug("Request to get all WineInCellars");
        return wineInCellarRepository.findByUserIsCurrentUser(pageable);
    }

    /**
     * Get one wineInCellar by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public WineInCellar findOne(Long id) {
        log.debug("Request to get WineInCellar : {}", id);
        return wineInCellarRepository.findOne(id);
    }

    /**
     * Delete the wineInCellar by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete WineInCellar : {}", id);
        wineInCellarRepository.deleteUserIsOwner(id);
        wineInCellarSearchRepository.delete(id);
    }

    /**
     * Search for the wineInCellar corresponding to the query.
     *
     * @param query the query of the search
     * @param cellarId identifiant de la cave
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<WineInCellar> search(String query, Long cellarId, Pageable pageable) {
        log.debug("Request to search for a page of WineInCellars for query {}", query);
        List<Long> cellarIdList = new ArrayList<>();

        if(cellarId == null){
            cellarIdList.addAll(
                this.cellarRepository.findByUserIsCurrentUser().stream().map(Cellar::getId).collect(Collectors.toList())
            );
        } else {
            cellarIdList.add(cellarId);
        }

        if(cellarIdList.isEmpty()){
            throw new BadRequestAlertException("No cellar found for user " + SecurityUtils.getCurrentUserLogin(), "wineInCellar", "nocellar");
        }

        BoolQueryBuilder queryBuild = boolQuery().must(queryStringQuery(query));
        cellarIdList.forEach(id -> queryBuild.filter(matchQuery("cellarId", id)));
        Page<WineInCellar> result = wineInCellarSearchRepository.search(queryBuild, pageable);
        return result;
    }

    /**
     * Get total number of wine
     *
     * @return the number of wine in cellar of user
     */
    @Transactional(readOnly = true)
    public Long getWineSum(Long id) {
        log.debug("Request to get total number of wine");
        return Optional.ofNullable(wineInCellarRepository.sumWine(id)).orElse(0L);
    }

    /**
     * Get number of wine group by region
     *
     * @return the list of wine by region
     */
    @Transactional(readOnly = true)
    public List<WineByRegion> getWineByRegion(Long id) {
        log.debug("Request to get number of wine by region");
        return wineInCellarRepository.sumWineByRegion(id);
    }

    /**
     * Get number of wine group by color
     *
     * @return the list of wine by color
     */
    @Transactional(readOnly = true)
    public List<WineByColor> getWineByColor(Long id) {
        log.debug("Request to get number of wine by color");
        return wineInCellarRepository.sumWineByColor(id);
    }

    /**
     * Get number of wine group by year
     *
     * @return the list of wine by year
     */
    @Transactional(readOnly = true)
    public List<WineByYear> getWineByYear(Long id) {
        log.debug("Request to get number of wine by color");
        return wineInCellarRepository.sumWineByYear(id);
    }

    /**
     * Set maxKeep or minKeep if it is not with default config values
     * @param wineInCellar the wine to add aging data
     */
    private void setWineAgingData(WineInCellar wineInCellar) {
        Wine wine = wineInCellar.getVintage().getWine();
        if(wine == null){
            return;
        }
        WineAgingData wineAgingData = wineAgingDataRepository.findByColorAndRegion(wine.getColor(), wine.getRegion());
        if(wineAgingData != null) {
            Integer minKeep = wineInCellar.getMinKeep() != null ? wineInCellar.getMinKeep() : wineAgingData.getMinKeep();
            wineInCellar.setMinKeep(minKeep);
            Integer maxKeep = wineInCellar.getMaxKeep() != null ? wineInCellar.getMaxKeep() : wineAgingData.getMaxKeep();
            wineInCellar.setMaxKeep(maxKeep);
        }
    }
}
