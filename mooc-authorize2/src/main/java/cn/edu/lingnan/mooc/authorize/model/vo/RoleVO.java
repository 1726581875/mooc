package cn.edu.lingnan.mooc.authorize.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author xmz
 * @date: 2020/11/08
 */
@Data
public class RoleVO {

    private Long id;
    // 角色名称
    private String name;
    // 创建时间
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;

    private List<Long> menuIdList;

    private List<Long> leafNodeIdList;

}
