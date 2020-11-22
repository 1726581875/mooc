package cn.edu.lingnan.mooc.model;

import javax.persistence.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import lombok.Data;
import java.util.Date;

@Data
@DynamicUpdate
@DynamicInsert
@Entity(name = "mooc_resource")
public class MoocResource{
    // ID   
    @Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;
    // 资源名称   
    private String name;
    // 页面路由   
    private String pageRouter;
    // 资源URL   
    private String url;
    // 父资源ID   
    private Integer parentId;
    // 创建时间   
    private Date createTime;
    // 修改时间   
    private Date updateTime;
    
}