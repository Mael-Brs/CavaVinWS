package com.mbras.cavavin.repository.search;

import com.mbras.cavavin.domain.WineInCellar;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the WineInCellar entity.
 */
public interface WineInCellarSearchRepository extends ElasticsearchRepository<WineInCellar, Long> {
}
