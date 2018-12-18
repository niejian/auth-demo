package cn.com.authDemo.test.user;/**
 * Created by niejian on 2018/12/1.
 */

import cn.com.authDemo.model.user.Role;
import cn.com.authDemo.model.user.User;
import cn.com.authDemo.model.user.UserRole;
import cn.com.authDemo.service.user.UserService;
import cn.com.authDemo.util.SnowflakeIdWorker;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.security.MD5Encoder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.DigestUtils;


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
    SnowflakeIdWorker snowflakeIdWorker = new SnowflakeIdWorker(0, 0);


    @Ignore
    @Test
    public void testAddUser() throws Exception{

        User user = new User();
        user.setId(snowflakeIdWorker.nextId() + "");
        user.setUserCode("80468295");
        String pwd = DigestUtils.md5DigestAsHex("12345678".getBytes());
        user.setUserName("Mike");
        //userService.addUser(user);
        user.setPwd(pwd);
        user.setState(true);
        this.userService.addUser(user);

    }

    @Ignore
    @Test
    public void testFindUserByUserName() throws Exception{
        //String userName = "Mike";
        String userName = "83632156835841";
        User user = this.userService.findUserByUserId(userName);
        log.info("----------------------------------------");
        log.info("查询结果：{}", user.toString());
        log.info("----------------------------------------");

    }

    //2857853127753728
    @Ignore
    @Test
    public void testAddUserRole() throws Exception{
        Role role = new Role();
        String roleId = snowflakeIdWorker.nextId() + "";
        String roleCode = "ROLE_PC_1";
        String roleName = "PC_1";
        String roleDesc = "pc1";
        role.setRoleCode(roleCode);
        role.setRoleName(roleName);
        role.setRoleId(roleId);
        role.setRoleDesc(roleDesc);

        this.userService.addRole(role);
        String userId = "28578531277537281";
        UserRole userRole = new UserRole();
        userRole.setId(snowflakeIdWorker.nextId() + "");
        userRole.setRoleId(roleId);
        userRole.setUserId(userId);

        this.userService.addUserRole(userRole);

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

}
