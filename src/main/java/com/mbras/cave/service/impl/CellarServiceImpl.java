package com.mbras.cave.service.impl;

import com.mbras.cave.service.CellarService;
import com.mbras.cave.domain.Cellar;
import com.mbras.cave.repository.CellarRepository;
import com.mbras.cave.repository.search.CellarSearchRepository;
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
 * Service Implementation for managing Cellar.
 */
@Service
@Transactional
public class CellarServiceImpl implements CellarService{

    private final Logger log = LoggerFactory.getLogger(CellarServiceImpl.class);

    @Inject
    private CellarRepository cellarRepository;

    @Inject
    private CellarSearchRepository cellarSearchRepository;

    /**
     * Save a cellar.
     *
     * @param cellar the entity to save
     * @return the persisted entity
     */
    public Cellar save(Cellar cellar) {
        log.debug("Request to save Cellar : {}", cellar);
        Cellar result = cellarRepository.save(cellar);
        cellarSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the cellars.
     *
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<Cellar> findAll() {
        log.debug("Request to get all Cellars");
        List<Cellar> result = cellarRepository.findAll();

        return result;
    }

    /**
     *  Get one cellar by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public Cellar findOne(Long id) {
        log.debug("Request to get Cellar : {}", id);
        Cellar cellar = cellarRepository.findOne(id);
        return cellar;
    }

    /**
     *  Get all cellars for given user login
     *
     *  @param login the login of the User for cellar to retrieve
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public Cellar findByUser(String login) {
        log.debug("Request to get Cellar : {}", login);
        Cellar cellar = cellarRepository.findByUser_Login(login);
        return cellar;
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
    public List<Cellar> search(String query) {
        log.debug("Request to search Cellars for query {}", query);
        return StreamSupport
            .stream(cellarSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
