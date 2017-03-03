package com.mbras.cave.repository.search;

import com.mbras.cave.domain.Wine;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Wine entity.
 */
public interface WineSearchRepository extends ElasticsearchRepository<Wine, Long> {
}
