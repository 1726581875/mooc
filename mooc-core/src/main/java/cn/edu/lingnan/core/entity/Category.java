package cn.edu.lingnan.core.entity;

import javax.persistence.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import lombok.Data;

import java.util.Date;

@Data
@DynamicUpdate
@DynamicInsert
@Entity(name = "category")
public class Category{
    // id   
    @Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;
    // 分类名称   
    private String name;
    // 分类描述   
    private String description;
    // 创建时间
    private Date createTime;
    // 修改时间
    private Date updateTime;
    
}