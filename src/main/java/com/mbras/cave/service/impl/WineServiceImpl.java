package com.mbras.cave.service.impl;

import com.mbras.cave.service.WineService;
import com.mbras.cave.domain.Wine;
import com.mbras.cave.repository.WineRepository;
import com.mbras.cave.repository.search.WineSearchRepository;
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
 * Service Implementation for managing Wine.
 */
@Service
@Transactional
public class WineServiceImpl implements WineService{

    private final Logger log = LoggerFactory.getLogger(WineServiceImpl.class);
    
    @Inject
    private WineRepository wineRepository;

    @Inject
    private WineSearchRepository wineSearchRepository;

    /**
     * Save a wine.
     *
     * @param wine the entity to save
     * @return the persisted entity
     */
    public Wine save(Wine wine) {
        log.debug("Request to save Wine : {}", wine);
        Wine result = wineRepository.save(wine);
        wineSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the wines.
     *  
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<Wine> findAll() {
        log.debug("Request to get all Wines");
        List<Wine> result = wineRepository.findAll();

        return result;
    }

    /**
     *  Get one wine by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Wine findOne(Long id) {
        log.debug("Request to get Wine : {}", id);
        Wine wine = wineRepository.findOne(id);
        return wine;
    }

    /**
     *  Delete the  wine by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Wine : {}", id);
        wineRepository.delete(id);
        wineSearchRepository.delete(id);
    }

    /**
     * Search for the wine corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<Wine> search(String query) {
        log.debug("Request to search Wines for query {}", query);
        return StreamSupport
            .stream(wineSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
