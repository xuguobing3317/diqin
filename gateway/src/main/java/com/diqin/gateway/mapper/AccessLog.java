package com.diqin.gateway.mapper;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
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
    private Long id;

    @Column(name = "person_id")
    private Long personId;

    /**
     * 随访时间
     */
    @Column(name = "access_time")
    private Date accessTime;

    /**
     * 1: 已随访
     * 2: 未随访
     */
    @Column(name = "state")
    private int state;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime = new Date();
}
