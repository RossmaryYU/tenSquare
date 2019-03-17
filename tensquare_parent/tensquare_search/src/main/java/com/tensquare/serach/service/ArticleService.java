package com.tensquare.serach.service;

import com.tensquare.serach.dao.ArticleDao;
import com.tensquare.serach.pojo.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

/**
 * @author yfy
 * @date 2019/3/10
 */
@Service
public class ArticleService {

    @Autowired
    private ArticleDao articleDao;

    /**
     *根据关键字分页搜索文章
     */
    public Page<Article> search(String keyword, int page, int size) {
        return articleDao.findByTitleOrContentLike(keyword,keyword, PageRequest.of(page-1,size));
    }
}
