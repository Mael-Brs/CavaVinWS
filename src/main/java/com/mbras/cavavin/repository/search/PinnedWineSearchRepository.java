package com.mbras.cavavin.repository.search;

import com.mbras.cavavin.domain.PinnedWine;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the PinnedWine entity.
 */
public interface PinnedWineSearchRepository extends ElasticsearchRepository<PinnedWine, Long> {
}
