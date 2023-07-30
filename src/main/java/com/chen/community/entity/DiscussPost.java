package com.chen.community.entity;

import lombok.Data;

import java.util.Date;

/**
 * @Description 帖子
 * @date 2023-07-27 16:07
 **/
@Data
public class DiscussPost {
    private Integer id;

    private Integer userId;

    private String title;

    private String content;

    private Integer type;

    private Integer status;

    private Date createTime;

    private Integer commentCount;

    private double score;
}
