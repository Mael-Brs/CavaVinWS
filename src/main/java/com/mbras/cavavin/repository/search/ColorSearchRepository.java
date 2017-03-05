package com.mbras.cavavin.repository.search;

import com.mbras.cavavin.domain.Color;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Color entity.
 */
public interface ColorSearchRepository extends ElasticsearchRepository<Color, Long> {
}
