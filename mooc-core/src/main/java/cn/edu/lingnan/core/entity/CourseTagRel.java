package cn.edu.lingnan.core.entity;
import javax.persistence.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import lombok.Data;
import java.util.Date;
@Data
@DynamicUpdate
@DynamicInsert
@Entity(name = "course_tag_rel")
public class CourseTagRel{
    // id   
    @Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;
    // 课程id   
    private Integer courseId;
    // 标签id   
    private Integer tagId;
    // 创建时间   
    private Date createTime;
    
}