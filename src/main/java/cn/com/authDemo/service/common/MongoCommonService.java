package cn.com.authDemo.service.common;

import net.sf.json.JSONObject;
import org.springframework.data.mongodb.core.query.Query;

import java.util.Map;

/**
 * mongo相关的进一步封装的方法
 * 利用反射的方式获取到表实体上的注解从而获得对应的表字段
 * @author: nj
 * @date: 2018/12/21:下午5:20
 */
public interface MongoCommonService {

    /**
     * 根据表明添加 实体信息
     * @param clazz
     * @param dbName
     * @throws Exception
     */
    void addEntity(Class<?> clazz, String dbName) throws Exception;

    /**
     * 根据主键获取实体现象
     * @param clazz
     * @param primaryKeyFieldName 主键字段名称
     * @param primaryVal
     * @param dbName
     * @return
     * @throws Exception
     */
    Object getEntityByPrimarykey(Class<?> clazz, String primaryKeyFieldName,
                                   String primaryVal, String dbName) throws Exception;

    /**
     * 根据入参实体更新对应表的信息
     * @param clazz
     * @param query
     * @param updateFieldValMap 属性名、值键值对
     * @throws Exception
     */
    void updateEntityBySelectived(Class<?> clazz, Query query, Map<String, Object> updateFieldValMap ) throws Exception;

}
