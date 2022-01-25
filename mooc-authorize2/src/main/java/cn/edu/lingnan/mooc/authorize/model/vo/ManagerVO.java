package cn.edu.lingnan.mooc.authorize.model.vo;

import cn.edu.lingnan.mooc.authorize.model.entity.Role;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author xiaomingzhang
 * @date 2022/1/23
 */
@Data
public class ManagerVO {

    private Long id;

    private String name;

    private String account;

    /**
     * 1启用 true
     * 2禁用 false
     */
    private boolean status;

    private Date createTime;

}
