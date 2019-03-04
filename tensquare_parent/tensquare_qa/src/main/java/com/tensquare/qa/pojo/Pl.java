package com.tensquare.qa.pojo;

import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 问题标签中间表实体
 */
@Entity
@Table(name = "tb_pl")
@IdClass(Pl.class)  // @IdClass: @Id属性所在的类
public class Pl implements Serializable{
    //联合主键

    @Id
    private String problemid;//问题id
    @Id
    private String labelid;//标签id


    public String getProblemid() {
        return problemid;
    }

    public void setProblemid(String problemid) {
        this.problemid = problemid;
    }

    public String getLabelid() {
        return labelid;
    }

    public void setLabelid(String labelid) {
        this.labelid = labelid;
    }
}
