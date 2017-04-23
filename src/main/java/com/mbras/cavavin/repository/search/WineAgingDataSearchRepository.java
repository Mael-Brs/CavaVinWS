package com.mbras.cavavin.repository.search;

import com.mbras.cavavin.domain.WineAgingData;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the WineAgingData entity.
 */
public interface WineAgingDataSearchRepository extends ElasticsearchRepository<WineAgingData, Long> {
}
