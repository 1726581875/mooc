package cn.edu.lingnan.core.param;

import lombok.Data;
import lombok.ToString;

/**
 * @author xmz
 * @date: 2020/11/30
 * 封装日志查询参数
 */
@Data
@ToString
public class MonitorRecordParam {
    /**
     * 教师名字或者账号
     */
    private String nameOrAccount;
    /**
     * 记录类型
     */
    private String recordType;
    /**
     * 开始时间
     */
    private String startTime;
    /**
     * 结束时间
     */
    private String endTime;


}
