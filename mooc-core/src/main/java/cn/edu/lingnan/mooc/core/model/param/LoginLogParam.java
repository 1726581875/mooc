package cn.edu.lingnan.mooc.core.model.param;

import lombok.Data;
import lombok.ToString;

/**
 * @author xmz
 * @date: 2020/11/30
 * 封装日志查询参数
 */
@Data
@ToString
public class LoginLogParam {

    private String matchStr;

    private String startTime;

    private String endTime;


}
