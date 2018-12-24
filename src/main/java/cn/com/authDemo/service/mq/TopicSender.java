package cn.com.authDemo.service.mq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author: nj
 * @date: 2018/12/24:下午3:25
 */
@Slf4j
@Component
public class TopicSender {
    @Autowired
    private AmqpTemplate amqpTemplate;

    public void send() {
        String msg = "topic:" + new Date();
        log.info("---->发送消息：{}", msg);
        amqpTemplate.convertAndSend("topicExchange","cn.com.test", msg);
    }

    public void send2() {
        String msg = "topic:" + new Date();
        log.info("---->发送消息：{}", msg);
        amqpTemplate.convertAndSend("topicExchange2","cn.com.test", msg);
    }
}
