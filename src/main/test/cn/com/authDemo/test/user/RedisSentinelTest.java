package cn.com.authDemo.test.user;

import cn.com.authDemo.service.common.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: nj
 * @date: 2018/12/30:下午1:05
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisSentinelTest {
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private RedisService redisService;

    @Ignore
    @Test
    public void testSetVal() throws Exception{
        List<String> a = new ArrayList<>();
        a.add("1");
        a.add("1");
        a.add("1");
       redisService.setValue("adc1", a, 10000L);
    }

    @Ignore
    @Test
    public void getVal() {
        String name = redisTemplate.opsForValue().get("adc");
        log.info("---->{}", name);
    }


}
