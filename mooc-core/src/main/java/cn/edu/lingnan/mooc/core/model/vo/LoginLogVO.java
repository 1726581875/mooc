package cn.edu.lingnan.mooc.core.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @author xmz
 * @date: 2020/11/29
 */
@Data
public class LoginLogVO {

    // 管理员账号
    private String account;
    // 日志名
    private String logName;
    // 是否执行成功
    private String succeed;
    // 具体消息
    private String message;
    // 登录ip
    private String ip;
    // 创建时间
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;

}
