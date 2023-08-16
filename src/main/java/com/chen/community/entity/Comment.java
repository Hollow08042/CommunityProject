package com.chen.community.entity;

import lombok.Data;

import java.util.Date;

/**
 * @Description 评论
 * @date 2023-08-08 20:55
 **/
@Data
public class Comment {
    private Integer id;

    private Integer userId;

    private Integer entityType;

    private Integer entityId;

    private Integer targetId;

    private String content;

    private Integer status;

    private Date createTime;
}
