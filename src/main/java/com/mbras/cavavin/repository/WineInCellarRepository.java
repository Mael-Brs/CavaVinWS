package com.mbras.cavavin.repository;

import com.mbras.cavavin.domain.WineByRegion;
import com.mbras.cavavin.domain.WineByColor;
import com.mbras.cavavin.domain.WineByYear;
import com.mbras.cavavin.domain.WineInCellar;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the WineInCellar entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WineInCellarRepository extends JpaRepository<WineInCellar,Long> {
	List<WineInCellar> findByCellarId(Long id);

    @Query(value = "select sum(w.quantity) from WineInCellar w join Cellar c on c.id = :id")
    Long sumWine(@Param("id") Long id);

    @Query(value = "select  new com.mbras.cavavin.domain.WineByRegion(w.vintage.wine.region.regionName, sum(w.quantity)) from WineInCellar w join Cellar c on c.id = :id group by w.vintage.wine.region.regionName")
    List<WineByRegion> sumWineByRegion(@Param("id") Long id);

    @Query(value = "select new com.mbras.cavavin.domain.WineByColor(w.vintage.wine.color.colorName, sum(w.quantity)) from WineInCellar w join Cellar c on c.id = :id group by w.vintage.wine.color.colorName")
    List<WineByColor> sumWineByColor(@Param("id") Long id);

    @Query(value = "select new com.mbras.cavavin.domain.WineByYear(w.vintage.year.number, sum(w.quantity)) from WineInCellar w join Cellar c on c.id = :id group by w.vintage.year.number")
    List<WineByYear> sumWineByYear(@Param("id") Long id);
}
