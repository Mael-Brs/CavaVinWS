package com.mbras.cavavin.repository.search;

import com.mbras.cavavin.domain.PinnedVintage;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the PinnedVintage entity.
 */
public interface PinnedVintageSearchRepository extends ElasticsearchRepository<PinnedVintage, Long> {
}
