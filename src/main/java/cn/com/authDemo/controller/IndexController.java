package cn.com.authDemo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: nj
 * @date: 2018/11/28:下午4:22
 */
@Controller
@RequestMapping("/")
public class IndexController {

//    @RequestMapping("")
//    public ModelAndView root() {
//        ModelAndView modelAndView = new ModelAndView("login");
//        return modelAndView;
//    }


    @RequestMapping("/login")
    public ModelAndView login() {
        ModelAndView modelAndView = new ModelAndView("login");
        return modelAndView;
    }




    @RequestMapping("index")
    public ModelAndView index() {

        Map<String, Object> map = new HashMap<>();
        map.put("date", new Date());
        map.put("timestamp", System.currentTimeMillis());
        ModelAndView modelAndView = new ModelAndView("index/index", map);

        return modelAndView;
    }
}
