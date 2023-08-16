package com.chen.community;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * @Description 信息
 * @date 2023-08-13 22:21
 **/
public class KafkaTest extends CommunityApplicationTests{
    @Autowired
    private KafkaProducer kafkaProducer;
    @Test
    public void testKafka(){
        kafkaProducer.sendMessage("test","你好");
        kafkaProducer.sendMessage("test","在嘛");
        try{
            Thread.sleep(1000*10);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }
}
@Component
class KafkaProducer {
    @Autowired
    private KafkaTemplate kafkaTemplate;
    public void sendMessage(String topic, String content) {
    kafkaTemplate.send(topic, content);
    }
}
@Component
class KafkaConsumer{
    @KafkaListener(topics = {"test"})
    public void handleMessage(ConsumerRecord record){
        System.out.println(record.value());
    }
}
