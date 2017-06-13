package com.mbras.cavavin.repository;

import com.mbras.cavavin.domain.WineInCellar;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the WineInCellar entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WineInCellarRepository extends JpaRepository<WineInCellar,Long> {
    
}
