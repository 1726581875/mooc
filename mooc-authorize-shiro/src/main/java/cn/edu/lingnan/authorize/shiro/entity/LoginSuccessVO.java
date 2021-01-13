package cn.edu.lingnan.authorize.shiro.entity;

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

    private List<MenuTreeDTO> menuList;



}
