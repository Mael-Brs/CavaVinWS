package com.mbras.cavavin.repository;

import com.mbras.cavavin.domain.Cellar;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the Cellar entity.
 */
@SuppressWarnings("unused")
public interface CellarRepository extends JpaRepository<Cellar,Long> {
	Cellar findByUser_Login(String login);
}
