package com.mbras.cellar.repository;

import com.mbras.cellar.domain.Wine;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Wine entity.
 */
@SuppressWarnings("unused")
public interface WineRepository extends JpaRepository<Wine,Long> {

}
