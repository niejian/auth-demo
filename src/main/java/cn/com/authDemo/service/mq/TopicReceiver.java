package cn.com.authDemo.service.mq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author: nj
 * @date: 2018/12/24:下午3:27
 */
@Component
@Slf4j
public class TopicReceiver {

    @RabbitListener(queues = "topicQueue")
    public void process(String msg) {
        log.info("-------");
        log.info("1111接收topic信息：{}", msg);
        log.info("-------");


    }

    @RabbitListener(queues = "topicQueue")
    public void process2(String msg) {
        log.info("-------");
        log.info("2222接收topic信息：{}", msg);
        log.info("-------");


    }
}
