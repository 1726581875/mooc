package cn.edu.lingnan.mooc.authorize.model.vo;

import cn.edu.lingnan.mooc.common.enums.UserTypeEnum;
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

    private UserTypeEnum type;

    private Long userId;

    private List<UserMenuTreeVO> menuList;

}
