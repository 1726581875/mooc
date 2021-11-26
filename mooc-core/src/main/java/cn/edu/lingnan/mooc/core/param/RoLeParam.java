package cn.edu.lingnan.mooc.core.param;

import lombok.Data;

import java.util.List;

/**
 * @author xmz
 * @date: 2020/11/08
 */
@Data
public class RoLeParam {

    private Integer id;
    // 角色名称
    private String name;

    private List<Integer> menuIdList;
}
