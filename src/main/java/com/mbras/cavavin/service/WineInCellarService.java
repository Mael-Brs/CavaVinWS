package com.mbras.cavavin.service;

import com.mbras.cavavin.domain.*;
import com.mbras.cavavin.repository.VintageRepository;
import com.mbras.cavavin.repository.WineAgingDataRepository;
import com.mbras.cavavin.repository.WineInCellarRepository;
import com.mbras.cavavin.repository.WineRepository;
import com.mbras.cavavin.repository.search.WineInCellarSearchRepository;
import com.mbras.cavavin.repository.search.WineSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

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

    public WineInCellarService(WineInCellarRepository wineInCellarRepository, WineInCellarSearchRepository wineInCellarSearchRepository, WineRepository wineRepository, WineSearchRepository wineSearchRepository, VintageRepository vintageRepository, WineAgingDataRepository wineAgingDataRepository) {
        this.wineInCellarRepository = wineInCellarRepository;
        this.wineInCellarSearchRepository = wineInCellarSearchRepository;
        this.wineRepository = wineRepository;
        this.wineSearchRepository = wineSearchRepository;
        this.vintageRepository = vintageRepository;
        this.wineAgingDataRepository = wineAgingDataRepository;
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
        WineInCellar result = wineInCellarRepository.save(wineInCellar);
        wineInCellarSearchRepository.save(result);
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
        WineInCellar result = wineInCellarRepository.save(wineInCellar);

        wineInCellarSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the wineInCellars.
     *
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<WineInCellar> findAll() {
        log.debug("Request to get all WineInCellars");
        return wineInCellarRepository.findByUserIsCurrentUser();
    }

    /**
     *  Get one wineInCellar by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public WineInCellar findOne(Long id) {
        log.debug("Request to get WineInCellar : {}", id);
        return wineInCellarRepository.findOne(id);
    }

    /**
     *  Get all wineInCellar by cellar id.
     *
     *  @param id the id of the cellar entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public List<WineInCellar> findByCellar(Long id) {
        log.debug("Request to get WineInCellar for cellar : {}", id);
        return wineInCellarRepository.findByCellarId(id);
    }

    /**
     *  Delete the  wineInCellar by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete WineInCellar : {}", id);
        wineInCellarRepository.delete(id);
        wineInCellarSearchRepository.delete(id);
    }

    /**
     * Search for the wineInCellar corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<WineInCellar> search(String query) {
        log.debug("Request to search WineInCellars for query {}", query);
        return StreamSupport
            .stream(wineInCellarSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
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

    private void setWineAgingData(WineInCellar wineInCellar) {
        Wine wine = wineInCellar.getVintage().getWine();
        WineAgingData wineAgingData = wineAgingDataRepository.findByColorAndRegion(wine.getColor(), wine.getRegion());
        if(wineAgingData != null) {
            wineInCellar.setMinKeep(wineAgingData.getMinKeep());
            wineInCellar.setMaxKeep(wineAgingData.getMaxKeep());
        }
    }
}
