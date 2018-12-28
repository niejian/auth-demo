package cn.com.authDemo.util;

import cn.com.authDemo.model.user.Menu;
import cn.com.authDemo.model.user.User;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.query.Update;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *反射相关
 * @author: nj
 * @date: 2018/12/23:上午10:28
 */
public class RefectUtils {

    /**
     * 根据类的主键注解@ID获得具体的字段名称
     * @param clazz
     * @param primaryKey
     * @return
     * @throws Exception
     */
    public static String getFieldIdAnnotation(Class<?> clazz, String primaryKey) throws Exception {
       String idFiled = "";
        boolean isContinue = true;
        Field primaryField = clazz.getDeclaredField(primaryKey);
        if (primaryField != null) {
            primaryField.setAccessible(true);
            org.springframework.data.mongodb.core.mapping.Field primaryFieldAnnotation = (primaryField.getAnnotation(org.springframework.data.mongodb.core.mapping.Field.class));
            idFiled = primaryFieldAnnotation.value();
            idFiled = "_" + idFiled;
        }



        return idFiled;
    }

    public static void  getUpdateFieldsByPo(Class clazz) {
        Method[] declaredMethods = clazz.getDeclaredMethods();

    }

    public static void main(String[] args) throws Exception{
        User user = new User();
        user.setEmail("sd");
        getUpdateFieldsByPo(user.getClass());
    }


}
