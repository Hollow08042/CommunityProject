package com.chen.community.entity;

import lombok.Data;

import java.util.Date;

/**
 * @Description 用户
 * @date 2023-07-26 18:40
 **/
@Data
public class User {
    private Integer Id;

    private String username;

    private String password;

    private String salt;

    private String email;

    private Integer type;

    private Integer status;

    private String activationCode;

    private String headerUrl;

    private Date createTime;

}
