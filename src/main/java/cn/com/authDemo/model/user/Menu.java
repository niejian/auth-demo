package cn.com.authDemo.model.user;

import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;

/**
 * @author: nj
 * @date: 2018/12/21:下午4:56
 */
@Data
@ToString
public class Menu implements Serializable {
    @Id
    @Field(value = "id")
    private String menuId;
    @Field(value = "menu_code")
    private String menuCode;
    @Field(value = "page_url")
    private String pageUrl;
    @Field(value = "is_root")
    /**是否是一级菜单*/
    private Boolean isRoot;
    @Field(value = "parent_menu_id")
    private String parentMenuId;
    @Field(value = "deleted")
    private Boolean deleted;
}
