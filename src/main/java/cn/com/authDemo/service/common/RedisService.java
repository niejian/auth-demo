package cn.com.authDemo.service.common;

/**
 * @author: nj
 * @date: 2018/12/30:下午2:00
 */
public interface RedisService {

    /**
     * 设置缓存
     * @param key 键
     * @param value 值
     * @param timeout 超时时间， 毫秒
     * @throws Exception
     */
    void setValue(String key, Object value, Long timeout) throws Exception;


    Object getValue(String key, Class returnClazz) throws Exception;
}
