package cn.com.authDemo.service.mq;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
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

//    @RabbitListener(queues = "topicQueue")
    public void process(String msg) {
        log.info("-------");
        log.info("1111接收topic信息：{}", msg);
        log.info("-------");


    }

//    @RabbitListener(queues = "demo-test")
    public void process2(String msg, Channel channel, Message message) throws Exception{
        log.info("-------");
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);

        log.info("2222接收topic信息：{}", msg);
        log.info("-------");


    }
}
