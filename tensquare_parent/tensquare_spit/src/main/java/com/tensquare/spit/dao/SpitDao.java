package com.tensquare.spit.dao;

import com.tensquare.spit.pojo.Spit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * 吐槽dao
 */
public interface SpitDao extends MongoRepository<Spit,String> {

    /**
     * 根据parentid查询
     */
    public Page<Spit> findByParentid(String parentid, Pageable pageable);
}
