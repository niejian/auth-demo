package cn.com.authDemo.controller;

import cn.com.authDemo.service.mq.RabbitMQComponent;
import cn.com.authDemo.service.mq.TopicSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * @author: nj
 * @date: 2018/12/19:下午5:10
 */
@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private RabbitMQComponent rabbitMQComponent;
    @Autowired
    private TopicSender topicSender;

    @RequestMapping("/getTime")
    public Long test() {
        return System.currentTimeMillis();

    }

    @GetMapping("/sendMsg")
    public void sendMag() {
        this.rabbitMQComponent.send();

    }

    @GetMapping(value = "/TopicSender")
    public void TopicSender() {
        topicSender.send2();
    }

    @GetMapping(value = "/getReqId")
    public String getRequestId(HttpServletRequest request) {
        return request.getRequestedSessionId();
    }
}
