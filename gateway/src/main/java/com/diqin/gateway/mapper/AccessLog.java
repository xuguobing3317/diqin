package com.diqin.gateway.mapper;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

/**
 * 随访日志
 * @Auther: guobing
 * @Date: 2019-1-15 11:01
 * @Description:
 */
@Data
@Entity(name = "t_access_log")
public class AccessLog {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "person_id")
    private Long personId;

    /**
     * 随访时间
     */
    @Column(name = "access_time")
    private String accessTime;

    /**
     * 1: 已随访
     * 2: 未随访
     */
    @Column(name = "state")
    private String state;

    @Column(name = "remark")
    private String remark = "系统默认";

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime = new Date();
}
