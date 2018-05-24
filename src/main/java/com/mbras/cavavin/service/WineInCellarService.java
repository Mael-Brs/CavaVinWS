package com.mbras.cavavin.service;

import com.mbras.cavavin.domain.WineInCellar;
import com.mbras.cavavin.repository.WineInCellarRepository;
import com.mbras.cavavin.repository.search.WineInCellarSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


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

    public WineInCellarService(WineInCellarRepository wineInCellarRepository, WineInCellarSearchRepository wineInCellarSearchRepository) {
        this.wineInCellarRepository = wineInCellarRepository;
        this.wineInCellarSearchRepository = wineInCellarSearchRepository;
    }

    /**
     * Save a wineInCellar.
     *
     * @param wineInCellar the entity to save
     * @return the persisted entity
     */
    public WineInCellar save(WineInCellar wineInCellar) {
        log.debug("Request to save WineInCellar : {}", wineInCellar);
        WineInCellar result = wineInCellarRepository.save(wineInCellar);
        wineInCellarSearchRepository.save(result);
        return result;
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
        return wineInCellarRepository.findAll(pageable);
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
        wineInCellarRepository.delete(id);
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
    public Page<WineInCellar> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of WineInCellars for query {}", query);
        Page<WineInCellar> result = wineInCellarSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
