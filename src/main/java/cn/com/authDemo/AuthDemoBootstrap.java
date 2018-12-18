package cn.com.authDemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.ServletContextApplicationContextInitializer;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.context.ConfigurableWebApplicationContext;

/**
 * @author: nj
 * @date: 2018/11/26:下午5:31
 * 继承SpringBootServletInitializer部署到独立的tomcat中
 */
@EnableAsync
@SpringBootApplication
@ComponentScan(basePackageClasses = {
        AuthenticationManager.class
})
public class AuthDemoBootstrap extends SpringBootServletInitializer {

    /**
     * Configure the application. Normally all you would need to do is to add sources
     * (e.g. config classes) because other settings have sensible defaults. You might
     * choose (for instance) to add default command line arguments, or set an active
     * Spring profile.
     *
     * @param builder a builder for the application context
     * @return the application builder
     * @see SpringApplicationBuilder
     */
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(AuthDemoBootstrap.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(AuthDemoBootstrap.class);
    }
}
