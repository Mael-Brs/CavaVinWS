package com.mbras.cave.repository.search;

import com.mbras.cave.domain.Cellar;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Cellar entity.
 */
public interface CellarSearchRepository extends ElasticsearchRepository<Cellar, Long> {
}
