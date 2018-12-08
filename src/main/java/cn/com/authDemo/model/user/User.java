package cn.com.authDemo.model.user;/**
 * Created by niejian on 2018/12/1.
 */

import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * 定义实体
 * @author niejian
 * @date 2018/12/1
 */
@Data
@ToString
@Document(collection = "user")
public class User {

    @Id
    @Field("userId")
    private String id;
    @Field("userName")
    private String userName;
    @Field("userCode")
    private String userCode;
}
