package cn.edu.lingnan.mooc.core.model.entity;

import javax.persistence.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import lombok.Data;

import java.util.Date;

@Data
@DynamicUpdate
@DynamicInsert
@Entity(name = "Tag")
public class Tag{
    // id   
    @Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    // 分类id   
    private Integer categoryId;
    // 标签名   
    private String name;
    // 创建时间
    private Date createTime;
    // 修改时间
    private Date updateTime;
    
}