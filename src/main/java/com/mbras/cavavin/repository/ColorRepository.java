package com.mbras.cavavin.repository;

import com.mbras.cavavin.domain.Color;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Color entity.
 */
@SuppressWarnings("unused")
public interface ColorRepository extends JpaRepository<Color,Long> {

}
