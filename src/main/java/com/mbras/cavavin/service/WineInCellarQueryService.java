package com.mbras.cavavin.service;


import com.mbras.cavavin.domain.*;
import com.mbras.cavavin.repository.WineInCellarRepository;
import com.mbras.cavavin.repository.search.WineInCellarSearchRepository;
import com.mbras.cavavin.security.SecurityUtils;
import com.mbras.cavavin.service.dto.WineInCellarCriteria;
import com.mbras.cavavin.service.dto.WineInCellarDTO;
import com.mbras.cavavin.service.mapper.WineInCellarMapper;
import com.mbras.cavavin.web.rest.errors.InternalServerErrorException;
import io.github.jhipster.service.QueryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.SingularAttribute;
import java.util.List;
import java.util.Optional;

/**
 * Service for executing complex queries for WineInCellar entities in the database.
 * The main input is a {@link WineInCellarCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link WineInCellarDTO} or a {@link Page} of {@link WineInCellarDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class WineInCellarQueryService extends QueryService<WineInCellar> {

    private final Logger log = LoggerFactory.getLogger(WineInCellarQueryService.class);


    private final WineInCellarRepository wineInCellarRepository;

    private final WineInCellarMapper wineInCellarMapper;

    private final WineInCellarSearchRepository wineInCellarSearchRepository;

    public WineInCellarQueryService(WineInCellarRepository wineInCellarRepository, WineInCellarMapper wineInCellarMapper, WineInCellarSearchRepository wineInCellarSearchRepository) {
        this.wineInCellarRepository = wineInCellarRepository;
        this.wineInCellarMapper = wineInCellarMapper;
        this.wineInCellarSearchRepository = wineInCellarSearchRepository;
    }

    /**
     * Return a {@link List} of {@link WineInCellarDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<WineInCellarDTO> findByCriteria(WineInCellarCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<WineInCellar> specification = createSpecification(criteria);
        return wineInCellarMapper.toDto(wineInCellarRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link WineInCellarDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<WineInCellarDTO> findByCriteria(WineInCellarCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<WineInCellar> specification = createSpecification(criteria);
        final Page<WineInCellar> result = wineInCellarRepository.findAll(specification, page);
        return result.map(wineInCellarMapper::toDto);
    }

    /**
     * Function to convert WineInCellarCriteria to a {@link Specifications}
     */
    private Specifications<WineInCellar> createSpecification(WineInCellarCriteria criteria) {
        Optional<String> userLogin = SecurityUtils.getCurrentUserLogin();
        if(!userLogin.isPresent()){
            throw new InternalServerErrorException("User could not be found");
        }
        // Restrict on current user CellarIds
        Specifications<WineInCellar> specification = Specifications.where((root, criteriaQuery, criteriaBuilder) -> {
            //Do a join for User
            Join<Cellar, User> user = root.join(WineInCellar_.cellar).join(Cellar_.user);
            return criteriaBuilder.and(criteriaBuilder.equal(user.get("login"), userLogin.get()));
        });
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), WineInCellar_.id));
            }
            if (criteria.getChildYear() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getChildYear(), WineInCellar_.childYear));
            }
            if (criteria.getApogeeYear() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getApogeeYear(), WineInCellar_.apogeeYear));
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
                specification = specification.and(buildReferringEntitySpecification(criteria.getCellarId(), WineInCellar_.cellar, Cellar_.id));
            }
            if (criteria.getVintageId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getVintageId(), WineInCellar_.vintage, Vintage_.id));
            }
            if(criteria.getRegion() != null){
                specification = specification.and((root, criteriaQuery, criteriaBuilder) ->
                    criteriaBuilder.equal(root.join(WineInCellar_.vintage).join(Vintage_.wine).join(Wine_.region).get(Region_.regionName), criteria.getRegion().getEquals())
                );
            }
            if(criteria.getColor() != null){
                specification = specification.and((root, criteriaQuery, criteriaBuilder) ->
                    criteriaBuilder.equal(root.join(WineInCellar_.vintage).join(Vintage_.wine).join(Wine_.color).get(Color_.colorName), criteria.getColor().getEquals())
                );
            }
            if(criteria.getKeywords() != null){
                String keywords = "%" + criteria.getKeywords().toUpperCase() + "%";
                specification = specification.and((root, criteriaQuery, criteriaBuilder) ->
                    criteriaBuilder.or(
                        buildLikeQuery(keywords, root, criteriaBuilder, Wine_.name),
                        buildLikeQuery(keywords, root, criteriaBuilder, Wine_.producer),
                        buildLikeQuery(keywords, root, criteriaBuilder, Wine_.appellation),
                        criteriaBuilder.like(criteriaBuilder.upper(root.get(WineInCellar_.comments)), keywords)
                    )
                );
            }
        }
        return specification;
    }

    private Predicate buildLikeQuery(String keywords, Root<WineInCellar> root, CriteriaBuilder criteriaBuilder, SingularAttribute<Wine, String> field) {
        return criteriaBuilder.like(criteriaBuilder.upper(root.join(WineInCellar_.vintage).join(Vintage_.wine).get(field)), keywords);
    }

}
