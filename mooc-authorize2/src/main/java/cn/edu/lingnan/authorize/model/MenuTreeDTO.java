package cn.edu.lingnan.authorize.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * @author xmz
 * @date: 2020/11/28
 * 贴合vue的菜单树
 */
@Data
@ToString
public class MenuTreeDTO {

    /**
     * id
     */
    @JsonIgnore
    private Long id;

    /**
     * 图标
     */
    private String icon;
    /**
     * 菜单唯一key
     */
    private String index;
    /**
     * 菜单标题
     */
    private String title;
    /**
     * 子菜单
     */
    private List<MenuTreeDTO> subs;

}
