package cn.edu.lingnan.mooc.authorize.model.vo;

import lombok.Data;

import java.util.Date;

/**
 * @author xiaomingzhang
 * @date 2022/1/23
 */
@Data
public class ManagerVO {

    private Long id;

    private String name;

    private String account;

    private Date createTime;

}
