package com.mbras.cave.repository.search;

import com.mbras.cave.domain.WineInCellar;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the WineInCellar entity.
 */
public interface WineInCellarSearchRepository extends ElasticsearchRepository<WineInCellar, Long> {
}
