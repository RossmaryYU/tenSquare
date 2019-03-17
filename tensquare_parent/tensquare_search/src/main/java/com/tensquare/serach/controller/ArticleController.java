package com.tensquare.serach.controller;

import com.tensquare.serach.pojo.Article;
import com.tensquare.serach.service.ArticleService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

/**
 * @author yfy
 * @date 2019/3/10
 * 文章controller
 */
@RestController
@CrossOrigin
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    /**
     * 文章搜索
     */
    @RequestMapping(value = "/search/{keyword}/{page}/{size}",method = RequestMethod.GET)
    public Result search(@PathVariable String keyword,@PathVariable int page,@PathVariable int size){
        Page<Article> pageResult = articleService.search(keyword,page,size);
        return new Result(true, StatusCode.OK,"搜索成功",new PageResult<>(pageResult.getTotalElements(),pageResult.getContent()));
    }
}
