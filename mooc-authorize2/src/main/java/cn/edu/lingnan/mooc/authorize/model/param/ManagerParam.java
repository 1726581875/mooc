package cn.edu.lingnan.mooc.authorize.model.param;

import cn.edu.lingnan.mooc.authorize.model.entity.Role;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.ToString;

import java.util.Date;
import java.util.List;

/**
 * @author xmz
 * @date: 2020/11/12
 */
@Data
@ToString
public class ManagerParam {

    private Integer id;
    // 名字
    private String name;
    // 登录账号
    private String account;

    private String password;

    // 用户状态| 1正常，2禁用，3已删除
    private Integer status;
    // 创建时间
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;

    private List<Role> roleList;

}
