package cn.edu.lingnan.mooc.portal.model.entity;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

/**
 * @author xmz
 * @date: 2021/09/30
 */
@Data
@DynamicInsert
@DynamicUpdate
@Entity(name = "category")
public class Category{
    /**
     * id
     */
    @Id
  	@GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;
    /**
     * 分类名称
     */
    private String name;
    /**
     * 分类描述
     */
    private String description;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 修改时间
     */
    private Date updateTime;

}