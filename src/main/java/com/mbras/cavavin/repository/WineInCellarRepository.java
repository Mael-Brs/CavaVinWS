package com.mbras.cavavin.repository;

import com.mbras.cavavin.domain.WineByColor;
import com.mbras.cavavin.domain.WineByRegion;
import com.mbras.cavavin.domain.WineByYear;
import com.mbras.cavavin.domain.WineInCellar;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA repository for the WineInCellar entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WineInCellarRepository extends JpaRepository<WineInCellar, Long>, JpaSpecificationExecutor<WineInCellar> {

    @Query(value = "select sum(w.quantity) from WineInCellar w where w.cellar.id = :id")
    Long sumWine(@Param("id") Long id);

    @Query(value = "select  new com.mbras.cavavin.domain.WineByRegion(w.vintage.wine.region.regionName, sum(w.quantity)) from WineInCellar w where w.cellar.id = :id group by w.vintage.wine.region.regionName")
    List<WineByRegion> sumWineByRegion(@Param("id") Long id);

    @Query(value = "select new com.mbras.cavavin.domain.WineByColor(w.vintage.wine.color.colorName, sum(w.quantity)) from WineInCellar w where w.cellar.id = :id group by w.vintage.wine.color.colorName")
    List<WineByColor> sumWineByColor(@Param("id") Long id);

    @Query(value = "select new com.mbras.cavavin.domain.WineByYear(w.vintage.year, sum(w.quantity)) from WineInCellar w where w.cellar.id = :id group by w.vintage.year")
    List<WineByYear> sumWineByYear(@Param("id") Long id);

    @Modifying
    @Query("delete from WineInCellar w where w.id in (select w1.id from WineInCellar w1 where w1.cellar.user.login = ?#{principal.username}) and w.id = :id")
    void deleteUserIsOwner(@Param("id") Long id);

    @Query("select w from WineInCellar w where w.cellar.user.login = ?#{principal.username}")
    Page<WineInCellar> findByUserIsCurrentUser(Pageable pageable);
}
