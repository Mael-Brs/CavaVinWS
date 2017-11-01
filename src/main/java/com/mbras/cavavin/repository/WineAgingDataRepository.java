package com.mbras.cavavin.repository;

import com.mbras.cavavin.domain.Color;
import com.mbras.cavavin.domain.Region;
import com.mbras.cavavin.domain.WineAgingData;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data JPA repository for the WineAgingData entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WineAgingDataRepository extends JpaRepository<WineAgingData,Long> {
    @Cacheable("AgingDataByColorAndRegion")
    WineAgingData findByColorAndRegion(Color color, Region region);
}
