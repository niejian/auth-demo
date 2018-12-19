package cn.com.authDemo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * @author: nj
 * @date: 2018/12/19:下午5:10
 */
@RestController
@RequestMapping("/test")
public class TestController {

    @RequestMapping("/getTime")
    public Long test() {
        return System.currentTimeMillis();

    }
}
