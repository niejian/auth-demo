package cn.com.authDemo.model.user;

import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;

/**
 * @author: nj
 * @date: 2018/12/21:下午4:56
 */
@Data
@ToString
@Document(collection = "role_menu")
public class RoleMenu implements Serializable {
    @Id
    @Field(value = "id")
    private String id;
    @Field(value = "role_id")
    private String roleId;
    @Field(value = "role_code")
    private String roleCode;
    @Field(value = "menu_id")
    private String menuId;
    @Field(value = "menu_code")
    private String menuCode;
    @Field(value = "deleted")
    private boolean deleted;
}
