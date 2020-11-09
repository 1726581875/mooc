package cn.edu.lingnan.mooc.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author xmz
 * @date: 2020/11/08
 */
@Data
public class RoleVO {

    private Integer id;
    // 角色名称
    private String name;
    // 创建时间
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;

    private List<Integer> menuIdList;

    private List<Integer> leafNodeIdList;

}
