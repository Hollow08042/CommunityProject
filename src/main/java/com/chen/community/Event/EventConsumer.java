package com.chen.community.Event;

import com.alibaba.fastjson.JSONObject;
import com.chen.community.dao.MessageMapper;
import com.chen.community.entity.DiscussPost;
import com.chen.community.entity.Event;
import com.chen.community.entity.Message;
import com.chen.community.service.IDiscussPostService;
import com.chen.community.service.Impl.ElasticsearchServiceImpl;
import com.chen.community.util.CommunityConstant;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description 消费者,往message表里发数据
 * @date 2023-08-14 16:32
 **/
@Component
public class EventConsumer implements CommunityConstant {
    private static final Logger logger = LoggerFactory.getLogger(EventConsumer.class);

    @Autowired
    private MessageMapper messageMapper;
    @Autowired
    private IDiscussPostService discussPostService;
    @Autowired
    private ElasticsearchServiceImpl elasticsearchService;

    @KafkaListener(topics = {TOPIC_COMMENT,TOPIC_LIKE,TOPIC_FOLLOW})
    public void handleCommentMessage(ConsumerRecord record){
        if (record == null || record.value() == null){
            logger.error("消息的内容为空！");
            return ;
        }
        Event event = JSONObject.parseObject(record.value().toString(), Event.class);
        if (event == null){
            logger.error("消息的格式错误！");
            return;
        }

        //发送站内通知
        Message message = new Message();
        message.setFromId(SYSTEM_USER_ID);
        message.setToId(event.getEntityUserId());
        message.setConversationId(event.getTopic());
        message.setCreateTime(new Date());
        message.setStatus(0);

        Map<String,Object> content = new HashMap<>();
        content.put("userId",event.getUserId());
        content.put("entityType",event.getEntityType());
        content.put("entityId",event.getEntityId());

        if (!event.getData().isEmpty()){
            for (Map.Entry<String,Object> map:event.getData().entrySet()){
                content.put(map.getKey(),map.getValue());
            }
        }
        message.setContent(JSONObject.toJSONString(content));
        messageMapper.insertMessage(message);
    }

    //消费者发帖事件
    @KafkaListener(topics = {TOPIC_PUBLISH})
    public void handlePublishMessage(ConsumerRecord record){
        if (record == null || record.value() == null){
            logger.error("消息的内容为空！");
            return ;
        }
        Event event = JSONObject.parseObject(record.value().toString(), Event.class);
        if (event == null){
            logger.error("消息的格式错误！");
            return;
        }
        DiscussPost post = discussPostService.findDiscussPostById(event.getEntityId());
        elasticsearchService.saveDiscussPost(post);
    }
}
