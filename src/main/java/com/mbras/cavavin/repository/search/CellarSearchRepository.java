package com.mbras.cavavin.repository.search;

import com.mbras.cavavin.domain.Cellar;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Cellar entity.
 */
public interface CellarSearchRepository extends ElasticsearchRepository<Cellar, Long> {
}
