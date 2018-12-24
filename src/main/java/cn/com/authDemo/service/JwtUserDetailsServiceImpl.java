package cn.com.authDemo.service;

import cn.com.authDemo.model.user.User;
import cn.com.authDemo.model.vo.JwtUser;
import cn.com.authDemo.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author: nj
 * @date: 2018/12/18:下午4:13
 */
@Service
public class JwtUserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserService userService;


    /**
     * Locates the user based on the username. In the actual implementation, the search
     * may possibly be case sensitive, or case insensitive depending on how the
     * implementation instance is configured. In this case, the <code>UserDetails</code>
     * object that comes back may have a username that is of a different case than what
     * was actually requested..
     *
     * @param username the username identifying the user whose data is required.
     * @return a fully populated user record (never <code>null</code>)
     * @throws UsernameNotFoundException if the user could not be found or the user has no
     *                                   GrantedAuthority
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        //设置查询条件，邮箱是唯一的
        User queryUser = new User();
        queryUser.setEmail(username);
        List<User> userList = null;
        try {
            userList = this.userService.getUser(queryUser);

            if (CollectionUtils.isEmpty(userList)) {
                //return new JwtUser(username, queryUser.getPwd(), authorities);
                throw new UsernameNotFoundException("用户账号：" + username + "，不存在");
            } else {
                queryUser = userList.get(0);
                Set<GrantedAuthority> authorities = new HashSet<>();
                //获取该用户所有的权限信息
                this.userService.getRoleByUserId(queryUser.getId()).forEach(role -> {
                    authorities.add(new SimpleGrantedAuthority(role.getRoleCode()));
                });

                return new JwtUser(username, queryUser.getPwd(), authorities);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return new JwtUser(null, null, null);
    }
}
