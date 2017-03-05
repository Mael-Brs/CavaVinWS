package com.mbras.cavavin.repository.search;

import com.mbras.cavavin.domain.Year;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Year entity.
 */
public interface YearSearchRepository extends ElasticsearchRepository<Year, Long> {
}
