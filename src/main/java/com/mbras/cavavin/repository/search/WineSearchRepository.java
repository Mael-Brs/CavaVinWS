package com.mbras.cavavin.repository.search;

import com.mbras.cavavin.domain.Wine;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

/**
 * Spring Data Elasticsearch repository for the Wine entity.
 */
public interface WineSearchRepository extends ElasticsearchRepository<Wine, Long> {
    List<Wine> findByNameOrProducer(String name, String producer);
}
