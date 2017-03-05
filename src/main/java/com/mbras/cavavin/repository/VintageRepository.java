package com.mbras.cavavin.repository;

import com.mbras.cavavin.domain.Vintage;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Vintage entity.
 */
@SuppressWarnings("unused")
public interface VintageRepository extends JpaRepository<Vintage,Long> {
    List<Vintage> findByWine_Id(Long id);
}
