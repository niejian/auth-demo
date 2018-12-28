package cn.com.authDemo.service.common.impl;

import cn.com.authDemo.service.common.MongoCommonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author: nj
 * @date: 2018/12/21:下午5:27
 */
@Slf4j
@Service(value = "mongoCommonService")
public class MongoCommonServiceImpl implements MongoCommonService {

    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 根据表明添加 实体信息
     *
     * @param clazz
     * @param dbName
     * @throws Exception
     */
    @Override
    public void addEntity(Class<?> clazz, String dbName) throws Exception {
        this.mongoTemplate.insert(clazz, dbName);
    }

    /**
     * 根据主键获取实体现象
     *
     * @param clazz
     * @param primaryKeyFieldName 主键字段名称
     * @param primaryVal
     * @param dbName
     * @return
     * @throws Exception
     */
    @Override
    public Object getEntityByPrimarykey(Class<?> clazz, String primaryKeyFieldName,
                                          String primaryVal, String dbName) throws Exception {
        return null;
    }

//

    /**
     * 根据入参实体更新对应表的信息
     *
     * @param clazz
     * @param query
     * @param updateFieldValMap 属性名、值键值对
     * @throws Exception
     */
    @Override
    public void updateEntityBySelectived(Class<?> clazz, Query query, Map<String, Object> updateFieldValMap) throws Exception {
        Update update = new Update();
        if (null != updateFieldValMap && updateFieldValMap.size() > 0) {

            updateFieldValMap.forEach((field, value) -> {
                update.addToSet(field, value);
            });

            this.mongoTemplate.updateMulti(query, update, clazz);
        }


    }
}
