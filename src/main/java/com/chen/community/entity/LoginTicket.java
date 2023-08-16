package com.chen.community.entity;

import lombok.Data;

import java.util.Date;

/**
 * @Description 登录凭证
 * @date 2023-08-04 14:16
 **/
@Data
public class LoginTicket {
    private Integer id;

    private Integer userId;

    private String ticket;

    private Integer status;

    private Date expired;
}
