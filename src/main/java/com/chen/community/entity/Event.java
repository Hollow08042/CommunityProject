package com.chen.community.entity;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description 发送系统通知触发事件时所需的数据进行封装
 * @date 2023-08-14 16:05
 **/
public class Event {
    private String topic;
    private Integer userId;
    private Integer entityType;
    private Integer entityId;
    private Integer entityUserId;
    private Map<String,Object> data = new HashMap<>();

    public String getTopic() {
        return topic;
    }

    public Event setTopic(String topic) {
        this.topic = topic;
        return this;
    }

    public Integer getUserId() {
        return userId;
    }

    public Event setUserId(Integer userId) {
        this.userId = userId;
        return this;
    }

    public Integer getEntityType() {
        return entityType;
    }

    public Event setEntityType(Integer entityType) {
        this.entityType = entityType;
        return this;
    }

    public Integer getEntityId() {
        return entityId;
    }

    public Event setEntityId(Integer entityId) {
        this.entityId = entityId;
        return this;
    }

    public Integer getEntityUserId() {
        return entityUserId;
    }

    public Event setEntityUserId(Integer entityUserId) {
        this.entityUserId = entityUserId;
        return this;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public Event setData(String key , Object value) {
        this.data.put(key, value);
        return this;
    }
}
