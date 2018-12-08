package cn.com.authDemo.controller;

import cn.com.authDemo.common.BaseResponse;
import cn.com.authDemo.common.CommonFunction;
import cn.com.authDemo.model.user.User;
import cn.com.authDemo.service.user.UserService;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author: nj
 * @date: 2018/12/8:下午4:08
 */
@Slf4j
@Controller
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    private UserService userService;

    @ResponseBody
    @PostMapping(value = "/login")
    public BaseResponse login(@RequestBody JSONObject jsonObject, HttpServletRequest request) {
        BaseResponse response = new BaseResponse();
        Boolean isSuccess = false;
        String responseMsg = "请求失败";
        Integer responseCode = -1;
        CommonFunction.beforeProcess(log, jsonObject);
        boolean isContinue = true;

        try {
            String userName = jsonObject.has("userName") ? jsonObject.getString("userName") : "";
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
                user.setUserCode(userName);
                List<User> users = this.userService.getUser(user);
                if (!CollectionUtils.isEmpty(users)) {
                    user = users.get(0);
                    request.getSession().setAttribute("user", user);
                    isSuccess = true;
                    responseMsg = "请求成功";
                    responseCode = 0;
                    isContinue = false;
                } else {


                }
            }

            //判断userCode是否存在
            if (isContinue && null != user) {
                user = new User();
                user.setState(true);
                //user.setPwd(password);
                user.setUserCode(userName);
                List<User> users = this.userService.getUser(user);
                if (CollectionUtils.isEmpty(users)) {
                    isContinue = false;
                    responseMsg = "用户名不存在";
                }
            }

            //用户存在
            if (isContinue && null != user) {
                responseMsg = "密码错误";
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

    @GetMapping(value = "/signup")
    public ModelAndView signup() {
        return new ModelAndView("user/signup");
    }
}
