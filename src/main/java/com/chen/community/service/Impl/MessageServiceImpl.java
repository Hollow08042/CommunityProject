package com.chen.community.service.Impl;

import com.chen.community.dao.MessageMapper;
import com.chen.community.entity.Message;
import com.chen.community.service.IMessageService;
import com.chen.community.util.SensitiveFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

/**
 * @Description 私信的业务层
 * @date 2023-08-10 16:17
 **/
@Service
public class MessageServiceImpl implements IMessageService {
    @Autowired
    private MessageMapper messageMapper;
    @Autowired
    private SensitiveFilter sensitiveFilter;

    public List<Message> findConversations(Integer userId, int offset, int limit) {
        return messageMapper.selectConversations(userId, offset, limit);
    }

    public int findConversationCount(Integer userId) {
        return messageMapper.selectConversationCount(userId);
    }

    public List<Message> findLetters(String conversationId, int offset, int limit) {
        return messageMapper.selectLetters(conversationId, offset, limit);
    }

    public int findLetterCount(String conversationId) {
        return messageMapper.selectLetterCount(conversationId);
    }

    public int findLetterUnreadCount(Integer userId, String conversationId) {
        return messageMapper.selectLetterUnreadCount(userId, conversationId);
    }

    @Override
    public int addMessage(Message message) {
        message.setContent(HtmlUtils.htmlEscape(message.getContent()));
        message.setContent(sensitiveFilter.filter(message.getContent()));
        return messageMapper.insertMessage(message);
    }

    @Override
    public int readMessage(List<Integer> ids) {
        return messageMapper.updateStatus(ids,1);
    }

    @Override
    public Message findLatestNotice(Integer userId, String topic) {
        return messageMapper.selectLatestNotice(userId,topic);
    }

    @Override
    public int findNoticeCount(Integer userId, String topic) {
        return messageMapper.selectNoticeCount(userId,topic);
    }

    @Override
    public int findNoticeUnreadCount(Integer userId, String topic) {
        return messageMapper.selectNoticeUnreadCount(userId,topic);
    }

    @Override
    public List<Message> findNotices(Integer userId, String topic, int offset, int limit) {
        return messageMapper.selectNotices(userId,topic,offset,limit);
    }
}
