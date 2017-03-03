package com.mbras.cave.repository;

import com.mbras.cave.domain.Color;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Color entity.
 */
@SuppressWarnings("unused")
public interface ColorRepository extends JpaRepository<Color,Long> {

}
