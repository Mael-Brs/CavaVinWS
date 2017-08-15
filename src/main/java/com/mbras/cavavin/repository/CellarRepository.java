package com.mbras.cavavin.repository;

import com.mbras.cavavin.domain.Cellar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Spring Data JPA repository for the Cellar entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CellarRepository extends JpaRepository<Cellar,Long> {
	Cellar findByUserId(Long id);

    @Query("select c from Cellar c join User u on u.id = c.userId where u.login = ?#{principal.username}")
    List<Cellar> findByUserIsCurrentUser();
}
