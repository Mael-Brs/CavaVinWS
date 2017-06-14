package com.mbras.cavavin.repository;

import com.mbras.cavavin.domain.Year;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Year entity.
 */
@SuppressWarnings("unused")
@Repository
public interface YearRepository extends JpaRepository<Year,Long> {
    
}
