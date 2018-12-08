package cn.com.authDemo.service.user;

import cn.com.authDemo.model.user.User;

import java.util.List;

/**
 * Created by niejian on 2018/12/1.
 */
public interface UserService {
    /**
     * 添加用户信息
     * @param user
     */
    void addUser(User user);

    List<User> findUserByUserName(String userName);

    /**
     * 根据条件获取用户信息
     * @param user
     * @return
     */
    List<User> getUser(User user);
}
