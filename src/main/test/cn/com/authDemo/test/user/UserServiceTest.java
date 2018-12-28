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
import lombok.extern.slf4j.Slf4j;
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

    @Test
    public void addMenu() throws Exception{
        Menu menu = (Menu)mongoCommonService.getEntityByPrimarykey(Menu.class, "menuId", "124", "menu");
        System.out.println(menu.toString());
    }

}
