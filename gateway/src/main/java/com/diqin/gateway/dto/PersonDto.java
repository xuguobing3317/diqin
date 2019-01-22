package com.diqin.gateway.dto;

import lombok.Data;

/**
 * @Auther: guobing
 * @Date: 2019-1-17 12:40
 * @Description:
 */
@Data
public class PersonDto {

    public PersonDto() {
    }

    public PersonDto(String userId, String userName, int age, String mobile, String address, String weixin) {
        this.userId = userId;
        this.userName = userName;
        this.age = age;
        this.mobile = mobile;
        this.address = address;
        this.weixin = weixin;
    }

    private Long id;

    private String userId;

    private String userName;

    private int age;

    private String mobile;

    private String address;

    private String weixin;

    private String lastAccessTime;

    private String nextAccessTime;

}
