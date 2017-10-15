package com.mbras.cavavin.service;

import com.mbras.cavavin.domain.Cellar;
import com.mbras.cavavin.repository.CellarRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service Implementation for managing Cellar.
 */
@Service
@Transactional
public class CellarService {

    private final Logger log = LoggerFactory.getLogger(CellarService.class);

    private final CellarRepository cellarRepository;

    public CellarService(CellarRepository cellarRepository) {
        this.cellarRepository = cellarRepository;
    }

    /**
     * Save a cellar.
     *
     * @param cellar the entity to save
     * @return the persisted entity
     */
    public Cellar save(Cellar cellar) {
        log.debug("Request to save Cellar : {}", cellar);
        return cellarRepository.save(cellar);
    }

    /**
     *  Get all the cellars.
     *
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<Cellar> findAll() {
        log.debug("Request to get all Cellars");
        return cellarRepository.findByUserIsCurrentUser();
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
        return cellarRepository.findOne(id);
    }

    /**
     *  Delete the  cellar by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Cellar : {}", id);
        cellarRepository.delete(id);
    }
}
