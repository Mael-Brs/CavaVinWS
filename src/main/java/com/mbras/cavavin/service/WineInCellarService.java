package com.mbras.cavavin.service;

import com.mbras.cavavin.domain.*;
import com.mbras.cavavin.repository.*;
import com.mbras.cavavin.repository.search.WineInCellarSearchRepository;
import com.mbras.cavavin.repository.search.WineSearchRepository;
import com.mbras.cavavin.security.SecurityUtils;
import com.mbras.cavavin.service.dto.WineInCellarDTO;
import com.mbras.cavavin.service.mapper.WineInCellarMapper;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing WineInCellar.
 */
@Service
@Transactional
public class WineInCellarService {

    private final Logger log = LoggerFactory.getLogger(WineInCellarService.class);

    private final WineInCellarRepository wineInCellarRepository;

    private final WineInCellarMapper wineInCellarMapper;

    private final WineInCellarSearchRepository wineInCellarSearchRepository;

    private final WineRepository wineRepository;

    private final WineSearchRepository wineSearchRepository;

    private final VintageRepository vintageRepository;

    private final WineAgingDataRepository wineAgingDataRepository;

    private final CellarRepository cellarRepository;

    public WineInCellarService(WineInCellarRepository wineInCellarRepository, WineInCellarMapper wineInCellarMapper, WineInCellarSearchRepository wineInCellarSearchRepository,  WineRepository wineRepository, WineSearchRepository wineSearchRepository, VintageRepository vintageRepository, WineAgingDataRepository wineAgingDataRepository, CellarRepository cellarRepository) {
        this.wineInCellarRepository = wineInCellarRepository;
        this.wineInCellarMapper = wineInCellarMapper;
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
     * @param wineInCellarDTO the entity to save
     * @return the persisted entity
     */
    public WineInCellarDTO save(WineInCellarDTO wineInCellarDTO) {
        log.debug("Request to save WineInCellar : {}", wineInCellarDTO);
        WineInCellar wineInCellar = wineInCellarMapper.toEntity(wineInCellarDTO);
        if(wineInCellar.getChildYear() == null || wineInCellar.getApogeeYear() == null) {
            setWineAgingData(wineInCellar);
        }
        wineInCellar = wineInCellarRepository.save(wineInCellar);
        WineInCellarDTO result = wineInCellarMapper.toDto(wineInCellar);
        wineInCellarSearchRepository.save(wineInCellar);
        return result;
    }

    /**
     * Save a wineInCellar.
     *
     * @param wineInCellarDTO the entity to save
     * @return the persisted entity
     */
    public WineInCellarDTO saveFromScratch(WineInCellarDTO wineInCellarDTO) {
        log.debug("Request to save WineInCellar : {}", wineInCellarDTO);
        WineInCellar wineInCellar = wineInCellarMapper.toEntity(wineInCellarDTO);
        Vintage newVintage = wineInCellar.getVintage();
        Wine newWine = newVintage.getWine();

        newWine = wineRepository.save(newWine);
        wineSearchRepository.save(newWine);
        newVintage.setWine(newWine);

        newVintage = vintageRepository.save(newVintage);
        wineInCellar.setVintage(newVintage);

        if(wineInCellar.getChildYear() == null || wineInCellar.getApogeeYear() == null) {
            setWineAgingData(wineInCellar);
        }
        wineInCellar = wineInCellarRepository.save(wineInCellar);
        asyncIndexing(wineInCellar);

        return wineInCellarMapper.toDto(wineInCellar);
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
    public Page<WineInCellarDTO> findAll(Pageable pageable) {
        log.debug("Request to get all WineInCellars");
        return wineInCellarRepository.findByUserIsCurrentUser(pageable)
            .map(wineInCellarMapper::toDto);
    }

    /**
     * Get one wineInCellar by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public WineInCellarDTO findOne(Long id) {
        log.debug("Request to get WineInCellar : {}", id);
        WineInCellar wineInCellar = wineInCellarRepository.findOne(id);
        return wineInCellarMapper.toDto(wineInCellar);
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
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<WineInCellarDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of WineInCellars for query {}", query);
        Optional<String> userLogin = SecurityUtils.getCurrentUserLogin();

        BoolQueryBuilder queryBuild = boolQuery().must(queryStringQuery(query));
        queryBuild.filter(matchQuery("cellar.user.login", userLogin));
        Page<WineInCellar> result = wineInCellarSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(wineInCellarMapper::toDto);
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
     * Set getChildYear or getApogeeYear if it is not with default config values
     * @param wineInCellar the wine to add aging data
     */
    private void setWineAgingData(WineInCellar wineInCellar) {
        Wine wine = wineInCellar.getVintage().getWine();
        if(wine == null){
            return;
        }
        WineAgingData wineAgingData = wineAgingDataRepository.findByColorAndRegion(wine.getColor(), wine.getRegion());
        if(wineAgingData != null) {
            if(wineInCellar.getChildYear() == null) {
                wineInCellar.setChildYear(wineAgingData.getMinKeep() + wineInCellar.getVintage().getYear());
            }
            if (wineInCellar.getApogeeYear() == null) {
                wineInCellar.setApogeeYear(wineAgingData.getMaxKeep() + wineInCellar.getVintage().getYear());
            }
        }
    }
}
