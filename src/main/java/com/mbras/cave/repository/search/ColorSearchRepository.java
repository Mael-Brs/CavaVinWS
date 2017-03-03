package com.mbras.cave.repository.search;

import com.mbras.cave.domain.Color;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Color entity.
 */
public interface ColorSearchRepository extends ElasticsearchRepository<Color, Long> {
}
