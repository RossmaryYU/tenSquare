package com.tensquare.spit.service;

import com.tensquare.spit.dao.SpitDao;
import com.tensquare.spit.pojo.Spit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import util.IdWorker;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 吐槽service
 */
@Service
public class SpitService {

    @Autowired
    private SpitDao spitDao;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 查询所有
     */
    public List<Spit> findAll(){
        return spitDao.findAll();
    }

    /**
     * 查询一个
     */
    public Spit findById(String id,String userid){
        //从redis中获取数据,判断此用户是否浏览过此吐槽
        String flag = (String)redisTemplate.opsForValue().get("visits_"+userid+"_"+id);
        if(flag==null){
            //1.构建查询条件
            Query query = new Query();
            query.addCriteria(Criteria.where("_id").is(id));

            //2.构建更新对象
            Update update = new Update();
            update.inc("visits",1);

            //3.使用MongoTemplae调用方法
            mongoTemplate.updateFirst(query,update,"spit");
            //记录此用户浏览过此吐槽信息
            redisTemplate.opsForValue().set("visits_"+userid+"_"+id,"1");
        }

        return spitDao.findById(id).get();
    }

    /**
     * 添加
     */
    public void add(Spit spit){
        spit.setId(idWorker.nextId()+"");
        spit.setPublishtime(new Date());//发布日期
        spit.setVisits(0);//浏览量
        spit.setShare(0);//分享数
        spit.setThumbup(0);//点赞数
        spit.setComment(0);//回复数
        spit.setState("1");//状态
        spitDao.save(spit);


        //判断当前吐槽信息是否为吐槽的评论，如果是，对该评论的吐槽的comment+1
        if(spit.getParentid()!=null && !spit.getParentid().equals("")){
            //对该评论的吐槽的comment+1

            Query query = new Query();
            query.addCriteria(Criteria.where("_id").is(spit.getParentid()));

            Update update = new Update();
            update.inc("comment",1);

            mongoTemplate.updateFirst(query,update,"spit");
        }


    }

    /**
     * 修改
     */
    public void update(Spit spit){
        spitDao.save(spit);//spit必须有数据库存在的id
    }

    /**
     * 删除
     */
    public void deleteById(String id){
        spitDao.deleteById(id);
    }

    /**
     * 根据上级ID查询吐槽数据
     */
    public Page<Spit> comment(String parentid,int page,int size){
        /**
         * 根据parentid查询吐槽
         */
       return spitDao.findByParentid(parentid,PageRequest.of(page-1,size));
    }

    /**
     * 吐槽点赞-方案一
     */
    /*public void thumbup(String spitid){
        //1.先查询对象
        Spit spit = findById(spitid);
        //2.修改thumbup值
        spit.setThumbup(spit.getThumbup()+1);
        //3.更新对象
        update(spit);
    }*/

    /**
     * MongoTemplate的作用：实现构建不同命令操作mongoDB。
     *         db.spit.update({},{})
     *         db.spit.insert()
     *         db.spit.find({xxx})
     *
     */
    @Autowired
    private MongoTemplate mongoTemplate;


    /**
     * 吐槽点赞-第二种方案
     */
    public void thumbup(String spitid){
        /**
         * 以下命令可以实现对thumbup字段进行增长
         *  db.spit.update( {"_id":"1064806492508647424"},{$inc:{"thumbup":NumberInt(1)}}   )
         */

        //1.构建查询条件
        //{"_id":"1064806492508647424"}
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(spitid));

        //2.构建更新对象
        //{$inc:{"thumbup":NumberInt(1)}
        Update update = new Update();
        update.inc("thumbup",1);

        //3.使用MongoTemplae调到方法
        //db.spit.update( {"_id":"1064806492508647424"},{$inc:{"thumbup":NumberInt(1)}}   )
        mongoTemplate.updateFirst(query,update,"spit");
    }

    /**
     * 取消点赞
     * @param spitid
     */
    public void cancelThumbup(String spitid){
        //1.构建查询条件
        //{"_id":"1064806492508647424"}
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(spitid));

        //2.构建更新对象
        //{$inc:{"thumbup":NumberInt(1)}
        Update update = new Update();
        update.inc("thumbup",-1);

        //3.使用MongoTemplae调到方法
        //db.spit.update( {"_id":"1064806492508647424"},{$inc:{"thumbup":NumberInt(1)}}   )
        mongoTemplate.updateFirst(query,update,"spit");
    }
}
