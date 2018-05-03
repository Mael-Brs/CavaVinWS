package com.mbras.cavavin.service;


import com.mbras.cavavin.domain.Vintage_;
import com.mbras.cavavin.domain.WineInCellar;
import com.mbras.cavavin.domain.WineInCellar_;
import com.mbras.cavavin.repository.WineInCellarRepository;
import com.mbras.cavavin.service.dto.WineInCellarCriteria;
import io.github.jhipster.service.QueryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * Service for executing complex queries for WineInCellar entities in the database.
 * The main input is a {@link WineInCellarCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link WineInCellar} or a {@link Page} of {@link WineInCellar} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class WineInCellarQueryService extends QueryService<WineInCellar> {

    private final Logger log = LoggerFactory.getLogger(WineInCellarQueryService.class);


    private final WineInCellarRepository wineInCellarRepository;

    public WineInCellarQueryService(WineInCellarRepository wineInCellarRepository) {
        this.wineInCellarRepository = wineInCellarRepository;
    }

    /**
     * Return a {@link List} of {@link WineInCellar} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<WineInCellar> findByCriteria(WineInCellarCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<WineInCellar> specification = createSpecification(criteria);
        return wineInCellarRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link WineInCellar} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<WineInCellar> findByCriteria(WineInCellarCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<WineInCellar> specification = createSpecification(criteria);
        return wineInCellarRepository.findAll(specification, page);
    }

    /**
     * Function to convert WineInCellarCriteria to a {@link Specifications}
     */
    private Specifications<WineInCellar> createSpecification(WineInCellarCriteria criteria) {
        Specifications<WineInCellar> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), WineInCellar_.id));
            }
            if (criteria.getMinKeep() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMinKeep(), WineInCellar_.minKeep));
            }
            if (criteria.getMaxKeep() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMaxKeep(), WineInCellar_.maxKeep));
            }
            if (criteria.getPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPrice(), WineInCellar_.price));
            }
            if (criteria.getQuantity() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getQuantity(), WineInCellar_.quantity));
            }
            if (criteria.getComments() != null) {
                specification = specification.and(buildStringSpecification(criteria.getComments(), WineInCellar_.comments));
            }
            if (criteria.getLocation() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLocation(), WineInCellar_.location));
            }
            if (criteria.getCellarId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCellarId(), WineInCellar_.cellarId));
            }
            if (criteria.getVintageId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getVintageId(), WineInCellar_.vintage, Vintage_.id));
            }
        }
        return specification;
    }

}
