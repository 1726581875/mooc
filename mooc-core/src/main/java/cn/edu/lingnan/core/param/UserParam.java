package cn.edu.lingnan.core.param;

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
@Accessors(chain=true)
public class UserParam {
    // ID
    private Integer id;
    // 用户头像   
    private String userImage;
    // 用户昵称   
    private String name;
    /**
     * 个人座右铭、格言
     */
    private String motto;

    private Integer status;
    
}