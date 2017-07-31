package com.mbras.cavavin.repository;

import com.mbras.cavavin.domain.PinnedVintage;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import java.util.List;


/**
 * Spring Data JPA repository for the PinnedVintage entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PinnedVintageRepository extends JpaRepository<PinnedVintage,Long> {
    List<PinnedVintage> findByUserId(Long id);
}
