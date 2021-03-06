package cn.com.authDemo.test.user;/**
 * Created by niejian on 2018/12/1.
 */

import cn.com.authDemo.model.user.Menu;
import cn.com.authDemo.model.user.Role;
import cn.com.authDemo.model.user.User;
import cn.com.authDemo.model.user.UserRole;
import cn.com.authDemo.service.common.MongoCommonService;
import cn.com.authDemo.service.user.UserService;
import cn.com.authDemo.util.SnowflakeIdWorker;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.impl.AMQImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.ReceiveAndReplyCallback;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.mongodb.core.query.Query;

import org.apache.tomcat.util.security.MD5Encoder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.DigestUtils;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
    SnowflakeIdWorker snowflakeIdWorker = new SnowflakeIdWorker(0, 0);
    @Autowired
    private MongoCommonService mongoCommonService;
    @Autowired
    private MongoTemplate mongoTemplate;
    //    @Autowired
//    private AmqpTemplate amqpTemplate;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Ignore
    @Test
    public void testAddUser() throws Exception{

        User user = new User();
        user.setId(snowflakeIdWorker.nextId() + "");
        user.setUserCode("80468295");
        user.setEmail("80468295@qq.com");

        String pwd = DigestUtils.md5DigestAsHex("12345678".getBytes());
        //String pwd = "12345678";

        user.setUserName("Mike");
        //userService.addUser(user);
        user.setPwd(pwd);
        user.setState(true);
        this.userService.addUser(user);

    }

    @Ignore
    @Test
    public void testUpdate() throws Exception{

        Query query = new Query(Criteria.where("id").is("6703898366574592"));
        query.addCriteria(Criteria.where("state").is(true));
        Map<String, Object> map = new HashMap<>();
        map.put("avatar", "http://imgsrc.baidu.com/forum/w%3D580%3B/sign=b440e70ac6ef76093c0b99971ee6a1cc/e1fe9925bc315c608c1c887880b1cb1348547798.jpg");
        this.mongoCommonService.updateEntityBySelectived(User.class, query, map);

    }

    //@Ignore
    @Test
    public void testFindUserByUserName() throws Exception{
        //String userName = "Mike";
        String userName = "6703898366574592";
        User user = this.userService.findUserByUserId(userName);
        log.info("----------------------------------------");
        log.info("查询结果：{}", user.getAvatar());
        log.info("----------------------------------------");

    }

    //2857853127753728
    //@Ignore
    @Test
    public void testAddUserRole() throws Exception{
        Role role = new Role();
        String roleId = snowflakeIdWorker.nextId() + "";
        String roleCode = "ROLE_ADMIN";
        String roleName = "PC_1";
        String roleDesc = "pc1";
        role.setRoleCode(roleCode);
        role.setRoleName(roleName);
        role.setRoleId(roleId);
        role.setRoleDesc(roleDesc);

        this.userService.addRole(role);
        String userId = "7092653464223744";
        UserRole userRole = new UserRole();
        userRole.setId(snowflakeIdWorker.nextId() + "");
        userRole.setRoleId(roleId);
        userRole.setUserId(userId);

        this.userService.addUserRole(userRole);


    }

    @Test
    public void testPwd() {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String pwd = bCryptPasswordEncoder.encode("4266bf8d3dc65bc84fd3badf2edfdbe7");
        System.out.println(pwd);
    }

    @Ignore
    @Test
    public void getRoleInfo()  throws Exception{
        String userId = "2857853127753728";
        List<UserRole> userRoles = this.userService.getUserRoleByUserId(userId);
        log.info("==================");
        log.info("查询的角色信息：{}", userRoles.toString());
        log.info("==================");

    }

    @Ignore
    @Test
    public void addMenu() throws Exception{
        Menu menu = (Menu)mongoCommonService.getEntityByPrimarykey(Menu.class, "menuId", "124", "menu");
        System.out.println(menu.toString());
    }

    /**
     * 模拟并发从队列中取值
     * @throws Exception
     */
    //@Ignore
    @Test
    public void testGetMsg() throws Exception{
        ExecutorService service = Executors.newCachedThreadPool(); //创建一个线程池
        final CountDownLatch beginCountDownLatch = new CountDownLatch(1);
        final CountDownLatch countDownLatch = new CountDownLatch(100);

        for (int i = 0; i < 100; i++) {
            Runnable runnable = new Runnable() {
                int index = 1;
                @Override
                public void run() {
                    try {
                        /**
                         *如果调用对象上的await()方法，那么调用者就会一直阻塞在这里，直到别人通过cutDown方法，将计数减到0，才可以继续执行。
                         * 这里先调用beginCountDownLatch的await方法，等到循环结束后，内存中就有100个线程等待去运行。
                         * 所以等到beginCountDownLatch调用countDown的时候，100个线程就开始执行
                         */
                        beginCountDownLatch.await();
                        log.info("------->index:{}", index);
//                        String data = (String) rabbitTemplate.receiveAndConvert("demo-test");
//                        log.info("==============>消息n内容：{}", data);

                        //客户端手动确认消息
                        boolean ack = rabbitTemplate.receiveAndReply("demo-test", new Callback(), "demoTestTopic", "cn.com.test");

                        log.info("==============>是否接受到消息：{}", ack);

                        countDownLatch.countDown();
                        index++;

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }
            };
            service.execute(runnable);
        }
        //释放主线程，之前声明的100个线程就开始执行
        beginCountDownLatch.countDown();
        //
        countDownLatch.await();

    }

    /**
     *继承ReceiveAndReplyCallback ，客户端接收并相应消息
     */
    private static class Callback implements  ReceiveAndReplyCallback {
        @Override
        public Object handle(Object payload) {
            log.info("====消息确认====，playload：{}", (String)payload);
            return null;
        }
    }
}
