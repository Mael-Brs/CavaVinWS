package com.mbras.cellar.service;

import com.codahale.metrics.annotation.Timed;
import com.mbras.cellar.domain.WineByColor;
import com.mbras.cellar.domain.WineByRegion;
import com.mbras.cellar.domain.WineInCellar;
import com.mbras.cellar.repository.WineInCellarRepository;
import com.mbras.cellar.repository.search.WineInCellarSearchRepository;
import com.mbras.cellar.service.dto.WineInCellarDTO;
import com.mbras.cellar.service.mapper.WineInCellarMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import javax.inject.Inject;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing WineInCellar.
 */
@Service
@Transactional
public class WineInCellarService {

    private final Logger log = LoggerFactory.getLogger(WineInCellarService.class);

    @Inject
    private WineInCellarRepository wineInCellarRepository;

    @Inject
    private WineInCellarMapper wineInCellarMapper;

    @Inject
    private WineInCellarSearchRepository wineInCellarSearchRepository;

    /**
     * Save a wineInCellar.
     *
     * @param wineInCellarDTO the entity to save
     * @return the persisted entity
     */
    public WineInCellarDTO save(WineInCellarDTO wineInCellarDTO) {
        log.debug("Request to save WineInCellar : {}", wineInCellarDTO);
        WineInCellar wineInCellar = wineInCellarMapper.wineInCellarDTOToWineInCellar(wineInCellarDTO);
        wineInCellar = wineInCellarRepository.save(wineInCellar);
        WineInCellarDTO result = wineInCellarMapper.wineInCellarToWineInCellarDTO(wineInCellar);
        wineInCellarSearchRepository.save(wineInCellar);
        return result;
    }

    /**
     *  Get all the wineInCellars.
     *
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<WineInCellarDTO> findAll() {
        log.debug("Request to get all WineInCellars");
        List<WineInCellarDTO> result = wineInCellarRepository.findAll().stream()
            .map(wineInCellarMapper::wineInCellarToWineInCellarDTO)
            .collect(Collectors.toCollection(LinkedList::new));

        return result;
    }

    /**
     *  Get one wineInCellar by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public WineInCellarDTO findOne(Long id) {
        log.debug("Request to get WineInCellar : {}", id);
        WineInCellar wineInCellar = wineInCellarRepository.findOne(id);
        WineInCellarDTO wineInCellarDTO = wineInCellarMapper.wineInCellarToWineInCellarDTO(wineInCellar);
        return wineInCellarDTO;
    }

    /**
     *  Get all wineInCellar by cellar id.
     *
     *  @param id the id of the cellar entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public List<WineInCellarDTO> findByCellar(Long id) {
        log.debug("Request to get WineInCellar for cellar : {}", id);
        List<WineInCellarDTO> result = wineInCellarRepository.findByCellar_Id(id).stream()
            .map(wineInCellarMapper::wineInCellarToWineInCellarDTO)
            .collect(Collectors.toCollection(LinkedList::new));
        return result;
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
    public List<WineInCellarDTO> search(String query) {
        log.debug("Request to search WineInCellars for query {}", query);
        return StreamSupport
            .stream(wineInCellarSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(wineInCellarMapper::wineInCellarToWineInCellarDTO)
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
        return wineInCellarRepository.sumWine(id);
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
     * GET  /wine-by-color : get number of wine group by color
     *
     * @return the list of wine by color
     */
    @Transactional(readOnly = true)
    public List<WineByColor> getWineByColor(Long id) {
        log.debug("Request to get number of wine by color");
        return wineInCellarRepository.sumWineByColor(id);
    }
}
