package cn.com.authDemo.model.user;

import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;

/**
 * 角色表
 * @author: nj
 * @date: 2018/12/18:下午4:21
 */
@Data
@ToString
@Document(collection = "role")
public class Role  implements Serializable {
    @Id
    @Field("role_id")
    private String roleId;
    @Field("role_code")
    private String roleCode;
    @Field("role_name")
    private String roleName;
    @Field("role_desc")
    private String roleDesc;
}
