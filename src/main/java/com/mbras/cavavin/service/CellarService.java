package com.mbras.cavavin.service;

import com.mbras.cavavin.domain.Cellar;
import com.mbras.cavavin.repository.CellarRepository;
import com.mbras.cavavin.repository.search.CellarSearchRepository;
import com.mbras.cavavin.service.dto.CellarDTO;
import com.mbras.cavavin.service.mapper.CellarMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * Service Implementation for managing Cellar.
 */
@Service
@Transactional
public class CellarService {

    private final Logger log = LoggerFactory.getLogger(CellarService.class);

    private final CellarRepository cellarRepository;

    private final CellarMapper cellarMapper;

    private final CellarSearchRepository cellarSearchRepository;

    public CellarService(CellarRepository cellarRepository, CellarMapper cellarMapper, CellarSearchRepository cellarSearchRepository) {
        this.cellarRepository = cellarRepository;
        this.cellarMapper = cellarMapper;
        this.cellarSearchRepository = cellarSearchRepository;
    }

    /**
     * Save a cellar.
     *
     * @param cellarDTO the entity to save
     * @return the persisted entity
     */
    public CellarDTO save(CellarDTO cellarDTO) {
        log.debug("Request to save Cellar : {}", cellarDTO);
        Cellar cellar = cellarMapper.toEntity(cellarDTO);
        cellar = cellarRepository.save(cellar);
        CellarDTO result = cellarMapper.toDto(cellar);
        cellarSearchRepository.save(cellar);
        return result;
    }

    /**
     *  Get all the cellars.
     *
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<CellarDTO> findAll() {
        log.debug("Request to get all Cellars");
        return cellarRepository.findAll().stream()
            .map(cellarMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     *  Get one cellar by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public CellarDTO findOne(Long id) {
        log.debug("Request to get Cellar : {}", id);
        Cellar cellar = cellarRepository.findOne(id);
        return cellarMapper.toDto(cellar);
    }

    /**
     *  Get all cellars for given user login
     *
     *  @param id the login of the User for cellar to retrieve
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public CellarDTO findByUser(Long id) {
        log.debug("Request to get Cellar : {}", id);
        Cellar cellar = cellarRepository.findByUser_id(id);
        return cellarMapper.toDto(cellar);
    }

    /**
     *  Delete the  cellar by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Cellar : {}", id);
        cellarRepository.delete(id);
        cellarSearchRepository.delete(id);
    }

    /**
     * Search for the cellar corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<CellarDTO> search(String query) {
        log.debug("Request to search Cellars for query {}", query);
        return StreamSupport
            .stream(cellarSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(cellarMapper::toDto)
            .collect(Collectors.toList());
    }
}
