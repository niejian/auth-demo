package cn.com.authDemo.service.mq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author: nj
 * @date: 2018/12/24:下午2:31
 */
@Slf4j
@Component
@RabbitListener(queues = "cn.com")
public class HelloReceiver1 {

    @RabbitHandler
    public void process(String msg) {
       log.info("-----------");
       log.info("-----》接收消息：{}", msg);
       log.info("-----------");
    }
}
