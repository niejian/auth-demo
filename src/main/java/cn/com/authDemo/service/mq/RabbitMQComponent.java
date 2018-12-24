package cn.com.authDemo.service.mq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author: nj
 * @date: 2018/12/24:下午2:28
 */
@Slf4j
@Component
public class RabbitMQComponent {

    @Autowired
    private AmqpTemplate amqpTemplate;

    public void send() {
        String msg = "hello:" + new Date();
        log.info("---->发送消息：{}", msg);
        amqpTemplate.convertAndSend("helloQueue", msg);
    }
}
