package cn.com.authDemo.test.user;/**
 * Created by niejian on 2018/12/1.
 */

import cn.com.authDemo.model.user.User;
import cn.com.authDemo.service.user.UserService;
import cn.com.authDemo.util.SnowflakeIdWorker;
import lombok.extern.slf4j.Slf4j;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @author niejian
 * @date 2018/12/1
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {
    @Autowired
    private UserService userService;

    @Ignore
    @Test
    public void testAddUser(){
        SnowflakeIdWorker snowflakeIdWorker = new SnowflakeIdWorker(0, 0);
        User user = new User();
        user.setId(snowflakeIdWorker.nextId() + "");
        user.setUserCode("80468295");
        user.setUserName("Mike");
        userService.addUser(user);
    }

    @Ignore
    @Test
    public void testFindUserByUserName() {
        //String userName = "Mike";
        String userName = "83632156835841";
        List<User> users = this.userService.findUserByUserName(userName);
        log.info("----------------------------------------");
        log.info("查询结果：{}", users.toString());
        log.info("----------------------------------------");

    }
}
