package cn.edu.lingnan.mooc.core.model.entity;

import javax.persistence.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import lombok.Data;
import java.util.Date;

@Data
@DynamicUpdate
@DynamicInsert
@Entity(name = "mooc_ip_blacklist")
public class IpBlacklist{
    /**
     * 主键id
     */
    @Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;
    /**
     * 名称
     */
    private String name;
    /**
     * 禁用ip
     */
    private String ip;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     *  修改时间
     */
    private Date updateTime;


    public IpBlacklist(){}

    public IpBlacklist(String name,String ip){
        this.name = name;
        this.ip = ip;
    }
    
}