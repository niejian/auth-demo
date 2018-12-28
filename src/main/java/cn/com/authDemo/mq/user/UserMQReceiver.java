package cn.com.authDemo.mq.user;

import cn.com.authDemo.model.user.User;
import cn.com.authDemo.service.user.UserService;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author: nj
 * @date: 2018/12/28:下午2:06
 */
@Component
@Slf4j
public class UserMQReceiver {
    @Autowired
    private UserService userService;

    @RabbitListener(queues = "demo-user-add")
    public void getMsg(String msg) throws Exception{
        log.info("获取消息{}", msg);
        User user = (User) JSONObject.toBean(JSONObject.fromObject(msg), User.class);
        userService.addUser(user);
    }


//    /**
//     * 模拟并发情况
//     * @param msg
//     * @throws Exception
//     */
//    @RabbitListener(queues = "demo-test")
//    public void distributeGetMsg(String msg) throws Exception{
//        log.info("模拟并发情况-->获取消息{}", msg);
//
//
//    }

}
