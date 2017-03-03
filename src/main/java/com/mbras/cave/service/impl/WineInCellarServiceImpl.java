package com.mbras.cave.service.impl;

import com.mbras.cave.service.WineInCellarService;
import com.mbras.cave.domain.WineInCellar;
import com.mbras.cave.repository.WineInCellarRepository;
import com.mbras.cave.repository.search.WineInCellarSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing WineInCellar.
 */
@Service
@Transactional
public class WineInCellarServiceImpl implements WineInCellarService{

    private final Logger log = LoggerFactory.getLogger(WineInCellarServiceImpl.class);

    @Inject
    private WineInCellarRepository wineInCellarRepository;

    @Inject
    private WineInCellarSearchRepository wineInCellarSearchRepository;

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
     *  Get all the wineInCellars.
     *
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<WineInCellar> findAll() {
        log.debug("Request to get all WineInCellars");
        List<WineInCellar> result = wineInCellarRepository.findAll();

        return result;
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
        WineInCellar wineInCellar = wineInCellarRepository.findOne(id);
        return wineInCellar;
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
        List<WineInCellar> wineInCellars = wineInCellarRepository.findByCellar_Id(id);
        return wineInCellars;
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
}
