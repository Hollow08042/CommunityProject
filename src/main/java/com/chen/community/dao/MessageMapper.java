package com.chen.community.dao;

import com.chen.community.entity.Message;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MessageMapper {
    //查询当前用户的会话列表，针对每个会话只返回一条最新私信
    List<Message> selectConversations(Integer userId,int offset,int limit);
    //查询当前用户的会话数量
    int selectConversationCount(Integer userId);
    //查询某个会话的私信列表
    List<Message> selectLetters(String conversationId,int offset,int limit);
    //查询某个会话的私信数量
    int selectLetterCount(String conversationId);
    //查询当前未读私信数量
    int selectLetterUnreadCount(Integer userId,String conversationId);
    //新增消息
    int insertMessage(Message message);
    //修改消息的状态
    int updateStatus(List<Integer> ids, Integer status);

    /**
     * 系统通知需用方法
     */
    //查询某个主题下的最新通知
    Message selectLatestNotice(Integer userId,String topic);
    //查询某个主题所包含的通知数量
    int selectNoticeCount(Integer userId,String topic);
    //查询未读的通知数量
    int selectNoticeUnreadCount(Integer userId,String topic);
    //查询某个主题所包含的通知列表
    List<Message> selectNotices(Integer userId,String topic,int offset,int limit);
}
