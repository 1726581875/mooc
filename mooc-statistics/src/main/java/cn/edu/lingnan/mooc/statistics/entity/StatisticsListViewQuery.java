package cn.edu.lingnan.mooc.statistics.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author: Carlson
 * @Date: 2020/3/11 11:47
 * 统计列表-客户联系/消息发送/敏感词命中报表查询参数
 */
@Data
public class StatisticsListViewQuery {

    /**
     * 开始时间
     */
    private String beginTime;

    /**
     * 结束时间
     */
    private String endTime;

    /**
     * 关键字（姓名/帐号、敏感词）
     */
    private String keyword;

    /**
     * 当前页
     */
    private Integer currPage;

    /**
     * 分页条数
     */
    private Integer pageSize;

    /**
     * 排序字段
     */
    private String orderField;

    /**
     * 是否降序
     */
    private Boolean isAsc;
}
