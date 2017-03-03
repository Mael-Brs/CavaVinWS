package com.mbras.cave.repository;

import com.mbras.cave.domain.WineInCellar;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the WineInCellar entity.
 */
@SuppressWarnings("unused")
public interface WineInCellarRepository extends JpaRepository<WineInCellar,Long> {
    List<WineInCellar> findByCellar_Id(Long id);
}
