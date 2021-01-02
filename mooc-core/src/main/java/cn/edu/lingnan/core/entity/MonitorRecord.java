package cn.edu.lingnan.core.entity;
import javax.persistence.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import lombok.Data;
import java.util.Date;

@Data
@DynamicUpdate
@DynamicInsert
@Entity(name = "course_monitor_record")
public class MonitorRecord{
    // 主键   
    @Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;
    // 管理员账号   
    private Integer teacherId;
    // 课程id   
    private Integer courseId;
    // 具体消息   
    private String message;
    // 类型|新增课程、上传视频、删除课程   
    private String recordType;
    // 登录ip   
    private String ip;
    // 创建时间   
    private Date createTime;
    
}