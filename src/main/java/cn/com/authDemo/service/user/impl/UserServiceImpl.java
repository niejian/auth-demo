package cn.com.authDemo.service.user.impl;/**
 * Created by niejian on 2018/12/1.
 */

import cn.com.authDemo.model.user.Role;
import cn.com.authDemo.model.user.User;
import cn.com.authDemo.model.user.UserRole;
import cn.com.authDemo.service.JwtUserDetailsServiceImpl;
import cn.com.authDemo.service.user.UserService;
import cn.com.authDemo.util.JwtTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author niejian
 * @date 2018/12/1
 */
@Slf4j
@Service(value = "userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUserDetailsServiceImpl userDetailsService;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * 添加用户信息
     *
     * @param user
     */
    @Override
    public void addUser(User user)  throws Exception{
        log.info("插入数据表：user，信息：{}", user.toString());
        String pwd = user.getPwd();
        if (!StringUtils.isEmpty(pwd)) {
            pwd = passwordEncoder.encode(pwd);
            user.setPwd(pwd);

        }
        mongoTemplate.insert(user, "user");

    }



    /**
     * 登录操作，返回token
     * @param userName
     * @param password
     * @return
     * @throws Exception
     */
    @Override
    public String login(String userName, String password) throws Exception {
        UsernamePasswordAuthenticationToken upToken = new UsernamePasswordAuthenticationToken(userName, password);
        Authentication authentication = authenticationManager.authenticate(upToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
        String token = jwtTokenUtil.generateToken(userDetails);

        return token;
    }

    /**
     * 通过id查询用户信息
     * @param userId
     * @return
     * @throws Exception
     */
    @Override
    public User findUserByUserId(String userId)  throws Exception{
        log.info("通过userId查询信息；userId：{}", userId);
        //Query query = new Query(Criteria.where("userName").is(userName));
        Query query = new Query(Criteria.where("id").is(userId));
        query.addCriteria(Criteria.where("state").is(true));
        List<User> users = this.mongoTemplate.find(query, User.class);
        User user = null;
        if (!CollectionUtils.isEmpty(users)) {
            user = users.get(0);
        }
        return user;
    }

    /**
     * 根据条件获取用户信息
     *
     * @param user
     * @return
     */
    @Override
    public List<User> getUser(User user)  throws Exception{
        log.info("根据条件获取用户信息: {}", user.toString());
        String userId = user.getId();
        Query query = new Query(Criteria.where("state").is(Boolean.TRUE));
        if (!StringUtils.isEmpty(userId)) {
            query.addCriteria(Criteria.where("id").is(userId));
        }

        String userName = user.getUserName();
//        //完全匹配
//        Pattern pattern = Pattern.compile("^王$", Pattern.CASE_INSENSITIVE);
////右匹配
//        Pattern pattern = Pattern.compile("^.*王$", Pattern.CASE_INSENSITIVE);
////左匹配
//        Pattern pattern = Pattern.compile("^王.*$", Pattern.CASE_INSENSITIVE);

        //模糊匹配
        Pattern pattern = Pattern.compile("^.*"+userName+".*$", Pattern.CASE_INSENSITIVE);
        if (!StringUtils.isEmpty(userName)) {
            query.addCriteria(Criteria.where("user_name").regex(pattern));
        }

        String password = user.getPwd();
        if (!StringUtils.isEmpty(password)) {
            password = passwordEncoder.encode(password);
            query.addCriteria(Criteria.where("pwd").is(password));
        }

        String emial = user.getEmail();
        if (!StringUtils.isEmpty(emial)) {
            query.addCriteria(Criteria.where("email").is(emial));
        }

        List<User> users = this.mongoTemplate.find(query, User.class);
        return users;
    }

    @Override
    public User getUserByLoginAccount(String loginAccount) throws Exception {
        log.info("根据条件获取用户信息: {}", loginAccount);

        Query query = new Query(Criteria.where("state").is(Boolean.TRUE));
        if (!StringUtils.isEmpty(loginAccount)) {
            query.addCriteria(Criteria.where("email").is(loginAccount));
        }
        List<User> users = this.mongoTemplate.find(query, User.class);
        User user = null;
        if (!CollectionUtils.isEmpty(users)) {
            user = users.get(0);
        }

        return user;

    }

    /**
     * 创建角色
     * @param role
     * @throws Exception
     */
    @Override
    public void addRole(Role role) throws Exception {
        mongoTemplate.insert(role, "role");
    }

    /**
     * 添加用户角色
     *
     * @param userRole
     * @throws Exception
     */
    @Override
    public void addUserRole(UserRole userRole) throws Exception {
        mongoTemplate.insert(userRole, "user_role");
    }

    /**
     * 获取指定账号的权限信息
     *
     * @param userId
     * @return
     * @throws Exception
     */
    @Override
    public List<Role> getRoleByUserId(String userId) throws Exception {
        List<Role> roles = new ArrayList<>();
        List<UserRole> userRoles = getUserRoleByUserId(userId);
        List<String> roleIdList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(userRoles)) {
            userRoles.forEach(userRole -> {
                roleIdList.add(userRole.getRoleId());
            });
            Query query = new Query(Criteria.where("_id").in(roleIdList));
            roles = mongoTemplate.find(query, Role.class);
        }

        return roles;
    }

    /**
     * 获取userRole信息
     *
     * @param userId
     * @return
     * @throws Exception
     */
    @Override
    public List<UserRole> getUserRoleByUserId(String userId) throws Exception {
        List<UserRole> userRoles = new ArrayList<>();
        Query query = new Query(Criteria.where("userId").is(userId));
        userRoles = mongoTemplate.find(query, UserRole.class);
        return userRoles;
    }

    /**
     * 更新
     *
     * @param user
     */
    @Override
    public void updateUserBySelectived(User user) {
//this.mongoTemplate.updateMulti()
    }
}
