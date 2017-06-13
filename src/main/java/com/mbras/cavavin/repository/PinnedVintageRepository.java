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

    @Query("select pinned_vintage from PinnedVintage pinned_vintage where pinned_vintage.user.login = ?#{principal.username}")
    List<PinnedVintage> findByUserIsCurrentUser();
    
}
