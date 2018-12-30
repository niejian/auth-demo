package cn.com.authDemo.service.common.impl;

import cn.com.authDemo.service.common.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @author: nj
 * @date: 2018/12/30:下午2:01
 */
@Service(value = "redisService")
public class RedisServiceImpl implements RedisService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public void setValue(String key, Object value, long timeout) throws Exception{
        redisTemplate.opsForValue().set(key, value, timeout);
    }

    @Override
    public Object getValue(String key, Class returnClazz) throws Exception {

        Object o = this.redisTemplate.opsForValue().get(key);
        return o;
    }
}
