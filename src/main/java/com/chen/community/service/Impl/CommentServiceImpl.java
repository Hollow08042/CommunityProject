package com.chen.community.service.Impl;

import com.chen.community.dao.CommentMapper;
import com.chen.community.dao.DiscussPostMapper;
import com.chen.community.entity.Comment;
import com.chen.community.service.ICommentService;
import com.chen.community.util.CommunityConstant;
import com.chen.community.util.SensitiveFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;
import org.unbescape.html.HtmlEscape;

import java.util.List;

/**
 * @Description 评论的业务逻辑
 * @date 2023-08-08 21:18
 **/
@Service
public class CommentServiceImpl implements ICommentService, CommunityConstant {
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private SensitiveFilter sensitiveFilter;
    @Autowired
    private DiscussPostMapper discussPostMapper;

    @Override
    public List<Comment> findCommentByEntity(Integer entityType, Integer entityId, Integer offset, Integer limit) {
        return commentMapper.selectCommentByEntity(entityType,entityId,offset,limit);
    }

    @Override
    public int findCommentCount(Integer entityType, Integer entityId) {
        return commentMapper.selectCountByEntity(entityType,entityId);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED,propagation = Propagation.REQUIRED)
    @Override
    public int addComment(Comment comment) {
        //判空
        if (comment == null){
            throw new IllegalArgumentException("参数不能为空！");
        }
        //处理数据
        comment.setContent(HtmlUtils.htmlEscape(comment.getContent()));
        comment.setContent(sensitiveFilter.filter(comment.getContent()));
        //更新评论
        int rows = commentMapper.insertComment(comment);
        //更新帖子评论量
        if (comment.getEntityType() == ENTITY_TYPE_POST){
            int count = commentMapper.selectCountByEntity(comment.getEntityType(), comment.getEntityId());
            discussPostMapper.updateCommentCount(comment.getEntityId(), count);

        }
        return rows;
    }

    public Comment findCommentById(Integer id){
        return commentMapper.selectCommentById(id);
    }
}
