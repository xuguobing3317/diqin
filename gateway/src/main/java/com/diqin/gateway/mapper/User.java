package com.diqin.gateway.mapper;

import lombok.Data;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

/**
 * 用户实体类
 * @Auther: guobing
 * @Date: 2019-1-15 11:01
 * @Description:
 */
@Data
@Entity(name = "t_user")
public class User {

    @Id
    @Column(name = "USER_ID", length = 32)
    private String userId;

    @Column(name = "user_name", nullable = false, unique = true)
    private String userName;

    @Column(name = "password", nullable = false, unique = true)
    private String password;

    @Column(name = "state", nullable = false, unique = true)
    private int state;

    @Column(name = "age")
    private int age;

    @Column(name = "mobile")
    private String mobile;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime = new Date();


}
