package com.mbras.cavavin.service;


import com.mbras.cavavin.domain.*;
import com.mbras.cavavin.repository.WineInCellarRepository;
import com.mbras.cavavin.repository.search.WineInCellarSearchRepository;
import com.mbras.cavavin.security.SecurityUtils;
import com.mbras.cavavin.service.dto.WineInCellarCriteria;
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
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.SingularAttribute;
import java.util.List;
import java.util.Optional;


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

    private final WineInCellarSearchRepository wineInCellarSearchRepository;

    public WineInCellarQueryService(WineInCellarRepository wineInCellarRepository, WineInCellarSearchRepository wineInCellarSearchRepository) {
        this.wineInCellarRepository = wineInCellarRepository;
        this.wineInCellarSearchRepository = wineInCellarSearchRepository;
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
        Optional<String> userLogin = SecurityUtils.getCurrentUserLogin();
        if(!userLogin.isPresent()){
            throw new InternalServerErrorException("User could not be found");
        }
        // Restrict on current user CellarIds
        Specifications<WineInCellar> specification = Specifications.where((root, criteriaQuery, criteriaBuilder) -> {
            Root<Cellar> cellar = criteriaQuery.from(Cellar.class);
            Root<User> user = criteriaQuery.from(User.class);
            return criteriaBuilder.and(criteriaBuilder.equal(root.get("cellarId"), cellar.get("id")), criteriaBuilder.equal(user.get("id"), cellar.get("userId")), criteriaBuilder.equal(user.get("login"), userLogin.get()));
        });
        if (criteria != null) {
            specification = getWineInCellarSpecifications(criteria, specification);
        }
        return specification;
    }

    /**
     * Function to convert each WineInCellarCriteria to a {@link Specifications}
     */
    private Specifications<WineInCellar> getWineInCellarSpecifications(WineInCellarCriteria criteria, Specifications<WineInCellar> specification) {
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
        if (criteria.getChildYear() != null) {
            specification = specification.and(buildRangeSpecification(criteria.getChildYear(), WineInCellar_.childYear));
        }
        if (criteria.getApogeeYear() != null) {
            specification = specification.and(buildRangeSpecification(criteria.getApogeeYear(), WineInCellar_.apogeeYear));
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
        return specification;
    }

    private Predicate buildLikeQuery(String keywords, Root<WineInCellar> root, CriteriaBuilder criteriaBuilder, SingularAttribute<Wine, String> field) {
        return criteriaBuilder.like(criteriaBuilder.upper(root.join(WineInCellar_.vintage).join(Vintage_.wine).get(field)), keywords);
    }

}
