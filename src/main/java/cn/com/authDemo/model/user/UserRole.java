package cn.com.authDemo.model.user;

import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;

/**
 * 用户权限信息
 * 菜单权限和按钮权限
 * @author: nj
 * @date: 2018/12/18:下午4:20
 */
@Data
@ToString
@Document(collection = "user_role")
public class UserRole implements Serializable {
    @Id
    @Field(value = "id")
    private String id;
    @Field("role_id")
    private String roleId;
    @Field("user_id")
    private String userId;

}
