package com.mbras.cavavin.repository;

import com.mbras.cavavin.domain.PinnedWine;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the PinnedWine entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PinnedWineRepository extends JpaRepository<PinnedWine,Long> {
    
}
