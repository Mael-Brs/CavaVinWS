package com.mbras.cave.service;

import com.mbras.cave.domain.WineInCellar;

import java.util.List;

/**
 * Service Interface for managing WineInCellar.
 */
public interface WineInCellarService {

    /**
     * Save a wineInCellar.
     *
     * @param wineInCellar the entity to save
     * @return the persisted entity
     */
    WineInCellar save(WineInCellar wineInCellar);

    /**
     *  Get all the wineInCellars.
     *
     *  @return the list of entities
     */
    List<WineInCellar> findAll();

    /**
     *  Get the "id" wineInCellar.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    WineInCellar findOne(Long id);

    /**
     *  Get all wineInCellar by cellar id.
     *
     *  @param id the id of the cellar entity
     *  @return the entity
     */
    List<WineInCellar> findByCellar(Long id);

    /**
     *  Delete the "id" wineInCellar.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the wineInCellar corresponding to the query.
     *
     *  @param query the query of the search
     *
     *  @return the list of entities
     */
    List<WineInCellar> search(String query);
}
