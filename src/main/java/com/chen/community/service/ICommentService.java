package com.chen.community.service;

import com.chen.community.entity.Comment;

import java.util.List;

public interface ICommentService {
    List<Comment> findCommentByEntity(Integer entityType,Integer entityId,Integer offset,Integer limit);

    int findCommentCount(Integer entityType,Integer entityId);

    int addComment(Comment comment);

    Comment findCommentById(Integer id);
}
