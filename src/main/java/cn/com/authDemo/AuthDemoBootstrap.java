package cn.com.authDemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @author: nj
 * @date: 2018/11/26:下午5:31
 */
@EnableAsync
@SpringBootApplication
public class AuthDemoBootstrap {
    public static void main(String[] args) {
        SpringApplication.run(AuthDemoBootstrap.class);
    }
}
