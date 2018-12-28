package cn.com.authDemo.controller;

import cn.com.authDemo.common.BaseResponseExt;
import cn.com.authDemo.model.user.User;
import cn.com.authDemo.mq.user.UserMQSender;
import cn.com.authDemo.service.mq.RabbitMQComponent;
import cn.com.authDemo.service.mq.TopicSender;
import cn.com.authDemo.util.SnowflakeIdWorker;
import net.sf.json.JSONObject;
import org.apache.tomcat.util.security.MD5Encoder;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.DigestUtils;
import org.springframework.util.IdGenerator;
import org.springframework.web.bind.annotation.*;

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
    @Autowired
    private UserMQSender userMQSender;
    @Autowired
    private AmqpTemplate amqpTemplate;

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
            this.userMQSender.sendUserMQ("demoTestTopic", "cn.com.test", i + "");

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
}
