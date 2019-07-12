package com.mbras.cavavin.service;

import com.mbras.cavavin.domain.Cellar;
import com.mbras.cavavin.repository.CellarRepository;
import com.mbras.cavavin.service.dto.CellarDTO;
import com.mbras.cavavin.service.mapper.CellarMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing Cellar.
 */
@Service
@Transactional
public class CellarService {

    private final Logger log = LoggerFactory.getLogger(CellarService.class);

    private final CellarRepository cellarRepository;

    private final CellarMapper cellarMapper;

    public CellarService(CellarRepository cellarRepository, CellarMapper cellarMapper) {
        this.cellarRepository = cellarRepository;
        this.cellarMapper = cellarMapper;
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
        return cellarMapper.toDto(cellar);
    }

    /**
     * Get all the cellars.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<CellarDTO> findAll() {
        log.debug("Request to get all Cellars");
        return cellarRepository.findByUserIsCurrentUser().stream()
            .map(cellarMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one cellar by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public CellarDTO findOne(Long id) {
        log.debug("Request to get Cellar : {}", id);
        Cellar cellar = cellarRepository.findOne(id);
        return cellarMapper.toDto(cellar);
    }

    /**
     * Delete the cellar by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Cellar : {}", id);
        cellarRepository.delete(id);
    }
}
