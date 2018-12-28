package cn.com.authDemo.mq.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 用户相关消息队列
 * @author: nj
 * @date: 2018/12/28:下午1:59
 */
@Slf4j
@Component
public class UserMQSender {
    @Autowired
    private AmqpTemplate amqpTemplate;

    /**
     * 发送消息
     * @param exchangeNanme 队列名称
     * @param routingKey 路由key
     * @param msg 具体消息内容
     * @throws Exception
     */
    public void sendUserMQ(String exchangeNanme, String routingKey, String msg) throws Exception {
        log.info("向交换机：{}，匹配规则：{}， 发送消息：{}", exchangeNanme, routingKey, msg);
        this.amqpTemplate.convertAndSend(exchangeNanme, routingKey, msg);
    }

}
