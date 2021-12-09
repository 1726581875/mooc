package cn.edu.lingnan.mooc.core.model.param;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain=true)
public class UserParam {
    // ID
    private Long id;
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