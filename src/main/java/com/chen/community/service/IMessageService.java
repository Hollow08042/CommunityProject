package com.chen.community.service;

import com.chen.community.entity.Message;

import java.util.List;

public interface IMessageService {
    List<Message> findConversations(Integer userId, int offset, int limit);

    int findConversationCount(Integer userId);

    List<Message> findLetters(String conversationId, int offset, int limit);

    int findLetterCount(String conversationId);

    int findLetterUnreadCount(Integer userId, String conversationId);

    int addMessage(Message message);

    int readMessage(List<Integer> ids);
    /**
     * 系统通知需用方法
     */
    Message findLatestNotice(Integer userId,String topic);

    int findNoticeCount(Integer userId,String topic);

    int findNoticeUnreadCount(Integer userId,String topic);

    List<Message> findNotices(Integer userId, String topic, int offset, int limit);
}
