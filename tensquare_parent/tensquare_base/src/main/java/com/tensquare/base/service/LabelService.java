package com.tensquare.base.service;

import com.tensquare.base.dao.LabelDao;
import com.tensquare.base.pojo.Label;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import util.IdWorker;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
/**
 * 标签service
 */
@Service
public class LabelService {

    @Autowired
    private LabelDao labelDao;

    @Autowired
    private IdWorker idWorker;

    /**
     * 查询所有
     */
    public List<Label> findAll(){
        return labelDao.findAll();
    }

    /**
     * 查询一个
     */
    public Label findById(String id){
        return labelDao.findById(id).get();
    }

    /**
     * 添加
     */
    public void add(Label label){
        label.setId(idWorker.nextId()+"");
        labelDao.save(label);
    }

    /**
     * 修改
     */
    public void update(Label label){
        labelDao.save(label);//label必须有数据库存在的id
    }

    /**
     * 删除
     */
    public void deleteById(String id){
        labelDao.deleteById(id);
    }

    /**
     * 创建Specification对象
     */
    private Specification<Label> createSpecification(Map<String,Object> searchMap){
        return new Specification<Label>() {
            @Override
            public Predicate toPredicate(Root<Label> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                //1.创建Predicate集合，用于存放所有查询条件
                List<Predicate> preList = new ArrayList<Predicate>();

                //2.根据查询条件拼装条件
                if( searchMap.get("labelname")!=null && !searchMap.get("labelname").equals("") ){
                    // labelname like '%xxxx%'
                    preList.add( criteriaBuilder.like(root.get("labelname").as(String.class),"%"+searchMap.get("labelname")+"%"));
                }
                if( searchMap.get("state")!=null && !searchMap.get("state").equals("") ){
                    // state = 'xxxx'
                    preList.add( criteriaBuilder.equal(root.get("state").as(String.class),searchMap.get("state")));
                }
                if( searchMap.get("recommend")!=null && !searchMap.get("recommend").equals("") ){
                    // recommend = 'xxxx'
                    preList.add( criteriaBuilder.equal(root.get("recommend").as(String.class),searchMap.get("recommend")));
                }

                // where labelname like '%xxxx%' and state = 'xxxx' and recommend = 'xxxx'
                //3.把所有条件使用and进行连接
                Predicate[] preArray = new Predicate[preList.size()];
                //preList.toArray(preArray): 从preList集合里面取出每个元素，逐个放入preArray数组里面，返回preArray数组
                return criteriaBuilder.and(preList.toArray(preArray));
            }
        };
    }


    /**
     * 条件查询
     */
    public List<Label> findSearch(Map<String,Object> searchMap){
        Specification<Label> spec = createSpecification(searchMap);
        return labelDao.findAll(spec);
    }

    /**
     * 条件分页查询
     */
    public Page<Label> findSearch(Map<String,Object> searchMap, int page, int size){
        Specification<Label> spec = createSpecification(searchMap);
        //page: Spring data jpa的当前页码从0开始
        return labelDao.findAll(spec, PageRequest.of(page-1,size));
    }
}
