package cn.edu.lingnan.mooc.authorize.model.param;

import lombok.Data;

import java.util.List;

/**
 * @author xmz
 * @date: 2020/11/08
 */
@Data
public class RoLeParam {

    private Long id;
    // 角色名称
    private String name;

    private List<Long> menuIdList;
}
