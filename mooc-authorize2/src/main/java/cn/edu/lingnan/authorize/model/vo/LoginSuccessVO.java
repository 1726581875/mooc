package cn.edu.lingnan.authorize.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

/**
 * @author xmz
 * @date: 2020/11/23
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class LoginSuccessVO {

    private String token;
    /**
     * 1、管理员  2教师
     */
    private Integer type;

    private Integer userId;

    private List<MenuTreeDTO> menuList;

}
