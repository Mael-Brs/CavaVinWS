package com.mbras.cave.service;

import com.mbras.cave.domain.Cellar;

import java.util.List;

/**
 * Service Interface for managing Cellar.
 */
public interface CellarService {

    /**
     * Save a cellar.
     *
     * @param cellar the entity to save
     * @return the persisted entity
     */
    Cellar save(Cellar cellar);

    /**
     *  Get all the cellars.
     *
     *  @return the list of entities
     */
    List<Cellar> findAll();

    /**
     *  Get the "id" cellar.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    Cellar findOne(Long id);

    /**
     *  Get all cellars for given user login
     *
     *  @param login the login of the User for cellar to retrieve
     *  @return the entity
     */
    Cellar findByUser(String login);

    /**
     *  Delete the "id" cellar.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the cellar corresponding to the query.
     *
     *  @param query the query of the search
     *
     *  @return the list of entities
     */
    List<Cellar> search(String query);
}
