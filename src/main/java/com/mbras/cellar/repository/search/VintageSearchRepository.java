package com.mbras.cellar.repository.search;

import com.mbras.cellar.domain.Vintage;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Vintage entity.
 */
public interface VintageSearchRepository extends ElasticsearchRepository<Vintage, Long> {
}
