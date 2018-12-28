package cn.com.authDemo.service.user;

import cn.com.authDemo.model.user.Role;
import cn.com.authDemo.model.user.User;
import cn.com.authDemo.model.user.UserRole;

import java.util.List;

/**
 * Created by niejian on 2018/12/1.
 */
public interface UserService {
    /**
     * 添加用户信息
     * @param user
     */
    void addUser(User user) throws Exception;

    User findUserByUserId(String userId) throws Exception;

    String login(String userName, String password) throws Exception;

    /**
     * 根据条件获取用户信息
     * @param user
     * @return
     */
    List<User> getUser(User user) throws Exception;

    User getUserByLoginAccount(String loginAccount) throws Exception;

    /**
     * 创建角色
     * @param role
     * @throws Exception
     */
    void addRole(Role role) throws Exception;

    /**
     * 添加用户角色
     * @param userRole
     * @throws Exception
     */
    void addUserRole(UserRole userRole) throws Exception;

    /**
     * 获取指定账号的权限信息
     * @param userId
     * @return
     * @throws Exception
     */
    List<Role> getRoleByUserId(String userId) throws Exception;

    /**
     * 获取userRole信息
     * @param userId
     * @return
     * @throws Exception
     */
    List<UserRole> getUserRoleByUserId(String userId) throws Exception;

    /**
     * 更新
     * @param user
     */
    void updateUserBySelectived(User user);
}
