package com.mbras.cellar.repository.search;

import com.mbras.cellar.domain.Wine;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Wine entity.
 */
public interface WineSearchRepository extends ElasticsearchRepository<Wine, Long> {
}
