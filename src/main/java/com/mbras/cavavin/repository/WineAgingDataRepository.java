package com.mbras.cavavin.repository;

import com.mbras.cavavin.domain.Color;
import com.mbras.cavavin.domain.Region;
import com.mbras.cavavin.domain.WineAgingData;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the WineAgingData entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WineAgingDataRepository extends JpaRepository<WineAgingData,Long> {
    WineAgingData findByColorAndRegion(Color color, Region region);
}
