package cn.com.authDemo.controller;

import cn.com.authDemo.common.BaseResponse;
import cn.com.authDemo.common.BaseResponseExt;
import cn.com.authDemo.common.CommonFunction;
import cn.com.authDemo.model.user.User;
import cn.com.authDemo.service.user.UserService;
import cn.com.authDemo.util.JwtTokenUtil;
import cn.com.authDemo.util.SnowflakeIdWorker;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author: nj
 * @date: 2018/12/8:下午4:08
 *
 *
 */
@CrossOrigin
@Slf4j
@Controller
@RequestMapping(value = "/user")
public class UserController {

    SnowflakeIdWorker idWorker = new SnowflakeIdWorker(0, 0);

    @Autowired
    private UserService userService;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @ResponseBody
    @PostMapping(value = "/login")
    public BaseResponseExt login(@RequestBody JSONObject jsonObject, HttpServletRequest request) {
        BaseResponse response = new BaseResponse();
        BaseResponseExt baseResponseExt = new BaseResponseExt();
        Boolean isSuccess = false;
        String responseMsg = "请求失败";
        Integer responseCode = -1;
        CommonFunction.beforeProcess(log, jsonObject);
        boolean isContinue = true;
        String token = "";

        try {
            String userName = jsonObject.has("userName") ? jsonObject.getString("userName") : "";
//            String email = jsonObject.has("userName") ? jsonObject.getString("userName") : "";
            String password = jsonObject.has("password") ? jsonObject.getString("password") : "";
            if (StringUtils.isEmpty(userName)) {
                responseMsg = "用户名不能为空";
                isContinue = false;
            }

            if (isContinue && StringUtils.isEmpty(password)) {
                responseMsg = "密码不能为空";
                isContinue = false;
            }

            User user = null;


            if (isContinue) {
                user = new User();
                user.setState(true);
                user.setPwd(password);
                user.setEmail(userName);
                //List<User> users = this.userService.getUser(user);
                token = this.userService.login(userName, password);
                log.info("token生成---->{}", token);
                log.info("token生成---->{}", token);
                log.info("token生成---->{}", token);
                isSuccess = true;
                responseCode = 0;
                responseMsg = "请求成功";


            }

//            //判断userCode是否存在
//            if (isContinue && null != user) {
//                user = new User();
//                user.setState(true);
//                //user.setPwd(password);
//                user.setEmail(userName);
//                List<User> users = this.userService.getUser(user);
//                if (CollectionUtils.isEmpty(users)) {
//                    isContinue = false;
//                    responseMsg = "用户名不存在";
//                }
//            }
//
//            //用户存在
//            if (isContinue && null != user) {
//                responseMsg = "密码错误";
//            }


        } catch (Exception e) {
            if (e instanceof BadCredentialsException) {
                responseMsg = "密码错误";
            } else if (e instanceof UsernameNotFoundException) {
                responseMsg = "账号不存在";
            }
            CommonFunction.genErrorMessage(log, e);
            e.printStackTrace();
        }


        baseResponseExt.setIsSuccess(isSuccess);
        baseResponseExt.setResponseCode(responseCode);
        baseResponseExt.setResponseMsg(responseMsg);
        baseResponseExt.setData(token);

        CommonFunction.afterProcess(log, response);

        return baseResponseExt;
    }

    @PreAuthorize("isAuthenticated()")
    @ResponseBody
    @PostMapping(value = "/getUserInfo")
    public BaseResponseExt getUserInfo(HttpServletRequest request, @RequestBody JSONObject jsonObject) {
        BaseResponseExt baseResponse = new BaseResponseExt();
        CommonFunction.beforeProcess(log, jsonObject);
        Boolean isSuccess = false;
        String responseMsg = "请求失败";
        Integer responseCode = -1;
        User user = new User();

        try {

            String token = request.getHeader("Authorization");
            if (!StringUtils.isEmpty(token)) {
                //Bearer
                token = token.substring(7);
            }
            String email = jwtTokenUtil.getUsernameFromToken(token);

            if (!StringUtils.isEmpty(email)) {
                user.setEmail(email);
            }

            user = this.userService.getUserByLoginAccount(email);

            isSuccess = true;
            responseCode = 0;
            responseMsg = "请求成功";

        } catch (Exception e) {
            CommonFunction.genErrorMessage(log, e, jsonObject);

        }

        baseResponse.setIsSuccess(isSuccess);
        baseResponse.setResponseCode(responseCode);
        baseResponse.setResponseMsg(responseMsg);
        baseResponse.setData(user);

        return baseResponse;
    }
    /**
     * 显示调用
     *获取所有权信息
     * @return
     */
    @ResponseBody
    @GetMapping("/getAuths")
    public List<String> getAuths() {
        List<String> auths = new ArrayList<>();
        Collection<? extends GrantedAuthority> authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        authorities.forEach(auth -> {
            auths.add(auth.getAuthority());
        });
        return auths;

    }


    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ResponseBody
    @GetMapping("/getAuths2")
    public String getAuths2() {
        return "admin";
    }

    @GetMapping(value = "/signup")
    public ModelAndView signup() {
        return new ModelAndView("user/signup");
    }

    @ResponseBody
    @RequestMapping(value = "/signup")
    public BaseResponse signup(@RequestBody JSONObject jsonObject) {
        BaseResponse response = new BaseResponse();
        Boolean isSuccess = false;
        String responseMsg = "请求失败";
        Integer responseCode = -1;
        CommonFunction.beforeProcess(log, jsonObject);
        boolean isContinue = true;

        try {

            String userName = jsonObject.has("userName") ? jsonObject.getString("userName") : "";
            String password = jsonObject.has("pwd") ? jsonObject.getString("pwd") : "";
            String email = jsonObject.has("email") ? jsonObject.getString("email") : "";

            if (StringUtils.isEmpty(userName)) {
                responseMsg = "用户昵称不能为空";
                isContinue = false;
            }

            if (isContinue && StringUtils.isEmpty(password)) {
                responseMsg = "请填写密码";
                isContinue = false;
            }

            if (isContinue && StringUtils.isEmpty(email)) {
                responseMsg = "请填写注册邮箱";
                isContinue = false;
            }

            User user = new User();
            if (isContinue) {

                user.setEmail(email);
                List<User> users = this.userService.getUser(user);
                if (!CollectionUtils.isEmpty(users)) {
                    isContinue = false;
                    responseMsg = "该邮箱已被注册，请重新选择邮箱";
                }
            }

            if (isContinue) {
                user.setUserName(userName);
                user.setState(true);
                user.setPwd(password);
                user.setId(idWorker.nextId() + "");
                this.userService.addUser(user);
                isSuccess = true;
                responseMsg = "请求成功";
                responseCode = 0;
            }

        } catch (Exception e) {
            CommonFunction.genErrorMessage(log, e);
            e.printStackTrace();
        }


        response.setIsSuccess(isSuccess);
        response.setResponseCode(responseCode);
        response.setResponseMsg(responseMsg);
        CommonFunction.afterProcess(log, response);

        return response;
    }
}
