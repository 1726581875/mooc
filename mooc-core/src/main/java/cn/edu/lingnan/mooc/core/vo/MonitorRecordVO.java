package cn.edu.lingnan.mooc.core.vo;

import lombok.Data;
import java.util.Date;

@Data
public class MonitorRecordVO {
    // 主键
    private Integer id;
    // 教师id
    private Long teacherId;
    // 教师昵称+账号
    private String nameAccount;
    // 课程id   
    private Integer courseId;
    // 具体消息   
    private String message;
    // 类型|新增课程、上传视频、删除课程   
    private String recordType;
    // 操作ip
    private String ip;
    // 创建时间   
    private Date createTime;
    
}