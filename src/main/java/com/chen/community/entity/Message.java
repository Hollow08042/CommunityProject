package com.chen.community.entity;

import lombok.Data;

import java.util.Date;

/**
 * @Description 私信信息
 * @date 2023-08-09 21:40
 **/
@Data
public class Message {
    private Integer id;
    private Integer fromId;
    private Integer toId;
    private String conversationId;
    private String content;
    private Integer status;
    private Date createTime;
}
