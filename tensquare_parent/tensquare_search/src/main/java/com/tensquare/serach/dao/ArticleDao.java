package com.tensquare.serach.dao;

import com.tensquare.serach.pojo.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @author yfy
 * @date 2019/3/10
 * 文章dao
 */
public interface ArticleDao extends ElasticsearchRepository<Article,String> {

    /**
     * 文章搜索
     */
    Page<Article> findByTitleOrContentLike(String title, String content, Pageable pageable);
}
