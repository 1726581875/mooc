package cn.edu.lingnan.core.vo.reception;

import lombok.Data;
import lombok.ToString;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

/**
 * @author xmz
 * @date: 2021/02/08
 */
@Data
@ToString
public class UserDetailVO {

    private Integer id;
    // 用户头像
    private String userImage;
    // 用户昵称
    private String name;
    // 登录账号
    private String account;
    // 类型，教师/普通用户
    private String userType;

}
