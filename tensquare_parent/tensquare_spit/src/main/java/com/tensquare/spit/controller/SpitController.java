package com.tensquare.spit.controller;

import com.tensquare.spit.pojo.Spit;
import com.tensquare.spit.service.SpitService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;


/**
 * 吐槽Controller
 */
@RestController // @Controller+@ResponseBody
@RequestMapping("/spit")
@CrossOrigin // 支持前端跨域请求
public class SpitController {

    @Autowired
    private SpitService spitService;

    /**
     * 查询所有
     */
    @RequestMapping(method = RequestMethod.GET)
    public Result findAll(){
        return new Result(true, StatusCode.OK,"查询成功",spitService.findAll());
    }

    /**
     * 查询一个
     */
    @RequestMapping(value = "/{spitid}",method = RequestMethod.GET)
    public Result findById(@PathVariable String spitid){
        return new Result(true,StatusCode.OK,"查询成功",spitService.findById(spitid));
    }

    /**
     * 添加
     */
    @RequestMapping(method = RequestMethod.POST)
    public Result add(@RequestBody Spit spit){
        spitService.add(spit);
        return new Result(true,StatusCode.OK,"添加成功");
    }

    /**
     * 修改
     */
    @RequestMapping(value = "/{spitid}",method = RequestMethod.PUT)
    public Result update(@RequestBody Spit spit,@PathVariable String spitid){
        spit.setId(spitid);
        spitService.update(spit);
        return new Result(true,StatusCode.OK,"修改成功");
    }

    /**
     * 删除
     */
    @RequestMapping(value = "/{spitid}",method = RequestMethod.DELETE)
    public Result deleteById(@PathVariable String spitid){
        spitService.deleteById(spitid);
        return new Result(true,StatusCode.OK,"删除成功");
    }

    /**
     * 根据上级ID查询吐槽数据
     */
    @RequestMapping(value = "/comment/{parentid}/{page}/{size}",method = RequestMethod.GET)
    public Result comment(@PathVariable String parentid,@PathVariable int page,@PathVariable int size){
        Page<Spit> pageData = spitService.comment(parentid,page,size);
        return new Result(true,StatusCode.OK,"查询成功",new PageResult<>(pageData.getTotalElements(),pageData.getContent()));
    }

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 吐槽点赞
     */
    @RequestMapping(value = "/thumbup/{spitid}",method = RequestMethod.PUT)
    public Result thumbup(@PathVariable String spitid){
        //模拟当前登录用户
        String userid = "1001";

        //1.从redis查询该用户是否对该吐槽点赞过
        String flag = (String)redisTemplate.opsForValue().get("thumbup_"+userid+"_"+spitid);
        if(flag!=null){
            //该用户已经对该吐槽点赞过
            //取消点赞
            spitService.cancelThumbup(spitid);

            //把redis的缓存清空
            redisTemplate.delete("thumbup_"+userid+"_"+spitid);

            return new Result(true,StatusCode.OK,"取消点赞成功");
            //return new Result(false,StatusCode.REPEAT_ERROR,"你已经点赞过啦");
        }

        spitService.thumbup(spitid);

        //把该用户已经对该吐槽的记录存入redis
        redisTemplate.opsForValue().set("thumbup_"+userid+"_"+spitid,"1",1, TimeUnit.DAYS);
        return new Result(true,StatusCode.OK,"点赞成功");
    }

}
