package cn.com.authDemo.service.common.impl;

import cn.com.authDemo.service.common.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @author: nj
 * @date: 2018/12/30:下午2:01
 */
@Service(value = "redisService")
public class RedisServiceImpl implements RedisService {

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 设置缓存
     * @param key 键
     * @param value 值
     * @param timeout 超时时间， 毫秒
     * @throws Exception
     */
    @Override
    public void setValue(String key, Object value, Long timeout) throws Exception{
        if (null != timeout && timeout > 0) {
            redisTemplate.opsForValue().set(key, value, timeout, TimeUnit.MILLISECONDS);
        } else {
            redisTemplate.opsForValue().set(key, value, timeout);

        }

    }

    @Override
    public Object getValue(String key, Class returnClazz) throws Exception {

        Object o = this.redisTemplate.opsForValue().get(key);
        return o;
    }
}
