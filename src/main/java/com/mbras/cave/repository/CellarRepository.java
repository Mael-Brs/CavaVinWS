package com.mbras.cave.repository;

import com.mbras.cave.domain.Cellar;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Cellar entity.
 */
@SuppressWarnings("unused")
public interface CellarRepository extends JpaRepository<Cellar,Long> {

    Cellar findByUser_Login(String login);

}
