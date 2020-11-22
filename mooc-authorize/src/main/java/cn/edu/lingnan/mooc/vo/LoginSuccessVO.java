package cn.edu.lingnan.mooc.vo;

import cn.edu.lingnan.mooc.model.MenuTree;
import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * @author xmz
 * @date: 2020/11/20
 */
@Data
@ToString
public class LoginSuccessVO {

    private String token;

    private List<MenuTree> menuList;




}
