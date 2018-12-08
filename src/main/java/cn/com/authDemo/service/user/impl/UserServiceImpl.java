package cn.com.authDemo.service.user.impl;/**
 * Created by niejian on 2018/12/1.
 */

import cn.com.authDemo.model.user.User;
import cn.com.authDemo.service.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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
    /**
     * 添加用户信息
     *
     * @param user
     */
    @Override
    public void addUser(User user) {
        log.info("插入数据表：user，信息：{}", user.toString());
        mongoTemplate.insert(user, "user");

    }

    @Override
    public List<User> findUserByUserName(String userName) {
        log.info("通过userName查询信息；userName：{}", userName);
        //Query query = new Query(Criteria.where("userName").is(userName));
        Query query = new Query(Criteria.where("id").is(userName));
        List<User> users = this.mongoTemplate.find(query, User.class);
        log.info("查询结果：{}", userName.toString());
        return users;
    }

    /**
     * 根据条件获取用户信息
     *
     * @param user
     * @return
     */
    @Override
    public List<User> getUser(User user) {
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
            query.addCriteria(Criteria.where("pwd").is(password));
        }

        List<User> users = this.mongoTemplate.find(query, User.class);
        return users;
    }
}
