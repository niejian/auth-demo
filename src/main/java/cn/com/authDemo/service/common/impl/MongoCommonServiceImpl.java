//package cn.com.authDemo.service.common.impl;
//
//import cn.com.authDemo.service.common.MongoCommonService;
//import cn.com.authDemo.util.RefectUtils;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.mongodb.core.MongoTemplate;
//import org.springframework.data.mongodb.core.query.Criteria;
//import org.springframework.data.mongodb.core.query.Query;
//import org.springframework.stereotype.Service;
//import org.springframework.util.CollectionUtils;
//import org.springframework.util.StringUtils;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * @author: nj
// * @date: 2018/12/21:下午5:27
// */
//@Slf4j
//@Service(value = "mongoCommonService")
//public class MongoCommonServiceImpl implements MongoCommonService {
//
//    @Autowired
//    private MongoTemplate mongoTemplate;
//
//    /**
//     * 根据表明添加 实体信息
//     *
//     * @param clazz
//     * @param dbName
//     * @throws Exception
//     */
//    @Override
//    public void addEntity(Class<?> clazz, String dbName) throws Exception {
//        this.mongoTemplate.insert(clazz, dbName);
//    }
//
//    /**
//     * 根据主键获取实体现象
//     *
//     * @param clazz
//     * @param primaryKeyFieldName 主键字段名称
//     * @param primaryVal
//     * @param dbName
//     * @return
//     * @throws Exception
//     */
//    @Override
//    public Object getEntityByPrimarykey(Class<?> clazz, String primaryKeyFieldName,
//                                          String primaryVal, String dbName) throws Exception {
//
//        Object object = null;
//        log.info("--------->{}", clazz.getName());
//        Class<?> aClass = Class.forName(clazz.getName());
//        String calssName = clazz.getName();
//        List<Object> objectList = new ArrayList<>();
//
//        Query query = new Query(Criteria.where("_id").is(primaryVal));
//        objectList = ()this.mongoTemplate.find(query, clazz.getClass(), dbName);
//
//
//        if (!CollectionUtils.isEmpty(objectList)) {
//
//            object = objectList.get(0);
//        }
//
//        return object;
//
//    }
//
//    /**
//     * 根据入参实体更新对应表的信息
//     *
//     * @param clazz
//     * @param dbName
//     * @throws Exception
//     */
//    @Override
//    public void updateEntityBySelectived(Class<?> clazz, String dbName) throws Exception {
//
//    }
//}
