package com.mbras.cavavin.repository;

import com.mbras.cavavin.domain.Color;
import com.mbras.cavavin.domain.Region;
import com.mbras.cavavin.domain.Wine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


/**
 * Spring Data JPA repository for the Wine entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WineRepository extends JpaRepository<Wine, Long> {
    Optional<Wine> findOneByNameIgnoreCaseAndProducerIgnoreCaseAndColorAndRegion(String name, String producer, Color color, Region region);
}
