package com.mbras.cellar.repository;

import com.mbras.cellar.domain.Vintage;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Vintage entity.
 */
@SuppressWarnings("unused")
public interface VintageRepository extends JpaRepository<Vintage,Long> {
    List<Vintage> findByWine_Id(Long id);
}
