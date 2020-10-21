package cn.edu.lingnan.mooc.entity;

import javax.persistence.*;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SelectBeforeUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

@Data
@Entity(name = "chapter")
//@EntityListeners(AuditingEntityListener.class)
@DynamicUpdate
@DynamicInsert
public class Chapter{
    // id   
    @Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;
    // 课程id   
    private Integer courseId;
    // 名称   
    private String name;
    // 创建时间
    //@CreatedDate
    private Date createTime;
    // 修改时间
   // @LastModifiedDate
    private Date updateTime;
    
}