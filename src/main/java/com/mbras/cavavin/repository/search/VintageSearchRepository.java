package com.mbras.cavavin.repository.search;

import com.mbras.cavavin.domain.Vintage;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Vintage entity.
 */
public interface VintageSearchRepository extends ElasticsearchRepository<Vintage, Long> {
}
