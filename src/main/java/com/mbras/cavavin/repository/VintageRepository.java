package com.mbras.cavavin.repository;

import com.mbras.cavavin.domain.Vintage;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Vintage entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VintageRepository extends JpaRepository<Vintage,Long> {
    
}
