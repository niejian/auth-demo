package cn.com.authDemo.service.common;

/**
 * @author: nj
 * @date: 2018/12/30:下午2:00
 */
public interface RedisService {

    /**
     * 设置缓存
     * @param key
     * @param value
     * @param timeout
     * @throws Exception
     */
    void setValue(String key, Object value, long timeout) throws Exception;


    Object getValue(String key, Class returnClazz) throws Exception;
}
