package cn.edu.lingnan.mooc.core.model.entity;
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
    private Long id;
    // 管理员账号   
    private Long teacherId;
    // 课程id   
    private Long courseId;
    // 具体消息   
    private String message;
    // 类型|新增课程、上传视频、删除课程、课程点赞、取消点赞
    private String recordType;
    // 登录ip   
    private String ip;
    // 创建时间   
    private Date createTime;
    
}