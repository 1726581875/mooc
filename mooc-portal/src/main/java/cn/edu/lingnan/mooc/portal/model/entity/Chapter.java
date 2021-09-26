package cn.edu.lingnan.mooc.portal.model.entity;

import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Data
@DynamicInsert
@DynamicUpdate
@Entity(name = "chapter")
@Accessors(chain=true)
public class Chapter {
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
     * 名称
     */
    private String name;
    /**
     * 时长|单位秒
     */
    private Integer duration;
    /**
     * 顺序
     */
    private Integer sort;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 修改时间
     */
    private Date updateTime;

}