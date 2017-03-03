package com.mbras.cave.service;

import com.mbras.cave.domain.Wine;

import java.util.List;

/**
 * Service Interface for managing Wine.
 */
public interface WineService {

    /**
     * Save a wine.
     *
     * @param wine the entity to save
     * @return the persisted entity
     */
    Wine save(Wine wine);

    /**
     *  Get all the wines.
     *  
     *  @return the list of entities
     */
    List<Wine> findAll();

    /**
     *  Get the "id" wine.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    Wine findOne(Long id);

    /**
     *  Delete the "id" wine.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the wine corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @return the list of entities
     */
    List<Wine> search(String query);
}
