package com.mbras.cellar.repository.search;

import com.mbras.cellar.domain.Year;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Year entity.
 */
public interface YearSearchRepository extends ElasticsearchRepository<Year, Long> {
}
