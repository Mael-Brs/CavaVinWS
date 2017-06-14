package com.mbras.cavavin.repository;

import com.mbras.cavavin.domain.Wine;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Wine entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WineRepository extends JpaRepository<Wine,Long> {
    
}
