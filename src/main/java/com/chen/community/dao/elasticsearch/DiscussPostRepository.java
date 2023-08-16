package com.chen.community.dao.elasticsearch;

import com.chen.community.entity.DiscussPost;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
//ElasticsearchRepository<要处理的类,主键类型>
public interface DiscussPostRepository extends ElasticsearchRepository<DiscussPost,Integer> {
}
