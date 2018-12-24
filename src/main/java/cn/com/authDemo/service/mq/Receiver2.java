package cn.com.authDemo.service.mq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author: nj
 * @date: 2018/12/24:下午4:48
 */
@Component
@Slf4j
public class Receiver2 {
    @RabbitListener(queues = "cn.com.test")
    public void getMsg(String msg) {
        log.info("通配符获取消息:{}", msg);
    }
}
