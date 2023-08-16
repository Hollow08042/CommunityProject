package com.chen.community.dao;

import com.chen.community.entity.Comment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CommentMapper {
    List<Comment> selectCommentByEntity(Integer entityType,Integer entityId,
                                        Integer offset,Integer limit);

    int selectCountByEntity(Integer entityType,Integer entityId);

    int insertComment(Comment comment);

    Comment selectCommentById(Integer id);

}
