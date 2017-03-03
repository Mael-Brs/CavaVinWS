package com.mbras.cellar.repository.search;

import com.mbras.cellar.domain.Cellar;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Cellar entity.
 */
public interface CellarSearchRepository extends ElasticsearchRepository<Cellar, Long> {
}
