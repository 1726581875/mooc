package cn.edu.lingnan.mooc.core.entity;
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
    /**
     * id
     */
    @Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    /**
     * 课程id
     */
    private Long courseId;

    /**
     * 标签id
     */
    private Long tagId;
    /**
     * 创建时间
     */
    private Date createTime;

    public CourseTagRel(){};

    public CourseTagRel(Long courseId, Long tagId){
        this.courseId = courseId;
        this.tagId = tagId;
    }

    
}