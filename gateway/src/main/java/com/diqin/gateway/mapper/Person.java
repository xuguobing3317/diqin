package com.diqin.gateway.mapper;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

/**
 * 人员表
 * @Auther: guobing
 * @Date: 2019-1-15 11:01
 * @Description:
 */
@Data
@Entity(name = "t_person")
public class Person {

    @Id
    private Long id;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "name")
    private String userName;

    @Column(name = "age")
    private int age;

    @Column(name = "mobile")
    private String mobile;

    @Column(name = "address")
    private String address;

    @Column(name = "weixin")
    private String weixin;

    @Column(name = "last_access_time")
    private String lastAccessTime;

    @Column(name = "next_access_time")
    private String nextAccessTime;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime = new Date();


}
