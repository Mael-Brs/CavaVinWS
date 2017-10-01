package com.mbras.cavavin.repository;

import com.mbras.cavavin.domain.PinnedWine;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import java.util.List;


/**
 * Spring Data JPA repository for the PinnedWine entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PinnedWineRepository extends JpaRepository<PinnedWine,Long> {
    List<PinnedWine> findByUserId(Long id);

    @Query("select p from PinnedWine p join User u on u.id = p.userId where u.login = ?#{principal.username}")
    List<PinnedWine> findByUserIsCurrentUser();
}
