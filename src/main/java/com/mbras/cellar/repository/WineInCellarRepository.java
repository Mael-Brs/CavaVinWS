package com.mbras.cellar.repository;

import com.mbras.cellar.domain.WineInCellar;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the WineInCellar entity.
 */
@SuppressWarnings("unused")
public interface WineInCellarRepository extends JpaRepository<WineInCellar,Long> {

}
