package cn.com.authDemo.controller;

import cn.com.authDemo.common.BaseResponseExt;
import cn.com.authDemo.model.user.User;
import cn.com.authDemo.mq.user.UserMQSender;
import cn.com.authDemo.service.mq.RabbitMQComponent;
import cn.com.authDemo.service.mq.TopicSender;
import cn.com.authDemo.util.SnowflakeIdWorker;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.apache.tomcat.util.security.MD5Encoder;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.DigestUtils;
import org.springframework.util.IdGenerator;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * @author: nj
 * @date: 2018/12/19:下午5:10
 */
@Slf4j
@RestController
@RequestMapping("/test")
public class TestController implements  RabbitTemplate.ReturnCallback {

    @Autowired
    private RabbitMQComponent rabbitMQComponent;
    @Autowired
    private TopicSender topicSender;
    @Autowired
    private UserMQSender userMQSender;
    @Autowired
    private AmqpTemplate amqpTemplate;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @PostConstruct
    public void init() {
        this.rabbitTemplate.setMandatory(true);
        this.rabbitTemplate.setReturnCallback(this);
    }
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

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @GetMapping(value = "/addUser")
    public void addUser() throws Exception{
        User user = new User();
        SnowflakeIdWorker snowflakeIdWorker = new SnowflakeIdWorker(0, 0);


        user.setId(snowflakeIdWorker.nextId() + "");
        user.setEmail("niejian9001@163.com");
        user.setUserName("Jack");
        user.setState(true);
        String pwd = DigestUtils.md5DigestAsHex("12345678".getBytes());

        user.setPwd(passwordEncoder.encode(pwd));
        user.setUserCode("123");
        JSONObject jsonObject = JSONObject.fromObject(user);
        this.userMQSender.sendUserMQ("addUserTopic", "cn.com.user.add", jsonObject.toString());
    }

    @GetMapping(value = "/testDistribute")
    public void testDistribute() throws Exception{
        for (int i = 1; i < 10; i++) {
            User user = new User();


            user.setId(i + "");
            user.setEmail(i + "@163.com");
            user.setUserName("Jack" + i);
            user.setState(true);
            String pwd = DigestUtils.md5DigestAsHex("12345678".getBytes());

            user.setPwd(passwordEncoder.encode(pwd));
            user.setUserCode("123" + i);
            JSONObject jsonObject = JSONObject.fromObject(user);

            //this.userMQSender.sendUserMQ("demoTestTopic", "cn.com.test", jsonObject.toString());

            this.rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
                if (!ack) {
                    log.error("消息发送失败,{}, {}" , cause , correlationData.toString());
                } else {
                    log.info("消息发送成功 ");
                }


            });

            this.userMQSender.sendUserMQ("demoTestTopic", "cn.com.test", jsonObject.toString());

        }

    }

    @ResponseBody
    @PostMapping(value = "/getMsgManual")
    public BaseResponseExt getMsgManual(@RequestBody JSONObject jsonObject) throws Exception{
        BaseResponseExt baseResponseExt = new BaseResponseExt();

        String queueName = jsonObject.optString("queueName", "");

        String data = (String) this.amqpTemplate.receiveAndConvert(queueName);

        baseResponseExt.setIsSuccess(true);
        baseResponseExt.setData(data);

        baseResponseExt.setResponseCode(0);
        baseResponseExt.setResponseMsg("请求成功");

        return baseResponseExt;

    }

    /**
     * Returned message callback.
     *
     * @param message    the returned message.
     * @param replyCode  the reply code.
     * @param replyText  the reply text.
     * @param exchange   the exchange.
     * @param routingKey the routing key.
     */
    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        log.info("发送成功，消息内容：{}, 返回编码：{}, replyText:{}, exchange:{}, routingKey:{}",
                new String(message.getBody()), replyCode, replyText, exchange, routingKey);
    }
}
