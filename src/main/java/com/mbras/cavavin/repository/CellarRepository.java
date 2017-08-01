package com.mbras.cavavin.repository;

import com.mbras.cavavin.domain.Cellar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data JPA repository for the Cellar entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CellarRepository extends JpaRepository<Cellar,Long> {
	Cellar findByUserId(Long id);
}
