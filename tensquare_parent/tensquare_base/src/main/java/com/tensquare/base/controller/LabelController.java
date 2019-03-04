package com.tensquare.base.controller;

import com.tensquare.base.pojo.Label;
import com.tensquare.base.service.LabelService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
/**
 * 标签Controller
 */
@RestController // @Controller+@ResponseBody
@RequestMapping("/label")
@CrossOrigin // 支持前端跨域请求
public class LabelController {

    @Autowired
    private LabelService labelService;

    /**
     * 查询所有
     */
    @RequestMapping(method = RequestMethod.GET)
    public Result findAll(){
        return new Result(true, StatusCode.OK,"查询成功",labelService.findAll());
    }

    /**
     * 查询一个
     */
    @RequestMapping(value = "/{labelid}",method = RequestMethod.GET)
    public Result findById(@PathVariable String labelid){
        return new Result(true,StatusCode.OK,"查询成功",labelService.findById(labelid));
    }

    /**
     * 添加
     */
    @RequestMapping(method = RequestMethod.POST)
    public Result add(@RequestBody Label label){
        labelService.add(label);
        return new Result(true,StatusCode.OK,"添加成功");
    }

    /**
     * 修改
     */
    @RequestMapping(value = "/{labelid}",method = RequestMethod.PUT)
    public Result update(@RequestBody Label label,@PathVariable String labelid){
        label.setId(labelid);
        labelService.update(label);
        return new Result(true,StatusCode.OK,"修改成功");
    }

    /**
     * 删除
     */
    @RequestMapping(value = "/{labelid}",method = RequestMethod.DELETE)
    public Result deleteById(@PathVariable String labelid){
        labelService.deleteById(labelid);
        return new Result(true,StatusCode.OK,"删除成功");
    }

    /**
     * 条件查询
     */
    @RequestMapping(value = "/search",method = RequestMethod.POST)
    public Result findSearch(@RequestBody Map<String,Object> searchMap){
        List<Label> list = labelService.findSearch(searchMap);
        return new Result(true,StatusCode.OK,"查询成功",list);
    }

    /**
     * 条件分页查询
     */
    @RequestMapping(value = "/search/{page}/{size}",method = RequestMethod.POST)
    public Result findSearch(@RequestBody Map<String,Object> searchMap,@PathVariable int page,@PathVariable int size){
        Page<Label> pageData = labelService.findSearch(searchMap,page,size);
        return new Result(true,StatusCode.OK,"查询成功",new PageResult<>(pageData.getTotalElements(),pageData.getContent()));
    }
}
