package com.mbras.cavavin.repository;

import com.mbras.cavavin.domain.Year;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Year entity.
 */
@SuppressWarnings("unused")
public interface YearRepository extends JpaRepository<Year,Long> {

}
